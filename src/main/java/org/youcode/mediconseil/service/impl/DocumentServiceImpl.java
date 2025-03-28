package org.youcode.mediconseil.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.youcode.mediconseil.domain.Consultation;
import org.youcode.mediconseil.domain.Doctor;
import org.youcode.mediconseil.domain.Document;
import org.youcode.mediconseil.domain.User;
import org.youcode.mediconseil.domain.enums.DocumentType;
import org.youcode.mediconseil.repository.ConsultationRepository;
import org.youcode.mediconseil.repository.DocumentRepository;
import org.youcode.mediconseil.service.DocumentService;
import org.youcode.mediconseil.web.exception.document.FileStorageException;
import org.youcode.mediconseil.web.vm.request.DocumentRequestVm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class DocumentServiceImpl implements DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private ConsultationRepository consultationRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JavaMailSender mailSender;

    @Value("${app.email.from:noreply@mediconseil.com}")
    private String fromEmail;

    private final String UPLOAD_DIR = "uploads/";

    private static final Logger log = LoggerFactory.getLogger(DocumentServiceImpl.class); // Logger

    @Override
    public Document createDocument(DocumentRequestVm documentRequestVm, MultipartFile file) {
        try {
            // Ensure the uploads directory exists
            Path uploadDir = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Fetch consultation
            Consultation consultation = consultationRepository.findById(documentRequestVm.getConsultationId())
                    .orElseThrow(() -> new RuntimeException("Consultation not found"));

            // Save the file
            byte[] bytes = file.getBytes();
            String safeFileName = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-_]", "_");
            Path path = Paths.get(UPLOAD_DIR, safeFileName);
            Files.write(path, bytes);
            log.info("File saved at: {}", path.toAbsolutePath());

            // Create document entity
            Document document = new Document();
            document.setTitle(documentRequestVm.getTitle());
            document.setContent(documentRequestVm.getContent());
            document.setDateGeneration(LocalDateTime.now());
            document.setFileName(safeFileName);
            document.setFileUrl(path.toAbsolutePath().toString());
            document.setFileSize(file.getSize());
            document.setType(documentRequestVm.getType());
            document.setConsultation(consultation);

            Document savedDocument = documentRepository.save(document);

            // Send email
            sendDocumentEmailToPatient(savedDocument, consultation.getPatient());

            return savedDocument;
        } catch (IOException e) {
            throw new FileStorageException("Failed to store file: " + file.getOriginalFilename());
        }
    }

    @Override
    public byte[] downloadDocument(UUID documentId) {
        Document document = documentRepository.findById(documentId)
                .orElseThrow(() -> new RuntimeException("Document not found"));
        try {
            Path path = Paths.get(document.getFileUrl());
            return Files.readAllBytes(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read file", e);
        }
    }

    private void sendDocumentEmailToPatient(Document document, User patient) {
        try {
            String downloadLink = "http://localhost:8080/api/documents/download/" + document.getId();
            String emailContent = buildDocumentEmailContent(document, downloadLink);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail);
            helper.setTo(patient.getEmail());
            helper.setSubject("Your Consultation Document - MediConseil");
            helper.setText(emailContent, true);

            mailSender.send(message);

            log.info("Document email sent to: {}", patient.getEmail());
        } catch (MessagingException e) {
            log.error("Failed to send document email", e);
            throw new RuntimeException("Email sending failed", e);
        }
    }

    private String buildDocumentEmailContent(Document document, String downloadLink) {
        // Get relevant information
        User patient = document.getConsultation().getPatient();
        Doctor doctor = document.getConsultation().getDoctor();
        String patientName = patient.getFirstName() + " " + patient.getLastName();
        String doctorName = doctor.getFirstName() + " " + doctor.getLastName();
        String doctorSpecialty = doctor.getSpeciality() != null ? doctor.getSpeciality().getName() : "Médecin";
        String documentTitle = document.getTitle();
        DocumentType documentType = document.getType();
        // Format dates with DateTimeFormatter
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy à HH:mm", Locale.FRENCH);
        String consultationDate = document.getConsultation().getDateConsultation().format(dateFormatter);
        String generationDate = document.getDateGeneration().format(dateFormatter);

        return String.format("""
                        <!DOCTYPE html>
                        <html>
                        <head>
                          <meta charset="UTF-8">
                          <meta name="viewport" content="width=device-width, initial-scale=1.0">
                          <title>Votre Document Médical - MediConseil</title>
                          <style>
                            body {
                              font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                              line-height: 1.6;
                              color: #333;
                              margin: 0;
                              padding: 0;
                              background-color: #f9f9f9;
                            }
                            .container {
                              max-width: 600px;
                              margin: 0 auto;
                              background-color: #ffffff;
                              border-radius: 8px;
                              overflow: hidden;
                              box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                            }
                            .header {
                              background: linear-gradient(135deg, #3b82f6 0%%, #2563eb 100%%);
                              padding: 30px 20px;
                              text-align: center;
                              color: white;
                            }
                            .logo {
                              margin-bottom: 15px;
                              width: 150px;
                            }
                            .header h1 {
                              margin: 0;
                              font-size: 24px;
                              font-weight: 600;
                            }
                            .content {
                              padding: 30px;
                            }
                            .doctor-info {
                              background-color: #f0f7ff;
                              padding: 15px;
                              border-radius: 6px;
                              margin-bottom: 25px;
                              border-left: 4px solid #3b82f6;
                            }
                            .doctor-name {
                              font-weight: bold;
                              color: #1e40af;
                            }
                            .document-type {
                              font-size: 18px;
                              font-weight: 600;
                              margin-bottom: 5px;
                              color: #1e40af;
                            }
                            .download-btn {
                              display: inline-block;
                              background-color: #2563eb;
                              color: white;
                              text-decoration: none;
                              padding: 12px 25px;
                              border-radius: 6px;
                              font-weight: 500;
                              margin: 20px 0;
                              text-align: center;
                              transition: background-color 0.3s ease;
                            }
                            .download-btn:hover {
                              background-color: #1d4ed8;
                            }
                            .link-fallback {
                              word-break: break-all;
                              font-size: 14px;
                              color: #6b7280;
                              background-color: #f3f4f6;
                              padding: 10px;
                              border-radius: 4px;
                              margin-top: 15px;
                            }
                            .footer {
                              background-color: #f3f4f6;
                              padding: 20px;
                              text-align: center;
                              font-size: 14px;
                              color: #6b7280;
                            }
                            .social-links {
                              margin-top: 15px;
                            }
                            .social-links a {
                              display: inline-block;
                              margin: 0 8px;
                            }
                            .consultation-info {
                              margin: 20px 0;
                              padding: 15px;
                              background-color: #f9fafb;
                              border-radius: 6px;
                            }
                            .info-label {
                              font-weight: 500;
                              color: #4b5563;
                              display: inline-block;
                              width: 140px;
                            }
                          </style>
                        </head>
                        <body>
                          <div class="container">
                            <div class="header">
                              <img src="https://i.ibb.co/YPXxBZQ/mediconseil-logo.png" alt="MediConseil Logo" class="logo">
                              <h1>Votre Document Médical est Prêt</h1>
                            </div>
                        
                            <div class="content">
                              <p>Cher(e) %s,</p>
                        
                              <p>Nous espérons que vous allez bien. Le Dr. %s a préparé le document médical suivant suite à votre consultation:</p>
                        
                              <div class="doctor-info">
                                <p class="document-type">%s</p>
                                <p><span class="doctor-name">Dr. %s</span> • %s</p>
                                <p>%s</p>
                              </div>
                        
                              <div class="consultation-info">
                                <p><span class="info-label">Date de consultation:</span> %s</p>
                                <p><span class="info-label">Document généré le:</span> %s</p>
                              </div>
                        
                              <p>Vous pouvez télécharger votre document en cliquant sur le bouton ci-dessous:</p>
                        
                              <a href="%s" class="download-btn">Télécharger le Document</a>
                        
                              <p>Si le bouton ci-dessus ne fonctionne pas, copiez et collez ce lien dans votre navigateur:</p>
                              <div class="link-fallback">
                                %s
                              </div>
                        
                              <p>N'hésitez pas à nous contacter si vous avez des questions concernant votre document ou votre traitement.</p>
                        
                              <p>Cordialement,<br>
                              L'équipe MediConseil</p>
                            </div>
                        
                            <div class="footer">
                              <p>&copy; 2025 MediConseil. Tous droits réservés.</p>
                              <p>Pour toute assistance, contactez-nous à <a href="mailto:support@mediconseil.com">support@mediconseil.com</a></p>
                              <div class="social-links">
                                <a href="#"><img src="https://i.ibb.co/bRZmT0k/facebook.png" alt="Facebook" width="24"></a>
                                <a href="#"><img src="https://i.ibb.co/J2zY8YF/twitter.png" alt="Twitter" width="24"></a>
                                <a href="#"><img src="https://i.ibb.co/NsgQzK2/instagram.png" alt="Instagram" width="24"></a>
                              </div>
                            </div>
                          </div>
                        </body>
                        </html>
                        """,
                patientName,
                doctorName,
                documentType,
                doctorName,
                doctorSpecialty,
                documentTitle,
                consultationDate,
                generationDate,
                downloadLink,
                downloadLink
        );
    }
}
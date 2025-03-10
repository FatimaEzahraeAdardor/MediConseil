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
import org.youcode.mediconseil.domain.Document;
import org.youcode.mediconseil.domain.User;
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
        return String.format("""
            <!DOCTYPE html>
            <html>
            <body style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                <div style="background-color: #f4f4f4; padding: 20px; text-align: center;">
                    <h2 style="color: #333;">Your Consultation Document</h2>
                </div>
                <div style="padding: 20px;">
                    <p>Dear Patient,</p>
                    <p>Your consultation document is ready. You can download it using the link below:</p>
                    <p><a href="%s" style="background-color: #007bff; color: white; padding: 10px 20px; text-decoration: none; border-radius: 5px;">Download Document</a></p>
                    <p>If the button above does not work, copy and paste this link into your browser:</p>
                    <p>%s</p>
                    <p style="margin-top: 20px;">Thank you for choosing MediConseil.</p>
                </div>
            </body>
            </html>
            """,
                downloadLink,
                downloadLink
        );
    }
}
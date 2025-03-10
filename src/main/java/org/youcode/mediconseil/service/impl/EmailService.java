package org.youcode.mediconseil.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.youcode.mediconseil.domain.Consultation;
import org.youcode.mediconseil.domain.User;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${app.email.from:noreply@mediconseil.com}")
    private String fromEmail;

    @Async
    public void sendConsultationConfirmationEmail(Consultation consultation) {
        try {
            // Validate input
            validateConsultation(consultation);

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            // Use Mailtrap settings
            helper.setFrom(fromEmail);
            helper.setTo(consultation.getPatient().getEmail());
            helper.setSubject("Consultation Confirmation - MediConseil");

            String emailContent = buildConfirmationEmailContent(consultation);
            helper.setText(emailContent, true);

            mailSender.send(message);

            log.info("Consultation confirmation email sent to: {}", consultation.getPatient().getEmail());
        } catch (MessagingException e) {
            log.error("Failed to send confirmation email", e);
            throw new RuntimeException("Email sending failed", e);
        } catch (IllegalArgumentException e) {
            log.error("Invalid consultation data", e);
            throw e;
        }
    }

    private void validateConsultation(Consultation consultation) {
        if (consultation == null) {
            throw new IllegalArgumentException("Consultation cannot be null");
        }
        if (consultation.getPatient() == null) {
            throw new IllegalArgumentException("Patient information is missing");
        }
        if (consultation.getDoctor() == null) {
            throw new IllegalArgumentException("Doctor information is missing");
        }
        if (consultation.getPatient().getEmail() == null || consultation.getPatient().getEmail().isEmpty()) {
            throw new IllegalArgumentException("Patient email is required");
        }
    }

    private String buildConfirmationEmailContent(Consultation consultation) {
        String patientName = getFullName(consultation.getPatient());
        String doctorName = getFullName(consultation.getDoctor());

        String consultationDate = consultation.getDateConsultation() != null
                ? consultation.getDateConsultation().toString()
                : "Not specified";

        String motif = consultation.getMotif() != null
                ? consultation.getMotif()
                : "Not provided";

        return String.format("""
                        <!DOCTYPE html>
                        <html>
                        <body style="font-family: Arial, sans-serif; max-width: 600px; margin: 0 auto;">
                            <div style="background-color: #f4f4f4; padding: 20px; text-align: center;">
                                <h2 style="color: #333;">Consultation Confirmation</h2>
                            </div>
                            <div style="padding: 20px;">
                                <p>Dear %s,</p>
                                <p>Your consultation has been confirmed with the following details:</p>
                                <table style="width: 100%%; border-collapse: collapse; margin-top: 20px;">
                                    <tr>
                                        <td style="border: 1px solid #ddd; padding: 8px;"><strong>Doctor:</strong></td>
                                        <td style="border: 1px solid #ddd; padding: 8px;">%s</td>
                                    </tr>
                                    <tr>
                                        <td style="border: 1px solid #ddd; padding: 8px;"><strong>Date:</strong></td>
                                        <td style="border: 1px solid #ddd; padding: 8px;">%s</td>
                                    </tr>
                                    <tr>
                                        <td style="border: 1px solid #ddd; padding: 8px;"><strong>Motif:</strong></td>
                                        <td style="border: 1px solid #ddd; padding: 8px;">%s</td>
                                    </tr>
                                </table>
                                <p style="margin-top: 20px;">Thank you for choosing MediConseil.</p>
                            </div>
                        </body>
                        </html>
                        """,
                patientName,
                doctorName,
                consultationDate,
                motif
        );
    }

    private String getFullName(User user) {
        if (user == null) return "User";
        return String.join(" ", user.getFirstName(), user.getLastName())
                .trim();
    }
}
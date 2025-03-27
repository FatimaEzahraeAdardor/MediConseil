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

import java.time.format.DateTimeFormatter;
import java.util.Locale;

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
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd MMMM yyyy à HH:mm", Locale.FRENCH);
        String consultationDate = consultation.getDateConsultation() != null
                ? consultation.getDateConsultation().format(dateFormatter)
                : "Not specified";

        String motif = consultation.getMotif() != null
                ? consultation.getMotif()
                : "Not provided";

        // The HTML template is now a regular string, not using String.format()
        return "<!DOCTYPE html>\n" +
                "<html lang=\"en\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\">\n" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                "    <title>MediConseil - Consultation Confirmation</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;\n" +
                "            line-height: 1.6;\n" +
                "            color: #333;\n" +
                "            max-width: 600px;\n" +
                "            margin: 0 auto;\n" +
                "            background-color: #f9f9f9;\n" +
                "        }\n" +
                "        .email-container {\n" +
                "            background-color: #ffffff;\n" +
                "            border-radius: 10px;\n" +
                "            overflow: hidden;\n" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);\n" +
                "            margin: 20px;\n" +
                "        }\n" +
                "        .header {\n" +
                "            background: linear-gradient(135deg, #4584b6 0%, #2a5298 100%);\n" +
                "            padding: 30px 20px;\n" +
                "            text-align: center;\n" +
                "            color: white;\n" +
                "        }\n" +
                "        .header h1 {\n" +
                "            margin: 0;\n" +
                "            font-size: 28px;\n" +
                "            font-weight: 600;\n" +
                "        }\n" +
                "        .logo {\n" +
                "            margin-bottom: 15px;\n" +
                "        }\n" +
                "        .content {\n" +
                "            padding: 30px;\n" +
                "        }\n" +
                "        .greeting {\n" +
                "            font-size: 18px;\n" +
                "            margin-bottom: 20px;\n" +
                "            color: #2a5298;\n" +
                "            font-weight: 600;\n" +
                "        }\n" +
                "        .details-table {\n" +
                "            width: 100%;\n" +
                "            border-collapse: collapse;\n" +
                "            margin: 25px 0;\n" +
                "            border-radius: 8px;\n" +
                "            overflow: hidden;\n" +
                "            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);\n" +
                "        }\n" +
                "        .details-table th, .details-table td {\n" +
                "            border: 1px solid #e0e0e0;\n" +
                "            padding: 15px;\n" +
                "        }\n" +
                "        .details-table th {\n" +
                "            background-color: #f0f7ff;\n" +
                "            text-align: left;\n" +
                "            font-weight: 600;\n" +
                "            width: 30%;\n" +
                "            color: #2a5298;\n" +
                "        }\n" +
                "        .details-table td {\n" +
                "            background-color: #ffffff;\n" +
                "        }\n" +
                "        .message {\n" +
                "            margin: 25px 0;\n" +
                "            line-height: 1.8;\n" +
                "        }\n" +
                "        .footer {\n" +
                "            background-color: #f0f7ff;\n" +
                "            padding: 20px;\n" +
                "            text-align: center;\n" +
                "            color: #555;\n" +
                "            font-size: 14px;\n" +
                "            border-top: 1px solid #e0e0e0;\n" +
                "        }\n" +
                "        .contact-info {\n" +
                "            margin-top: 15px;\n" +
                "        }\n" +
                "        .buttons {\n" +
                "            margin: 30px 0;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .button {\n" +
                "            display: inline-block;\n" +
                "            padding: 12px 24px;\n" +
                "            background-color: #4584b6;\n" +
                "            color: white;\n" +
                "            text-decoration: none;\n" +
                "            border-radius: 4px;\n" +
                "            font-weight: 600;\n" +
                "            margin: 0 10px;\n" +
                "            transition: background-color 0.3s;\n" +
                "        }\n" +
                "        .button:hover {\n" +
                "            background-color: #2a5298;\n" +
                "        }\n" +
                "        .icons {\n" +
                "            margin-top: 20px;\n" +
                "            text-align: center;\n" +
                "        }\n" +
                "        .icons img {\n" +
                "            width: 32px;\n" +
                "            height: 32px;\n" +
                "            margin: 0 8px;\n" +
                "        }\n" +
                "        .date-highlight {\n" +
                "            font-weight: bold;\n" +
                "            color: #2a5298;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "    <div class=\"email-container\">\n" +
                "        <div class=\"header\">\n" +
                "            <div class=\"logo\">\n" +
                "                <!-- You can add your logo here -->\n" +
                "                <span style=\"font-size: 40px;\">🏥</span>\n" +
                "            </div>\n" +
                "            <h1>Your Consultation is Confirmed!</h1>\n" +
                "        </div>\n" +
                "       \n" +
                "        <div class=\"content\">\n" +
                "            <p class=\"greeting\">Dear " + patientName + ",</p>\n" +
                "           \n" +
                "            <p class=\"message\">We're pleased to confirm your upcoming appointment with MediConseil. Your health is our priority, and we look forward to providing you with excellent care.</p>\n" +
                "           \n" +
                "            <table class=\"details-table\">\n" +
                "                <tr>\n" +
                "                    <th>Doctor:</th>\n" +
                "                    <td>" + doctorName + "</td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <th>Date & Time:</th>\n" +
                "                    <td><span class=\"date-highlight\">" + consultationDate + "</span></td>\n" +
                "                </tr>\n" +
                "                <tr>\n" +
                "                    <th>Reason:</th>\n" +
                "                    <td>" + motif + "</td>\n" +
                "                </tr>\n" +
                "            </table>\n" +
                "           \n" +
                "            <p class=\"message\">\n" +
                "                <strong>Before your appointment:</strong><br>\n" +
                "                • Please arrive 15 minutes early to complete any necessary paperwork<br>\n" +
                "                • Bring your ID and insurance information<br>\n" +
                "                • Prepare a list of questions or concerns you'd like to discuss\n" +
                "            </p>\n" +
                "           \n" +
                "            <p class=\"message\">Thank you for choosing MediConseil for your healthcare needs. If you need to reschedule or have any questions, please contact our support team.</p>\n" +
                "        </div>\n" +
                "       \n" +
                "        <div class=\"footer\">\n" +
                "            <p>© 2025 MediConseil - Your Health, Our Priority</p>\n" +
                "            <div class=\"contact-info\">\n" +
                "                <p>Email: support@mediconseil.com | Phone: (123) 456-7890</p>\n" +
                "            </div>\n" +
                "            <div class=\"icons\">\n" +
                "                <!-- Social media icons can go here -->\n" +
                "                <span style=\"font-size: 20px;\">📱 💻 📞</span>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "    </div>\n" +
                "</body>\n" +
                "</html>";
    }

    private String getFullName(User user) {
        if (user == null) return "User";
        return String.join(" ", user.getFirstName(), user.getLastName())
                .trim();
    }
}
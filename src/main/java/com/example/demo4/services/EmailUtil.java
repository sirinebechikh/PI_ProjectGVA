package org.example.dao;



import org.example.models.Evenement;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class EmailUtil {
    // Gmail SMTP server configuration
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final String SMTP_PORT = "587";

    // Gmail account credentials - Replace these with your actual credentials
    private static final String USERNAME = "az.backup04@gmail.com"; // Replace with your Gmail address
    private static final String PASSWORD = "pfgx wzej kilc azga"; // Replace with your app password (not your regular Gmail password)

    // Sender's email
    private static final String FROM_EMAIL = "az.backup04@gmail.com"; // Replace with your Gmail address
    private static final String SENDER_NAME = "Event Management System"; // The name that will appear as the sender

    /**
     * Sends an email using Gmail SMTP server
     *
     * @param toEmail Recipient's email address
     * @param subject Email subject
     * @param body Email body (HTML content is supported)
     * @return true if the email was sent successfully, false otherwise
     */
    public static boolean sendEmail(String toEmail, String subject, String body) {
        try {
            // Get system properties
            Properties properties = new Properties();

            // Setup mail server
            properties.put("mail.smtp.host", SMTP_HOST);
            properties.put("mail.smtp.port", SMTP_PORT);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            // Get the Session object
            Session session = Session.getInstance(properties, new Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            // Create a default MimeMessage object
            MimeMessage message = new MimeMessage(session);

            // Set From: header field
            message.setFrom(new InternetAddress(FROM_EMAIL, SENDER_NAME));

            // Set To: header field
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));

            // Set Subject: header field
            message.setSubject(subject);

            // Set the actual message content as HTML
            message.setContent(body, "text/html; charset=utf-8");

            // Set sent date
            message.setSentDate(new Date());

            // Send message
            Transport.send(message);

            System.out.println("Email sent successfully to " + toEmail);
            return true;
        } catch (MessagingException | IOException e) {
            System.err.println("Failed to send email to " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Creates an HTML email body for employee event assignment notifications
     *
     * @param employeeName The name of the employee being assigned
     * @param eventName The name of the event
     * @param eventLocation The location of the event
     * @param eventStartDate The start date of the event
     * @param eventEndDate The end date of the event
     * @param assignedRole The role assigned to the employee
     * @return HTML formatted email body
     */
    public static String createAssignmentEmailBody(String employeeName, String eventName,
                                                   String eventLocation, String eventStartDate,
                                                   String eventEndDate, String assignedRole) {
        return "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "    <style>"
                + "        body { font-family: Arial, sans-serif; line-height: 1.6; }"
                + "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }"
                + "        .header { background-color: #4dabf7; color: white; padding: 15px; text-align: center; }"
                + "        .content { padding: 20px; border: 1px solid #ddd; }"
                + "        .footer { text-align: center; margin-top: 20px; font-size: 12px; color: #777; }"
                + "        .info-item { margin-bottom: 10px; }"
                + "        .label { font-weight: bold; }"
                + "        .role { background-color: #e9ecef; display: inline-block; padding: 5px 10px; border-radius: 3px; }"
                + "    </style>"
                + "</head>"
                + "<body>"
                + "    <div class='container'>"
                + "        <div class='header'>"
                + "            <h2>Event Assignment Notification</h2>"
                + "        </div>"
                + "        <div class='content'>"
                + "            <p>Hello " + employeeName + ",</p>"
                + "            <p>You have been assigned to the following event:</p>"
                + "            <div class='info-item'><span class='label'>Event:</span> " + eventName + "</div>"
                + "            <div class='info-item'><span class='label'>Location:</span> " + eventLocation + "</div>"
                + "            <div class='info-item'><span class='label'>Start Date:</span> " + eventStartDate + "</div>"
                + "            <div class='info-item'><span class='label'>End Date:</span> " + eventEndDate + "</div>"
                + "            <div class='info-item'><span class='label'>Your Role:</span> <span class='role'>" + assignedRole + "</span></div>"
                + "            <p>Please log into the Event Management System for more details about your responsibilities.</p>"
                + "            <p>If you have any questions, please contact your manager.</p>"
                + "            <p>Thank you,<br>The Event Management Team</p>"
                + "        </div>"
                + "        <div class='footer'>"
                + "            <p>This is an automated message. Please do not reply to this email.</p>"
                + "        </div>"
                + "    </div>"
                + "</body>"
                + "</html>";
    }

    public static boolean sendSponsorshipResponseEmail(String clientEmail, String clientName,
                                                       String sponsorName, Evenement event,
                                                       boolean isApproved, String responseMessage,
                                                       Double approvedAmount) {
        if (clientEmail == null || clientEmail.isEmpty()) {
            System.err.println("Cannot send sponsorship response: Invalid client email");
            return false;
        }

        // Current date and time for the email
        String currentDateTime = "2025-03-02 22:38:48";

        // Format dates for better readability
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
        String formattedStartDate = event.getDateDebut() != null ? dateFormat.format(event.getDateDebut()) : "Non spécifié";
        String formattedEndDate = event.getDateFin() != null ? dateFormat.format(event.getDateFin()) : "Non spécifié";

        // Subject for the sponsorship response
        String subject = isApproved ?
                "Demande de Parrainage Approuvée: " + event.getNom() :
                "Demande de Parrainage Refusée: " + event.getNom();

        // Email body header color based on approval status
        String headerColor = isApproved ? "#28a745" : "#dc3545";
        String headerTitle = isApproved ? "Demande de Parrainage Approuvée! 🎉" : "Demande de Parrainage Refusée";

        // Email body for the sponsorship response
        String emailBody = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "    <style>"
                + "        body { font-family: Arial, sans-serif; line-height: 1.6; }"
                + "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }"
                + "        .header { background-color: " + headerColor + "; color: white; padding: 15px; text-align: center; }"
                + "        .content { padding: 20px; border: 1px solid #ddd; }"
                + "        .footer { text-align: center; margin-top: 20px; font-size: 12px; color: #777; }"
                + "        .info-item { margin-bottom: 10px; }"
                + "        .label { font-weight: bold; }"
                + "        .message { background-color: #f8f9fa; padding: 15px; border-left: 4px solid " + headerColor + "; margin: 15px 0; }"
                + "        .amount { color: #e83e8c; font-weight: bold; font-size: 1.1em; }"
                + "    </style>"
                + "</head>"
                + "<body>"
                + "    <div class='container'>"
                + "        <div class='header'>"
                + "            <h2>" + headerTitle + "</h2>"
                + "        </div>"
                + "        <div class='content'>"
                + "            <p>Bonjour " + clientName + ",</p>";

        if (isApproved) {
            emailBody += "            <p>Nous avons le plaisir de vous informer que votre demande de parrainage pour l'événement suivant a été <strong>approuvée</strong> par " + sponsorName + ":</p>";
        } else {
            emailBody += "            <p>Nous regrettons de vous informer que votre demande de parrainage pour l'événement suivant a été <strong>refusée</strong> par " + sponsorName + ":</p>";
        }

        emailBody += "            <div class='info-item'><span class='label'>Nom de l'événement:</span> " + event.getNom() + "</div>"
                + "            <div class='info-item'><span class='label'>Type:</span> " + event.getType() + "</div>"
                + "            <div class='info-item'><span class='label'>Description:</span> " + event.getDescription() + "</div>"
                + "            <div class='info-item'><span class='label'>Lieu:</span> " + event.getLieuEvenement() + "</div>"
                + "            <div class='info-item'><span class='label'>Date:</span> " + formattedStartDate + " au " + formattedEndDate + "</div>"
                + "            <div class='message'>"
                + "                <p><span class='label'>Message de " + sponsorName + ":</span></p>"
                + "                <p>" + (responseMessage != null ? responseMessage.replace("\n", "<br>") : "Aucun message fourni") + "</p>"
                + "            </div>";

        if (isApproved && approvedAmount != null) {
            emailBody += "            <div class='info-item'><span class='label'>Montant approuvé:</span> <span class='amount'>"
                    + String.format("%.2f DT", approvedAmount) + "</span></div>"
                    + "            <p>Veuillez contacter " + sponsorName + " pour discuter des détails de cette collaboration et finaliser les arrangements.</p>";
        }

        if (isApproved) {
            emailBody += "            <p>Félicitations pour avoir obtenu ce parrainage pour votre événement!</p>";
        } else {
            emailBody += "            <p>N'hésitez pas à soumettre d'autres demandes de parrainage ou à modifier votre demande actuelle pour une future considération.</p>";
        }

        emailBody += "            <p>Merci d'utiliser notre Système de Gestion d'Événements.</p>"
                + "            <p>Cordialement,<br>L'Équipe de Gestion d'Événements</p>"
                + "        </div>"
                + "        <div class='footer'>"
                + "            <p>Ceci est un message automatique. Veuillez ne pas répondre à cet email.</p>"
                + "            <p>Système de Gestion d'Événements - " + java.time.Year.now().getValue() + "</p>"
                + "            <p>Envoyé le: " + currentDateTime + "</p>"
                + "        </div>"
                + "    </div>"
                + "</body>"
                + "</html>";

        // Send the email notification
        try {
            return sendEmail(clientEmail, subject, emailBody);
        } catch (Exception e) {
            System.err.println("Error sending sponsorship response email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    public static boolean sendSponsorshipRequestEmail(String sponsorEmail, String sponsorName,
                                                      String clientName, Evenement event,
                                                      String justification, Double requestedBudget) {
        if (sponsorEmail == null || sponsorEmail.isEmpty()) {
            System.err.println("Cannot send sponsorship request: Invalid sponsor email");
            return false;
        }

        // Format dates for better readability
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
        String formattedStartDate = event.getDateDebut() != null ? dateFormat.format(event.getDateDebut()) : "Non spécifié";
        String formattedEndDate = event.getDateFin() != null ? dateFormat.format(event.getDateFin()) : "Non spécifié";

        // Current date and time for the email
        String currentDateTime = "2025-03-02 22:34:14"; // Use the provided time

        // Subject for the sponsorship request
        String subject = "New Sponsorship Request: " + event.getNom();

        // Email body for the sponsorship request
        String emailBody = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "    <style>"
                + "        body { font-family: Arial, sans-serif; line-height: 1.6; }"
                + "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }"
                + "        .header { background-color: #4dabf7; color: white; padding: 15px; text-align: center; }"
                + "        .content { padding: 20px; border: 1px solid #ddd; }"
                + "        .footer { text-align: center; margin-top: 20px; font-size: 12px; color: #777; }"
                + "        .info-item { margin-bottom: 10px; }"
                + "        .label { font-weight: bold; }"
                + "        .highlight { background-color: #f8f9fa; padding: 15px; border-left: 4px solid #4dabf7; margin: 15px 0; }"
                + "        .budget { color: #e83e8c; font-weight: bold; font-size: 1.1em; }"
                + "        .button { display: inline-block; background-color: #007bff; color: white; padding: 10px 20px; "
                + "                 text-decoration: none; border-radius: 5px; margin-top: 15px; }"
                + "    </style>"
                + "</head>"
                + "<body>"
                + "    <div class='container'>"
                + "        <div class='header'>"
                + "            <h2>New Sponsorship Request</h2>"
                + "        </div>"
                + "        <div class='content'>"
                + "            <p>Dear " + sponsorName + ",</p>"
                + "            <p>You have received a new sponsorship request from <strong>" + clientName + "</strong> for the following event:</p>"
                + "            <div class='highlight'>"
                + "                <div class='info-item'><span class='label'>Event Name:</span> " + event.getNom() + "</div>"
                + "                <div class='info-item'><span class='label'>Type:</span> " + event.getType() + "</div>"
                + "                <div class='info-item'><span class='label'>Description:</span> " + event.getDescription() + "</div>"
                + "                <div class='info-item'><span class='label'>Location:</span> " + event.getLieuEvenement() + "</div>"
                + "                <div class='info-item'><span class='label'>Date:</span> " + formattedStartDate + " to " + formattedEndDate + "</div>"
                + "                <div class='info-item'><span class='label'>Number of Invitees:</span> " + event.getNombreInvite() + "</div>";

        // Add budget information if available
        if (requestedBudget != null && requestedBudget > 0) {
            emailBody += "                <div class='info-item'><span class='label'>Requested Sponsorship Amount:</span> <span class='budget'>"
                    + String.format("%.2f DT", requestedBudget) + "</span></div>";
        }

        emailBody += "            </div>"
                + "            <div class='highlight'>"
                + "                <p><span class='label'>Client's Justification:</span></p>"
                + "                <p>" + justification.replace("\n", "<br>") + "</p>"
                + "            </div>"
                + "            <p>Please log in to your account to review this sponsorship request and make a decision.</p>"
                + "            <p>You can accept or decline this request based on your availability and interest.</p>"
                + "            <p>Thank you for your consideration.</p>"
                + "            <p>Best regards,<br>Event Management System</p>"
                + "        </div>"
                + "        <div class='footer'>"
                + "            <p>This is an automated message. Please do not reply to this email.</p>"
                + "            <p>Event Management System - " + java.time.Year.now().getValue() + "</p>"
                + "            <p>Sent on: " + currentDateTime + "</p>"
                + "        </div>"
                + "    </div>"
                + "</body>"
                + "</html>";

        // Send the email notification
        try {
            return sendEmail(sponsorEmail, subject, emailBody);
        } catch (Exception e) {
            System.err.println("Error sending sponsorship request email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
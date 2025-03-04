 package org.example.controllers;

import org.example.models.DemandeSponsoring;
import org.example.models.Evenement;
import org.example.models.RoleType;
import org.example.models.User;
import org.example.dao.DemandeSponsoringService;
import org.example.dao.EmailUtil;
import org.example.dao.EvenementService;
import org.example.dao.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ItemController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView img; // Image view for the event

    @FXML
    private Label nameLabel; // Label for the event name

    @FXML
    private Label priceLable; // Label for the event price

    private Evenement evenement; // Stores the current event

    private User currentUser; // Stores the current logged-in user

    @FXML
    private Button validateButton; // Button to validate an event

    @FXML
    private Button rejectButton; // Button to reject an event

    @FXML
    private Button sponsorEventButton; // Button to request sponsorship

    private RoleType role = RoleType.ADMIN; // Default role is CLIENT
    // Update these constants in both ItemController and AddEvent classes
    private static final String CURRENT_DATETIME = "2025-03-02 21:41:52";
    private static final String CURRENT_USER_LOGIN = "Syrine";

    @FXML
    void click(MouseEvent event) {
        // Method can be used for future click handling
    }

    @FXML
    void initialize() {
        applyRoundedCornersToImage(); // Apply rounded corners to the image
        updateButtonVisibility(role); // Update button visibility based on role
    }

    /**
     * Sets the current user of the application
     * @param user The currently logged-in user
     */


    /**
     * Updates the visibility of buttons based on the user's role
     * @param role The role of the current user
     */
    public void updateButtonVisibility(RoleType role) {
        validateButton.setVisible(false);
        rejectButton.setVisible(false);
        sponsorEventButton.setVisible(false);

        if (role == RoleType.ADMIN) {
            validateButton.setVisible(true);
            rejectButton.setVisible(true);
        } else if (role == RoleType.CLIENT) {
            sponsorEventButton.setVisible(true);
            if (sponsorEventButton.getParent() instanceof HBox) {
                HBox sponsorButtonContainer = (HBox) sponsorEventButton.getParent();
                sponsorButtonContainer.setAlignment(Pos.CENTER);
            }
        }
    }

    /**
     * Applies rounded corners to the image
     */
    private void applyRoundedCornersToImage() {
        if (img != null) {
            javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(
                    img.getFitWidth(), img.getFitHeight());
            clip.setArcWidth(20);
            clip.setArcHeight(20);
            img.setClip(clip);
        }
    }

    /**
     * Sets the event information in the interface
     * @param name Event name
     * @param price Event price
     * @param image Event image
     * @param evenement Event object
     */
    public void setLocation(String name, double price, Image image, Evenement evenement) {
        this.evenement = evenement;
        nameLabel.setText(name);
        priceLable.setText(String.format("%.2f DT", price));
        img.setImage(image);

        validateButton.setOnAction(e -> {
            try {
                handleValidate();
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error",
                        "Could not validate event: " + ex.getMessage());
            }
        });

        rejectButton.setOnAction(e -> {
            try {
                handleReject();
            } catch (SQLException ex) {
                showAlert(Alert.AlertType.ERROR, "Database Error",
                        "Could not reject event: " + ex.getMessage());
            }
        });
    }

    /**
     * Handles the validation of an event
     * @throws SQLException if database operation fails
     */

    private void handleValidate() throws SQLException {
        if (evenement != null) {
            // Ask for confirmation
            boolean confirmed = showConfirmationDialog(
                    "Validate Event",
                    "Are you sure you want to validate this event?",
                    "Event: " + evenement.getNom()
            );

            if (confirmed) {
                // Get any additional notes for the approval
                String approvalNotes = getTextInput(
                        "Approval Notes (Optional)",
                        "Add any notes or comments about this approval:",
                        "These will be shared with the event creator."
                );

                // Update event status in the database
                evenement.setValidated(true);
                EvenementService evenementService = new EvenementService();
                evenementService.updateEventValidation(evenement.getId(), true);

                // Debug information
                System.out.println("Event: " + evenement.getNom() + " (ID: " + evenement.getId() + ")");
                System.out.println("User associated with event: " + (evenement.getUser() == null ? "NULL" : evenement.getUser().getNom()));

                // Proper null check before accessing user's email
                boolean hasValidUserEmail = evenement.getUser() != null &&
                        evenement.getUser().getEmail() != null &&
                        !evenement.getUser().getEmail().isEmpty();

                if (hasValidUserEmail) {
                    boolean emailSent = sendEventValidationEmail(evenement, approvalNotes);

                    if (emailSent) {
                        showAlert(Alert.AlertType.INFORMATION, "Success",
                                "Event has been validated successfully and notification email has been sent to " +
                                        evenement.getUser().getEmail());
                    } else {
                        showAlert(Alert.AlertType.INFORMATION, "Success",
                                "Event has been validated successfully, but there was a problem sending the notification email.");
                    }
                } else {
                    // If user is null or has no email, show a different message
                    showAlert(Alert.AlertType.INFORMATION, "Success",
                            "Event has been validated successfully. No email notification was sent (client information not available).");

                    System.out.println("Cannot send email notification: User or email address is missing for event ID " + evenement.getId());
                }

                refreshScene(validateButton);
            }
        }
    }
    /**
     * Handles the rejection of an event
     * @throws SQLException if database operation fails
     */
    private void handleReject() throws SQLException {
        if (evenement != null) {
            // Ask for confirmation
            boolean confirmed = showConfirmationDialog(
                    "Reject Event",
                    "Are you sure you want to reject this event?",
                    "Event: " + evenement.getNom()
            );

            if (confirmed) {
                // Get rejection reason
                String reason = getTextInput(
                        "Rejection Reason",
                        "Please provide a reason for rejecting this event:",
                        "This information will be shared with the event creator."
                );

                if (reason != null && !reason.trim().isEmpty()) {
                    // Update event status in the database
                    evenement.setValidated(false);
                    EvenementService evenementService = new EvenementService();
                    evenementService.updateEventValidation(evenement.getId(), false);

                    // Send rejection notification email to the client
                    if (evenement.getUser() != null && evenement.getUser().getEmail() != null) {
                        boolean emailSent = sendEventRejectionEmail(evenement, reason);

                        if (emailSent) {
                            showAlert(Alert.AlertType.INFORMATION, "Success",
                                    "Event has been rejected with reason: " + reason +
                                            ". A notification email has been sent to " + evenement.getUser().getEmail());
                        } else {
                            showAlert(Alert.AlertType.INFORMATION, "Success",
                                    "Event has been rejected with reason: " + reason +
                                            ", but there was a problem sending the notification email.");
                        }
                    } else {
                        // We don't have the creator's email, just show basic success message
                        showAlert(Alert.AlertType.INFORMATION, "Success",
                                "Event has been rejected with reason: " + reason +
                                        ". No email notification was sent (no client email available).");
                    }

                    refreshScene(rejectButton);
                } else {
                    showAlert(Alert.AlertType.WARNING, "Missing Information",
                            "You must provide a reason for rejecting the event.");
                }
            }
        }
    }

    private boolean sendEventValidationEmail(Evenement event, String approvalNotes) {
        if (event.getUser() == null || event.getUser().getEmail() == null || event.getUser().getEmail().isEmpty()) {
            System.err.println("Cannot send notification: No valid client email for event #" + event.getId());
            return false;
        }

        String recipientEmail = event.getUser().getEmail();
        String recipientName = event.getUser().getNom();

        // Format dates for better readability
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
        String formattedStartDate = event.getDateDebut() != null ? dateFormat.format(event.getDateDebut()) : "Non spécifié";
        String formattedEndDate = event.getDateFin() != null ? dateFormat.format(event.getDateFin()) : "Non spécifié";

        // Subject for approval
        String subject = "Votre Événement a été Approuvé: " + event.getNom();

        // Email body for approval
        String emailBody = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "    <style>"
                + "        body { font-family: Arial, sans-serif; line-height: 1.6; }"
                + "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }"
                + "        .header { background-color: #28a745; color: white; padding: 15px; text-align: center; }"
                + "        .content { padding: 20px; border: 1px solid #ddd; }"
                + "        .footer { text-align: center; margin-top: 20px; font-size: 12px; color: #777; }"
                + "        .info-item { margin-bottom: 10px; }"
                + "        .label { font-weight: bold; }"
                + "        .notes { background-color: #f8f9fa; padding: 15px; border-left: 4px solid #28a745; margin: 15px 0; }"
                + "    </style>"
                + "</head>"
                + "<body>"
                + "    <div class='container'>"
                + "        <div class='header'>"
                + "            <h2>Événement Approuvé! 🎉</h2>"
                + "        </div>"
                + "        <div class='content'>"
                + "            <p>Bonjour " + recipientName + ",</p>"
                + "            <p>Nous sommes heureux de vous informer que votre événement a été approuvé par notre équipe d'administration!</p>"
                + "            <div class='info-item'><span class='label'>Nom de l'événement:</span> " + event.getNom() + "</div>"
                + "            <div class='info-item'><span class='label'>Type:</span> " + event.getType() + "</div>"
                + "            <div class='info-item'><span class='label'>Description:</span> " + event.getDescription() + "</div>"
                + "            <div class='info-item'><span class='label'>Lieu:</span> " + event.getLieuEvenement() + "</div>"
                + "            <div class='info-item'><span class='label'>Date de début:</span> " + formattedStartDate + "</div>"
                + "            <div class='info-item'><span class='label'>Date de fin:</span> " + formattedEndDate + "</div>"
                + "            <div class='info-item'><span class='label'>Budget:</span> " + event.getBudgetPrevu() + " DT</div>";

        // Add admin notes if provided
        if (approvalNotes != null && !approvalNotes.trim().isEmpty()) {
            emailBody += "            <div class='notes'>"
                    + "                <p><span class='label'>Notes de l'administrateur:</span></p>"
                    + "                <p>" + approvalNotes + "</p>"
                    + "            </div>";
        }

        emailBody += "            <p>Votre événement est maintenant visible pour tous les utilisateurs sur notre plateforme. Vous pouvez maintenant procéder aux arrangements supplémentaires pour votre événement.</p>"
                + "            <p>Besoin de sponsors? N'oubliez pas que vous pouvez demander des parrainages pour votre événement via notre plateforme!</p>"
                + "            <p>Merci d'utiliser notre Système de Gestion d'Événements!</p>"
                + "            <p>Cordialement,<br>L'Équipe d'Administration</p>"
                + "        </div>"
                + "        <div class='footer'>"
                + "            <p>Ceci est un message automatique. Veuillez ne pas répondre à cet email.</p>"
                + "            <p>Système de Gestion d'Événements - " + java.time.Year.now().getValue() + "</p>"
                + "            <p>Envoyé le: " + CURRENT_DATETIME + "</p>"
                + "            <p>Par: " + "Admin" + "</p>"
                + "        </div>"
                + "    </div>"
                + "</body>"
                + "</html>";

        // Send the email notification
        try {
            return EmailUtil.sendEmail(recipientEmail, subject, emailBody);
        } catch (Exception e) {
            System.err.println("Error sending event validation email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private boolean sendEventRejectionEmail(Evenement event, String rejectionReason) {
        if (event.getUser() == null || event.getUser().getEmail() == null || event.getUser().getEmail().isEmpty()) {
            System.err.println("Cannot send notification: No valid client email for event #" + event.getId());
            return false;
        }

        String recipientEmail = event.getUser().getEmail();
        String recipientName = event.getUser().getNom();

        // Format dates for better readability
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, d MMMM yyyy");
        String formattedStartDate = event.getDateDebut() != null ? dateFormat.format(event.getDateDebut()) : "Non spécifié";
        String formattedEndDate = event.getDateFin() != null ? dateFormat.format(event.getDateFin()) : "Non spécifié";

        // Subject for rejection
        String subject = "Votre Événement N'a Pas Été Approuvé: " + event.getNom();

        // Email body for rejection
        String emailBody = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "    <style>"
                + "        body { font-family: Arial, sans-serif; line-height: 1.6; }"
                + "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }"
                + "        .header { background-color: #dc3545; color: white; padding: 15px; text-align: center; }"
                + "        .content { padding: 20px; border: 1px solid #ddd; }"
                + "        .footer { text-align: center; margin-top: 20px; font-size: 12px; color: #777; }"
                + "        .info-item { margin-bottom: 10px; }"
                + "        .label { font-weight: bold; }"
                + "        .reason { background-color: #f8f9fa; padding: 15px; border-left: 4px solid #dc3545; margin: 15px 0; }"
                + "    </style>"
                + "</head>"
                + "<body>"
                + "    <div class='container'>"
                + "        <div class='header'>"
                + "            <h2>Événement Non Approuvé</h2>"
                + "        </div>"
                + "        <div class='content'>"
                + "            <p>Bonjour " + recipientName + ",</p>"
                + "            <p>Nous regrettons de vous informer que votre événement n'a pas pu être approuvé pour le moment.</p>"
                + "            <div class='info-item'><span class='label'>Nom de l'événement:</span> " + event.getNom() + "</div>"
                + "            <div class='info-item'><span class='label'>Type:</span> " + event.getType() + "</div>"
                + "            <div class='info-item'><span class='label'>Description:</span> " + event.getDescription() + "</div>"
                + "            <div class='info-item'><span class='label'>Lieu:</span> " + event.getLieuEvenement() + "</div>"
                + "            <div class='info-item'><span class='label'>Date prévue:</span> " + formattedStartDate + " au " + formattedEndDate + "</div>"
                + "            <div class='reason'>"
                + "                <p><span class='label'>Raison du refus:</span></p>"
                + "                <p>" + rejectionReason + "</p>"
                + "            </div>"
                + "            <p>Vous pouvez apporter les modifications nécessaires et soumettre à nouveau votre événement pour approbation.</p>"
                + "            <p>Si vous avez des questions ou avez besoin de clarifications supplémentaires concernant les raisons du refus, n'hésitez pas à contacter notre équipe de support.</p>"
                + "            <p>Merci pour votre compréhension.</p>"

                + "            <p>Cordialement,<br>L'équipe d'Administration</p>"
                + "        </div>"
                + "        <div class='footer'>"
                + "            <p>Ceci est un message automatique. Veuillez ne pas répondre à cet email.</p>"
                + "            <p>Système de Gestion d'Événements - " + java.time.Year.now().getValue() + "</p>"
                + "            <p>Envoyé le: " + CURRENT_DATETIME + "</p>"
                + "            <p>Par: " + "Admin" + "</p>"
                + "        </div>"
                + "    </div>"
                + "</body>"
                + "</html>";

        // Send the email notification
        try {
            return EmailUtil.sendEmail(recipientEmail, subject, emailBody);
        } catch (Exception e) {
            System.err.println("Error sending event rejection email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    /**
     * Sends an email notification to the client about event validation status
     *
     * @param event The event that was validated or rejected
     * @param isValidated Whether the event was validated (true) or rejected (false)
     * @param rejectionReason The reason for rejection (null if the event was validated)
     */
    private void notifyClientAboutEventValidation(Evenement event, boolean isValidated, String rejectionReason) {
        try {
            // Get the client's information using the event's user ID
            int clientId = event.getUser().getId();
            User client = new UserService().getUserById(clientId);

            if (client != null && client.getEmail() != null && !client.getEmail().isEmpty()) {
                String subject;
                String emailBody;

                if (isValidated) {
                    // Subject for validation
                    subject = "Your Event Has Been Approved: " + event.getNom();

                    // Email body for validation
                    emailBody = "<!DOCTYPE html>"
                            + "<html>"
                            + "<head>"
                            + "    <style>"
                            + "        body { font-family: Arial, sans-serif; line-height: 1.6; }"
                            + "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }"
                            + "        .header { background-color: #28a745; color: white; padding: 15px; text-align: center; }"
                            + "        .content { padding: 20px; border: 1px solid #ddd; }"
                            + "        .footer { text-align: center; margin-top: 20px; font-size: 12px; color: #777; }"
                            + "        .info-item { margin-bottom: 10px; }"
                            + "        .label { font-weight: bold; }"
                            + "        .button { display: inline-block; background-color: #4dabf7; color: white; padding: 10px 20px; "
                            + "                 text-decoration: none; border-radius: 5px; margin-top: 15px; }"
                            + "    </style>"
                            + "</head>"
                            + "<body>"
                            + "    <div class='container'>"
                            + "        <div class='header'>"
                            + "            <h2>Event Approved! 🎉</h2>"
                            + "        </div>"
                            + "        <div class='content'>"
                            + "            <p>Hello " + client.getNom() + ",</p>"
                            + "            <p>We're pleased to inform you that your event has been approved by our administration team!</p>"
                            + "            <div class='info-item'><span class='label'>Event Name:</span> " + event.getNom() + "</div>"
                            + "            <div class='info-item'><span class='label'>Description:</span> " + event.getDescription() + "</div>"
                            + "            <div class='info-item'><span class='label'>Location:</span> " + event.getLieuEvenement() + "</div>"
                            + "            <div class='info-item'><span class='label'>Start Date:</span> " + event.getDateDebut() + "</div>"
                            + "            <div class='info-item'><span class='label'>End Date:</span> " + event.getDateFin() + "</div>"
                            + "            <div class='info-item'><span class='label'>Price:</span> " + event.getBudgetPrevu() + " DT</div>"
                            + "            <p>Your event is now visible to all users on our platform. You can now proceed with further arrangements for your event.</p>"
                            + "            <p>Need sponsors? Don't forget you can request sponsorships for your event through our platform!</p>"
                            + "            <p>Thank you for using our Event Management System!</p>"
                            + "            <p>Best regards,<br>The Administration Team</p>"
                            + "        </div>"
                            + "        <div class='footer'>"
                            + "            <p>This is an automated message. Please do not reply to this email.</p>"
                            + "            <p>Event Management System - " + java.time.LocalDate.now().getYear() + "</p>"
                            + "        </div>"
                            + "    </div>"
                            + "</body>"
                            + "</html>";
                } else {
                    // Subject for rejection
                    subject = "Your Event Was Not Approved: " + event.getNom();

                    // Email body for rejection
                    emailBody = "<!DOCTYPE html>"
                            + "<html>"
                            + "<head>"
                            + "    <style>"
                            + "        body { font-family: Arial, sans-serif; line-height: 1.6; }"
                            + "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }"
                            + "        .header { background-color: #dc3545; color: white; padding: 15px; text-align: center; }"
                            + "        .content { padding: 20px; border: 1px solid #ddd; }"
                            + "        .footer { text-align: center; margin-top: 20px; font-size: 12px; color: #777; }"
                            + "        .info-item { margin-bottom: 10px; }"
                            + "        .label { font-weight: bold; }"
                            + "        .reason { background-color: #f8f9fa; padding: 15px; border-left: 4px solid #dc3545; margin: 15px 0; }"
                            + "        .button { display: inline-block; background-color: #4dabf7; color: white; padding: 10px 20px; "
                            + "                 text-decoration: none; border-radius: 5px; margin-top: 15px; }"
                            + "    </style>"
                            + "</head>"
                            + "<body>"
                            + "    <div class='container'>"
                            + "        <div class='header'>"
                            + "            <h2>Event Not Approved</h2>"
                            + "        </div>"
                            + "        <div class='content'>"
                            + "            <p>Hello " + client.getNom() + ",</p>"
                            + "            <p>We regret to inform you that your event could not be approved at this time.</p>"
                            + "            <div class='info-item'><span class='label'>Event Name:</span> " + event.getNom() + "</div>"
                            + "            <div class='info-item'><span class='label'>Description:</span> " + event.getDescription() + "</div>"
                            + "            <div class='info-item'><span class='label'>Location:</span> " + event.getLieuEvenement() + "</div>"
                            + "            <div class='info-item'><span class='label'>Submitted on:</span> " + event.getType() + "</div>"
                            + "            <div class='reason'>"
                            + "                <p><span class='label'>Reason for rejection:</span></p>"
                            + "                <p>" + rejectionReason + "</p>"
                            + "            </div>"
                            + "            <p>You may make the necessary adjustments and resubmit your event for approval.</p>"
                            + "            <p>If you have any questions or need further clarification about the rejection reason, please contact our support team.</p>"
                            + "            <p>Thank you for your understanding.</p>"
                            + "            <p>Regards,<br>The Administration Team</p>"
                            + "        </div>"
                            + "        <div class='footer'>"
                            + "            <p>This is an automated message. Please do not reply to this email.</p>"
                            + "            <p>Event Management System - " + java.time.LocalDate.now().getYear() + "</p>"
                            + "        </div>"
                            + "    </div>"
                            + "</body>"
                            + "</html>";
                }

                // Send the email notification
                try {
                    boolean emailSent = EmailUtil.sendEmail(client.getEmail(), subject, emailBody);

                    if (emailSent) {
                        System.out.println("Event " + (isValidated ? "validation" : "rejection") + " email sent successfully to: " + client.getEmail());
                    } else {
                        System.err.println("Failed to send event " + (isValidated ? "validation" : "rejection") + " email to: " + client.getEmail());
                    }
                } catch (Exception e) {
                    System.err.println("Error sending event validation email: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.err.println("Could not send notification email: Client information is missing or incomplete");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving client information for email notification: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Unexpected error when sending event validation notification: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Refreshes the scene after validation/rejection
     * @param button The button that was clicked
     */
    private void refreshScene(Button button) {
        Stage currentStage = (Stage) button.getScene().getWindow();
        currentStage.hide();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/events.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Events Management");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Navigation Error",
                    "Could not return to events page: " + e.getMessage());
        }
    }

    @FXML
    private ComboBox<User> sponsorComboBox;

    /**
     * Handles the sponsorship request process
     * @param event The action event
     */
    @FXML
    void handleSponsorRequest(ActionEvent event) {
        // Current date and time for logging
        String currentDateTime = CURRENT_DATETIME;

        if (evenement == null) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Aucun événement sélectionné");
            return;
        }

        // Check if the event is already validated
        if (!evenement.isValidated()) {
            showAlert(Alert.AlertType.WARNING, "Événement Non Validé",
                    "Cet événement doit être validé par un administrateur avant de pouvoir faire des demandes de parrainage.");
            return;
        }

        // Check if there's already a pending sponsorship request
        if (hasActiveSponsorshipRequest()) {
            showAlert(Alert.AlertType.WARNING, "Demande Existante",
                    "Cet événement a déjà une demande de parrainage en attente. Veuillez attendre une réponse avant de faire une nouvelle demande.");
            return;
        }

        // Create a dialog for sponsor selection
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("Demande de Parrainage");
        dialog.setHeaderText("Demander un parrainage pour: " + evenement.getNom());

        ButtonType submitButtonType = new ButtonType("Soumettre la Demande", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(submitButtonType, ButtonType.CANCEL);

        // Create the content for the dialog
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(15);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Create sponsor selection dropdown
        sponsorComboBox = new ComboBox<>();
        sponsorComboBox.setPromptText("Sélectionner un sponsor");
        sponsorComboBox.setPrefWidth(300);

        // Create justification input field
        TextArea justificationArea = new TextArea();
        justificationArea.setPromptText("Entrez votre justification pour le parrainage");
        justificationArea.setPrefRowCount(5);
        justificationArea.setWrapText(true);
        GridPane.setHgrow(justificationArea, Priority.ALWAYS);
        GridPane.setVgrow(justificationArea, Priority.ALWAYS);

        // Add budget field
        TextField budgetField = new TextField();
        budgetField.setPromptText("Entrez le budget requis (DT)");

        // Add form fields to grid
        grid.add(new Label("Sélectionner un Sponsor:"), 0, 0);
        grid.add(sponsorComboBox, 1, 0);
        grid.add(new Label("Justification:"), 0, 1);
        grid.add(justificationArea, 1, 1);
        grid.add(new Label("Budget Requis (DT):"), 0, 2);
        grid.add(budgetField, 1, 2);

        dialog.getDialogPane().setContent(grid);

        // Load sponsor users from database
        loadSponsors();

        // Request focus on the sponsor combobox by default
        dialog.setOnShown(dialogEvent -> sponsorComboBox.requestFocus());

        // Enable/disable submit button based on input validation
        Button submitButton = (Button) dialog.getDialogPane().lookupButton(submitButtonType);
        submitButton.setDisable(true);

        // Add listeners to validate form input
        sponsorComboBox.valueProperty().addListener((observable, oldValue, newValue) ->
                validateSponsorForm(submitButton, sponsorComboBox, justificationArea, budgetField));

        justificationArea.textProperty().addListener((observable, oldValue, newValue) ->
                validateSponsorForm(submitButton, sponsorComboBox, justificationArea, budgetField));

        budgetField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Ensure budget field only accepts numbers
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                budgetField.setText(oldValue);
            }
            validateSponsorForm(submitButton, sponsorComboBox, justificationArea, budgetField);
        });

        // Process the result
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == submitButtonType) {
            User selectedSponsor = sponsorComboBox.getValue();
            String justification = justificationArea.getText().trim();

            // Process budget value
            Double requestedBudget = null;
            if (!budgetField.getText().trim().isEmpty()) {
                try {
                    requestedBudget = Double.parseDouble(budgetField.getText().trim());
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing budget: " + e.getMessage());
                }
            }

            try {
                // Create budget note combining justification and budget
                String budgetNote = justification;
                if (requestedBudget != null) {
                    budgetNote += "\n\nBudget requis: " + String.format("%.2f DT", requestedBudget);
                }

                // Create sponsorship request
                DemandeSponsoring demande = new DemandeSponsoring(
                        0,
                        selectedSponsor.getNom(),  // Store sponsor name
                        evenement.getId(),
                        "Pending",
                        budgetNote
                );

                // Save to database
                new DemandeSponsoringService().addDemandeSponsoring(demande);

                // Get current user (client) information
                String clientName = CURRENT_USER_LOGIN; // Use the static login
                if (evenement.getUser() != null) {
                    clientName = evenement.getUser().getNom();
                }

                System.out.println("Sending sponsorship request email to: " + selectedSponsor.getEmail() +
                        " from: " + clientName + " for event: " + evenement.getNom());

                // Send email notification to the sponsor
                boolean emailSent = EmailUtil.sendSponsorshipRequestEmail(
                        selectedSponsor.getEmail(),
                        selectedSponsor.getNom(),
                        clientName,
                        evenement,
                        justification,
                        requestedBudget
                );

                // Show success message with email status
                if (emailSent) {
                    showAlert(Alert.AlertType.INFORMATION, "Demande de Parrainage Envoyée",
                            "Votre demande de parrainage a été envoyée avec succès à "
                                    + selectedSponsor.getNom() + ".\n\nIl(Elle) a été notifié(e) de votre demande par email.");

                    System.out.println("[" + currentDateTime + "] Email de demande de parrainage envoyé avec succès à: "
                            + selectedSponsor.getEmail() + " pour l'événement: " + evenement.getNom());
                } else {
                    showAlert(Alert.AlertType.INFORMATION, "Demande de Parrainage Envoyée",
                            "Votre demande de parrainage a été envoyée avec succès à "
                                    + selectedSponsor.getNom() + ".\n\nCependant, il y a eu un problème lors de l'envoi de l'email de notification.");

                    System.err.println("[" + currentDateTime + "] Échec de l'envoi de l'email de demande de parrainage à: "
                            + selectedSponsor.getEmail() + " pour l'événement: " + evenement.getNom());
                }

            } catch (SQLException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur de Base de Données",
                        "Échec de l'envoi de la demande de parrainage: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
    private boolean hasActiveSponsorshipRequest() {
        try {
            DemandeSponsoringService service = new DemandeSponsoringService();
            return service.hasPendingRequests(evenement.getId());
        } catch (SQLException e) {
            System.err.println("Error checking active sponsorship requests: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Validates the sponsor request form
     */
    private void validateSponsorForm(Button submitButton, ComboBox<User> sponsorBox,
                                     TextArea justificationArea, TextField budgetField) {
        boolean isValid = sponsorBox.getValue() != null &&
                !justificationArea.getText().trim().isEmpty();
        submitButton.setDisable(!isValid);
    }

    /**
     * Loads sponsors from the database into the ComboBox
     */
    private void loadSponsors() {
        try {
            UserService userService = new UserService();
            List<User> allUsers = userService.getAllUsers();

            // Filter users to only include sponsors
            List<User> sponsors = allUsers.stream()
                    .filter(user -> user.getRole() == RoleType.SPONSOR)
                    .collect(Collectors.toList());

            // Add sponsors to combo box
            sponsorComboBox.getItems().clear();
            sponsorComboBox.getItems().addAll(sponsors);

            // Set a custom cell factory to display the sponsor name
            sponsorComboBox.setCellFactory(param -> new ListCell<User>() {
                @Override
                protected void updateItem(User user, boolean empty) {
                    super.updateItem(user, empty);

                    if (empty || user == null) {
                        setText(null);
                    } else {
                        setText(user.getNom());
                    }
                }
            });

            // Set the same converter for the selected value
            sponsorComboBox.setConverter(new javafx.util.StringConverter<User>() {
                @Override
                public String toString(User user) {
                    return user == null ? null : user.getNom();
                }

                @Override
                public User fromString(String string) {
                    return sponsorComboBox.getItems().stream()
                            .filter(user -> user.getNom().equals(string))
                            .findFirst().orElse(null);
                }
            });

            if (sponsors.isEmpty()) {
                showAlert(Alert.AlertType.WARNING, "No Sponsors Available",
                        "There are currently no sponsors registered in the system.");
            }

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Could not load sponsors: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Shows a confirmation dialog
     * @return true if confirmed, false otherwise
     */
    private boolean showConfirmationDialog(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        return result.isPresent() && result.get() == ButtonType.OK;
    }

    /**
     * Gets text input from the user via a dialog
     * @return The user input or null if cancelled
     */
    private String getTextInput(String title, String header, String content) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(header);
        dialog.setContentText(content);

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }
    private TextArea createTextAreaDialog(Dialog<String> dialog, String prompt) {
        TextArea textArea = new TextArea();
        textArea.setPromptText(prompt);
        textArea.setPrefColumnCount(40);
        textArea.setPrefRowCount(5);

        // Set the result converter to use the text area's text
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                return textArea.getText();
            }
            return null;
        });

        return textArea;
    }


    /**
     * Shows an alert dialog
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Shows event details in a dialog
     */
    @FXML
    private void showEventDetails(MouseEvent event) {
        if (evenement == null) return;

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Event Details");
        dialog.setHeaderText(evenement.getNom());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        // Add event details
        grid.add(new Label("Description:"), 0, 0);
        grid.add(new Label(evenement.getDescription()), 1, 0);

        grid.add(new Label("Price:"), 0, 1);
        grid.add(new Label(String.format("%.2f DT", evenement.getBudgetPrevu())), 1, 1);

        grid.add(new Label("Location:"), 0, 2);
        grid.add(new Label(evenement.getLieuEvenement()), 1, 2);

        grid.add(new Label("Start Date:"), 0, 3);
        grid.add(new Label(evenement.getDateDebut().toString()), 1, 3);

        grid.add(new Label("End Date:"), 0, 4);
        grid.add(new Label(evenement.getDateFin().toString()), 1, 4);

        grid.add(new Label("Status:"), 0, 5);
        Label statusLabel = new Label(evenement.isValidated() ? "Validated" : "Not Validated");
        statusLabel.setStyle(evenement.isValidated() ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
        grid.add(statusLabel, 1, 5);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);

        dialog.showAndWait();
    }

    /**
     * Checks if the event already has a pending sponsorship request
     */

}

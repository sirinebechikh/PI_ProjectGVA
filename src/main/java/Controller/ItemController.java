package Controller;


import Entities.DemandeSponsoring;
import Entities.Evenement;
import Entities.RoleType;
import Entities.User;
import Service.DemandeSponsoringService;
import Service.EvenementService;
import Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class ItemController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView img;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLable;

    private Evenement evenement;

    @FXML
    private Button validateButton;

    @FXML
    private Button rejectButton;
    @FXML
    private Button sponsorEventButton;



    private RoleType role=RoleType.SPONSOR;
    @FXML
    void click(MouseEvent event) {


    }

    @FXML
    void initialize() {
        applyRoundedCornersToImage();
        updateButtonVisibility(role);

    }

    // Static method to update button visibility based on role
    public void updateButtonVisibility(RoleType role) {
        // Hide all buttons by default
        // Hide all buttons by default
        validateButton.setVisible(false);
        rejectButton.setVisible(false);
        // Show the button corresponding to the given role
        if (role == RoleType.ADMIN) {
            validateButton.setVisible(true);
            rejectButton.setVisible(true);
            sponsorEventButton.setVisible(false);
        } else if (role == RoleType.CLIENT) {
            validateButton.setVisible(false);
            rejectButton.setVisible(false);
            // Center the sponsorEventButton
            sponsorEventButton.setVisible(true); // Ensure it's visible
            HBox sponsorButtonContainer = (HBox) sponsorEventButton.getParent(); // Get the parent HBox
            sponsorButtonContainer.setAlignment(Pos.CENTER); // Center the button inside its container
        } else if (role == RoleType.SPONSOR) {
            validateButton.setVisible(false);
            rejectButton.setVisible(false);
            sponsorEventButton.setVisible(false); // Ensure it's visible

        }
    }

    // Method to apply rounded corners
    private void applyRoundedCornersToImage() {
        if (img != null) {
            // Create a rectangle with the same dimensions as the ImageView
            javafx.scene.shape.Rectangle clip = new javafx.scene.shape.Rectangle(img.getFitWidth(), img.getFitHeight());

            // Set the corner radius
            clip.setArcWidth(20);  // Adjust this value for more or less rounding
            clip.setArcHeight(20);

            // Apply the clip to the ImageView
            img.setClip(clip);
        }
    }
    // Method to set location information in the UI
    public void setLocation(String name, double price, Image image, Evenement evenement) {
        this.evenement = evenement; // Store event reference
        nameLabel.setText(name);
        priceLable.setText("$" + String.format("%.2f", price)); // Format price with two decimal places
        img.setImage(image);
        validateButton.setOnAction(e -> {
            try {
                handleValidate();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
        rejectButton.setOnAction(e -> {
            try {
                handleReject();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });
    }
    private void handleValidate() throws SQLException {
        if (evenement != null) {
            evenement.setValidated(true);
            EvenementService evenementService = new EvenementService();
            evenementService.updateEventValidation(evenement.getId(), true);
            System.out.println("Event validated: " + evenement.getNom());
            refreshScene(validateButton);
        }
    }

    private void handleReject() throws SQLException {
        if (evenement != null) {
            evenement.setValidated(false);
            EvenementService evenementService = new EvenementService();
            evenementService.updateEventValidation(evenement.getId(), false);
            System.out.println("Event rejected: " + evenement.getNom());
            refreshScene(validateButton);
        }
    }
    private void refreshScene(Button button) {
        button.getScene().getWindow().hide(); // Hide current window
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/events.fxml"));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    private ComboBox<String> sponsorComboBox; // Declare the ComboBox in your FXML file

    @FXML
    void handleSponsorRequest(ActionEvent event) {
        // Load all users into the ComboBox (Make sure it's initialized somewhere in your controller)
        UserService userService = new UserService();
        List<User> users = null; // Fetch users from DB
        try {
            users = userService.getAllUsers();
            System.out.println(users);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        // Extract only the names
        List<String> userNames = new ArrayList<>();
        for (User user : users) {
            userNames.add(user.getNom()); // Assuming User has a getName() method
        }

        sponsorComboBox.getItems().clear();
        sponsorComboBox.getItems().addAll(userNames);

        // Create dialog for user selection
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Select Sponsor");
        dialog.setHeaderText("Choose a sponsor for this event");

        // Add ComboBox to the dialog
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.getChildren().add(sponsorComboBox);
        dialog.getDialogPane().setContent(vbox);

        // Add buttons
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Show dialog and wait for response
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            String selectedUser = sponsorComboBox.getSelectionModel().getSelectedItem();
            if (selectedUser != null) {
                String justification = getJustification(); // Open TextInputDialog for justification
                if (justification != null && !justification.trim().isEmpty()) {
                    try {
                        int eventId = evenement.getId();
                        String sponsorId = selectedUser;

                        // Create sponsorship request
                        DemandeSponsoring demande = new DemandeSponsoring(0, sponsorId, eventId, "Pending", justification);
                        DemandeSponsoringService service = new DemandeSponsoringService();
                        service.addDemandeSponsoring(demande);

                        // Show success message
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Sponsorship request sent successfully!");
                    } catch (SQLException e) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to send sponsorship request: " + e.getMessage());
                        e.printStackTrace();
                    }
                } else {
                    showAlert(Alert.AlertType.WARNING, "Warning", "Justification cannot be empty!");
                }
            } else {
                showAlert(Alert.AlertType.WARNING, "Warning", "You must select a sponsor!");
            }
        }
    }

    // Method to get justification input
    private String getJustification() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Sponsorship Request");
        dialog.setHeaderText("Enter Justification for Sponsorship");
        dialog.setContentText("Justification:");

        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null); // No header text
        alert.setContentText(message);
        alert.showAndWait();
    }


}

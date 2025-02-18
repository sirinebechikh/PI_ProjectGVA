package Controller;

import Entities.Evenement;
import Service.EvenementService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

public class AddEvent {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField activitiesField;

    @FXML
    private TextField budgetField;

    @FXML
    private DatePicker dateDebutPicker;

    @FXML
    private DatePicker dateFinPicker;

    @FXML
    private TextArea descriptionField;

    @FXML
    private TextField imageTF1;

    @FXML
    private TextField lieuField;

    @FXML
    private TextField nomField;

    @FXML
    private TextField nombreInviteField;

    @FXML
    private TextField typeField;
    @FXML
    private Pane imagePane;

    private EvenementService eventService = new EvenementService();

    @FXML
    void ajouterEvenement(ActionEvent event) {
        try {
            // Retrieve and validate input data
            String nom = nomField.getText().trim();
            String type = typeField.getText().trim();
            String lieu = lieuField.getText().trim();
            String description = descriptionField.getText().trim();
            String activities = activitiesField.getText().trim();
            String imagePath = imageTF1.getText().trim();

            if (nom.isEmpty() || type.isEmpty() || lieu.isEmpty() || description.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please fill in all required fields.");
                return;
            }

            int nombreInvite;
            double budget;

            try {
                nombreInvite = Integer.parseInt(nombreInviteField.getText().trim());
                budget = Double.parseDouble(budgetField.getText().trim());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid number format for guests or budget.");
                return;
            }

            LocalDate dateDebut = dateDebutPicker.getValue();
            LocalDate dateFin = dateFinPicker.getValue();

            if (dateDebut == null || dateFin == null || dateDebut.isAfter(dateFin)) {
                showAlert(Alert.AlertType.ERROR, "Error", "Invalid event dates.");
                return;
            }

            // Convert LocalDate to Date
            Date startDate = Date.from(dateDebut.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(dateFin.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Create Event Object
            Evenement newEvent = new Evenement(0, nom, type, nombreInvite, startDate, endDate, description, lieu, budget, activities, imagePath);

            // Add event to database
            eventService.addEvenement(newEvent);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Event added successfully!");

            // Clear fields after success
            clearFields();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Database Error", "Could not save event: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    private void clearFields() {
        nomField.clear();
        typeField.clear();
        nombreInviteField.clear();
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        descriptionField.clear();
        lieuField.clear();
        budgetField.clear();
        activitiesField.clear();
        imageTF1.clear();
        imagePane.getChildren().clear();
    }


    @FXML
    void chooseImageFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            // Set the image path to the imageTF text field
            imageTF1.setText(selectedFile.getAbsolutePath());

            // Load the selected image into the imagePane
            Image image = new Image(selectedFile.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(300); // Adjust the width as needed
            imageView.setFitHeight(300); // Adjust the height as needed
            imagePane.getChildren().clear(); // Clear existing content
            imagePane.getChildren().add(imageView); // Add the image to the pane
        }
    }

    @FXML
    void initialize() {

    }

}

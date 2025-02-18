package Controller;

import Entities.Evenement;
import Service.EvenementService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class UpdateEventController {

    @FXML private TextField nameField;
    @FXML private TextField typeField;
    @FXML private TextField budgetField;
    @FXML private TextField descriptionField;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TextField locationField;
    @FXML private TextField imageTF;
    @FXML private TextField Activities;

    private int eventId;
    private events eventListController; // Reference to main event list controller

    // Method to set data from the selected event
    public void setEventData(Evenement event) {
        this.eventId = event.getId();
        nameField.setText(event.getNom());
        typeField.setText(event.getType());
        budgetField.setText(String.valueOf(event.getBudgetPrevu()));
        descriptionField.setText(event.getDescription());
        startDatePicker.setValue(convertToLocalDate(event.getDateDebut()));
        endDatePicker.setValue(convertToLocalDate(event.getDateFin()));
        locationField.setText(event.getLieuEvenement());
        Activities.setText(event.getActivities());
        imageTF.setText(event.getImagePath());
    }

    // Method to set reference to event list controller
    public void setEventListController(events controller) {
        this.eventListController = controller;
    }

    private LocalDate convertToLocalDate(Date date) {
        return ((java.sql.Date) date).toLocalDate();
    }

    private Date convertToDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @FXML
    void handleSave(ActionEvent event) {
        try {
            // Check for empty fields
            if (nameField.getText().isEmpty() ||
                    typeField.getText().isEmpty() ||
                    budgetField.getText().isEmpty() ||
                    startDatePicker.getValue() == null ||
                    endDatePicker.getValue() == null ||
                    descriptionField.getText().isEmpty() ||
                    locationField.getText().isEmpty() ||
                    Activities.getText().isEmpty() ||
                    imageTF.getText().isEmpty()) {

                showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields must be filled!");
                return;
            }

            // Parse budget
            int budget = (int) Float.parseFloat(budgetField.getText());

            // Convert dates
            Date startDate = convertToDate(startDatePicker.getValue());
            Date endDate = convertToDate(endDatePicker.getValue());

            // Validate date order
            if (startDate.after(endDate)) {
                showAlert(Alert.AlertType.ERROR, "Date Error", "Start date cannot be after the end date!");
                return;
            }

            // Create and update event
            Evenement updatedEvent = new Evenement(
                    eventId,
                    nameField.getText(),
                    typeField.getText(),
                    budget,
                    startDate,
                    endDate,
                    descriptionField.getText(),
                    locationField.getText(),
                    Double.parseDouble(budgetField.getText()),
                    Activities.getText(),
                    imageTF.getText()
            );

            EvenementService evenementService = new EvenementService();
            evenementService.updateEvenement(updatedEvent);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Event updated successfully!");

            // Refresh event list if controller is available
            if (eventListController != null) {
                eventListController.refreshEventList();
            }

            // Close the window
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to update event: " + e.getMessage());
            e.printStackTrace();
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Invalid budget format!");
        }
    }


    @FXML
    void handleDelete(ActionEvent event) {
        try {
            EvenementService evenementService = new EvenementService();
            evenementService.deleteEvenement(eventId);

            showAlert(Alert.AlertType.INFORMATION, "Success", "Event deleted successfully!");

            // Refresh event list if controller is available
            if (eventListController != null) {
                eventListController.refreshEventList();
            }

            // Close the window
            Stage stage = (Stage) nameField.getScene().getWindow();
            stage.close();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete event: " + e.getMessage());
            e.printStackTrace();
        }
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
            imageTF.setText(selectedFile.getAbsolutePath());
            Image image = new Image(selectedFile.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(300);
            imageView.setFitHeight(300);
        }
    }

    // Utility method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

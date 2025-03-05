package org.example.dao;


import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertUtil {

    /**
     * Shows an alert dialog with the specified type, title, and message
     *
     * @param alertType The type of alert (INFORMATION, WARNING, ERROR, etc.)
     * @param title The title of the alert dialog
     * @param message The message content for the alert dialog
     */
    public static void showAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
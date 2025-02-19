package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class DestinationController {

    @FXML
    private TextField destinationField;

    @FXML
    private void nextPage() {
        String destination = destinationField.getText();

        // Navigate to Flight Selection Screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/flights.fxml"));
            Parent root = loader.load();

            FlightController flightController = loader.getController();
            flightController.setDestination(destination);

            Stage stage = (Stage) destinationField.getScene().getWindow();
            stage.setScene(new Scene(root, 600, 400));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void viewHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/History.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) destinationField.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Booking History");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
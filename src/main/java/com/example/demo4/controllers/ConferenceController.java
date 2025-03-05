package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import org.example.dao.ConferenceLocationDAO;
import org.example.models.*;

import java.io.IOException;
import java.util.List;

public class ConferenceController {
    @FXML
    private ListView<ConferenceLocation> conferenceList;

    private Flight selectedFlight;
    private Hotel selectedHotel;
    private Evenement selectedEventData; // To store event data
    private List<ConferenceLocation> locations;
    private Booking selectedBooking;

    /**
     * Sets the selected flight, hotel, and event data, and populates the conference list based on the flight's destination.
     */
    public void setSelectedFlightHotelAndEventData(Flight selectedFlight, Hotel selectedHotel, Evenement eventData) {
        this.selectedFlight = selectedFlight;
        this.selectedHotel = selectedHotel;
        this.selectedEventData = eventData;

        // Fetch conference locations based on the flight's destination
        ConferenceLocationDAO conferenceLocationDAO = new ConferenceLocationDAO();
        try {
            locations = conferenceLocationDAO.findByLocation(selectedFlight.getDestination());
            populateConferenceList(locations);
        } catch (Exception e) {
            showError("Database Error", "Failed to fetch conference locations for the selected destination.");
            e.printStackTrace();
        }
    }

    /**
     * Populates the conference list with the given locations.
     */
    private void populateConferenceList(List<ConferenceLocation> locations) {
        ObservableList<ConferenceLocation> locationItems = FXCollections.observableArrayList(locations);
        conferenceList.setItems(locationItems);

        conferenceList.setCellFactory(lv -> new ListCell<ConferenceLocation>() {
            @Override
            protected void updateItem(ConferenceLocation location, boolean empty) {
                super.updateItem(location, empty);
                if (empty || location == null) {
                    setGraphic(null);
                } else {
                    // Create square cell layout
                    BorderPane cellPane = new BorderPane();
                    cellPane.setPrefSize(240, 220);
                    cellPane.setStyle("-fx-border-color: #BDBDBD; -fx-border-radius: 5; -fx-padding: 15;");

                    // Left Section (Location Image + Name)
                    VBox leftSection = new VBox(5);
                    leftSection.setAlignment(Pos.CENTER_LEFT);
                    try {
                        Image image = new Image(getClass().getResourceAsStream(
                                "/images/" + location.getName() + ".png"
                        ));
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(100);
                        imageView.setPreserveRatio(true);

                        Label nameLabel = new Label(location.getName());
                        nameLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

                        leftSection.getChildren().addAll(imageView, nameLabel);
                    } catch (Exception e) {
                        leftSection.getChildren().add(new Label(location.getName()));
                    }
                    cellPane.setLeft(leftSection);

                    // Right Section (Price/Day)
                    Label priceLabel = new Label(String.format("$%.2f/day", location.getPricePerDay()));
                    priceLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #2196F3;");
                    BorderPane.setAlignment(priceLabel, Pos.CENTER_RIGHT);
                    cellPane.setRight(priceLabel);

                    // Center (Address + Capacity)
                    VBox centerBox = new VBox(5);
                    centerBox.setAlignment(Pos.CENTER);
                    Label addressLabel = new Label(location.getAddress());
                    Label capacityLabel = new Label("Capacity: " + location.getCapacity() + " people");
                    addressLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
                    capacityLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #757575;");
                    centerBox.getChildren().addAll(addressLabel, capacityLabel);
                    cellPane.setCenter(centerBox);

                    setGraphic(cellPane);
                }
            }
        });
    }

    /**
     * Handles navigation to the transport selection screen.
     */
    @FXML
    private void nextPage() {
        int selectedIndex = conferenceList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/transport.fxml"));
                Parent root = loader.load();
                TransportController transportController = loader.getController();

                // Pass the selected flight, hotel, conference location, and event data to the TransportController
                transportController.setSelectedData(
                        selectedFlight,
                        selectedHotel,
                        locations.get(selectedIndex),
                        selectedEventData
                );

                Stage stage = (Stage) conferenceList.getScene().getWindow();
                stage.setScene(new Scene(root, 934, 720));// Match window size
                stage.show();
            } catch (IOException e) {
                showError("Navigation Error", "Failed to load the transport selection screen.");
                e.printStackTrace();
            }
        } else {
            showError("Selection Error", "Please select a conference location before proceeding.");
        }
    }

    /**
     * Displays an error alert to the user.
     */
    private void showError(String title, String content) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void setSelectedBookingData(Booking booking) {
        this.selectedBooking = booking;

        // Fetch conference locations based on the booking's destination
        ConferenceLocationDAO conferenceDAO = new ConferenceLocationDAO();
        try {
            locations = conferenceDAO.findByLocation(booking.getHotelLocation());
            populateConferenceList(locations);
        } catch (Exception e) {
            showError("Database Error", "Failed to fetch conference locations for the selected booking.");
            e.printStackTrace();
        }
    }
}

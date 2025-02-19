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
import org.example.dao.TransportDAO;
import org.example.models.ConferenceLocation;
import org.example.models.Flight;
import org.example.models.Hotel;
import org.example.models.Transport;
import java.io.IOException;
import java.util.List;

public class TransportController {

    @FXML
    private ListView<Transport> transportList;
    private Flight selectedFlight;
    private Hotel selectedHotel;
    private ConferenceLocation selectedConference;
    private List<Transport> transports;

    public void setSelectedData(Flight flight, Hotel hotel, ConferenceLocation conference) {
        this.selectedFlight = flight;
        this.selectedHotel = hotel;
        this.selectedConference = conference;
        TransportDAO transportDAO = new TransportDAO();
        transports = transportDAO.findAll();

        ObservableList<Transport> transportItems = FXCollections.observableArrayList(transports);
        transportList.setItems(transportItems);

        transportList.setCellFactory(lv -> new ListCell<Transport>() {
            @Override
            protected void updateItem(Transport transport, boolean empty) {
                super.updateItem(transport, empty);
                if (empty || transport == null) {
                    setGraphic(null);
                } else {
                    BorderPane cellPane = new BorderPane();
                    cellPane.setPrefSize(240, 220);
                    cellPane.setStyle("-fx-border-color: #BDBDBD; -fx-border-radius: 5; -fx-padding: 15;");

                    // Left Section (Transport Image + Type)
                    VBox leftSection = new VBox(5);
                    leftSection.setAlignment(Pos.CENTER_LEFT);
                    try {
                        Image image = new Image(getClass().getResourceAsStream(
                                "/images/" + transport.getType() + ".png"
                        ));
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(100);
                        imageView.setPreserveRatio(true);

                        Label typeLabel = new Label(transport.getType());
                        typeLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

                        leftSection.getChildren().addAll(imageView, typeLabel);
                    } catch (Exception e) {
                        leftSection.getChildren().add(new Label(transport.getType()));
                    }
                    cellPane.setLeft(leftSection);

                    // Right Section (Price)
                    Label priceLabel = new Label(String.format("$%.2f", transport.getPrice()));
                    priceLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #2196F3;");
                    BorderPane.setAlignment(priceLabel, Pos.CENTER_RIGHT);
                    cellPane.setRight(priceLabel);

                    // Center (Details)
                    VBox centerBox = new VBox(5);
                    centerBox.setAlignment(Pos.CENTER);




                    cellPane.setCenter(centerBox);

                    setGraphic(cellPane);
                }
            }
        });
    }

    @FXML
    private void nextPage() {
        int selectedIndex = transportList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/booking.fxml"));
                Parent root = loader.load();

                BookingController bookingController = loader.getController();
                // Pass actual objects instead of indices
                bookingController.setBookingData(
                        selectedFlight,
                        selectedHotel,          // Hotel object from previous screen
                        selectedConference,       // ConferenceLocation object
                        transports.get(selectedIndex)  // Transport object from current list
                );

                Stage stage = (Stage) transportList.getScene().getWindow();
                stage.setScene(new Scene(root, 800, 600));  // Match window size
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
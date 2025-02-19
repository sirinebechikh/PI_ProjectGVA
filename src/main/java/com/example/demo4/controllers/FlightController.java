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
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.example.dao.FlightDAO;
import org.example.dao.FlightDAOimplt;
import org.example.models.Flight;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

public class FlightController {
    @FXML
    private ListView<Flight> flightList;
    private List<Flight> flights;

    public void setDestination(String destination) {
        FlightDAO flightDAO = new FlightDAOimplt();
        flights = flightDAO.findByDestination(destination);
        ObservableList<Flight> flightItems = FXCollections.observableArrayList(flights);
        flightList.setItems(flightItems);

        flightList.setCellFactory(lv -> new ListCell<Flight>() {
            @Override
            protected void updateItem(Flight flight, boolean empty) {
                super.updateItem(flight, empty);
                if (empty || flight == null) {
                    setGraphic(null);
                } else {
                    // Create square cell layout
                    BorderPane cellPane = new BorderPane();
                    cellPane.setPrefSize(240, 200);  // Increased width for better date display
                    cellPane.setStyle("-fx-border-color: #BDBDBD; -fx-border-radius: 5; -fx-padding: 15;");

                    // Left Section (Airline Image + Name)
                    VBox leftSection = new VBox(5);
                    leftSection.setAlignment(Pos.CENTER_LEFT);
                    try {
                        Image image = new Image(getClass().getResourceAsStream(
                                "/images/" + flight.getAirline() + ".png"
                        ));
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(200);
                        imageView.setPreserveRatio(true);

                        Label airlineLabel = new Label(flight.getAirline());
                        airlineLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

                        leftSection.getChildren().addAll(imageView, airlineLabel);
                    } catch (Exception e) {
                        leftSection.getChildren().add(new Label(flight.getAirline()));
                    }
                    cellPane.setLeft(leftSection);

                    // Right Section (Price)
                    Label priceLabel = new Label(String.format("$%.2f", flight.getPrice()));
                    priceLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #2196F3;");
                    BorderPane.setAlignment(priceLabel, Pos.CENTER_RIGHT);
                    cellPane.setRight(priceLabel);

                    // Center (Full Departure Date/Time)
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    VBox timeBox = new VBox(5);
                    timeBox.setAlignment(Pos.CENTER);

                    String[] dateTimeParts = sdf.format(flight.getDepartureTime()).split(" ");
                    Label dateLabel = new Label(dateTimeParts[0]);
                    Label timeLabel = new Label(dateTimeParts[1]);

                    dateLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");
                    timeLabel.setStyle("-fx-font-size: 20; -fx-font-weight: bold;");

                    timeBox.getChildren().addAll(dateLabel, timeLabel);
                    cellPane.setCenter(timeBox);

                    setGraphic(cellPane);
                }
            }
        });
    }

    @FXML
    private void nextPage() {
        int selectedIndex = flightList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/hotels.fxml"));
                Parent root = loader.load();
                HotelController hotelController = loader.getController();
                hotelController.setSelectedFlight(flights.get(selectedIndex));
                Stage stage = (Stage) flightList.getScene().getWindow();
                stage.setScene(new Scene(root, 600, 400));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
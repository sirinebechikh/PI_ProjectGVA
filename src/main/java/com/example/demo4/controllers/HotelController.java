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
import org.example.dao.HotelDAO;
import org.example.dao.HotelDAOimplt;
import org.example.models.Flight;
import org.example.models.Hotel;
import java.io.IOException;
import java.util.List;

public class HotelController {

    @FXML
    private ListView<Hotel> hotelList;
    private Flight selectedFlight;
    private List<Hotel> hotels;

    public void setSelectedFlight(Flight selectedFlight) {
        this.selectedFlight = selectedFlight;
        HotelDAO hotelDAO = new HotelDAOimplt();
        hotels = hotelDAO.findByLocation(selectedFlight.getDestination());

        ObservableList<Hotel> hotelItems = FXCollections.observableArrayList(hotels);
        hotelList.setItems(hotelItems);

        hotelList.setCellFactory(lv -> new ListCell<Hotel>() {
            @Override
            protected void updateItem(Hotel hotel, boolean empty) {
                super.updateItem(hotel, empty);
                if (empty || hotel == null) {
                    setGraphic(null);
                } else {
                    BorderPane cellPane = new BorderPane();
                    cellPane.setPrefSize(240, 220);
                    cellPane.setStyle("-fx-border-color: #BDBDBD; -fx-border-radius: 5; -fx-padding: 15;");

                    // Left Section (Hotel Image + Name)
                    VBox leftSection = new VBox(5);
                    leftSection.setAlignment(Pos.CENTER_LEFT);
                    try {
                        Image image = new Image(getClass().getResourceAsStream(
                                "/images/" + hotel.getName() + ".png"
                        ));
                        ImageView imageView = new ImageView(image);
                        imageView.setFitWidth(100);
                        imageView.setPreserveRatio(true);

                        Label nameLabel = new Label(hotel.getName());
                        nameLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

                        leftSection.getChildren().addAll(imageView, nameLabel);
                    } catch (Exception e) {
                        leftSection.getChildren().add(new Label(hotel.getName()));
                    }
                    cellPane.setLeft(leftSection);

                    // Right Section (Price/Night)
                    Label priceLabel = new Label(String.format("$%.2f/night", hotel.getPricePerNight()));
                    priceLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #2196F3;");
                    BorderPane.setAlignment(priceLabel, Pos.CENTER_RIGHT);
                    cellPane.setRight(priceLabel);

                    // Center (Rating)
                    VBox centerBox = new VBox(5);
                    centerBox.setAlignment(Pos.CENTER);

                    Label ratingLabel = new Label("★ ".repeat(hotel.getRating()));
                    Label ratingText = new Label(hotel.getRating() + "/5 Stars");

                    ratingLabel.setStyle("-fx-font-size: 24; -fx-text-fill: #FFD700;");
                    ratingText.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

                    centerBox.getChildren().addAll(ratingLabel, ratingText);
                    cellPane.setCenter(centerBox);

                    setGraphic(cellPane);
                }
            }
        });
    }
    @FXML
    private void nextPage() {
        int selectedIndex = hotelList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/conference.fxml"));
                Parent root = loader.load();

                ConferenceController conferenceController = loader.getController();
                // Pass the Hotel object instead of index
                conferenceController.setSelectedFlightAndHotel(
                        selectedFlight,
                        hotels.get(selectedIndex)  // Get the selected Hotel object
                );

                Stage stage = (Stage) hotelList.getScene().getWindow();
                stage.setScene(new Scene(root, 800, 600));  // Match window size
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
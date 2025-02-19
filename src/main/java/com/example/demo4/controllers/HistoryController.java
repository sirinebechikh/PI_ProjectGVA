package org.example.controllers;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.dao.Bookingimplt;
import org.example.models.Booking;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

public class HistoryController {
    @FXML
    private TableView<Booking> bookingsTable;
    @FXML
    private TableColumn<Booking, Integer> bookingIdColumn;
    @FXML
    private TableColumn<Booking, String> userNameColumn;
    @FXML
    private TableColumn<Booking, String> statusColumn;
    @FXML
    private TableColumn<Booking, String> airlinesColumn;
    @FXML
    private TableColumn<Booking, Double> flightPriceColumn;
    @FXML
    private TableColumn<Booking, String> hotelNameColumn;
    @FXML
    private TableColumn<Booking, String> hotelLocationColumn;
    @FXML
    private TableColumn<Booking, String> conferenceNameColumn;
    @FXML
    private TableColumn<Booking, String> transportTypeColumn;
    @FXML
    private TableColumn<Booking, Timestamp> departureTimeColumn;
    @FXML
    private TableColumn<Booking, Double> priceTotalColumn;

    @FXML
    public void initialize() {
        // Set up table columns
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        airlinesColumn.setCellValueFactory(new PropertyValueFactory<>("airlines"));
        flightPriceColumn.setCellValueFactory(new PropertyValueFactory<>("flightPrice"));
        hotelNameColumn.setCellValueFactory(new PropertyValueFactory<>("hotelName"));
        hotelLocationColumn.setCellValueFactory(new PropertyValueFactory<>("hotelLocation"));
        conferenceNameColumn.setCellValueFactory(new PropertyValueFactory<>("conferenceName"));
        transportTypeColumn.setCellValueFactory(new PropertyValueFactory<>("transportType"));
        departureTimeColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));
        priceTotalColumn.setCellValueFactory(new PropertyValueFactory<>("priceTotal"));

        // Fetch bookings with status = "wait"
        refreshTable();
    }

    /*
        @FXML
        private void handleRowClick() {
            // Get the selected booking
            Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
            if (selectedBooking != null) {
                try {
                    // Load the details page
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/HistoryDetails.fxml"));
                    Parent root = loader.load();

                    // Pass the selected booking and a reference to this controller
                    HistoryDetails detailsController = loader.getController();
                    detailsController.setBooking(selectedBooking, this);

                    // Show the details page
                    Stage stage = new Stage();
                    stage.setTitle("Booking Details");
                    stage.setScene(new Scene(root, 600, 400));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


     */
    @FXML
    private void handleRowClick() {
        // Get the selected booking
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking != null) {
            try {
                // Load the payment page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/PaymentPage.fxml"));
                Parent root = loader.load();

                // Pass the selected booking ID and priceTotal to the PaymentController
                PaymentController paymentController = loader.getController();
                paymentController.setPaymentDetails(selectedBooking.getBookingId(), selectedBooking.getPriceTotal());

                // Show the payment page
                Stage stage = new Stage();
                stage.setTitle("Process Payment");
                stage.setScene(new Scene(root, 600, 600));
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // Method to refresh the table
    public void refreshTable() {
        Bookingimplt bookingDAO = new Bookingimplt();
        List<Booking> bookings = bookingDAO.findByStatus("confirmed");

        ObservableList<Booking> observableBookings = FXCollections.observableArrayList(bookings);
        bookingsTable.setItems(observableBookings);
    }
    @FXML
    private void navigateToDestination() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/destination.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) bookingsTable.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 500));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import org.example.dao.BookingDAO;
import org.example.dao.Bookingimplt;
import org.example.models.*;
import java.io.IOException;

public class BookingController {
    @FXML
    private Label flightDetailsLabel;
    @FXML
    private Label hotelDetailsLabel;
    @FXML
    private Label conferenceDetailsLabel;
    @FXML
    private Label transportDetailsLabel;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private TextArea specialRequestsTextArea; // New TextArea for Special Requests

    private Flight selectedFlight;
    private Hotel selectedHotel;
    private ConferenceLocation selectedConference;
    private Transport selectedTransport;
    private Evenement selectedEventData;

    public void setBookingData(Flight selectedFlight,
                               Hotel selectedHotel,
                               ConferenceLocation selectedConference,
                               Transport selectedTransport,
                               Evenement eventData) {
        this.selectedFlight = selectedFlight;
        this.selectedHotel = selectedHotel;
        this.selectedConference = selectedConference;
        this.selectedTransport = selectedTransport;
        this.selectedEventData = eventData;

        flightDetailsLabel.setText(selectedFlight.getAirline() + " | Departure: " + selectedFlight.getDepartureTime());
        hotelDetailsLabel.setText(selectedHotel.getName() + " | Location: " + selectedHotel.getLocation());
        conferenceDetailsLabel.setText(selectedConference.getName() + " | Price/day: $" + selectedConference.getPricePerDay());
        transportDetailsLabel.setText(selectedTransport.getType() + " | Description: " + selectedTransport.getDescription());

        int numberOfPeople = selectedEventData.getNombreInvite();
        double totalPrice = (selectedFlight.getPrice() +
                selectedHotel.getPricePerNight() +
                selectedConference.getPricePerDay() +
                selectedTransport.getPrice()) * numberOfPeople;
        totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice));
    }

    @FXML
    private void confirmBooking() {
        String specialRequests = specialRequestsTextArea.getText().trim();

        // Check if Special Requests is empty
        if (specialRequests.isEmpty()) {
            // Show an alert to the user
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Special Requests Required");
            alert.setHeaderText("Special Requests Cannot Be Empty");
            alert.setContentText("Please provide any special requests before confirming the booking.");
            alert.showAndWait(); // Display the alert and wait for user response
            return; // Stop the booking process
        }

        // Proceed with booking if Special Requests is not empty
        BookingDAO bookingDAO = new Bookingimplt();
        Booking booking = new Booking();

        booking.setFlightId(selectedFlight.getFlightId());
        booking.setAirlines(selectedFlight.getAirline());
        booking.setFlightPrice(selectedFlight.getPrice());
        booking.setDepartureTime(selectedFlight.getDepartureTime());

        booking.setHotelId(selectedHotel.getHotelId());
        booking.setHotelName(selectedHotel.getName());
        booking.setHotelLocation(selectedHotel.getLocation());
        booking.setHotelPricePerNight(selectedHotel.getPricePerNight());
        booking.setHotelRating(selectedHotel.getRating());

        booking.setConferenceLocationId(selectedConference.getLocationId());
        booking.setConferenceName(selectedConference.getName());
        booking.setConferencePricePerDay(selectedConference.getPricePerDay());

        booking.setTransportId(selectedTransport.getTransportId());
        booking.setTransportType(selectedTransport.getType());
        booking.setTransportPrice(selectedTransport.getPrice());
        booking.setTransportDescription(selectedTransport.getDescription());

        int numberOfPeople = selectedEventData.getNombreInvite();
        double totalPrice = (selectedFlight.getPrice() +
                selectedHotel.getPricePerNight() +
                selectedConference.getPricePerDay() +
                selectedTransport.getPrice()) * numberOfPeople;
        booking.setPriceTotal(totalPrice);

        booking.setUserName(selectedEventData.getNomutlisateur());
        booking.setBookingDate(new java.sql.Date(System.currentTimeMillis()));
        booking.setStatus("wait");

        booking.setNameEvement(selectedEventData.getNom());
        booking.setNumberOfInvites(selectedEventData.getNombreInvite());
        booking.setIdEvement(selectedEventData.getId());

        booking.setSpecialRequests(specialRequests); // Set special requests

        bookingDAO.save(booking);

        // Show confirmation message
        flightDetailsLabel.setText("Booking Confirmed!");
        hotelDetailsLabel.setText("");
        conferenceDetailsLabel.setText("");
        transportDetailsLabel.setText("");
        totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice));
    }

    @FXML
    private void navigateToDestination() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/destination.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) flightDetailsLabel.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 500));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

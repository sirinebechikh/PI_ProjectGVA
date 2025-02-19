package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
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
    private Label totalPriceLabel; // Label for total price

    private Flight selectedFlight;
    private Hotel selectedHotel;
    private ConferenceLocation selectedConference;
    private Transport selectedTransport;

    // Method to set booking data from previous selections
    public void setBookingData(Flight selectedFlight,
                               Hotel selectedHotel,
                               ConferenceLocation selectedConference,
                               Transport selectedTransport) {
        this.selectedFlight = selectedFlight;
        this.selectedHotel = selectedHotel;
        this.selectedConference = selectedConference;
        this.selectedTransport = selectedTransport;

        // Populate labels with selected details
        flightDetailsLabel.setText(selectedFlight.getAirline() + " | Departure: " + selectedFlight.getDepartureTime());
        hotelDetailsLabel.setText(selectedHotel.getName() + " | Location: " + selectedHotel.getLocation());
        conferenceDetailsLabel.setText(selectedConference.getName() + " | Price/day: $" + selectedConference.getPricePerDay());
        transportDetailsLabel.setText(selectedTransport.getType() + " | Description: " + selectedTransport.getDescription());

        // Calculate and display total price
        int numberOfPeople = 2; // Should be dynamic input
        double totalPrice = (selectedFlight.getPrice() +
                selectedHotel.getPricePerNight() +
                selectedConference.getPricePerDay() +
                selectedTransport.getPrice()) * numberOfPeople;
        totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice)); // Display total price
    }

    @FXML
    private void confirmBooking() {
        BookingDAO bookingDAO = new Bookingimplt();
        Booking booking = new Booking();

        // Set flight details
        booking.setFlightId(selectedFlight.getFlightId());
        booking.setAirlines(selectedFlight.getAirline());
        booking.setFlightPrice(selectedFlight.getPrice());
        booking.setDepartureTime(selectedFlight.getDepartureTime());

        // Set hotel details
        booking.setHotelId(selectedHotel.getHotelId());
        booking.setHotelName(selectedHotel.getName());
        booking.setHotelLocation(selectedHotel.getLocation());
        booking.setHotelPricePerNight(selectedHotel.getPricePerNight());
        booking.setHotelRating(selectedHotel.getRating());

        // Set conference details
        booking.setConferenceLocationId(selectedConference.getLocationId());
        booking.setConferenceName(selectedConference.getName());
        booking.setConferencePricePerDay(selectedConference.getPricePerDay());

        // Set transport details
        booking.setTransportId(selectedTransport.getTransportId());
        booking.setTransportType(selectedTransport.getType());
        booking.setTransportPrice(selectedTransport.getPrice());
        booking.setTransportDescription(selectedTransport.getDescription());

        // Calculate total price (reuse the same logic)
        int numberOfPeople = 2; // Should be dynamic input
        double totalPrice = (selectedFlight.getPrice() +
                selectedHotel.getPricePerNight() +
                selectedConference.getPricePerDay() +
                selectedTransport.getPrice()) * numberOfPeople;
        booking.setPriceTotal(totalPrice);

        // Common booking details
        booking.setUserName("moatez"); // Should be dynamic input
        booking.setBookingDate(new java.sql.Date(System.currentTimeMillis()));
        booking.setStatus("wait");

        // Save booking
        bookingDAO.save(booking);

        // Update UI to show confirmation message
        flightDetailsLabel.setText("Booking Confirmed!");
        hotelDetailsLabel.setText("");
        conferenceDetailsLabel.setText("");
        transportDetailsLabel.setText("");
        totalPriceLabel.setText("Total Price: $" + String.format("%.2f", totalPrice)); // Keep total price visible
    }
    @FXML
    private void navigateToDestination() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/destination.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) flightDetailsLabel.getScene().getWindow();
            // tbdel siz
            stage.setScene(new Scene(root,400,500));
            stage.setWidth(400);                        // Explicitly set stage width
            stage.setHeight(500);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
/*package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.dao.Bookingimplt;
import org.example.models.Booking;

public class BookingDetailsController {

    @FXML
    private TextField bookingIdField;

    @FXML
    private TextField userNameField;

    @FXML
    private TextField flightIdField;

    @FXML
    private TextField hotelIdField;

    @FXML
    private TextField transportIdField;

    @FXML
    private TextField conferenceLocationIdField;

    @FXML
    private TextField airlinesField;

    @FXML
    private TextField flightPriceField;

    @FXML
    private TextField departureTimeField;

    private Booking selectedBooking;

    public void setBooking(Booking booking) {
        this.selectedBooking = booking;

        // Populate the fields with booking details
        bookingIdField.setText(String.valueOf(booking.getBookingId()));
        userNameField.setText(booking.getUserName());
        flightIdField.setText(String.valueOf(booking.getFlightId()));
        hotelIdField.setText(String.valueOf(booking.getHotelId()));
        transportIdField.setText(String.valueOf(booking.getTransportId()));
        conferenceLocationIdField.setText(String.valueOf(booking.getConferenceLocationId()));
        airlinesField.setText(booking.getAirlines());
        flightPriceField.setText(String.valueOf(booking.getFlightPrice()));
        departureTimeField.setText(booking.getDepartureTime() != null ? booking.getDepartureTime().toString() : "N/A");
    }

    @FXML
    private void confirmBooking() {
        updateStatus("confirmed");
    }

    @FXML
    private void notConfirmBooking() {
        updateStatus("not confirmed");
    }

    private void updateStatus(String status) {
        Bookingimplt bookingDAO = new Bookingimplt();
        bookingDAO.updateStatus(selectedBooking.getBookingId(), status);
        System.out.println("Booking ID " + selectedBooking.getBookingId() + " status updated to: " + status);
    }
}

 */
package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.dao.Bookingimplt;
import org.example.models.Booking;

public class BookingDetailsController {

    @FXML
    private TextField bookingIdField;

    @FXML
    private TextField userNameField;

    @FXML
    private TextField flightIdField;

    @FXML
    private TextField hotelIdField;

    @FXML
    private TextField transportIdField;

    @FXML
    private TextField conferenceLocationIdField;

    @FXML
    private TextField airlinesField;

    @FXML
    private TextField flightPriceField;

    @FXML
    private TextField departureTimeField;

    private Booking selectedBooking;
    private AdminController adminController; // Reference to AdminController

    public void setBooking(Booking booking, AdminController adminController) {
        this.selectedBooking = booking;
        this.adminController = adminController;

        // Populate the fields with booking details
        bookingIdField.setText(String.valueOf(booking.getBookingId()));
        userNameField.setText(booking.getUserName());
        flightIdField.setText(String.valueOf(booking.getFlightId()));
        hotelIdField.setText(String.valueOf(booking.getHotelId()));
        transportIdField.setText(String.valueOf(booking.getTransportId()));
        conferenceLocationIdField.setText(String.valueOf(booking.getConferenceLocationId()));
        airlinesField.setText(booking.getAirlines());
        flightPriceField.setText(String.valueOf(booking.getFlightPrice()));
        departureTimeField.setText(booking.getDepartureTime() != null ? booking.getDepartureTime().toString() : "N/A");
    }

    @FXML
    private void confirmBooking() {
        updateStatus("confirmed");
    }

    @FXML
    private void notConfirmBooking() {
        updateStatus("not confirmed");
    }

    private void updateStatus(String status) {
        // Update the status in the database
        Bookingimplt bookingDAO = new Bookingimplt();
        bookingDAO.updateStatus(selectedBooking.getBookingId(), status);
        System.out.println("Booking ID " + selectedBooking.getBookingId() + " status updated to: " + status);

        // Refresh the admin table
        if (adminController != null) {
            adminController.refreshTable();
        }

        // Close the current window
        bookingIdField.getScene().getWindow().hide();
    }
}
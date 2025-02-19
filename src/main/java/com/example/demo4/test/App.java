package org.example;

import org.example.dao.*;
import org.example.models.*;

import java.util.List;

public class App{
    public static void main(String[] args) {

            BookingDAO bookingDAO = new Bookingimplt();

            // Save a new booking with default status 'wait'
            Booking booking = new Booking();
            booking.setFlightId(1);
            booking.setHotelId(1);
            booking.setTransportId(1);
            booking.setConferenceLocationId(1);
            booking.setUserName("JohnDoe");
            booking.setStatus("wait"); // Default status
            bookingDAO.save(booking);

            // Find all bookings
            List<Booking> bookings = bookingDAO.findAll();
            System.out.println("All Bookings:");
            bookings.forEach(b -> System.out.println("Booking ID: " + b.getBookingId() +
                    ", User: " + b.getUserName() +
                    ", Status: " + b.getStatus()));

            // Update status of a booking
            int bookingIdToUpdate = 29; // Replace with an actual booking ID
            bookingDAO.updateStatus(bookingIdToUpdate, "3asba");

            // Verify the status update
            Booking updatedBooking = bookingDAO.findById(bookingIdToUpdate);
            if (updatedBooking != null) {
                System.out.println("Updated Booking Status: " + updatedBooking.getStatus());
            }
        }
    }

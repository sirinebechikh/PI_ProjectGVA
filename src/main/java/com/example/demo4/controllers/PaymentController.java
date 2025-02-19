/*package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.dao.Bookingimplt;

public class PaymentController {

    @FXML
    private TextField nomTitulaireField;

    @FXML
    private TextField numeroCarteField;

    @FXML
    private TextField dateExpirationField;

    @FXML
    private TextField cvvField;

    private int bookingId;

    // Method to set the booking ID
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
        System.out.println("Payment for Booking ID: " + bookingId);
    }

    @FXML
    private void savePayment() {
        // Validate payment details (basic validation)
        String nomTitulaire = nomTitulaireField.getText();
        String numeroCarte = numeroCarteField.getText();
        String dateExpiration = dateExpirationField.getText();
        String cvv = cvvField.getText();

        if (nomTitulaire.isEmpty() || numeroCarte.isEmpty() || dateExpiration.isEmpty() || cvv.isEmpty()) {
            System.out.println("Please fill all payment details.");
            return;
        }

        // Simulate payment processing
        System.out.println("Processing payment for Booking ID: " + bookingId);
        System.out.println("Card Holder: " + nomTitulaire);
        System.out.println("Card Number: " + numeroCarte);
        System.out.println("Expiration Date: " + dateExpiration);
        System.out.println("CVV: " + cvv);

        // Update the booking status to "payment_confirmed"
        Bookingimplt bookingDAO = new Bookingimplt();
        bookingDAO.updateStatus(bookingId, "payment_confirmed");

        // Close the payment window
        nomTitulaireField.getScene().getWindow().hide();
    }
}

 */
package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.example.dao.PaiementDAO;
import org.example.models.Paiement;

import java.sql.Date;

public class PaymentController {

    @FXML
    private TextField nomTitulaireField;

    @FXML
    private TextField numeroCarteField;

    @FXML
    private TextField dateExpirationField;

    @FXML
    private TextField cvvField;


private HistoryController HistoryController;
    private int bookingId;
    private double priceTotal;

    // Method to set booking ID and priceTotal
    public void setPaymentDetails(int bookingId, double priceTotal) {
        this.bookingId = bookingId;
        this.priceTotal = priceTotal;


    }

    @FXML
    private void savePayment() {
        // Validate payment details (basic validation)
        String nomTitulaire = nomTitulaireField.getText();
        String numeroCarte = numeroCarteField.getText();
        String dateExpiration = dateExpirationField.getText();
        String cvv = cvvField.getText();

        if (nomTitulaire.isEmpty() || numeroCarte.isEmpty() || dateExpiration.isEmpty() || cvv.isEmpty()) {
            System.out.println("Please fill all payment details.");
            return;
        }

        // Parse expiration date
        Date parsedDateExpiration;
        try {
            parsedDateExpiration = Date.valueOf(dateExpiration); // Assumes format YYYY-MM-DD
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            return;
        }

        // Parse CVV
        int parsedCvv;
        try {
            parsedCvv = Integer.parseInt(cvv);
        } catch (NumberFormatException e) {
            System.out.println("Invalid CVV. Please enter a numeric value.");
            return;
        }

        // Simulate payment processing
        System.out.println("Processing payment for Booking ID: " + bookingId);
        System.out.println("Card Holder: " + nomTitulaire);
        System.out.println("Card Number: " + numeroCarte);
        System.out.println("Expiration Date: " + dateExpiration);
        System.out.println("CVV: " + cvv);

        // Save payment details using PaiementDAO
        PaiementDAO paiementDAO = new PaiementDAO();
        paiementDAO.createPaiement(
                nomTitulaire,
                bookingId, // idReservation corresponds to bookingId
                numeroCarte,
                parsedDateExpiration,
                parsedCvv,
                priceTotal // Example amount (you can calculate this dynamically)
        );

        // Update the booking status to "payment_confirmed"
        org.example.dao.Bookingimplt bookingDAO = new org.example.dao.Bookingimplt();
        bookingDAO.updateStatus(bookingId, "payment_confirmed");
        if (HistoryController != null) {
            HistoryController.refreshTable();
        } else {
            System.out.println("HistoryController reference is null.");
        }

        // Close the payment window
        nomTitulaireField.getScene().getWindow().hide();
    }
}
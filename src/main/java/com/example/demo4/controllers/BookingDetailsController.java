/*package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.example.dao.Bookingimplt;
import org.example.dao.DBconnection;
import org.example.dao.EventDAOImpl;
import org.example.models.Booking;
import org.example.models.Evenement;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class BookingDetailsController {

    @FXML
    private TextField bookingIdField; // Will not display

    @FXML
    private TextField userNameField;

    @FXML
    private TextField flightIdField; // Will not display

    @FXML
    private TextField hotelIdField; // Will not display

    @FXML
    private TextField transportIdField; // Will not display

    @FXML
    private TextField conferenceLocationIdField; // Will not display

    @FXML
    private TextField airlinesField;

    @FXML
    private TextField flightPriceField;

    @FXML
    private TextField departureTimeField;

    @FXML
    private TextField hotelNameField;

    @FXML
    private TextField hotelLocationField;

    @FXML
    private TextField hotelRatingField;

    @FXML
    private TextField conferenceNameField;

    @FXML
    private TextField transportTypeField;

    @FXML
    private TextField priceTotalField;

    @FXML
    private TextField eventStartField;

    @FXML
    private TextField eventEndField;

    private Booking selectedBooking;
    private AdminController adminController;

    public void setBooking(Booking booking, AdminController adminController) {
        this.selectedBooking = booking;
        this.adminController = adminController;

        // Fetch updated booking details from the database
        Bookingimplt bookingDAO = new Bookingimplt();
        Booking updatedBooking = bookingDAO.findById(booking.getBookingId());
        if (updatedBooking != null) {
            this.selectedBooking = updatedBooking;
        }

        // Fetch event details from the evenement table using id_evement
        try (Connection conn = DBconnection.getConnection()) {
            EventDAOImpl eventDAO = new EventDAOImpl(conn);
            java.util.Optional<Evenement> eventOpt = eventDAO.findEventById(booking.getIdEvement());
            if (eventOpt.isPresent()) {
                Evenement event = eventOpt.get();
                if (eventStartField != null) {
                    eventStartField.setText(event.getDateDebut().toString());
                }
                if (eventEndField != null) {
                    eventEndField.setText(event.getDateFin().toString());
                }
                selectedBooking.setStartEvement(new java.sql.Timestamp(event.getDateDebut().getTime()));
                selectedBooking.setEndEvement(new java.sql.Timestamp(event.getDateFin().getTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to fetch event details: " + e.getMessage());
        }

        // Populate fields without showing IDs
        bookingIdField.setVisible(false); // Hide booking ID field
        userNameField.setText(selectedBooking.getUserName());
        flightIdField.setVisible(false); // Hide flight ID field
        hotelIdField.setVisible(false); // Hide hotel ID field
        transportIdField.setVisible(false); // Hide transport ID field
        conferenceLocationIdField.setVisible(false); // Hide conference location ID field
        airlinesField.setText(selectedBooking.getAirlines());
        flightPriceField.setText(String.valueOf(selectedBooking.getFlightPrice()));
        departureTimeField.setText(selectedBooking.getDepartureTime() != null ? selectedBooking.getDepartureTime().toString() : "N/A");
        hotelNameField.setText(selectedBooking.getHotelName());
        hotelLocationField.setText(selectedBooking.getHotelLocation());
        hotelRatingField.setText(String.valueOf(selectedBooking.getHotelRating()));
        conferenceNameField.setText(selectedBooking.getConferenceName());
        transportTypeField.setText(selectedBooking.getTransportType());
        priceTotalField.setText(String.valueOf(selectedBooking.getPriceTotal()));
    }

    @FXML
    private void confirmBooking() {
        updateStatus("confirmed");
        sendConfirmationEmail("borgimoatez35@gmail.com");
    }

    @FXML
    private void notConfirmBooking() {
        updateStatus("not confirmed");
        sendNotConfirmedEmail("borgimoatez35@gmail.com");
    }

    private void updateStatus(String status) {
        Bookingimplt bookingDAO = new Bookingimplt();
        bookingDAO.updateStatus(selectedBooking.getBookingId(), status);
        System.out.println("Booking status updated to: " + status);

        if (adminController != null) {
            adminController.refreshTable();
        }

        bookingIdField.getScene().getWindow().hide();
    }

    private void sendConfirmationEmail(String recipientEmail) {
        String host = "smtp.gmail.com";
        String port = "587";
        String username = "borgimoatez@gmail.com";
        String password = "jkth ehop oogs izsl";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Booking Confirmation");

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText("Dear " + selectedBooking.getUserName() + ",\n\n"
                    + "Your booking has been confirmed.\n"
                    + "please on the PDF for more detais "
                    + "Thank you for choosing our service.\n\n"
                    + "Best regards,\nYour Booking Team");

            MimeBodyPart pdfAttachment = new MimeBodyPart();
            File pdfFile = generatePdf(selectedBooking);
            pdfAttachment.attachFile(pdfFile);

            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(pdfAttachment);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Confirmation email sent to " + recipientEmail);

            pdfFile.delete();
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    private void sendNotConfirmedEmail(String recipientEmail) {
        String host = "smtp.gmail.com";
        String port = "587";
        String username = "borgimoatez@gmail.com";
        String password = "jkth ehop oogs izsl";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Booking Not Confirmed");
            message.setText("Dear " + selectedBooking.getUserName() + ",\n\n"
                    + "We regret to inform you that your booking could not be confirmed.\n"
                    + "Flight: " + selectedBooking.getAirlines() + "\n"
                    + "Departure Time: " + selectedBooking.getDepartureTime() + "\n"
                    + "Hotel: " + selectedBooking.getHotelName() + " (" + selectedBooking.getHotelLocation() + ")\n"
                    + "Conference: " + selectedBooking.getConferenceName() + "\n"
                    + "Transport: " + selectedBooking.getTransportType() + "\n"
                    + "Total Price: " + selectedBooking.getPriceTotal() + "\n"
                    + "Event Start: " + (selectedBooking.getStartEvement() != null ? selectedBooking.getStartEvement() : "N/A") + "\n"
                    + "Event End: " + (selectedBooking.getEndEvement() != null ? selectedBooking.getEndEvement() : "N/A") + "\n"
                    + "Please contact our support team for further assistance.\n\n"
                    + "Best regards,\nYour Booking Team");

            Transport.send(message);
            System.out.println("Not Confirmed email sent to " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    private File generatePdf(Booking booking) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        float pageWidth = page.getMediaBox().getWidth();
        float pageHeight = page.getMediaBox().getHeight();
        float margin = 50;
        float yPosition = pageHeight - margin;

        // Add logo
        try {
            InputStream imageStream = getClass().getClassLoader().getResourceAsStream("fxml/123.png");
            if (imageStream == null) {
                throw new IOException("Image file 'fxml/123.png' not found in resources.");
            }
            PDImageXObject logo = PDImageXObject.createFromByteArray(document, imageStream.readAllBytes(), "123.png");
            imageStream.close();
            float logoWidth = 100;
            float logoHeight = 50;
            float logoX = margin;
            float logoY = yPosition - logoHeight;
            contentStream.drawImage(logo, logoX, logoY, logoWidth, logoHeight);
            yPosition = logoY - 20;
        } catch (IOException e) {
            System.err.println("Failed to load logo: " + e.getMessage());
        }

        // Add title
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
        contentStream.setNonStrokingColor(0, 102, 204);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Booking Confirmation.");
        contentStream.endText();
        yPosition -= 30;

        contentStream.setLineWidth(1f);
        contentStream.setStrokingColor(0, 102, 204);
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(pageWidth - margin, yPosition);
        contentStream.stroke();
        yPosition -= 20;

        // Booking Details Section (No IDs)
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.setNonStrokingColor(0, 0, 0);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Booking Details");
        contentStream.endText();
        yPosition -= 15;

        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Booking Date: " + java.time.LocalDate.now().toString());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Flight: " + booking.getAirlines());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Departure Time: " + (booking.getDepartureTime() != null ? booking.getDepartureTime().toString() : "N/A"));
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Hotel: " + booking.getHotelName() + " (" + booking.getHotelLocation() + ")");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Hotel Rating: " + booking.getHotelRating() + " stars");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Conference: " + booking.getConferenceName());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Transport: " + booking.getTransportType());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Event Start: " + (booking.getStartEvement() != null ? booking.getStartEvement().toString() : "N/A"));
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Event End: " + (booking.getEndEvement() != null ? booking.getEndEvement().toString() : "N/A"));
        contentStream.endText();
        yPosition -= 135; // Adjusted for fewer lines

        // Guest Information Section
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Guest Information");
        contentStream.endText();
        yPosition -= 15;

        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Name: " + booking.getUserName());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Email: [User Email]");
        contentStream.endText();
        yPosition -= 50;

        // Cost Breakdown Table
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Cost Breakdown");
        contentStream.endText();
        yPosition -= 15;

        float tableWidth = pageWidth - 2 * margin;
        float columnWidth = tableWidth / 4;
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        contentStream.setNonStrokingColor(255, 255, 255);
        contentStream.setStrokingColor(0, 102, 204);
        contentStream.addRect(margin, yPosition - 15, tableWidth, 15);
        contentStream.fill();

        contentStream.setNonStrokingColor(0, 0, 0);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 5, yPosition - 12);
        contentStream.showText("Description");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText("Unit Cost");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText("Quantity");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText("Amount");
        contentStream.endText();
        yPosition -= 15;

        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 5, yPosition - 12);
        contentStream.showText("Flight (" + booking.getAirlines() + ")");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText(booking.getFlightPrice() + " €");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText("1");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText(booking.getFlightPrice() + " €");
        contentStream.endText();
        yPosition -= 15;

        float hotelCost = 100; // Replace with actual logic if available
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 5, yPosition - 12);
        contentStream.showText("Hotel (" + booking.getHotelName() + ")");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText(hotelCost + " TND");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText("1");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText(hotelCost + " TND");
        contentStream.endText();
        yPosition -= 15;

        float vatRate = 0.20f;
        double subtotal = booking.getFlightPrice() + hotelCost;
        double vat = subtotal * vatRate;
        double total = booking.getPriceTotal();

        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 5, yPosition - 12);
        contentStream.showText("Subtotal");
        contentStream.newLineAtOffset(columnWidth * 3, 0);
        contentStream.showText(subtotal + " TND");
        contentStream.endText();
        yPosition -= 15;

        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 5, yPosition - 12);
        contentStream.showText("VAT (" + (vatRate * 100) + "%)");
        contentStream.newLineAtOffset(columnWidth * 3, 0);
        contentStream.showText(vat + " TND");
        contentStream.endText();
        yPosition -= 15;

        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 5, yPosition - 12);
        contentStream.showText("Total");
        contentStream.newLineAtOffset(columnWidth * 3, 0);
        contentStream.showText(total + " TND");
        contentStream.endText();
        yPosition -= 20;

        contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 8);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("You are not able to change anything now.");
        contentStream.endText();

        contentStream.close();
        File file = new File("BookingConfirmation.pdf");
        document.save(file);
        document.close();

        return file;
    }
}

 */
package org.example.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.example.dao.Bookingimplt;
import org.example.dao.DBconnection;
import org.example.dao.EventDAOImpl;
import org.example.dao.FlightDAOimplt;
import org.example.models.Booking;
import org.example.models.Evenement;
import org.example.models.Flight;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.Properties;

public class BookingDetailsController {

    @FXML
    private TextField bookingIdField; // Will not display

    @FXML
    private TextField userNameField;

    @FXML
    private TextField flightIdField; // Will not display

    @FXML
    private TextField hotelIdField; // Will not display

    @FXML
    private TextField transportIdField; // Will not display

    @FXML
    private TextField conferenceLocationIdField; // Will not display

    @FXML
    private TextField airlinesField;

    @FXML
    private TextField flightPriceField;

    @FXML
    private TextField departureTimeField;

    @FXML
    private TextField hotelNameField;

    @FXML
    private TextField hotelLocationField;

    @FXML
    private TextField hotelRatingField;

    @FXML
    private TextField conferenceNameField;

    @FXML
    private TextField transportTypeField;

    @FXML
    private TextField priceTotalField;

    @FXML
    private TextField eventStartField;

    @FXML
    private TextField eventEndField;

    private Booking selectedBooking;
    private AdminController adminController;

    public void setBooking(Booking booking, AdminController adminController) {
        this.selectedBooking = booking;
        this.adminController = adminController;

        // Fetch updated booking details from the database
        Bookingimplt bookingDAO = new Bookingimplt();
        Booking updatedBooking = bookingDAO.findById(booking.getBookingId());
        if (updatedBooking != null) {
            this.selectedBooking = updatedBooking;
        }

        // Fetch event details from the evenement table using id_evement
        try (Connection conn = DBconnection.getConnection()) {
            EventDAOImpl eventDAO = new EventDAOImpl(conn);
            java.util.Optional<Evenement> eventOpt = eventDAO.findEventById(booking.getIdEvement());
            if (eventOpt.isPresent()) {
                Evenement event = eventOpt.get();
                if (eventStartField != null) {
                    eventStartField.setText(event.getDateDebut().toString());
                }
                if (eventEndField != null) {
                    eventEndField.setText(event.getDateFin().toString());
                }
                selectedBooking.setStartEvement(new java.sql.Timestamp(event.getDateDebut().getTime()));
                selectedBooking.setEndEvement(new java.sql.Timestamp(event.getDateFin().getTime()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to fetch event details: " + e.getMessage());
        }

        // Populate fields without showing IDs
        bookingIdField.setVisible(false);
        userNameField.setText(selectedBooking.getUserName());
        flightIdField.setVisible(false);
        hotelIdField.setVisible(false);
        transportIdField.setVisible(false);
        conferenceLocationIdField.setVisible(false);
        airlinesField.setText(selectedBooking.getAirlines());
        flightPriceField.setText(String.valueOf(selectedBooking.getFlightPrice()));
        departureTimeField.setText(selectedBooking.getDepartureTime() != null ? selectedBooking.getDepartureTime().toString() : "N/A");
        hotelNameField.setText(selectedBooking.getHotelName());
        hotelLocationField.setText(selectedBooking.getHotelLocation());
        hotelRatingField.setText(String.valueOf(selectedBooking.getHotelRating()));
        conferenceNameField.setText(selectedBooking.getConferenceName());
        transportTypeField.setText(selectedBooking.getTransportType());
        priceTotalField.setText(String.valueOf(selectedBooking.getPriceTotal()));
    }

    @FXML
    private void confirmBooking() {
        updateStatus("confirmed");
        sendConfirmationEmail("borgimoatez35@gmail.com");
    }

    @FXML
    private void notConfirmBooking() {
        updateStatus("not confirmed");
        sendNotConfirmedEmail("borgimoatez35@gmail.com");
    }

    private void updateStatus(String status) {
        Bookingimplt bookingDAO = new Bookingimplt();
        bookingDAO.updateStatus(selectedBooking.getBookingId(), status);
        System.out.println("Booking status updated to: " + status);

        if (adminController != null) {
            adminController.refreshTable();
        }

        bookingIdField.getScene().getWindow().hide();
    }

    private void sendConfirmationEmail(String recipientEmail) {
        String host = "smtp.gmail.com";
        String port = "587";
        String username = "borgimoatez@gmail.com";
        String password = "jkth ehop oogs izsl";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Booking Confirmation");

            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setText("Dear " + selectedBooking.getUserName() + ",\n\n"
                    + "Your booking has been confirmed.\n"
                    + "Please see the attached PDF for more details.\n"
                    + "Thank you for choosing our service.\n\n"
                    + "Best regards,\nYour Booking Team");

            MimeBodyPart pdfAttachment = new MimeBodyPart();
            File pdfFile = generatePdf(selectedBooking);
            pdfAttachment.attachFile(pdfFile);

            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);
            multipart.addBodyPart(pdfAttachment);

            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Confirmation email sent to " + recipientEmail);

            pdfFile.delete();
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    private void sendNotConfirmedEmail(String recipientEmail) {
        String host = "smtp.gmail.com";
        String port = "587";
        String username = "borgimoatez@gmail.com";
        String password = "jkth ehop oogs izsl";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipientEmail));
            message.setSubject("Booking Not Confirmed");
            message.setText("Dear " + selectedBooking.getUserName() + ",\n\n"
                    + "We regret to inform you that your booking could not be confirmed.\n"
                    + "Flight: " + selectedBooking.getAirlines() + "\n"
                    + "Departure Time: " + selectedBooking.getDepartureTime() + "\n"
                    + "Hotel: " + selectedBooking.getHotelName() + " (" + selectedBooking.getHotelLocation() + ")\n"
                    + "Conference: " + selectedBooking.getConferenceName() + "\n"
                    + "Transport: " + selectedBooking.getTransportType() + "\n"
                    + "Total Price: " + selectedBooking.getPriceTotal() + "\n"
                    + "Event Start: " + (selectedBooking.getStartEvement() != null ? selectedBooking.getStartEvement() : "N/A") + "\n"
                    + "Event End: " + (selectedBooking.getEndEvement() != null ? selectedBooking.getEndEvement() : "N/A") + "\n"
                    + "Please contact our support team for further assistance.\n\n"
                    + "Best regards,\nYour Booking Team");

            Transport.send(message);
            System.out.println("Not Confirmed email sent to " + recipientEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }

    private File generatePdf(Booking booking) throws IOException {
        PDDocument document = new PDDocument();
        PDPage page = new PDPage(PDRectangle.A4);
        document.addPage(page);

        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        float pageWidth = page.getMediaBox().getWidth();
        float pageHeight = page.getMediaBox().getHeight();
        float margin = 50;
        float yPosition = pageHeight - margin;

        // Add logo
        try {
            InputStream imageStream = getClass().getClassLoader().getResourceAsStream("fxml/123.png");
            if (imageStream == null) {
                throw new IOException("Image file 'fxml/123.png' not found in resources.");
            }
            PDImageXObject logo = PDImageXObject.createFromByteArray(document, imageStream.readAllBytes(), "123.png");
            imageStream.close();
            float logoWidth = 100;
            float logoHeight = 50;
            float logoX = margin;
            float logoY = yPosition - logoHeight;
            contentStream.drawImage(logo, logoX, logoY, logoWidth, logoHeight);
            yPosition = logoY - 20;
        } catch (IOException e) {
            System.err.println("Failed to load logo: " + e.getMessage());
        }

        // Add title
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 20);
        contentStream.setNonStrokingColor(0, 102, 204);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Booking Confirmation");
        contentStream.endText();
        yPosition -= 30;

        contentStream.setLineWidth(1f);
        contentStream.setStrokingColor(0, 102, 204);
        contentStream.moveTo(margin, yPosition);
        contentStream.lineTo(pageWidth - margin, yPosition);
        contentStream.stroke();
        yPosition -= 20;

        // Booking Details Section (No IDs, include back time)
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.setNonStrokingColor(0, 0, 0);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Booking Details");
        contentStream.endText();
        yPosition -= 15;

        // Fetch flight details to get back_time
        FlightDAOimplt flightDAO = new FlightDAOimplt();
        Flight flight = flightDAO.findById(booking.getFlightId());
        String backTime = (flight != null && flight.getBackTime() != null) ? flight.getBackTime().toString() : "N/A";

        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Booking Date: " + java.time.LocalDate.now().toString());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Flight: " + booking.getAirlines());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Departure Time: " + (booking.getDepartureTime() != null ? booking.getDepartureTime().toString() : "N/A"));
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Return Time: " + backTime); // Added back time
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Hotel: " + booking.getHotelName() + " (" + booking.getHotelLocation() + ")");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Hotel Rating: " + booking.getHotelRating() + " stars");
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Conference: " + booking.getConferenceName());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Transport: " + booking.getTransportType());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Event Start: " + (booking.getStartEvement() != null ? booking.getStartEvement().toString() : "N/A"));
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Event End: " + (booking.getEndEvement() != null ? booking.getEndEvement().toString() : "N/A"));
        contentStream.endText();
        yPosition -= 150; // Adjusted for additional line

        // Guest Information Section
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Guest Information");
        contentStream.endText();
        yPosition -= 15;

        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Name: " + booking.getUserName());
        contentStream.newLineAtOffset(0, -15);
        contentStream.showText("Email: [User Email]");
        contentStream.endText();
        yPosition -= 50;

        // Cost Breakdown Table
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 14);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("Cost Breakdown");
        contentStream.endText();
        yPosition -= 15;

        float tableWidth = pageWidth - 2 * margin;
        float columnWidth = tableWidth / 4;
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        contentStream.setNonStrokingColor(255, 255, 255);
        contentStream.setStrokingColor(0, 102, 204);
        contentStream.addRect(margin, yPosition - 15, tableWidth, 15);
        contentStream.fill();

        contentStream.setNonStrokingColor(0, 0, 0);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 5, yPosition - 12);
        contentStream.showText("Description");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText("Unit Cost");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText("Quantity");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText("Amount");
        contentStream.endText();
        yPosition -= 15;

        contentStream.setFont(PDType1Font.HELVETICA, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 5, yPosition - 12);
        contentStream.showText("Flight (" + booking.getAirlines() + ")");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText(booking.getFlightPrice() + " €");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText("1");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText(booking.getFlightPrice() + " €");
        contentStream.endText();
        yPosition -= 15;

        float hotelCost = 100; // Replace with actual logic if available
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 5, yPosition - 12);
        contentStream.showText("Hotel (" + booking.getHotelName() + ")");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText(hotelCost + " TND");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText("1");
        contentStream.newLineAtOffset(columnWidth, 0);
        contentStream.showText(hotelCost + " TND");
        contentStream.endText();
        yPosition -= 15;

        float vatRate = 0.20f;
        double subtotal = booking.getFlightPrice() + hotelCost;
        double vat = subtotal * vatRate;
        double total = booking.getPriceTotal();

        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 5, yPosition - 12);
        contentStream.showText("Subtotal");
        contentStream.newLineAtOffset(columnWidth * 3, 0);
        contentStream.showText(subtotal + " TND");
        contentStream.endText();
        yPosition -= 15;

        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 5, yPosition - 12);
        contentStream.showText("VAT (" + (vatRate * 100) + "%)");
        contentStream.newLineAtOffset(columnWidth * 3, 0);
        contentStream.showText(vat + " TND");
        contentStream.endText();
        yPosition -= 15;

        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 10);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin + 5, yPosition - 12);
        contentStream.showText("Total");
        contentStream.newLineAtOffset(columnWidth * 3, 0);
        contentStream.showText(total + " TND");
        contentStream.endText();
        yPosition -= 20;

        contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 8);
        contentStream.beginText();
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("You are not able to change anything now.");
        contentStream.endText();

        contentStream.close();
        File file = new File("BookingConfirmation.pdf");
        document.save(file);
        document.close();

        return file;
    }
}

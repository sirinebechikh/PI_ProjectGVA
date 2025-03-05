/*package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.dao.DBconnection;
import org.example.dao.EventDAOImpl;
import org.example.models.Evenement;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class DestinationController {
    @FXML
    private ListView<Evenement> eventListView; // Change to ListView<Evenement>

    private final EventDAOImpl eventDAO;

    // ObservableList to hold all events for filtering
    private ObservableList<Evenement> allEvents;

    // Date formatter for displaying dates
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public DestinationController() throws SQLException {
        // Assuming DBconnection provides a valid Connection object
        this.eventDAO = new EventDAOImpl(DBconnection.getConnection());
    }

    @FXML
    private void initialize() {
        // Fetch events and populate the ListView
        try {
            allEvents = FXCollections.observableArrayList(eventDAO.getAllEvents());
            eventListView.setItems(allEvents);

            // Set a custom cell factory to display events with images and icons
            eventListView.setCellFactory(param -> new ListCell<Evenement>() {
                private final ImageView eventImageView = new ImageView();
                private final Text eventName = new Text();
                private final Text startDate = new Text();
                private final Text endDate = new Text();
                private final Text location = new Text();
                private final Text guests = new Text();

                private final ImageView locationIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/location.png")));
                private final ImageView guestsIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/people.png")));
                private final ImageView dateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/clock.png")));

                private final VBox detailsBox = new VBox(5);
                private final HBox rootBox = new HBox(10);

                {
                    // Set image size
                    eventImageView.setFitWidth(100);
                    eventImageView.setFitHeight(100);
                    eventImageView.setPreserveRatio(true);

                    // Set icon size
                    locationIcon.setFitWidth(16);
                    locationIcon.setFitHeight(16);
                    guestsIcon.setFitWidth(16);
                    guestsIcon.setFitHeight(16);
                    dateIcon.setFitWidth(16);
                    dateIcon.setFitHeight(16);

                    // Style event name
                    eventName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #2c3e50;");

                    // Add icons and text to details
                    HBox locationBox = new HBox(5, locationIcon, location);
                    HBox guestsBox = new HBox(5, guestsIcon, guests);
                    HBox dateBox = new HBox(5, dateIcon, new Text("Start: "), startDate, new Text(" | End: "), endDate);

                    detailsBox.getChildren().addAll(eventName, dateBox, locationBox, guestsBox);
                    rootBox.getChildren().addAll(eventImageView, detailsBox);
                }

                @Override
                protected void updateItem(Evenement event, boolean empty) {
                    super.updateItem(event, empty);

                    if (empty || event == null) {
                        setGraphic(null);
                    } else {
                        // Load the image from the imagePath
                        String imagePath = event.getImagePath();
                        if (imagePath != null && !imagePath.isEmpty()) {
                            Image image = new Image("file:" + imagePath); // Assuming imagePath is a file path
                            eventImageView.setImage(image);
                        } else {
                            eventImageView.setImage(null); // No image available
                        }

                        // Set event details
                        eventName.setText(event.getNom());
                        startDate.setText(dateFormatter.format(event.getDateDebut())); // Format start date
                        endDate.setText(dateFormatter.format(event.getDateFin())); // Format end date
                        location.setText(event.getLieuEvenement());
                        guests.setText(String.valueOf(event.getNombreInvite()));

                        setGraphic(rootBox);
                    }
                }
            });
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to fetch events from the database.");
            e.printStackTrace();
        }
    }

    @FXML
    private void nextPage() {
        Evenement selectedEvent = eventListView.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert("Input Error", "Please select an event from the list.");
            return;
        }

        // Navigate to Flight Selection Screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/flights.fxml"));
            Parent root = loader.load();
            FlightController flightController = loader.getController();

            // Pass the selected event data to the FlightController
            flightController.setSelectedEventData(selectedEvent);

            Stage stage = (Stage) eventListView.getScene().getWindow();
            stage.setScene(new Scene(root, 934, 720));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void viewHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/History.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) eventListView.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Booking History");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void filterOne() {
        filterEventsByCriteria(1);
    }

    @FXML
    private void filterTwo() {
        filterEventsByCriteria(2);
    }

    @FXML
    private void filterThree() {
        filterEventsByCriteria(3);
    }

    @FXML
    private void resetFilter() {
        eventListView.setItems(allEvents);
    }

    private void filterEventsByCriteria(int criteria) {
        List<Evenement> filteredEvents = allEvents.stream()
                .filter(event -> {
                    // Example filtering logic (customize as needed)
                    return event.getNombreInvite() == criteria; // Replace with actual filtering logic
                })
                .collect(Collectors.toList());

        eventListView.setItems(FXCollections.observableArrayList(filteredEvents));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

 */
/*
package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.dao.*;
import org.example.models.Evenement;
import org.example.models.Flight;
import org.example.models.Hotel;
import org.example.models.ConferenceLocation;
import org.example.models.Transport;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DestinationController {
    @FXML
    private ListView<Evenement> eventListView; // Change to ListView<Evenement>

    private final EventDAOImpl eventDAO;

    // ObservableList to hold all events for filtering
    private ObservableList<Evenement> allEvents;

    // Date formatter for displaying dates
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public DestinationController() throws SQLException {
        // Assuming DBconnection provides a valid Connection object
        this.eventDAO = new EventDAOImpl(DBconnection.getConnection());
    }

    @FXML
    private void initialize() {
        // Fetch events and populate the ListView
        try {
            allEvents = FXCollections.observableArrayList(eventDAO.getAllEvents());
            eventListView.setItems(allEvents);

            // Set a custom cell factory to display events with images and icons
            eventListView.setCellFactory(param -> new ListCell<Evenement>() {
                private final ImageView eventImageView = new ImageView();
                private final Text eventName = new Text();
                private final Text startDate = new Text();
                private final Text endDate = new Text();
                private final Text location = new Text();
                private final Text guests = new Text();

                private final ImageView locationIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/location.png")));
                private final ImageView guestsIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/people.png")));
                private final ImageView dateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/clock.png")));

                private final VBox detailsBox = new VBox(5);
                private final HBox rootBox = new HBox(10);

                {
                    // Set image size
                    eventImageView.setFitWidth(100);
                    eventImageView.setFitHeight(100);
                    eventImageView.setPreserveRatio(true);

                    // Set icon size
                    locationIcon.setFitWidth(16);
                    locationIcon.setFitHeight(16);
                    guestsIcon.setFitWidth(16);
                    guestsIcon.setFitHeight(16);
                    dateIcon.setFitWidth(16);
                    dateIcon.setFitHeight(16);

                    // Style event name
                    eventName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #2c3e50;");

                    // Add icons and text to details
                    HBox locationBox = new HBox(5, locationIcon, location);
                    HBox guestsBox = new HBox(5, guestsIcon, guests);
                    HBox dateBox = new HBox(5, dateIcon, new Text("Start: "), startDate, new Text(" | End: "), endDate);

                    detailsBox.getChildren().addAll(eventName, dateBox, locationBox, guestsBox);
                    rootBox.getChildren().addAll(eventImageView, detailsBox);
                }

                @Override
                protected void updateItem(Evenement event, boolean empty) {
                    super.updateItem(event, empty);

                    if (empty || event == null) {
                        setGraphic(null);
                    } else {
                        // Load the image from the imagePath
                        String imagePath = event.getImagePath();
                        if (imagePath != null && !imagePath.isEmpty()) {
                            Image image = new Image("file:" + imagePath); // Assuming imagePath is a file path
                            eventImageView.setImage(image);
                        } else {
                            eventImageView.setImage(null); // No image available
                        }

                        // Set event details
                        eventName.setText(event.getNom());
                        startDate.setText(dateFormatter.format(event.getDateDebut())); // Format start date
                        endDate.setText(dateFormatter.format(event.getDateFin())); // Format end date
                        location.setText(event.getLieuEvenement());
                        guests.setText(String.valueOf(event.getNombreInvite()));

                        setGraphic(rootBox);
                    }
                }
            });
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to fetch events from the database.");
            e.printStackTrace();
        }
    }

    @FXML
    private void nextPage() {
        Evenement selectedEvent = eventListView.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert("Input Error", "Please select an event from the list.");
            return;
        }

        // Navigate to Flight Selection Screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/flights.fxml"));
            Parent root = loader.load();
            FlightController flightController = loader.getController();

            // Pass the selected event data to the FlightController
            flightController.setSelectedEventData(selectedEvent);

            Stage stage = (Stage) eventListView.getScene().getWindow();
            stage.setScene(new Scene(root, 934, 720));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void viewHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/History.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) eventListView.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Booking History");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void filterOne() {
        filterEventsByCriteria(1);
    }

    @FXML
    private void filterTwo() {
        filterEventsByCriteria(2);
    }

    @FXML
    private void filterThree() {
        filterEventsByCriteria(3);
    }

    @FXML
    private void resetFilter() {
        eventListView.setItems(allEvents);
    }



    private Flight fetchFlightFromRecommendation(String recommendations) {
        // Parse recommendations and fetch the corresponding flight from the database
        // Example: Extract flight details from recommendations and use FlightDAO to fetch the flight
        return new FlightDAOimplt().findAll().get(0); // Replace with actual logic
    }

    private Hotel fetchHotelFromRecommendation(String recommendations) {
        // Parse recommendations and fetch the corresponding hotel from the database
        return new HotelDAOimplt().findAll().get(0); // Replace with actual logic
    }

    private ConferenceLocation fetchConferenceFromRecommendation(String recommendations) {
        // Parse recommendations and fetch the corresponding conference location from the database
        return new ConferenceLocationDAO().findAll().get(0); // Replace with actual logic
    }

    private Transport fetchTransportFromRecommendation(String recommendations) {
        // Parse recommendations and fetch the corresponding transport from the database
        return new TransportDAO().findAll().get(0); // Replace with actual logic
    }

    private void navigateToBookingController(Flight flight, Hotel hotel, ConferenceLocation conference, Transport transport, Evenement event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/booking.fxml"));
            Parent root = loader.load();
            BookingController bookingController = loader.getController();

            // Pass the selected data to the BookingController
            bookingController.setBookingData(flight, hotel, conference, transport, event);

            Stage stage = (Stage) eventListView.getScene().getWindow();
            stage.setScene(new Scene(root, 934, 720));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filterEventsByCriteria(int criteria) {
        List<Evenement> filteredEvents = allEvents.stream()
                .filter(event -> {
                    // Example filtering logic (customize as needed)
                    return event.getNombreInvite() == criteria; // Replace with actual filtering logic
                })
                .collect(Collectors.toList());

        eventListView.setItems(FXCollections.observableArrayList(filteredEvents));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}



 */
package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.example.dao.*;
import org.example.models.Evenement;
import org.example.models.Flight;
import org.example.models.Hotel;
import org.example.models.ConferenceLocation;
import org.example.models.Transport;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.stream.Collectors;

public class DestinationController {
    @FXML
    private ListView<Evenement> eventListView; // Change to ListView<Evenement>

    private final EventDAOImpl eventDAO;

    // ObservableList to hold all events for filtering
    private ObservableList<Evenement> allEvents;

    // Date formatter for displaying dates
    private final SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public DestinationController() throws SQLException {
        // Assuming DBconnection provides a valid Connection object
        this.eventDAO = new EventDAOImpl(DBconnection.getConnection());
    }

    @FXML
    private void initialize() {
        // Fetch events and populate the ListView
        try {
            allEvents = FXCollections.observableArrayList(eventDAO.getAllEvents());
            eventListView.setItems(allEvents);

            // Set a custom cell factory to display events with images and icons
            eventListView.setCellFactory(param -> new ListCell<Evenement>() {
                private final ImageView eventImageView = new ImageView();
                private final Text eventName = new Text();
                private final Text startDate = new Text();
                private final Text endDate = new Text();
                private final Text location = new Text();
                private final Text guests = new Text();

                private final ImageView locationIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/location.png")));
                private final ImageView guestsIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/people.png")));
                private final ImageView dateIcon = new ImageView(new Image(getClass().getResourceAsStream("/icons/clock.png")));

                private final VBox detailsBox = new VBox(5);
                private final HBox rootBox = new HBox(10);

                {
                    // Set image size
                    eventImageView.setFitWidth(100);
                    eventImageView.setFitHeight(100);
                    eventImageView.setPreserveRatio(true);

                    // Set icon size
                    locationIcon.setFitWidth(16);
                    locationIcon.setFitHeight(16);
                    guestsIcon.setFitWidth(16);
                    guestsIcon.setFitHeight(16);
                    dateIcon.setFitWidth(16);
                    dateIcon.setFitHeight(16);

                    // Style event name
                    eventName.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-fill: #2c3e50;");

                    // Add icons and text to details
                    HBox locationBox = new HBox(5, locationIcon, location);
                    HBox guestsBox = new HBox(5, guestsIcon, guests);
                    HBox dateBox = new HBox(5, dateIcon, new Text("Start: "), startDate, new Text(" | End: "), endDate);

                    detailsBox.getChildren().addAll(eventName, dateBox, locationBox, guestsBox);
                    rootBox.getChildren().addAll(eventImageView, detailsBox);
                }

                @Override
                protected void updateItem(Evenement event, boolean empty) {
                    super.updateItem(event, empty);

                    if (empty || event == null) {
                        setGraphic(null);
                    } else {
                        // Load the image from the imagePath
                        String imagePath = event.getImagePath();
                        if (imagePath != null && !imagePath.isEmpty()) {
                            Image image = new Image("file:" + imagePath); // Assuming imagePath is a file path
                            eventImageView.setImage(image);
                        } else {
                            eventImageView.setImage(null); // No image available
                        }

                        // Set event details
                        eventName.setText(event.getNom());
                        startDate.setText(dateFormatter.format(event.getDateDebut())); // Format start date
                        endDate.setText(dateFormatter.format(event.getDateFin())); // Format end date
                        location.setText(event.getLieuEvenement());
                        guests.setText(String.valueOf(event.getNombreInvite()));

                        setGraphic(rootBox);
                    }
                }
            });
        } catch (SQLException e) {
            showAlert("Database Error", "Failed to fetch events from the database.");
            e.printStackTrace();
        }
    }

    @FXML
    private void nextPage() {
        Evenement selectedEvent = eventListView.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert("Input Error", "Please select an event from the list.");
            return;
        }

        // Navigate to Flight Selection Screen
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/flights.fxml"));
            Parent root = loader.load();
            FlightController flightController = loader.getController();

            // Pass the selected event data to the FlightController
            flightController.setSelectedEventData(selectedEvent);

            Stage stage = (Stage) eventListView.getScene().getWindow();
            stage.setScene(new Scene(root, 934, 720));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void viewHistory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/update_booking.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) eventListView.getScene().getWindow();
            stage.setScene(new Scene(root, 800, 600));
            stage.setTitle("Booking History");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void filterOne() {
        filterEventsByCriteria(1);
    }

    @FXML
    private void filterTwo() {
        filterEventsByCriteria(2);
    }

    @FXML
    private void filterThree() {
        filterEventsByCriteria(3);
    }

    @FXML
    private void resetFilter() {
        eventListView.setItems(allEvents);
    }

    @FXML
    private void generateBooking() {
        Evenement selectedEvent = eventListView.getSelectionModel().getSelectedItem();
        if (selectedEvent == null) {
            showAlert("Input Error", "Please select an event from the list.");
            return;
        }

        // Fetch data for the selected event
        String destination = selectedEvent.getLieuEvenement();
        int numberOfPeople = selectedEvent.getNombreInvite();
        double budget = selectedEvent.getBudgetPrevu();
        String eventType = selectedEvent.getType();

        // Calculate budget allocation (example: 40% for flights, 30% for hotels, 20% for conference, 10% for transport)
        double flightBudget = budget * 0.4;
        double hotelBudget = budget * 0.3;
        double conferenceBudget = budget * 0.2;
        double transportBudget = budget * 0.1;

        // Filter flights, hotels, conference locations, and transport
        List<Flight> recommendedFlights = filterFlights(destination, flightBudget);
        List<Hotel> recommendedHotels = filterHotels(destination, hotelBudget, 3); // Minimum rating of 3
        List<ConferenceLocation> recommendedConferences = filterConferenceLocations(destination, conferenceBudget, numberOfPeople);
        List<Transport> recommendedTransports = filterTransport("Bus", transportBudget); // Example: Filter for bus transport

        // Select the first recommendation (or implement a ranking system)
        Flight selectedFlight = recommendedFlights.isEmpty() ? null : recommendedFlights.get(0);
        Hotel selectedHotel = recommendedHotels.isEmpty() ? null : recommendedHotels.get(0);
        ConferenceLocation selectedConference = recommendedConferences.isEmpty() ? null : recommendedConferences.get(0);
        Transport selectedTransport = recommendedTransports.isEmpty() ? null : recommendedTransports.get(0);

        // Check if all recommendations are available
        if (selectedFlight == null || selectedHotel == null || selectedConference == null || selectedTransport == null) {
            showAlert("Recommendation Error", "Unable to find suitable recommendations for your event.");
            return;
        }

        // Navigate to BookingController and pass the recommendations
        navigateToBookingController(selectedFlight, selectedHotel, selectedConference, selectedTransport, selectedEvent);
    }

    /**
     * Filters flights based on destination and maximum price.
     */
    private List<Flight> filterFlights(String destination, double maxPrice) {
        FlightDAOimplt flightDAO = new FlightDAOimplt();
        List<Flight> allFlights = flightDAO.findAll();

        return allFlights.stream()
                .filter(flight -> flight.getDestination().equalsIgnoreCase(destination))
                .filter(flight -> flight.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    /**
     * Filters hotels based on location, maximum price per night, and minimum rating.
     */
    private List<Hotel> filterHotels(String location, double maxPricePerNight, int minRating) {
        HotelDAOimplt hotelDAO = new HotelDAOimplt();
        List<Hotel> allHotels = hotelDAO.findAll();

        return allHotels.stream()
                .filter(hotel -> hotel.getLocation().equalsIgnoreCase(location))
                .filter(hotel -> hotel.getPricePerNight() <= maxPricePerNight)
                .filter(hotel -> hotel.getRating() >= minRating)
                .collect(Collectors.toList());
    }

    /**
     * Filters conference locations based on address, maximum price per day, and minimum capacity.
     */
    private List<ConferenceLocation> filterConferenceLocations(String address, double maxPricePerDay, int minCapacity) {
        ConferenceLocationDAO conferenceDAO = new ConferenceLocationDAO();
        List<ConferenceLocation> allConferences = conferenceDAO.findAll();

        return allConferences.stream()
                .filter(conference -> conference.getAddress().equalsIgnoreCase(address))
                .filter(conference -> conference.getPricePerDay() <= maxPricePerDay)
                .filter(conference -> conference.getCapacity() >= minCapacity)
                .collect(Collectors.toList());
    }

    /**
     * Filters transport options based on type and maximum price.
     */
    private List<Transport> filterTransport(String type, double maxPrice) {
        TransportDAO transportDAO = new TransportDAO();
        List<Transport> allTransports = transportDAO.findAll();

        return allTransports.stream()
                .filter(transport -> transport.getType().equalsIgnoreCase(type))
                .filter(transport -> transport.getPrice() <= maxPrice)
                .collect(Collectors.toList());
    }

    private void navigateToBookingController(Flight flight, Hotel hotel, ConferenceLocation conference, Transport transport, Evenement event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/booking.fxml"));
            Parent root = loader.load();
            BookingController bookingController = loader.getController();

            // Pass the selected data to the BookingController
            bookingController.setBookingData(flight, hotel, conference, transport, event);

            Stage stage = (Stage) eventListView.getScene().getWindow();
            stage.setScene(new Scene(root, 934, 720));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void filterEventsByCriteria(int criteria) {
        List<Evenement> filteredEvents = allEvents.stream()
                .filter(event -> {
                    // Example filtering logic (customize as needed)
                    return event.getNombreInvite() == criteria; // Replace with actual filtering logic
                })
                .collect(Collectors.toList());

        eventListView.setItems(FXCollections.observableArrayList(filteredEvents));
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}

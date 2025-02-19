/*package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import org.example.dao.Bookingimplt;
import org.example.models.Booking;

import java.util.List;

public class AdminController {

    @FXML
    private TableView<Booking> bookingsTable;

    @FXML
    private TableColumn<Booking, Integer> bookingIdColumn;

    @FXML
    private TableColumn<Booking, String> userNameColumn;

    @FXML
    private TableColumn<Booking, Integer> flightIdColumn;

    @FXML
    private TableColumn<Booking, Integer> hotelIdColumn;

    @FXML
    private TableColumn<Booking, Integer> transportIdColumn;

    @FXML
    private TableColumn<Booking, Integer> conferenceLocationIdColumn;

    @FXML
    private TableColumn<Booking, String> statusColumn;

    @FXML
    private TableColumn<Booking, String> airlinesColumn;

    @FXML
    private TableColumn<Booking, Double> flightsPriceColumn;

    @FXML
    private TableColumn<Booking, String> departureTimeColumn;

    @FXML
    private TableColumn<Booking, Void> actionsColumn; // New column for buttons

    @FXML
    public void initialize() {
        // Set up table columns
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));
        flightIdColumn.setCellValueFactory(new PropertyValueFactory<>("flightId"));
        hotelIdColumn.setCellValueFactory(new PropertyValueFactory<>("hotelId"));
        transportIdColumn.setCellValueFactory(new PropertyValueFactory<>("transportId"));
        conferenceLocationIdColumn.setCellValueFactory(new PropertyValueFactory<>("conferenceLocationId"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        airlinesColumn.setCellValueFactory(new PropertyValueFactory<>("airlines"));
        flightsPriceColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getFlightPrice()).asObject());
        departureTimeColumn.setCellValueFactory(cellData -> {
            return cellData.getValue().getDepartureTime() != null
                    ? new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDepartureTime().toString())
                    : new javafx.beans.property.SimpleStringProperty("N/A");
        });

        // Fetch bookings with status = "wait"
        Bookingimplt bookingDAO = new Bookingimplt();
        List<Booking> bookings = bookingDAO.findByStatus("wait");

        ObservableList<Booking> observableBookings = FXCollections.observableArrayList(bookings);
        bookingsTable.setItems(observableBookings);

        // Add buttons to the Actions column
        actionsColumn.setCellFactory(createActionButtonCellFactory(bookingDAO));
    }

    // Custom cell factory for the Actions column
    private Callback<TableColumn<Booking, Void>, TableCell<Booking, Void>> createActionButtonCellFactory(Bookingimplt bookingDAO) {
        return new Callback<>() {
            @Override
            public TableCell<Booking, Void> call(TableColumn<Booking, Void> param) {
                return new TableCell<>() {
                    private final Button confirmButton = new Button("Confirm");
                    private final Button notConfirmButton = new Button("Not Confirm");

                    {
                        confirmButton.setOnAction(event -> {
                            Booking booking = getTableView().getItems().get(getIndex());
                            bookingDAO.updateStatus(booking.getBookingId(), "confirmed");
                            refreshTable(bookingDAO);
                        });

                        notConfirmButton.setOnAction(event -> {
                            Booking booking = getTableView().getItems().get(getIndex());
                            bookingDAO.updateStatus(booking.getBookingId(), "not confirmed");
                            refreshTable(bookingDAO);
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setGraphic(null);
                        } else {
                            HBox hbox = new HBox(5, confirmButton, notConfirmButton); // Buttons in the same cell
                            setGraphic(hbox);
                        }
                    }
                };
            }
        };
    }

    // Refresh the table after updating the status
    private void refreshTable(Bookingimplt bookingDAO) {
        List<Booking> updatedBookings = bookingDAO.findByStatus("wait");
        ObservableList<Booking> observableBookings = FXCollections.observableArrayList(updatedBookings);
        bookingsTable.setItems(observableBookings);
    }
}
 show with wait
 */
/*package org.example.controllers;

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

import java.util.List;

public class AdminController {

    @FXML
    private TableView<Booking> bookingsTable;

    @FXML
    private TableColumn<Booking, Integer> bookingIdColumn;

    @FXML
    private TableColumn<Booking, String> userNameColumn;

    @FXML
    public void initialize() {
        // Set up table columns
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        // Fetch bookings with status = "wait"
        Bookingimplt bookingDAO = new Bookingimplt();
        List<Booking> bookings = bookingDAO.findByStatus("wait");

        ObservableList<Booking> observableBookings = FXCollections.observableArrayList(bookings);
        bookingsTable.setItems(observableBookings);
    }

    @FXML
    private void handleRowClick() {
        // Get the selected booking
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking != null) {
            try {
                // Load the details page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bookingDetails.fxml"));
                Parent root = loader.load();

                // Pass the selected booking to the details controller
                BookingDetailsController detailsController = loader.getController();
                detailsController.setBooking(selectedBooking);

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
}
show with buuton tt7rk
 */
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

import java.util.List;

public class AdminController {

    @FXML
    private TableView<Booking> bookingsTable;

    @FXML
    private TableColumn<Booking, Integer> bookingIdColumn;

    @FXML
    private TableColumn<Booking, String> userNameColumn;

    @FXML
    public void initialize() {
        // Set up table columns
        bookingIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookingId"));
        userNameColumn.setCellValueFactory(new PropertyValueFactory<>("userName"));

        // Fetch bookings with status = "wait"
        refreshTable();
    }

    @FXML
    private void handleRowClick() {
        // Get the selected booking
        Booking selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if (selectedBooking != null) {
            try {
                // Load the details page
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/bookingDetails.fxml"));
                Parent root = loader.load();

                // Pass the selected booking and a reference to this controller
                BookingDetailsController detailsController = loader.getController();
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

    // Method to refresh the table
    public void refreshTable() {
        Bookingimplt bookingDAO = new Bookingimplt();
        List<Booking> bookings = bookingDAO.findByStatus("wait");

        ObservableList<Booking> observableBookings = FXCollections.observableArrayList(bookings);
        bookingsTable.setItems(observableBookings);
    }
}
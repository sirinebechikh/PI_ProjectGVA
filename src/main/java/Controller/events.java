package Controller;

import Entities.Evenement;
import Entities.RoleType;
import Entities.User;
import Service.EvenementService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class events {

    public Button addEventButton;

    @FXML
    public Button demandeSponsoringButton;

    @FXML
    private Button updatebutton;

    @FXML
    private Button delete;
    @FXML
    private Label LocationNameLabel;

    @FXML
    private Label LocationPriceLabel;

    @FXML
    private Label adresseLabel;

    @FXML
    private VBox chosenFruitCard;

    @FXML
    private Label descriptionLabel;

    @FXML
    private GridPane grid;

    @FXML
    private ImageView locationimg;

    @FXML
    private ScrollPane scroll;

    private int selectedEventId;
    @FXML
    private ItemController itemController;

    private RoleType role=RoleType.SPONSOR;

    public void updateButtonVisibility(RoleType role) {
        demandeSponsoringButton.setVisible(false);
        addEventButton.setVisible(false);

        // Show the button corresponding to the given role
        if (role == RoleType.SPONSOR) {
            demandeSponsoringButton.setVisible(true);
            addEventButton.setVisible(false);
            delete.setVisible(false);
            updatebutton.setVisible(false);

        } else {
            demandeSponsoringButton.setVisible(false);
            addEventButton.setVisible(true);

            ;}
        }
    @FXML
    void handleReserveButtonClick(ActionEvent event) {

    }

    @FXML
    private void initialize() {
        updateButtonVisibility( role);
        // Fetch locations from the database
        EvenementService EvenementService = new EvenementService();
        List<Evenement> Evenements = EvenementService.getUnvalidatedEvenements();

        // Initialize column and row counters
        int column = 0;
        int row = 1; // Start displaying images from the second row

        // Populate the grid with items representing each location
        for (Evenement Evenement : Evenements) {
            // Fetch the associated image for the location

            String image = Evenement.getImagePath(); // Assuming location.getId() gives the location ID


            // Load the image
            if (image != null) {
                javafx.scene.image.Image fxImage = new javafx.scene.image.Image(image);

                // Create a new item controller for the location
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/item.fxml"));
                AnchorPane itemPane;
                try {
                    itemPane = fxmlLoader.load();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                ItemController itemController = fxmlLoader.getController();


                // Set the location information in the item controller
                itemController.setLocation(Evenement.getNom(), Evenement.getBudgetPrevu(), fxImage,Evenement);

                // Set size constraints for the itemPane
                itemPane.setPrefSize(300, 300); // Adjust size as needed

                // Add event handler for item click
               itemPane.setOnMouseClicked(event -> handleItemClick(event, Evenement.getId())); // Pass location ID to handleItemClick

                // Add the item to the grid
                grid.add(itemPane, column++, row);
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_COMPUTED_SIZE);

                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_COMPUTED_SIZE);
            } else {
                // Print "not available" in the console
                System.out.println("Image not available for location: " + Evenement.getNom());
            }

            // Check if column exceeds the maximum allowed (3)
            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }
    @FXML
    void handleUpdate(ActionEvent event) {
        if (selectedEventId == 0) {
            System.out.println("No event selected for update.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateEvent.fxml"));
            AnchorPane pane = loader.load();

            UpdateEventController updateController = loader.getController();
            EvenementService evenementService = new EvenementService();
            Evenement selectedEvent = evenementService.getEvenementById(selectedEventId);
            updateController.setEventData(selectedEvent);

            Stage stage = new Stage();
            stage.setScene(new Scene(pane));
            stage.setTitle("Update Event");
            stage.show();

            // Close the current event page when update is done
            stage.setOnHidden(e -> reloadEventPage(event));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void handledelete(ActionEvent event) {
        if (selectedEventId == 0) {
            System.out.println("No event selected for deletion.");
            return;
        }

        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Deletion");
            alert.setHeaderText("Are you sure you want to delete this event?");
            alert.setContentText("This action cannot be undone.");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    EvenementService evenementService = new EvenementService();
                    try {
                        evenementService.deleteEvenement(selectedEventId);
                        System.out.println("Event deleted successfully.");

                        // Reload event page
                        reloadEventPage(event);

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void reloadEventPage(ActionEvent event) {
        // Get the current stage
        Stage currentStage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        currentStage.close(); // Close the current window

        try {
            // Load the events page again
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/events.fxml"));
            AnchorPane pane = loader.load();

            Stage newStage = new Stage();
            newStage.setScene(new Scene(pane));
            newStage.setTitle("Events");
            newStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @FXML
    public void refreshEventList() {
        // Clear the current grid
        grid.getChildren().clear();

        // Fetch updated events from the database
        EvenementService evenementService = new EvenementService();
        List<Evenement> evenements = evenementService.getUnvalidatedEvenements();

        // Initialize column and row counters
        int column = 0;
        int row = 1;

        // Populate the grid with new event data
        for (Evenement evenement : evenements) {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/item.fxml"));
            AnchorPane itemPane;
            try {
                itemPane = fxmlLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            ItemController itemController = fxmlLoader.getController();
            javafx.scene.image.Image fxImage = new javafx.scene.image.Image(evenement.getImagePath());

            // Set the event information in the item controller
            itemController.setLocation(evenement.getType(), evenement.getBudgetPrevu(), fxImage,evenement);

            // Set size constraints for the itemPane
            itemPane.setPrefSize(300, 300);

            // Add event handler for item click
            itemPane.setOnMouseClicked(event -> handleItemClick(event, evenement.getId()));

            // Add the item to the grid
            grid.add(itemPane, column++, row);
            grid.setMinWidth(Region.USE_COMPUTED_SIZE);
            grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
            grid.setMaxWidth(Region.USE_COMPUTED_SIZE);

            grid.setMinHeight(Region.USE_COMPUTED_SIZE);
            grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
            grid.setMaxHeight(Region.USE_COMPUTED_SIZE);

            // Reset column if necessary
            if (column == 3) {
                column = 0;
                row++;
            }
        }
    }


    @FXML
    private void handleItemClick(MouseEvent event, int EventId) {
        // Store the selected location ID
        selectedEventId = EventId;

        // Fetch location details using locationId
        EvenementService EvenementService = new EvenementService();
        Evenement clickedLocation = EvenementService.getEvenementById(EventId);

        // Update chosen location
        updateChosenLocation(clickedLocation);

    }
    private void updateChosenLocation(Evenement Event) {
        Label locationNameLabel = (Label) chosenFruitCard.lookup("#LocationNameLabel");
        Label locationPriceLabel = (Label) chosenFruitCard.lookup("#LocationPriceLabel");
        ImageView locationImageView = (ImageView) chosenFruitCard.lookup("#locationimg");
        Label adresseLabel = (Label) chosenFruitCard.lookup("#adresseLabel"); // Add this line
        Label descriptionLabel = (Label) chosenFruitCard.lookup("#descriptionLabel"); // Add this line

        // Update labels with location information
        locationNameLabel.setText(Event.getType());
        locationPriceLabel.setText("$" + String.valueOf(Event.getBudgetPrevu()));
        adresseLabel.setText(Event.getLieuEvenement()); // Set address label text
        descriptionLabel.setText(Event.getDescription()); // Set description label text

        // Fetch the image for the selected location

        String image = Event.getImagePath();
        if (image != null) {
            // Load the image using the URL
            javafx.scene.image.Image fxImage = new javafx.scene.image.Image(image);
            locationImageView.setImage(fxImage);
        } else {
            // Handle case where no image is available
            locationImageView.setImage(null);
        }
    }

    @FXML
    void handleAddEvent(ActionEvent event) {
        try {
            // Load the AddEvent page
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AddEvent.fxml"));
            Parent root = loader.load();

            // Create a new scene and stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Add Event");

            // Show the Add Event window
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDemandeSponsoring(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/AdminDemandeSponsoring.fxml"));
            Parent root = loader.load();

            Stage newStage = new Stage(); // Create a new window
            newStage.setTitle("Demande Sponsoring");
            newStage.setScene(new Scene(root));
            newStage.show(); // Open new window without closing the current one
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to handle "Admin" button click
    // Method to handle "Admin" button click
    public void setAdminRole(ActionEvent event) {
        User user = new User("exemple", "test@mail.com", "test", "1231", RoleType.ADMIN, false);
        System.out.println("Admin role selected");

        // Call the static method in ItemController to update the button visibility
        itemController.updateButtonVisibility(user.getRole());
    }

    // Method to handle "Client" button click
    public void setClientRole(ActionEvent event) {
        User user = new User("exemple", "test@mail.com", "test", "1231", RoleType.CLIENT, false);
        System.out.println("Client role selected");

        // Call the static method in ItemController to update the button visibility
        itemController.updateButtonVisibility(user.getRole());
    }

    // Method to handle "Sponsor" button click
    public void setSponsorRole(ActionEvent event) {
        User user = new User("exemple", "test@mail.com", "test", "1231", RoleType.SPONSOR, false);
        System.out.println("Sponsor role selected");

        // Call the static method in ItemController to update the button visibility
        itemController.updateButtonVisibility(user.getRole());
    }
}


  package org.example.controllers;

// Importation des bibliothèques nécessaires

import org.example.models.Evenement;
import org.example.models.RoleType;
import org.example.models.User;
import org.example.dao.EmailUtil;
import org.example.dao.EvenementService;
import org.example.dao.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

// Déclaration de la classe AddEvent (Contrôleur pour ajouter un événement)
public class AddEvent {

    // Les éléments FXML reliés à l’interface graphique
    @FXML
    private ResourceBundle resources;  // Ressources du fichier FXML

    @FXML
    private URL location;  // Lien vers le fichier FXML

    @FXML
    private TextField activitiesField;  // Champ pour les activités

    @FXML
    private TextField budgetField;  // Champ pour le budget

    @FXML
    private DatePicker dateDebutPicker;  // Sélecteur de date de début

    @FXML
    private DatePicker dateFinPicker;  // Sélecteur de date de fin

    @FXML
    private TextArea descriptionField;  // Champ de description

    @FXML
    private TextField imageTF1;  // Champ pour le chemin de l’image

    @FXML
    private TextField lieuField;  // Champ pour le lieu

    @FXML
    private TextField nomField;  // Champ pour le nom de l’événement

    @FXML
    private TextField nombreInviteField;  // Champ pour le nombre d’invités

    @FXML
    private TextField typeField;  // Champ pour le type d’événement

    @FXML
    private Pane imagePane;  // Espace où afficher l’image sélectionnée

    // Instance du service pour gérer les événements dans la base de données
    private EvenementService eventService = new EvenementService();

    private UserService userService = new UserService();

    private static final String CURRENT_DATETIME = "2025-03-02 21:24:46";
    private static final String CLIENT_LOGIN = "sirine";
    private static final int CLIENT_ID = 1; // Assuming user ID 1 exists in the database
    private static final String CLIENT_EMAIL = "sirine@example.com";

    private static User staticClient;

    private User getStaticClient() {
        if (staticClient == null) {
            // Try to get the user from the database first
            try {
                User dbUser = userService.getUserById(CLIENT_ID);
                if (dbUser != null) {
                    staticClient = dbUser;
                    return staticClient;
                }
            } catch (SQLException e) {
                System.err.println("Error fetching static client from database: " + e.getMessage());
                // Continue to create a static user below if database fetch fails
            }

            // Create a static user if not found in the database
            staticClient = new User();
            staticClient.setId(CLIENT_ID);
            staticClient.setNom(CLIENT_LOGIN);
            staticClient.setEmail(CLIENT_EMAIL);
            staticClient.setRole(RoleType.CLIENT);
            staticClient.setCompteValide(true);
            staticClient.setTelephone("12345678"); // Default phone number
        }
        return staticClient;
    }
    /**
     * Méthode pour ajouter un événement
     */
    @FXML
    void ajouterEvenement(ActionEvent event) {
        try {
            // Récupération des valeurs saisies dans les champs
            String nom = nomField.getText().trim();
            String type = typeField.getText().trim();
            String lieu = lieuField.getText().trim();
            String description = descriptionField.getText().trim();
            String activities = activitiesField.getText().trim();
            String imagePath = imageTF1.getText().trim();

            // Vérification des champs obligatoires
            if (nom.isEmpty() || type.isEmpty() || lieu.isEmpty() || description.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Veuillez remplir tous les champs obligatoires.");
                return;
            }

            int nombreInvite;
            double budget;

            // Vérification des valeurs numériques
            try {
                nombreInvite = Integer.parseInt(nombreInviteField.getText().trim());
                budget = Double.parseDouble(budgetField.getText().trim());
            } catch (NumberFormatException e) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Format invalide pour le nombre d'invités ou le budget.");
                return;
            }

            // Vérification des dates
            LocalDate dateDebut = dateDebutPicker.getValue();
            LocalDate dateFin = dateFinPicker.getValue();

            if (dateDebut == null || dateFin == null || dateDebut.isAfter(dateFin)) {
                showAlert(Alert.AlertType.ERROR, "Erreur", "Les dates saisies sont invalides.");
                return;
            }

            // Conversion des dates en format Date
            Date startDate = Date.from(dateDebut.atStartOfDay(ZoneId.systemDefault()).toInstant());
            Date endDate = Date.from(dateFin.atStartOfDay(ZoneId.systemDefault()).toInstant());

            // Get the static client
            User client = getStaticClient();

            // Création de l'objet événement avec le client associé
            Evenement newEvent = new Evenement(
                    0,                // id (will be set by database)
                    nom,              // nom
                    type,             // type
                    nombreInvite,     // nombreInvite
                    startDate,        // dateDebut
                    endDate,          // dateFin
                    description,      // description
                    lieu,             // lieuEvenement
                    budget,           // budgetPrevu
                    activities,       // activities
                    imagePath,        // imagePath
                    false             // validate (always false for new events)
            );

            // Set the client (user) for this event
            newEvent.setUser(client);

            // Ajout de l'événement dans la base de données
            eventService.addEvenement(newEvent);

            // Send confirmation email to the client
            sendEventCreationConfirmationEmail(newEvent);

            // Affichage d'un message de succès
            showAlert(Alert.AlertType.INFORMATION, "Succès",
                    "Événement ajouté avec succès et associé au client " + client.getNom() +
                            "! Une confirmation a été envoyée à " + client.getEmail());

            // Réinitialisation des champs après l'ajout
            clearFields();

        } catch (SQLException e) {
            showAlert(Alert.AlertType.ERROR, "Erreur Base de Données",
                    "Impossible d'ajouter l'événement : " + e.getMessage());
        }
    }

    private void sendEventCreationConfirmationEmail(Evenement event) {
        User client = event.getUser();
        if (client == null || client.getEmail() == null || client.getEmail().isEmpty()) {
            System.err.println("Cannot send confirmation email: No valid client email available for event");
            return;
        }

        String recipientEmail = client.getEmail();
        String recipientName = client.getNom();

        // Format dates for better readability
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("EEEE, d MMMM yyyy");
        String formattedStartDate = event.getDateDebut() != null ? dateFormat.format(event.getDateDebut()) : "Non spécifié";
        String formattedEndDate = event.getDateFin() != null ? dateFormat.format(event.getDateFin()) : "Non spécifié";

        // Subject for the confirmation email
        String subject = "Confirmation de Soumission d'Événement: " + event.getNom();

        // Email body for the confirmation
        String emailBody = "<!DOCTYPE html>"
                + "<html>"
                + "<head>"
                + "    <style>"
                + "        body { font-family: Arial, sans-serif; line-height: 1.6; }"
                + "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }"
                + "        .header { background-color: #4dabf7; color: white; padding: 15px; text-align: center; }"
                + "        .content { padding: 20px; border: 1px solid #ddd; }"
                + "        .footer { text-align: center; margin-top: 20px; font-size: 12px; color: #777; }"
                + "        .info-item { margin-bottom: 10px; }"
                + "        .label { font-weight: bold; }"
                + "        .note { background-color: #f8f9fa; padding: 15px; border-left: 4px solid #4dabf7; margin: 15px 0; }"
                + "    </style>"
                + "</head>"
                + "<body>"
                + "    <div class='container'>"
                + "        <div class='header'>"
                + "            <h2>Confirmation de Soumission d'Événement</h2>"
                + "        </div>"
                + "        <div class='content'>"
                + "            <p>Bonjour " + recipientName + ",</p>"
                + "            <p>Nous vous remercions d'avoir soumis votre événement sur notre plateforme. Les détails de votre événement ont été reçus et sont en attente d'approbation.</p>"
                + "            <div class='info-item'><span class='label'>Nom de l'événement:</span> " + event.getNom() + "</div>"
                + "            <div class='info-item'><span class='label'>Type:</span> " + event.getType() + "</div>"
                + "            <div class='info-item'><span class='label'>Description:</span> " + event.getDescription() + "</div>"
                + "            <div class='info-item'><span class='label'>Lieu:</span> " + event.getLieuEvenement() + "</div>"
                + "            <div class='info-item'><span class='label'>Date de début:</span> " + formattedStartDate + "</div>"
                + "            <div class='info-item'><span class='label'>Date de fin:</span> " + formattedEndDate + "</div>"
                + "            <div class='info-item'><span class='label'>Budget prévu:</span> " + event.getBudgetPrevu() + " DT</div>"
                + "            <div class='note'>"
                + "                <p>Votre événement est actuellement en cours d'examen par notre équipe d'administration.</p>"
                + "                <p>Vous recevrez une autre notification par email une fois que votre événement sera approuvé ou si des informations supplémentaires sont nécessaires.</p>"
                + "            </div>"
                + "            <p>Vous pouvez vérifier l'état de votre événement à tout moment en vous connectant à votre compte.</p>"
                + "            <p>Merci d'utiliser notre Système de Gestion d'Événements!</p>"
                + "            <p>Cordialement,<br>L'équipe de Gestion d'Événements</p>"
                + "        </div>"
                + "        <div class='footer'>"
                + "            <p>Ceci est un message automatique. Veuillez ne pas répondre à cet email.</p>"
                + "            <p>Système de Gestion d'Événements - " + java.time.Year.now().getValue() + "</p>"
                + "            <p>Envoyé le: " + CURRENT_DATETIME + "</p>"
                + "        </div>"
                + "    </div>"
                + "</body>"
                + "</html>";

        // Send the email notification
        try {
            // Assume you have an EmailUtil class with a sendEmail method
            boolean emailSent = EmailUtil.sendEmail(recipientEmail, subject, emailBody);

            if (emailSent) {
                System.out.println("Confirmation d'événement envoyée avec succès à: " + recipientEmail);
            } else {
                System.err.println("Échec de l'envoi de l'email de confirmation d'événement à: " + recipientEmail);
            }
        } catch (Exception e) {
            System.err.println("Erreur lors de l'envoi de l'email de confirmation d'événement: " + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * Méthode pour afficher une alerte
     */
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.show();
    }

    /**
     * Méthode pour nettoyer les champs après un ajout réussi
     */
    private void clearFields() {
        nomField.clear();
        typeField.clear();
        nombreInviteField.clear();
        dateDebutPicker.setValue(null);
        dateFinPicker.setValue(null);
        descriptionField.clear();
        lieuField.clear();
        budgetField.clear();
        activitiesField.clear();
        imageTF1.clear();
        imagePane.getChildren().clear();
    }

    /**
     * Méthode pour choisir un fichier image
     */
    @FXML
    void chooseImageFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choisir une image");

        // Filtrer pour n'afficher que les fichiers image
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Fichiers Images", "*.png", "*.jpg", "*.gif")
        );

        // Ouvrir l'explorateur de fichiers pour sélectionner une image
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            // Afficher le chemin de l’image dans le champ texte
            imageTF1.setText(selectedFile.getAbsolutePath());

            // Charger et afficher l’image sélectionnée dans le Pane
            Image image = new Image(selectedFile.toURI().toString());
            ImageView imageView = new ImageView(image);
            imageView.setFitWidth(300); // Largeur ajustée
            imageView.setFitHeight(300); // Hauteur ajustée
            imagePane.getChildren().clear(); // Nettoyer l'affichage précédent
            imagePane.getChildren().add(imageView); // Ajouter la nouvelle image
        }
    }

    /**
     * Méthode d'initialisation, appelée automatiquement au chargement de la scène FXML
     */
    @FXML
    void initialize() {
        LocalDate today = LocalDate.now();
        dateDebutPicker.setValue(today);
        dateFinPicker.setValue(today.plusDays(1)); // Default to next day

        // Initialize static client
        getStaticClient();
    }
}

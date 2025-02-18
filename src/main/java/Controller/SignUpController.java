package Controller;

import Service.ServiceUtilisateur;
import Entities.Utilisateur;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import java.io.IOException;

public class SignUpController {

    @FXML
    private TextField nomField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField motDePasseField;
    @FXML
    private TextField telephoneField;
    @FXML
    private Label loginLink; // Label for the "Login here" link

    private ServiceUtilisateur serviceUtilisateur;

    public SignUpController() {
        this.serviceUtilisateur = new ServiceUtilisateur();
    }

    // Handle sign-up form submission
    @FXML
    private void handleSignUp(ActionEvent event) {
        String nom = nomField.getText();
        String email = emailField.getText();
        String motDePasse = motDePasseField.getText();
        String telephone = telephoneField.getText();

        // Basic validation
        if (nom.isEmpty() || email.isEmpty() || motDePasse.isEmpty() || telephone.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Erreur", "Tous les champs doivent être remplis !");
            return;
        }

        // Create a new Utilisateur object
        Utilisateur newUser = new Utilisateur(0, nom, email, motDePasse, telephone);

        // Call the service to create the user in the database
        serviceUtilisateur.create(newUser);

        // Show success message
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Utilisateur créé avec succès !");

        // Optionally, reset the fields or close the sign-up scene
        resetFields();
    }

    // Handle the click on the "Login here" link
    @FXML
    private void handleLoginRedirect(MouseEvent event) {
        try {
            // Load the login scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/login.fxml"));
            Scene loginScene = new Scene(loader.load());

            // Get the current window (stage) and set the new scene
            Stage stage = (Stage) loginLink.getScene().getWindow();
            stage.setScene(loginScene);
            stage.setTitle("Login");

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Erreur", "Impossible de charger la page de connexion.");
        }
    }

    // Utility method to show alert boxes
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Utility method to reset the input fields after sign-up
    private void resetFields() {
        nomField.clear();
        emailField.clear();
        motDePasseField.clear();
        telephoneField.clear();
    }
}

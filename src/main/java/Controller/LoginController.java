package Controller;

import dbConnection.DataSource;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Hyperlink;
import javafx.scene.input.MouseEvent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    // No changes needed in the constructor
    public LoginController() {
    }

    public void handleLogin(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();

        if (authenticateUser(email, password)) {
            showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + email);
            // Open the user/admin dashboard here based on role
        } else {
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid email or password.");
        }
    }

    private boolean authenticateUser(String email, String password) {
        String query = "SELECT * FROM Utilisateur WHERE email = ?";
        try (Connection con = DataSource.getInstance().getConnection();
             PreparedStatement stmt = con.prepareStatement(query)) {

            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Retrieve the stored password from the database
                    String storedPassword = rs.getString("motDePasse");

                    // Check if the entered password matches the stored password (plaintext)
                    if (password.equals(storedPassword)) {
                        // If the user is an admin
                        if (rs.getString("role").equals("admin")) {
                            showAlert(Alert.AlertType.INFORMATION, "Admin Login", "Welcome Admin!");
                            // Open admin dashboard
                            openDashboard("/path/to/admin_dashboard.fxml", "Admin Dashboard");
                        } else {
                            showAlert(Alert.AlertType.INFORMATION, "User Login", "Welcome " + email);
                            // Open user dashboard
                            openDashboard("/path/to/user_dashboard.fxml", "User Dashboard");
                        }
                        return true;
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Invalid Password", "The password you entered is incorrect.");
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to load the dashboard scene based on the user type
    private void openDashboard(String fxmlPath, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Scene dashboardScene = new Scene(loader.load());
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(dashboardScene);
            stage.setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the dashboard page.");
        }
    }

    // Redirect to sign-up page
    @FXML
    private void handleSignUp(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/signup.fxml"));
            Scene signUpScene = new Scene(loader.load());
            Stage stage = (Stage) emailField.getScene().getWindow();
            stage.setScene(signUpScene);
            stage.setTitle("Sign Up");

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Unable to load the sign-up page.");
        }
    }

    // Utility method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

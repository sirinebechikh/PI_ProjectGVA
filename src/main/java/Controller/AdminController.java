package Controller;


import Entities.Utilisateur;
import Service.ServiceUtilisateur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AdminController {



    @FXML private TableView<Utilisateur> userTable;
    @FXML private TableColumn<Utilisateur, Integer> colId;
    @FXML private TableColumn<Utilisateur, String> colNom;
    @FXML private TableColumn<Utilisateur, String> colEmail;
    @FXML private TableColumn<Utilisateur, String> colPassword;
    @FXML private TableColumn<Utilisateur, String> colTelephone;
    @FXML private TextField nameField;
    @FXML private TextField emailField;
    @FXML private TextField passwordField;
    @FXML private TextField phoneField;
    @FXML private Button updateUserButton;
    @FXML private Button deleteUserButton;

    private final ObservableList<Utilisateur> userList = FXCollections.observableArrayList();
    private final ServiceUtilisateur utilisateurService = new ServiceUtilisateur();

    public void initialize() {
        colId.setCellValueFactory(cellData -> new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().getId()).asObject());
        colNom.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNom()));
        colEmail.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        colTelephone.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTelephone()));

        userList.setAll(utilisateurService.read());
        userTable.setItems(userList);
    }

    @FXML
    private void handleUpdateUser() {
        Utilisateur selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            selectedUser.setNom(nameField.getText());
            selectedUser.setEmail(emailField.getText());
            selectedUser.setTelephone(phoneField.getText());

            utilisateurService.update(selectedUser);
            userTable.refresh();
            showAlert("Success", "User updated successfully!", AlertType.INFORMATION);
        } else {
            showAlert("Error", "No user selected!", AlertType.ERROR);
        }
    }

    @FXML
    private void handleDeleteUser() {
        Utilisateur selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser != null) {
            utilisateurService.delete(selectedUser.getId());
            userList.remove(selectedUser);
            showAlert("Success", "User deleted successfully!", AlertType.INFORMATION);
        } else {
            showAlert("Error", "No user selected!", AlertType.ERROR);
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

package org.example.controllers;

import org.example.models.EmployeEventAssignment;
import org.example.models.Evenement;
import org.example.models.RoleType;
import org.example.models.User;
import org.example.dao.AlertUtil;
import org.example.dao.EmployeEventAssignmentService;
import org.example.dao.EvenementService;
import org.example.dao.UserService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class AssignEmployeesController {

    @FXML private TableView<User> availableEmployeesTable;
    @FXML private TableColumn<User, Integer> availableIdColumn;
    @FXML private TableColumn<User, String> availableNameColumn;
    @FXML private TableColumn<User, String> availableEmailColumn;

    @FXML private TableView<EmployeEventAssignment> assignedEmployeesTable;
    @FXML private TableColumn<EmployeEventAssignment, Integer> assignedIdColumn;
    @FXML private TableColumn<EmployeEventAssignment, String> assignedNameColumn;
    @FXML private TableColumn<EmployeEventAssignment, String> assignedRoleColumn;
    @FXML private TableColumn<EmployeEventAssignment, String> assignedStatusColumn;

    @FXML private ComboBox<String> roleComboBox;
    @FXML private ComboBox<String> statusComboBox;
    @FXML private Button assignButton;
    @FXML private Button removeButton;
    @FXML private Button updateStatusButton;
    @FXML private Button closeButton;

    @FXML private Label eventNameLabel;
    @FXML private Label eventIdLabel;

    private UserService userService;
    private EvenementService evenementService;
    private EmployeEventAssignmentService assignmentService;

    private ObservableList<User> availableEmployees = FXCollections.observableArrayList();
    private ObservableList<EmployeEventAssignment> assignedEmployees = FXCollections.observableArrayList();

    private Evenement currentEvent;

    @FXML
    public void initialize() {
        userService = new UserService();
        evenementService = new EvenementService();
        assignmentService = new EmployeEventAssignmentService();

        // Initialize tables
        initializeAvailableEmployeesTable();
        initializeAssignedEmployeesTable();

        // Setup ComboBoxes
        roleComboBox.getItems().addAll("Event Manager", "Technical Support", "Marketing", "Security", "Host");
        statusComboBox.getItems().addAll("Assigned", "In Progress", "Completed");

        // Disable buttons initially
        assignButton.setDisable(true);
        removeButton.setDisable(true);
        updateStatusButton.setDisable(true);

        // Add selection listeners
        availableEmployeesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
                assignButton.setDisable(newValue == null || roleComboBox.getValue() == null));

        assignedEmployeesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            removeButton.setDisable(newValue == null);
            updateStatusButton.setDisable(newValue == null || statusComboBox.getValue() == null);
        });

        roleComboBox.valueProperty().addListener((observable, oldValue, newValue) ->
                assignButton.setDisable(newValue == null || availableEmployeesTable.getSelectionModel().getSelectedItem() == null));

        statusComboBox.valueProperty().addListener((observable, oldValue, newValue) ->
                updateStatusButton.setDisable(newValue == null || assignedEmployeesTable.getSelectionModel().getSelectedItem() == null));
    }

    private void initializeAvailableEmployeesTable() {
        availableIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        availableNameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        availableEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        availableEmployeesTable.setItems(availableEmployees);
    }

    private void initializeAssignedEmployeesTable() {
        assignedIdColumn.setCellValueFactory(new PropertyValueFactory<>("employeeId"));

        // Custom cell factory for employee name
        assignedNameColumn.setCellFactory(column -> {
            return new TableCell<EmployeEventAssignment, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setText(null);
                    } else {
                        EmployeEventAssignment assignment = getTableView().getItems().get(getIndex());
                        try {
                            User employee = userService.getUserById(assignment.getEmployeeId());
                            setText(employee != null ? employee.getNom() : "Unknown");
                        } catch (SQLException e) {
                            setText("Error");
                            e.printStackTrace();
                        }
                    }
                }
            };
        });

        assignedRoleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
        assignedStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        assignedEmployeesTable.setItems(assignedEmployees);
    }

    public void setEvent(Evenement event) {
        this.currentEvent = event;
        eventNameLabel.setText(event.getNom());
        eventIdLabel.setText(String.valueOf(event.getId()));

        loadAvailableEmployees();
        loadAssignedEmployees();
    }

    private void loadAvailableEmployees() {
        try {
            // Get all users with EMPLOY role
            List<User> allEmployees = userService.getUsersByRole(RoleType.EMPLOY);

            // Clear and add to observable list
            availableEmployees.clear();

            // Filter out already assigned employees
            for (User employee : allEmployees) {
                if (!assignmentService.isEmployeeAssignedToEvent(employee.getId(), currentEvent.getId())) {
                    availableEmployees.add(employee);
                }
            }
        } catch (SQLException e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error loading available employees: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadAssignedEmployees() {
        try {
            // Get all assignments for the current event
            List<EmployeEventAssignment> assignments = assignmentService.getAssignmentsByEventId(currentEvent.getId());

            // Clear and add to observable list
            assignedEmployees.clear();
            assignedEmployees.addAll(assignments);
        } catch (SQLException e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Database Error",
                    "Error loading assigned employees: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleAssignEmployee(ActionEvent event) {
        User selectedEmployee = availableEmployeesTable.getSelectionModel().getSelectedItem();
        String selectedRole = roleComboBox.getValue();

        if (selectedEmployee == null || selectedRole == null) {
            AlertUtil.showAlert(Alert.AlertType.WARNING, "Selection Required",
                    "Please select an employee and a role.");
            return;
        }

        try {
            // Create a new assignment
            EmployeEventAssignment assignment = new EmployeEventAssignment(
                    0, // ID will be generated by database
                    selectedEmployee.getId(),
                    currentEvent.getId(),
                    selectedRole,
                    "Assigned" // Initial status
            );

            // Add assignment to database
            assignmentService.addAssignment(assignment);

            // Show success notification
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Employee Assigned",
                    "Employee " + selectedEmployee.getNom() + " has been assigned to this event. An email notification has been sent.");

            // Refresh the lists
            loadAvailableEmployees();
            loadAssignedEmployees();

            // Reset selection
            roleComboBox.setValue(null);
        } catch (SQLException e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Assignment Error",
                    "Error assigning employee: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleRemoveEmployee(ActionEvent event) {
        EmployeEventAssignment selectedAssignment = assignedEmployeesTable.getSelectionModel().getSelectedItem();

        if (selectedAssignment == null) {
            AlertUtil.showAlert(Alert.AlertType.WARNING, "Selection Required",
                    "Please select an assigned employee to remove.");
            return;
        }

        try {
            // Get employee name for confirmation message
            User employee = userService.getUserById(selectedAssignment.getEmployeeId());
            String employeeName = employee != null ? employee.getNom() : "Unknown";

            // Ask for confirmation
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirm Removal");
            confirmAlert.setHeaderText("Remove Employee Assignment");
            confirmAlert.setContentText("Are you sure you want to remove " + employeeName + " from this event?");

            Optional<ButtonType> result = confirmAlert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                // Delete the assignment
                assignmentService.deleteAssignment(selectedAssignment.getId());

                // Show success notification
                AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Employee Removed",
                        "Employee " + employeeName + " has been removed from this event. A notification email has been sent.");

                // Refresh the lists
                loadAvailableEmployees();
                loadAssignedEmployees();
            }
        } catch (SQLException e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Removal Error",
                    "Error removing employee: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleUpdateStatus(ActionEvent event) {
        EmployeEventAssignment selectedAssignment = assignedEmployeesTable.getSelectionModel().getSelectedItem();
        String newStatus = statusComboBox.getValue();

        if (selectedAssignment == null || newStatus == null) {
            AlertUtil.showAlert(Alert.AlertType.WARNING, "Selection Required",
                    "Please select an assigned employee and a status.");
            return;
        }

        try {
            // Update assignment status
            assignmentService.updateAssignmentStatus(selectedAssignment.getId(), newStatus);

            // Get employee name for confirmation message
            User employee = userService.getUserById(selectedAssignment.getEmployeeId());
            String employeeName = employee != null ? employee.getNom() : "Unknown";

            // Show success notification
            AlertUtil.showAlert(Alert.AlertType.INFORMATION, "Status Updated",
                    "Status for " + employeeName + " has been updated to " + newStatus + ". A notification email has been sent.");

            // Refresh the assigned employees list
            loadAssignedEmployees();

            // Reset selection
            statusComboBox.setValue(null);
        } catch (SQLException e) {
            AlertUtil.showAlert(Alert.AlertType.ERROR, "Update Error",
                    "Error updating status: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    void handleClose(ActionEvent event) {
        closeButton.getScene().getWindow().hide();
    }
}
package org.example.dao;



import org.example.models.EmployeEventAssignment;
import org.example.models.Evenement;
import org.example.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeEventAssignmentService {

    private Connection connection;
    private UserService userService;
    private EvenementService evenementService;

    public EmployeEventAssignmentService() {
        connection = DataSource.getInstance().getCnx();
        userService = new UserService();
        evenementService = new EvenementService();
    }

    // Add a new assignment with email notification
    public void addAssignment(EmployeEventAssignment assignment) throws SQLException {
        // First add the assignment to the database
        String query = "INSERT INTO employee_event_assignments (employee_id, event_id, role, status) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, assignment.getEmployeeId());
            statement.setInt(2, assignment.getEventId());
            statement.setString(3, assignment.getRole());
            statement.setString(4, assignment.getStatus());

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                // Get the generated ID
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        assignment.setId(generatedKeys.getInt(1));

                        // Send email notification
                        sendAssignmentNotificationEmail(assignment);
                    }
                }
            }
        }
    }

    // Send email notification for new assignment
    private void sendAssignmentNotificationEmail(EmployeEventAssignment assignment) {
        try {
            // Get employee details
            User employee = userService.getUserById(assignment.getEmployeeId());

            // Get event details
            Evenement event = evenementService.getEvenementById(assignment.getEventId());

            if (employee != null && event != null && employee.getEmail() != null) {
                // Create email subject
                String subject = "New Event Assignment: " + event.getNom();

                // Create email body
                String emailBody = EmailUtil.createAssignmentEmailBody(
                        employee.getNom(),
                        event.getNom(),
                        event.getLieuEvenement(),
                        event.getDateDebut().toString(),
                        event.getDateFin().toString(),
                        assignment.getRole()
                );

                // Send the email
                boolean emailSent = EmailUtil.sendEmail(employee.getEmail(), subject, emailBody);

                // Log the result
                if (emailSent) {
                    System.out.println("Assignment notification email sent to: " + employee.getEmail());
                } else {
                    System.err.println("Failed to send assignment notification email to: " + employee.getEmail());
                }
            } else {
                System.err.println("Missing employee or event information for email notification");
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving information for email notification: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error sending assignment notification email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Get all assignments for a specific event
    public List<EmployeEventAssignment> getAssignmentsByEventId(int eventId) throws SQLException {
        List<EmployeEventAssignment> assignments = new ArrayList<>();
        String query = "SELECT * FROM employee_event_assignments WHERE event_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, eventId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    EmployeEventAssignment assignment = new EmployeEventAssignment(
                            resultSet.getInt("id"),
                            resultSet.getInt("employee_id"),
                            resultSet.getInt("event_id"),
                            resultSet.getString("role"),
                            resultSet.getString("status")
                    );
                    assignments.add(assignment);
                }
            }
        }

        return assignments;
    }

    // Get all assignments for a specific employee
    public List<EmployeEventAssignment> getAssignmentsByEmployeeId(int employeeId) throws SQLException {
        List<EmployeEventAssignment> assignments = new ArrayList<>();
        String query = "SELECT * FROM employee_event_assignments WHERE employee_id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    EmployeEventAssignment assignment = new EmployeEventAssignment(
                            resultSet.getInt("id"),
                            resultSet.getInt("employee_id"),
                            resultSet.getInt("event_id"),
                            resultSet.getString("role"),
                            resultSet.getString("status")
                    );
                    assignments.add(assignment);
                }
            }
        }

        return assignments;
    }

    // Update assignment status with email notification
    public void updateAssignmentStatus(int assignmentId, String newStatus) throws SQLException {
        String query = "UPDATE employee_event_assignments SET status = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newStatus);
            statement.setInt(2, assignmentId);

            int affectedRows = statement.executeUpdate();

            if (affectedRows > 0) {
                // Send status update notification
                sendStatusUpdateNotificationEmail(assignmentId, newStatus);
            }
        }
    }

    // Send email notification for status update
    private void sendStatusUpdateNotificationEmail(int assignmentId, String newStatus) {
        try {
            // Get assignment details
            String query = "SELECT * FROM employee_event_assignments WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, assignmentId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int employeeId = resultSet.getInt("employee_id");
                        int eventId = resultSet.getInt("event_id");
                        String role = resultSet.getString("role");

                        // Get employee details
                        User employee = userService.getUserById(employeeId);

                        // Get event details
                        Evenement event = evenementService.getEvenementById(eventId);

                        if (employee != null && event != null && employee.getEmail() != null) {
                            // Create email subject
                            String subject = "Event Assignment Status Updated: " + event.getNom();
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
                                    + "        .status { background-color: #e9ecef; display: inline-block; padding: 5px 10px; border-radius: 3px; }"
                                    + "        .status.in-progress { background-color: #ffc107; color: #212529; }"
                                    + "        .status.completed { background-color: #28a745; color: white; }"
                                    + "    </style>"
                                    + "</head>"
                                    + "<body>"
                                    + "    <div class='container'>"
                                    + "        <div class='header'>"
                                    + "            <h2>Event Assignment Update</h2>"
                                    + "        </div>"
                                    + "        <div class='content'>"
                                    + "            <p>Hello " + employee.getNom() + ",</p>"
                                    + "            <p>The status of your assignment has been updated:</p>"
                                    + "            <div class='info-item'><span class='label'>Event:</span> " + event.getNom() + "</div>"
                                    + "            <div class='info-item'><span class='label'>Role:</span> " + role + "</div>"
                                    + "            <div class='info-item'><span class='label'>New Status:</span> <span class='status "  + "'>" + newStatus + "</span></div>"
                                    + "            <p>Please log into the Event Management System for more details.</p>"
                                    + "            <p>Thank you,<br>The Event Management Team</p>"
                                    + "        </div>"
                                    + "        <div class='footer'>"
                                    + "            <p>This is an automated message. Please do not reply to this email.</p>"
                                    + "        </div>"
                                    + "    </div>"
                                    + "</body>"
                                    + "</html>";

                            // Send the email
                            boolean emailSent = EmailUtil.sendEmail(employee.getEmail(), subject, emailBody);

                            // Log the result
                            if (emailSent) {
                                System.out.println("Status update notification email sent to: " + employee.getEmail());
                            } else {
                                System.err.println("Failed to send status update notification email to: " + employee.getEmail());
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving information for status update notification: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error sending status update notification email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Delete an assignment
    public void deleteAssignment(int assignmentId) throws SQLException {
        // First notify the employee before deleting
        notifyAssignmentRemoval(assignmentId);

        // Then delete the assignment
        String query = "DELETE FROM employee_event_assignments WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, assignmentId);
            statement.executeUpdate();
        }
    }

    // Notify employee about assignment removal
    private void notifyAssignmentRemoval(int assignmentId) {
        try {
            // Get assignment details before deletion
            String query = "SELECT * FROM employee_event_assignments WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setInt(1, assignmentId);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        int employeeId = resultSet.getInt("employee_id");
                        int eventId = resultSet.getInt("event_id");

                        // Get employee details
                        User employee = userService.getUserById(employeeId);

                        // Get event details
                        Evenement event = evenementService.getEvenementById(eventId);

                        if (employee != null && event != null && employee.getEmail() != null) {
                            // Create email subject
                            String subject = "Event Assignment Removed: " + event.getNom();

                            // Create email body
                            String emailBody = "<!DOCTYPE html>"
                                    + "<html>"
                                    + "<head>"
                                    + "    <style>"
                                    + "        body { font-family: Arial, sans-serif; line-height: 1.6; }"
                                    + "        .container { max-width: 600px; margin: 0 auto; padding: 20px; }"
                                    + "        .header { background-color: #dc3545; color: white; padding: 15px; text-align: center; }"
                                    + "        .content { padding: 20px; border: 1px solid #ddd; }"
                                    + "        .footer { text-align: center; margin-top: 20px; font-size: 12px; color: #777; }"
                                    + "        .info-item { margin-bottom: 10px; }"
                                    + "        .label { font-weight: bold; }"
                                    + "    </style>"
                                    + "</head>"
                                    + "<body>"
                                    + "    <div class='container'>"
                                    + "        <div class='header'>"
                                    + "            <h2>Event Assignment Removed</h2>"
                                    + "        </div>"
                                    + "        <div class='content'>"
                                    + "            <p>Hello " + employee.getNom() + ",</p>"
                                    + "            <p>Your assignment to the following event has been removed:</p>"
                                    + "            <div class='info-item'><span class='label'>Event:</span> " + event.getNom() + "</div>"
                                    + "            <div class='info-item'><span class='label'>Location:</span> " + event.getLieuEvenement() + "</div>"
                                    + "            <div class='info-item'><span class='label'>Dates:</span> " + event.getDateDebut() + " to " + event.getDateFin() + "</div>"
                                    + "            <p>If you have any questions, please contact your manager.</p>"
                                    + "            <p>Thank you,<br>The Event Management Team</p>"
                                    + "        </div>"
                                    + "        <div class='footer'>"
                                    + "            <p>This is an automated message. Please do not reply to this email.</p>"
                                    + "        </div>"
                                    + "    </div>"
                                    + "</body>"
                                    + "</html>";

                            // Send the email
                            EmailUtil.sendEmail(employee.getEmail(), subject, emailBody);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving information for assignment removal notification: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error sending assignment removal notification email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Check if employee is already assigned to an event
    public boolean isEmployeeAssignedToEvent(int employeeId, int eventId) throws SQLException {
        String query = "SELECT COUNT(*) FROM employee_event_assignments WHERE employee_id = ? AND event_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, employeeId);
            statement.setInt(2, eventId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        }
        return false;
    }
}
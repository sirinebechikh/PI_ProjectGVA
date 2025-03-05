package org.example.dao;

import org.example.models.Evenement;
import org.example.models.RoleType;
import org.example.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementService {
    private Connection connection;

    public EvenementService() {
        connection = DBconnection.getConnection(); // Updated to use DBconnection
    }

    // Add an event
    public void addEvenement(Evenement event) throws SQLException {
        String query = "INSERT INTO evenement (nom, type, nombreInvite, dateDebut, dateFin, description, lieuEvenement, budgetPrevu, activities, imagePath, validated, nomtilisateur) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, event.getNom());
            statement.setString(2, event.getType());
            statement.setInt(3, event.getNombreInvite());
            statement.setTimestamp(4, new Timestamp(event.getDateDebut().getTime())); // Use Timestamp for DATETIME
            statement.setTimestamp(5, new Timestamp(event.getDateFin().getTime()));   // Use Timestamp for DATETIME
            statement.setString(6, event.getDescription());
            statement.setString(7, event.getLieuEvenement());
            statement.setDouble(8, event.getBudgetPrevu());
            statement.setString(9, event.getActivities());
            statement.setString(10, event.getImagePath());
            statement.setBoolean(11, false); // New events are unvalidated
            statement.setString(12, event.getUser() != null ? event.getUser().getNom() : "unknown"); // Use nomtilisateur

            statement.executeUpdate();

            System.out.println("Event added successfully: " + event.getNom() + " by user " +
                    (event.getUser() != null ? event.getUser().getNom() : "unknown"));
        } catch (SQLException e) {
            System.err.println("Error adding event: " + e.getMessage());
            throw e;
        }
    }

    // Retrieve all events
    public List<Evenement> getAllEvenements() {
        List<Evenement> events = new ArrayList<>();
        String query = "SELECT * FROM evenement ORDER BY dateDebut DESC";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Evenement event = new Evenement(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("type"),
                        resultSet.getInt("nombreInvite"),
                        resultSet.getTimestamp("dateDebut"), // Use Timestamp
                        resultSet.getTimestamp("dateFin"),   // Use Timestamp
                        resultSet.getString("description"),
                        resultSet.getString("lieuEvenement"),
                        resultSet.getDouble("budgetPrevu"),
                        resultSet.getString("activities"),
                        resultSet.getString("imagePath"),
                        resultSet.getBoolean("validated")
                );

                // Since nomtilisateur is a string, create a basic User object
                if (resultSet.getString("nomtilisateur") != null) {
                    User user = new User();
                    user.setNom(resultSet.getString("nomtilisateur"));
                    event.setUser(user);
                }

                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching events: " + e.getMessage());
            e.printStackTrace();
        }
        return events;
    }

    // Update an event
    public void updateEvenement(Evenement event) throws SQLException {
        String query = "UPDATE evenement SET nom=?, type=?, nombreInvite=?, dateDebut=?, dateFin=?, description=?, lieuEvenement=?, budgetPrevu=?, activities=?, imagePath=?, validated=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, event.getNom());
            statement.setString(2, event.getType());
            statement.setInt(3, event.getNombreInvite());
            statement.setTimestamp(4, new Timestamp(event.getDateDebut().getTime()));
            statement.setTimestamp(5, new Timestamp(event.getDateFin().getTime()));
            statement.setString(6, event.getDescription());
            statement.setString(7, event.getLieuEvenement());
            statement.setDouble(8, event.getBudgetPrevu());
            statement.setString(9, event.getActivities());
            statement.setString(10, event.getImagePath());
            statement.setBoolean(11, event.isValidated()); // Use the actual validated state
            statement.setInt(12, event.getId());
            statement.executeUpdate();
        }
    }

    // Retrieve an event by ID
    public Evenement getEvenementById(int id) {
        String query = "SELECT * FROM evenement WHERE id=?";
        Evenement event = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    event = new Evenement(
                            resultSet.getInt("id"),
                            resultSet.getString("nom"),
                            resultSet.getString("type"),
                            resultSet.getInt("nombreInvite"),
                            resultSet.getTimestamp("dateDebut"),
                            resultSet.getTimestamp("dateFin"),
                            resultSet.getString("description"),
                            resultSet.getString("lieuEvenement"),
                            resultSet.getDouble("budgetPrevu"),
                            resultSet.getString("activities"),
                            resultSet.getString("imagePath"),
                            resultSet.getBoolean("validated")
                    );

                    if (resultSet.getString("nomtilisateur") != null) {
                        User user = new User();
                        user.setNom(resultSet.getString("nomtilisateur"));
                        event.setUser(user);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching event by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return event;
    }

    // Delete an event
    public void deleteEvenement(int id) throws SQLException {
        String query = "DELETE FROM evenement WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }

    // Retrieve unvalidated events
    public List<Evenement> getUnvalidatedEvenements() {
        List<Evenement> events = new ArrayList<>();
        String query = "SELECT * FROM evenement WHERE validated = FALSE";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Evenement event = new Evenement(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("type"),
                        resultSet.getInt("nombreInvite"),
                        resultSet.getTimestamp("dateDebut"),
                        resultSet.getTimestamp("dateFin"),
                        resultSet.getString("description"),
                        resultSet.getString("lieuEvenement"),
                        resultSet.getDouble("budgetPrevu"),
                        resultSet.getString("activities"),
                        resultSet.getString("imagePath"),
                        resultSet.getBoolean("validated")
                );
                if (resultSet.getString("nomtilisateur") != null) {
                    User user = new User();
                    user.setNom(resultSet.getString("nomtilisateur"));
                    event.setUser(user);
                }
                events.add(event);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching unvalidated events: " + e.getMessage());
            e.printStackTrace();
        }
        return events;
    }

    // Update event validation status
    public void updateEventValidation(int eventId, boolean isValidated) throws SQLException {
        String query = "UPDATE evenement SET validated=? WHERE id=?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBoolean(1, isValidated);
            statement.setInt(2, eventId);
            statement.executeUpdate();
        }
    }

    // Search events (simplified version)
    public List<Evenement> searchEvents(String searchTerm, String searchType) {
        List<Evenement> events = new ArrayList<>();
        String query = "SELECT * FROM evenement WHERE " +
                switch (searchType.toLowerCase()) {
                    case "nom" -> "nom LIKE ?";
                    case "type" -> "type LIKE ?";
                    case "lieu" -> "lieuEvenement LIKE ?";
                    case "date" -> "(DATE_FORMAT(dateDebut, '%d/%m/%Y') LIKE ? OR DATE_FORMAT(dateFin, '%d/%m/%Y') LIKE ?)";
                    default -> "nom LIKE ? OR type LIKE ? OR description LIKE ? OR lieuEvenement LIKE ?";
                };

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            String searchPattern = "%" + searchTerm + "%";
            switch (searchType.toLowerCase()) {
                case "nom":
                case "type":
                case "lieu":
                    statement.setString(1, searchPattern);
                    break;
                case "date":
                    statement.setString(1, searchPattern);
                    statement.setString(2, searchPattern);
                    break;
                default:
                    statement.setString(1, searchPattern);
                    statement.setString(2, searchPattern);
                    statement.setString(3, searchPattern);
                    statement.setString(4, searchPattern);
                    break;
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Evenement event = new Evenement(
                            resultSet.getInt("id"),
                            resultSet.getString("nom"),
                            resultSet.getString("type"),
                            resultSet.getInt("nombreInvite"),
                            resultSet.getTimestamp("dateDebut"),
                            resultSet.getTimestamp("dateFin"),
                            resultSet.getString("description"),
                            resultSet.getString("lieuEvenement"),
                            resultSet.getDouble("budgetPrevu"),
                            resultSet.getString("activities"),
                            resultSet.getString("imagePath"),
                            resultSet.getBoolean("validated")
                    );
                    if (resultSet.getString("nomtilisateur") != null) {
                        User user = new User();
                        user.setNom(resultSet.getString("nomtilisateur"));
                        event.setUser(user);
                    }
                    events.add(event);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error searching events: " + e.getMessage());
            e.printStackTrace();
        }
        return events;
    }
}
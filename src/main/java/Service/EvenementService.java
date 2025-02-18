package Service;

import Entities.Evenement;
import dbConnection.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EvenementService {
    private Connection connection;

    public EvenementService() {
        connection = DataSource.getInstance().getCnx();
    }

    // Add an event
    public void addEvenement(Evenement event) throws SQLException {
        String query = "INSERT INTO evenement (nom, type, nombreInvite, dateDebut, dateFin, description, lieuEvenement, budgetPrevu, activities, imagePath,validated) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, event.getNom());
        statement.setString(2, event.getType());
        statement.setInt(3, event.getNombreInvite());
        statement.setDate(4, new Date(event.getDateDebut().getTime()));
        statement.setDate(5, new Date(event.getDateFin().getTime()));
        statement.setString(6, event.getDescription());
        statement.setString(7, event.getLieuEvenement());
        statement.setDouble(8, event.getBudgetPrevu());
        statement.setString(9, event.getActivities());
        statement.setString(10, event.getImagePath());
        statement.setBoolean(11,false);
        statement.executeUpdate();
    }

    // Retrieve all events
    public List<Evenement> getAllEvenements() {
        List<Evenement> events = new ArrayList<>();
        String query = "SELECT * FROM evenement";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                Evenement event = new Evenement(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("type"),
                        resultSet.getInt("nombreInvite"),
                        resultSet.getDate("dateDebut"),
                        resultSet.getDate("dateFin"),
                        resultSet.getString("description"),
                        resultSet.getString("lieuEvenement"),
                        resultSet.getDouble("budgetPrevu"),
                        resultSet.getString("activities"),
                        resultSet.getString("imagePath"),
                        resultSet.getBoolean("validated")
                );
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
        String query = "UPDATE evenement SET nom=?, type=?, nombreInvite=?, dateDebut=?, dateFin=?, description=?, lieuEvenement=?, budgetPrevu=?, activities=?, imagePath=? ,validated=FALSE WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, event.getNom());
        statement.setString(2, event.getType());
        statement.setInt(3, event.getNombreInvite());
        statement.setDate(4, new Date(event.getDateDebut().getTime()));
        statement.setDate(5, new Date(event.getDateFin().getTime()));
        statement.setString(6, event.getDescription());
        statement.setString(7, event.getLieuEvenement());
        statement.setDouble(8, event.getBudgetPrevu());
        statement.setString(9, event.getActivities());
        statement.setString(10, event.getImagePath());
        statement.setInt(11, event.getId());
        statement.executeUpdate();
    }

    // Retrieve an event by ID
    public Evenement getEvenementById(int id) {
        String query = "SELECT * FROM evenement WHERE id=?";
        Evenement event = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                event = new Evenement(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("type"),
                        resultSet.getInt("nombreInvite"),
                        resultSet.getDate("dateDebut"),
                        resultSet.getDate("dateFin"),
                        resultSet.getString("description"),
                        resultSet.getString("lieuEvenement"),
                        resultSet.getDouble("budgetPrevu"),
                        resultSet.getString("activities"),
                        resultSet.getString("imagePath"),
                        resultSet.getBoolean("validated")
                );
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
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        statement.executeUpdate();
    }

    // Retrieve events where validated is false
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
                        resultSet.getDate("dateDebut"),
                        resultSet.getDate("dateFin"),
                        resultSet.getString("description"),
                        resultSet.getString("lieuEvenement"),
                        resultSet.getDouble("budgetPrevu"),
                        resultSet.getString("activities"),
                        resultSet.getString("imagePath"),
                        resultSet.getBoolean("validated")
                );
                System.out.println  (events);
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
        } catch (SQLException e) {
            System.err.println("Error updating event validation: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}

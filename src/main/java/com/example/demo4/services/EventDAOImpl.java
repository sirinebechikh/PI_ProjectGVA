package org.example.dao;

import org.example.models.Evenement;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class EventDAOImpl implements IEventDAO {

    private final Connection connection; // Assume DBconnection.getConnection() provides the connection

    public EventDAOImpl(Connection connection) {
        this.connection = connection;
    }


    @Override
    public Optional<Evenement> findEventById(int id) {
        String query = "SELECT * FROM evenement WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Evenement event = new Evenement(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("type"),
                        resultSet.getInt("nombreInvite"),
                        new Date(resultSet.getTimestamp("dateDebut").getTime()), // Convert Timestamp to Date
                        new Date(resultSet.getTimestamp("dateFin").getTime()), // Convert Timestamp to Date
                        resultSet.getString("description"),
                        resultSet.getString("lieuEvenement"),
                        resultSet.getDouble("budgetPrevu"),
                        resultSet.getString("activities"),
                        resultSet.getString("imagePath"),
                        resultSet.getBoolean("validated"), // Corrected column name
                        resultSet.getString("nomtilisateur") // Corrected column name
                );
                return Optional.of(event);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Add more detailed logging if necessary
        }
        return Optional.empty();
    }
    public Optional<Evenement> findEventByDestination(String destination) {
        String query = "SELECT * FROM evenement WHERE lieuEvenement = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, destination);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Evenement event = new Evenement(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("type"),
                        resultSet.getInt("nombreInvite"),
                        new Date(resultSet.getTimestamp("dateDebut").getTime()), // Convert Timestamp to Date
                        new Date(resultSet.getTimestamp("dateFin").getTime()), // Convert Timestamp to Date
                        resultSet.getString("description"),
                        resultSet.getString("lieuEvenement"),
                        resultSet.getDouble("budgetPrevu"),
                        resultSet.getString("activities"),
                        resultSet.getString("imagePath"),
                        resultSet.getBoolean("validated"), // Corrected column name
                        resultSet.getString("nomtilisateur")// Add nomutlisateur
                );
                return Optional.of(event);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public List<Evenement> getAllEvents() throws SQLException {
        List<Evenement> events = new ArrayList<>();
        String query = "SELECT * FROM evenement WHERE validated = 1"; // Filter by validated = 1
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Evenement event = new Evenement(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("type"),
                        resultSet.getInt("nombreInvite"),
                        new Date(resultSet.getTimestamp("dateDebut").getTime()), // Convert Timestamp to Date
                        new Date(resultSet.getTimestamp("dateFin").getTime()), // Convert Timestamp to Date
                        resultSet.getString("description"),
                        resultSet.getString("lieuEvenement"),
                        resultSet.getDouble("budgetPrevu"),
                        resultSet.getString("activities"),
                        resultSet.getString("imagePath"),
                        resultSet.getBoolean("validated"), // Corrected column name
                        resultSet.getString("nomtilisateur") // Add nomutlisateur
                );
                events.add(event);
            }
        }
        return events;
    }

}
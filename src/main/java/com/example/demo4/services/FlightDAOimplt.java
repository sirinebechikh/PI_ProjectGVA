package org.example.dao;

import org.example.models.Flight;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FlightDAOimplt implements FlightDAO {

    @Override
    public List<Flight> findAll() {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights";
        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Flight flight = new Flight(
                        rs.getInt("flight_id"),
                        rs.getString("destination"),
                        rs.getString("airline"),
                        rs.getTime("departure_time"),
                        rs.getTime("back_time"),
                        rs.getString("aeroport"),
                        rs.getDouble("price"),
                        rs.getString("type")
                );
                flights.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    @Override
    public Flight findById(int id) {
        String sql = "SELECT * FROM flights WHERE flight_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Flight(
                        rs.getInt("flight_id"),
                        rs.getString("destination"),
                        rs.getString("airline"),
                        rs.getTime("departure_time"),
                        rs.getTime("back_time"),
                        rs.getString("aeroport"),
                        rs.getDouble("price"),
                        rs.getString("type")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Flight> findByDestination(String destination) {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights WHERE destination = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, destination);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Flight flight = new Flight(
                        rs.getInt("flight_id"),
                        rs.getString("destination"),
                        rs.getString("airline"),
                        rs.getTime("departure_time"),
                        rs.getTime("back_time"),
                        rs.getString("aeroport"),
                        rs.getDouble("price"),
                        rs.getString("type")
                );
                flights.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }


    public List<Flight> findByDestinationAndType(String destination, String type) {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT * FROM flights WHERE destination = ? AND type = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, destination);
            stmt.setString(2, type);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Flight flight = new Flight(
                        rs.getInt("flight_id"),
                        rs.getString("destination"),
                        rs.getString("airline"),
                        rs.getTime("departure_time"),
                        rs.getTime("back_time"),
                        rs.getString("aeroport"),
                        rs.getDouble("price"),
                        rs.getString("type")
                );
                flights.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    @Override
    public List<Flight> findByDestinationAndDates(String destination, Timestamp departureDate, Timestamp returnDate) {
        List<Flight> flights = new ArrayList<>();
        // Calculate departure_time as dateDebut + 1 day
        Calendar cal = Calendar.getInstance();
        cal.setTime(departureDate);
        cal.add(Calendar.DATE, 1); // Add 1 day
        Timestamp adjustedDepartureDate = new Timestamp(cal.getTimeInMillis());

        String sql = "SELECT * FROM flights WHERE destination = ? AND departure_time = ? AND back_time = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, destination);
            stmt.setTimestamp(2, adjustedDepartureDate);
            stmt.setTimestamp(3, returnDate);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Flight flight = new Flight(
                        rs.getInt("flight_id"),
                        rs.getString("destination"),
                        rs.getString("airline"),
                        rs.getTime("departure_time"),
                        rs.getTime("back_time"),
                        rs.getString("aeroport"),
                        rs.getDouble("price"),
                        rs.getString("type")
                );
                flights.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }

    private Flight fetchFlightFromRecommendation(String recommendations) {
        // Example: Extract flight details from recommendations
        String destination = extractFlightDestination(recommendations); // Implement this method
        String airline = extractAirline(recommendations); // Implement this method

        // Fetch flights by destination and airline
        List<Flight> flights = new FlightDAOimplt().findByDestinationAndType(destination, airline);

        if (!flights.isEmpty()) {
            return flights.get(0); // Return the first matching flight
        }
        return null; // No matching flight found
    }

    // Helper method to extract flight destination from recommendations
    private String extractFlightDestination(String recommendations) {
        // Example: Parse recommendations to extract destination
        // This is a placeholder implementation. Replace with actual parsing logic.
        if (recommendations.contains("flight to")) {
            return recommendations.split("flight to")[1].split(" ")[1];
        }
        return "Unknown Destination";
    }

    // Helper method to extract airline from recommendations
    private String extractAirline(String recommendations) {
        // Example: Parse recommendations to extract airline
        // This is a placeholder implementation. Replace with actual parsing logic.
        if (recommendations.contains("airline")) {
            return recommendations.split("airline")[1].split(" ")[1];
        }
        return "Unknown Airline";
    }
}
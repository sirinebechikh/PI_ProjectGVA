
/*package org.example.dao;

import org.example.models.Flight;

import java.sql.*;
        import java.util.ArrayList;
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
                        rs.getTimestamp("departure_time"),
                        rs.getDouble("price")
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
                        rs.getTimestamp("departure_time"),
                        rs.getDouble("price")
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
                        rs.getTimestamp("departure_time"),
                        rs.getDouble("price")
                );
                flights.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }
}

 */
package org.example.dao;

import org.example.models.Flight;

import java.sql.*;
import java.util.ArrayList;
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
                        rs.getTimestamp("departure_time"),
                        rs.getDouble("price")
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
                        rs.getTimestamp("departure_time"),
                        rs.getDouble("price")
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
                        rs.getTimestamp("departure_time"),
                        rs.getDouble("price")
                );
                flights.add(flight);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flights;
    }
}
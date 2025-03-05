/*package org.example.dao;

import org.example.models.Transport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransportDAO implements ITransportDAO {

    @Override
    public List<Transport> findAll() {
        List<Transport> transports = new ArrayList<>();
        String sql = "SELECT * FROM transport";

        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Transport transport = new Transport(
                        rs.getInt("transport_id"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        rs.getString("description")
                );
                transports.add(transport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transports;
    }

    @Override
    public Transport findById(int id) {
        String sql = "SELECT * FROM transport WHERE transport_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Transport(
                        rs.getInt("transport_id"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        rs.getString("description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}/*

 */
package org.example.dao;

import org.example.models.Transport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransportDAO implements ITransportDAO {

    @Override
    public List<Transport> findAll() {
        List<Transport> transports = new ArrayList<>();
        String sql = "SELECT * FROM transport";

        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Transport transport = new Transport(
                        rs.getInt("transport_id"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        rs.getString("description")
                );
                transports.add(transport);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transports;
    }

    @Override
    public Transport findById(int id) {
        String sql = "SELECT * FROM transport WHERE transport_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Transport(
                        rs.getInt("transport_id"),
                        rs.getString("type"),
                        rs.getDouble("price"),
                        rs.getString("description")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    private Transport fetchTransportFromRecommendation(String recommendations) {
        // Example: Extract transport details from recommendations
        String type = extractTransportType(recommendations); // Implement this method

        // Fetch transports by type
        List<Transport> transports = new TransportDAO().findAll();
        if (!transports.isEmpty()) {
            transports = transports.stream()
                    .filter(transport -> transport.getType().equalsIgnoreCase(type))
                    .collect(Collectors.toList());
            if (!transports.isEmpty()) {
                return transports.get(0); // Return the first matching transport
            }
        }
        return null; // No matching transport found
    }

    // Helper method to extract transport type from recommendations
    private String extractTransportType(String recommendations) {
        // Example: Parse recommendations to extract transport type
        // This is a placeholder implementation. Replace with actual parsing logic.
        if (recommendations.contains("transport by")) {
            return recommendations.split("transport by")[1].split(" ")[1];
        }
        return "Unknown Transport";
    }
}
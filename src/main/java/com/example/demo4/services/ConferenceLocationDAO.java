package org.example.dao;

import org.example.models.ConferenceLocation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConferenceLocationDAO implements IConferenceLocationDAO {

    @Override
    public List<ConferenceLocation> findAll() {
        List<ConferenceLocation> locations = new ArrayList<>();
        String sql = "SELECT * FROM conference_location";

        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ConferenceLocation location = new ConferenceLocation(
                        rs.getInt("location_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getInt("capacity"),
                        rs.getDouble("price_per_day")
                );
                locations.add(location);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locations;
    }

    @Override
    public ConferenceLocation findById(int id) {
        String sql = "SELECT * FROM conference_location WHERE location_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new ConferenceLocation(
                        rs.getInt("location_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getInt("capacity"),
                        rs.getDouble("price_per_day")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<ConferenceLocation> findByLocation(String location) {
        List<ConferenceLocation> locations = new ArrayList<>();
        String sql = "SELECT * FROM conference_location WHERE address = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, location);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ConferenceLocation loc = new ConferenceLocation(
                        rs.getInt("location_id"),
                        rs.getString("name"),
                        rs.getString("address"),
                        rs.getInt("capacity"),
                        rs.getDouble("price_per_day")
                );
                locations.add(loc);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locations;
    }
}
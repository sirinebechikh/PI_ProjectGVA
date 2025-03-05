package org.example.dao;

import org.example.models.Hotel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HotelDAOimplt implements HotelDAO {

    @Override
    public List<Hotel> findAll() {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM hotels";

        try (Connection conn = DBconnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Hotel hotel = new Hotel(
                        rs.getInt("hotel_id"),
                        rs.getString("location"),
                        rs.getString("name"),
                        rs.getDouble("price_per_night"),
                        rs.getInt("rating")
                );
                hotels.add(hotel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotels;
    }

    @Override
    public Hotel findById(int id) {
        String sql = "SELECT * FROM hotels WHERE hotel_id = ?";
        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Hotel(
                        rs.getInt("hotel_id"),
                        rs.getString("location"),
                        rs.getString("name"),
                        rs.getDouble("price_per_night"),
                        rs.getInt("rating")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Hotel> findByLocation(String location) {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM hotels WHERE location = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, location);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Hotel hotel = new Hotel(
                        rs.getInt("hotel_id"),
                        rs.getString("location"),
                        rs.getString("name"),
                        rs.getDouble("price_per_night"),
                        rs.getInt("rating")
                );
                hotels.add(hotel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotels;
    }

    @Override
    public List<Hotel> findByRating(int rating) {
        List<Hotel> hotels = new ArrayList<>();
        String sql = "SELECT * FROM hotels WHERE rating = ?";

        try (Connection conn = DBconnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, rating);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Hotel hotel = new Hotel(
                        rs.getInt("hotel_id"),
                        rs.getString("location"),
                        rs.getString("name"),
                        rs.getDouble("price_per_night"),
                        rs.getInt("rating")
                );
                hotels.add(hotel);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return hotels;
    }
    private Hotel fetchHotelFromRecommendation(String recommendations) {
        // Example: Extract hotel details from recommendations
        String location = extractHotelLocation(recommendations); // Implement this method
        int rating = extractHotelRating(recommendations); // Implement this method

        // Fetch hotels by location and rating
        List<Hotel> hotels = new HotelDAOimplt().findByLocation(location);
        if (!hotels.isEmpty()) {
            // Filter hotels by rating
            hotels = hotels.stream()
                    .filter(hotel -> hotel.getRating() >= rating)
                    .collect(Collectors.toList());
            if (!hotels.isEmpty()) {
                return hotels.get(0); // Return the first matching hotel
            }
        }
        return null; // No matching hotel found
    }

    // Helper method to extract hotel location from recommendations
    private String extractHotelLocation(String recommendations) {
        // Example: Parse recommendations to extract location
        // This is a placeholder implementation. Replace with actual parsing logic.
        if (recommendations.contains("hotel in")) {
            return recommendations.split("hotel in")[1].split(" ")[1];
        }
        return "Unknown Location";
    }

    // Helper method to extract hotel rating from recommendations
    private int extractHotelRating(String recommendations) {
        // Example: Parse recommendations to extract rating
        // This is a placeholder implementation. Replace with actual parsing logic.
        if (recommendations.contains("rating")) {
            return Integer.parseInt(recommendations.split("rating")[1].split(" ")[1]);
        }
        return 3; // Default rating
    }
}
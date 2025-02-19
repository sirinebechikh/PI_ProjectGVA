/*package org.example.dao;

import org.example.models.Hotel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
}

 */
package org.example.dao;

import org.example.models.Hotel;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
}
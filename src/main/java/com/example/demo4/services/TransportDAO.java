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
}
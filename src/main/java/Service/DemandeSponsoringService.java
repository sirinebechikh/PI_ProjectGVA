package Service;

import Entities.DemandeSponsoring;
import dbConnection.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DemandeSponsoringService {
    private Connection connection;

    public DemandeSponsoringService() {
        connection = DataSource.getInstance().getCnx();
    }

    // 1️⃣ Add a Sponsorship Request
    public void addDemandeSponsoring(DemandeSponsoring demande) throws SQLException {
        String query = "INSERT INTO demande_sponsoring (sponsor, event_id, statut, justification) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, demande.getSponsor());
        statement.setInt(2, demande.getEventId());
        statement.setString(3, demande.getStatut());
        statement.setString(4, demande.getJustification());
        statement.executeUpdate();
    }

    // 2️⃣ Retrieve All Sponsorship Requests
    public List<DemandeSponsoring> getAllDemandes() {
        List<DemandeSponsoring> demandes = new ArrayList<>();
        String query = "SELECT * FROM demande_sponsoring";

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                DemandeSponsoring demande = new DemandeSponsoring(
                        resultSet.getInt("id"),
                        resultSet.getString("sponsor"),
                        resultSet.getInt("event_id"),
                        resultSet.getString("statut"),
                        resultSet.getString("justification")
                );
                demandes.add(demande);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return demandes;
    }

    // 3️⃣ Update Sponsorship Request Status
    public void updateDemandeStatus(int id, String newStatus) throws SQLException {
        String query = "UPDATE demande_sponsoring SET statut=? WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, newStatus);
        statement.setInt(2, id);
        statement.executeUpdate();
    }

    // 4️⃣ Delete a Sponsorship Request
    public void deleteDemandeSponsoring(int id) throws SQLException {
        String query = "DELETE FROM demande_sponsoring WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        statement.executeUpdate();
    }
}

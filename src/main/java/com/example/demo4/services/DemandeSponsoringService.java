package org.example.dao;



import org.example.models.DemandeSponsoring;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DemandeSponsoringService {
    private Connection connection;

    public DemandeSponsoringService() {
        connection = DataSource.getInstance().getCnx();
    }

    //  Add a Sponsorship Request
    public void addDemandeSponsoring(DemandeSponsoring demande) throws SQLException {
        String query = "INSERT INTO demande_sponsoring (sponsor, event_id, statut, justification) VALUES (?, ?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, demande.getSponsor());
        statement.setInt(2, demande.getEventId());
        statement.setString(3, demande.getStatut());
        statement.setString(4, demande.getJustification());
        statement.executeUpdate();
    }

    //  Retrieve All Sponsorship Requests
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
    // Retrieve Sponsorship Requests for a specific Event
    public List<DemandeSponsoring> getDemandesByEventId(int eventId) throws SQLException {
        String query = "SELECT * FROM demande_sponsoring WHERE id_evenement = ?";
        List<DemandeSponsoring> demandes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, eventId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    DemandeSponsoring demande = new DemandeSponsoring(
                            resultSet.getInt("id"),
                            resultSet.getString("nom_sponsor"),
                            resultSet.getInt("id_evenement"),
                            resultSet.getString("statut"),
                            resultSet.getString("response")
                    );



                    demandes.add(demande);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving sponsorship requests for event: " + e.getMessage());
            throw e;
        }

        return demandes;
    }
    public boolean hasPendingRequests(int eventId) throws SQLException {
        String query = "SELECT COUNT(*) FROM demande_sponsoring WHERE id_evenement = ? AND statut = 'Pending'";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, eventId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking pending sponsorship requests: " + e.getMessage());
            throw e;
        }

        return false;
    }




    // Update Sponsorship Request Status
    public void updateDemandeStatus(int id, String newStatus) throws SQLException {
        String query = "UPDATE demande_sponsoring SET statut=? WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, newStatus);
        statement.setInt(2, id);
        statement.executeUpdate();
    }
    public List<DemandeSponsoring> getDemandesBySponsor(String sponsorName) throws SQLException {
        String query = "SELECT * FROM demande_sponsoring WHERE nom_sponsor = ?";
        List<DemandeSponsoring> demandes = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, sponsorName);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    DemandeSponsoring demande = new DemandeSponsoring(
                            resultSet.getInt("id"),
                            resultSet.getString("nom_sponsor"),
                            resultSet.getInt("id_evenement"),
                            resultSet.getString("statut"),
                            resultSet.getString("response")
                    );

                    // Set additional fields if they exist

                    demandes.add(demande);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving sponsorship requests for sponsor: " + e.getMessage());
            throw e;
        }

        return demandes;
    }

    //  Delete a Sponsorship Request
    public void deleteDemandeSponsoring(int id) throws SQLException {
        String query = "DELETE FROM demande_sponsoring WHERE id=?";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, id);
        statement.executeUpdate();
    }
    public DemandeSponsoring getDemandeById(int id) throws SQLException {
        String query = "SELECT * FROM demande_sponsoring WHERE id = ?";
        DemandeSponsoring demande = null;

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    demande = new DemandeSponsoring(
                            resultSet.getInt("id"),
                            resultSet.getString("nom_sponsor"),
                            resultSet.getInt("id_evenement"),
                            resultSet.getString("statut"),
                            resultSet.getString("response")
                    );


                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving sponsorship request: " + e.getMessage());
            throw e;
        }

        return demande;
    }
    public void updateStatus(int id, String newStatus) throws SQLException {
        String query = "UPDATE demande_sponsoring SET statut = ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, newStatus);
            statement.setInt(2, id);

            int rowsUpdated = statement.executeUpdate();
            System.out.println("Updated sponsorship request status: " + rowsUpdated + " rows affected.");
        } catch (SQLException e) {
            System.err.println("Error updating sponsorship request status: " + e.getMessage());
            throw e;
        }
    }
}

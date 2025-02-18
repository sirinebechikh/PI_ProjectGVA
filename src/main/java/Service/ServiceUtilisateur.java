package Service;

import Entities.Utilisateur;
import dbConnection.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceUtilisateur {
    private final Connection connection;

    // Initialize connection using DataSource singleton
    public ServiceUtilisateur() {
        this.connection = DataSource.getInstance().getConnection();
    }

    // CREATE: Insert a new user
    public void create(Utilisateur u) {
        String query = "INSERT INTO utilisateur (nom, email, motDePasse, telephone) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, u.getNom());
            statement.setString(2, u.getEmail());
            statement.setString(3, u.getMotDePasse());
            statement.setString(4, u.getTelephone());
            statement.executeUpdate();
            System.out.println("✅ Utilisateur créé avec succès !");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("❌ Erreur lors de la création de l'utilisateur.");
        }
    }

    // READ: Retrieve all users
    public List<Utilisateur> read() {
        List<Utilisateur> utilisateurs = new ArrayList<>();
        String query = "SELECT * FROM utilisateur";
        try (PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Utilisateur u = new Utilisateur(
                        resultSet.getInt("id"),
                        resultSet.getString("nom"),
                        resultSet.getString("email"),
                        resultSet.getString("motDePasse"),
                        resultSet.getString("telephone")
                );
                utilisateurs.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("❌ Erreur lors de la lecture des utilisateurs.");
        }
        return utilisateurs;
    }

    // UPDATE: Modify an existing user
    public void update(Utilisateur u) {
        String query = "UPDATE utilisateur SET nom = ?, email = ?, motDePasse = ?, telephone = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, u.getNom());
            statement.setString(2, u.getEmail());
            statement.setString(3, u.getMotDePasse());
            statement.setString(4, u.getTelephone());
            statement.setInt(5, u.getId());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("✅ Utilisateur mis à jour avec succès !");
            } else {
                System.out.println("⚠️ Aucun utilisateur mis à jour (ID non trouvé).");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("❌ Erreur lors de la mise à jour de l'utilisateur.");
        }
    }

    // DELETE: Remove a user by ID
    public void delete(int id) {
        String query = "DELETE FROM utilisateur WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, id);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("✅ Utilisateur supprimé avec succès !");
            } else {
                System.out.println("⚠️ Aucun utilisateur supprimé (ID non trouvé).");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("❌ Erreur lors de la suppression de l'utilisateur.");
        }
    }
}

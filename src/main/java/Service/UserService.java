package Service;

import Entities.RoleType;
import Entities.User;
import dbConnection.DataSource;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserService {
    private Connection connection;

    public UserService() {
        connection = DataSource.getInstance().getCnx();
    }

    // Add a new user
    public void addUser(User user) throws SQLException {
        String query = "INSERT INTO users (nom, email, motDePasse, telephone, role, compteValide) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, user.getNom());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getMotDePasse());
        preparedStatement.setString(4, user.getTelephone());
        preparedStatement.setString(5, user.getRole().toString());
        preparedStatement.setBoolean(6, user.isCompteValide());
        preparedStatement.executeUpdate();
    }

    // Retrieve all users
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM user";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            User user = new User(
                    resultSet.getInt("id"),
                    resultSet.getString("nom"),
                    resultSet.getString("email"),
                    resultSet.getString("motDePasse"),
                    resultSet.getString("telephone"),
                    RoleType.valueOf(resultSet.getString("role")),
                    resultSet.getBoolean("compteValide")
            );
            users.add(user);
        }
        return users;
    }

    // Update user details
    public void updateUser(User user) throws SQLException {
        String query = "UPDATE user SET nom=?, email=?, motDePasse=?, telephone=?, role=?, compteValide=? WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, user.getNom());
        preparedStatement.setString(2, user.getEmail());
        preparedStatement.setString(3, user.getMotDePasse());
        preparedStatement.setString(4, user.getTelephone());
        preparedStatement.setString(5, user.getRole().toString());
        preparedStatement.setBoolean(6, user.isCompteValide());
        preparedStatement.setInt(7, user.getId());
        preparedStatement.executeUpdate();
    }

    // Delete a user by ID
    public void deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM user WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        preparedStatement.executeUpdate();
    }
}

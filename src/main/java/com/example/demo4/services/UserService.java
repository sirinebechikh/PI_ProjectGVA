package org.example.dao;

import org.example.models.RoleType;
import org.example.models.User;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserService {
    private Connection connection;

    public UserService() {
        connection = DBconnection.getConnection(); // Assuming DBconnection is correctly implemented
    }

    // Add a new user and send confirmation email
    public int addUser(User user) throws SQLException {
        String query = "INSERT INTO User (nom, email, motDePasse, telephone, role, compteValide, image, montant, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getNom());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getMotDePasse());
            preparedStatement.setString(4, user.getTelephone());
            preparedStatement.setString(5, user.getRole().toString());
            preparedStatement.setBoolean(6, user.isCompteValide());
            preparedStatement.setString(7, user.getImage());
            if (user.getMontant() != null) {
                preparedStatement.setInt(8, user.getMontant());
            } else {
                preparedStatement.setNull(8, Types.INTEGER); // Handle NULL for montant
            }
            preparedStatement.setString(9, user.getStatus()); // Set status
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }

            int generatedId;
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    generatedId = generatedKeys.getInt(1);
                    user.setId(generatedId); // Set the generated ID to the User object
                } else {
                    throw new SQLException("Creating user failed, no ID obtained.");
                }
            }

            // Send email after successful insertion
            String subject = "Nouveau compte créé";
            String body = "Bonjour " + user.getNom() + ",\n\n" +
                    "Un nouveau compte a été créé pour votre email : " + user.getEmail() + ".\n" +
                    "Rôle : " + user.getRole().toString() + "\n" +
                    "Statut : " + user.getStatus() + "\n\n" +
                    "Cordialement,\nL'équipe de support";
            sendEmail(user.getEmail(), subject, body);

            return generatedId;
        } catch (SQLException e) {
            System.err.println("Error adding user: " + e.getMessage());
            throw e;
        }
    }

    // Email sending method
    private void sendEmail(String to, String subject, String body) {
        final String username = "anaskhelifi94@gmail.com"; // Replace with your email
        final String password = "qvgi gphb niyo xlox";     // Replace with your app-specific password

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            Transport.send(message);
            System.out.println("Email envoyé avec succès à " + to);
        } catch (MessagingException e) {
            System.err.println("Erreur d'envoi d'email : " + e.getMessage());
            // Email failure doesn't rollback the transaction; consider logging instead
        }
    }

    // Retrieve all users
    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM User";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                users.add(createUserFromResultSet(resultSet));
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all users: " + e.getMessage());
            throw e;
        }
        return users;
    }

    // Update user details
    public void updateUser(User user) throws SQLException {
        String query = "UPDATE User SET nom=?, email=?, motDePasse=?, telephone=?, role=?, compteValide=?, image=?, montant=?, status=? WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, user.getNom());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getMotDePasse());
            preparedStatement.setString(4, user.getTelephone());
            preparedStatement.setString(5, user.getRole().toString());
            preparedStatement.setBoolean(6, user.isCompteValide());
            preparedStatement.setString(7, user.getImage());
            if (user.getMontant() != null) {
                preparedStatement.setInt(8, user.getMontant());
            } else {
                preparedStatement.setNull(8, Types.INTEGER);
            }
            preparedStatement.setString(9, user.getStatus());
            preparedStatement.setInt(10, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating user: " + e.getMessage());
            throw e;
        }
    }

    // Delete a user by ID
    public void deleteUser(int userId) throws SQLException {
        String query = "DELETE FROM User WHERE id=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error deleting user: " + e.getMessage());
            throw e;
        }
    }

    // Get all users with EMPLOY role
    public List<User> getAllEmployees() throws SQLException {
        List<User> employees = new ArrayList<>();
        String query = "SELECT * FROM User WHERE role = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, RoleType.EMPLOY.toString());
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    employees.add(createUserFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees: " + e.getMessage());
            throw e;
        }
        return employees;
    }

    // Get employees assigned to a specific event
    public List<User> getEmployeesAssignedToEvent(int eventId) throws SQLException {
        List<User> employees = new ArrayList<>();
        String query = "SELECT u.* FROM User u " +
                "JOIN employee_event_assignments a ON u.id = a.employee_id " +
                "WHERE a.event_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, eventId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    employees.add(createUserFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching employees for event " + eventId + ": " + e.getMessage());
            throw e;
        }
        return employees;
    }

    // Get user by name
    public User getUserByName(String name) throws SQLException {
        String query = "SELECT * FROM User WHERE nom = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return createUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by name " + name + ": " + e.getMessage());
            throw e;
        }
        return null;
    }

    // Get users by role
    public List<User> getUsersByRole(RoleType role) throws SQLException {
        List<User> users = new ArrayList<>();
        String query = "SELECT * FROM User WHERE role = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, role.name());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(createUserFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving users with role " + role + ": " + e.getMessage());
            throw e;
        }
        return users;
    }

    // Get employees not assigned to a specific event
    public List<User> getUnassignedEmployees(int eventId) throws SQLException {
        List<User> employees = new ArrayList<>();
        String query = "SELECT * FROM User u WHERE u.role = ? " +
                "AND u.id NOT IN (SELECT employee_id FROM employee_event_assignments WHERE event_id = ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, RoleType.EMPLOY.toString());
            preparedStatement.setInt(2, eventId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    employees.add(createUserFromResultSet(resultSet));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching unassigned employees for event " + eventId + ": " + e.getMessage());
            throw e;
        }
        return employees;
    }

    // Get a user by ID
    public User getUserById(int userId) throws SQLException {
        String query = "SELECT * FROM User WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, userId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return createUserFromResultSet(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by ID " + userId + ": " + e.getMessage());
            throw e;
        }
        return null;
    }

    // Helper method to create User object from ResultSet
    private User createUserFromResultSet(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt("id"));
        user.setNom(resultSet.getString("nom"));
        user.setEmail(resultSet.getString("email"));
        user.setMotDePasse(resultSet.getString("motDePasse"));
        user.setTelephone(resultSet.getString("telephone"));
        String roleStr = resultSet.getString("role");
        try {
            user.setRole(RoleType.valueOf(roleStr));
        } catch (IllegalArgumentException e) {
            user.setRole(RoleType.CLIENT); // Default role if invalid
            System.err.println("Invalid role value in database: " + roleStr);
        }
        user.setCompteValide(resultSet.getBoolean("compteValide"));
        user.setImage(resultSet.getString("image"));
        user.setMontant(resultSet.getObject("montant") != null ? resultSet.getInt("montant") : null);
        user.setStatus(resultSet.getString("status"));
        return user;
    }
}
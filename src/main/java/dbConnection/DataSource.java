package dbConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DataSource {
    private final String url = "jdbc:mysql://localhost:3306/esprit";
    private final String username = "root";
    private final String password = "";
    private Connection cnx;
    private static DataSource instance;

    // Private constructor for Singleton pattern
    private DataSource() {
        try {
            cnx = DriverManager.getConnection(url, username, password);
            System.out.println("✅ Connexion établie avec succès !");
        } catch (SQLException ex) {
            Logger.getLogger(DataSource.class.getName()).log(Level.SEVERE, "❌ Erreur de connexion à la base de données", ex);
            throw new RuntimeException("Database connection failed", ex);
        }
    }

    // Singleton instance getter
    public static synchronized DataSource getInstance() {
        if (instance == null) {
            instance = new DataSource();
        }
        return instance;
    }

    // Proper connection getter
    public Connection getConnection() {
        return cnx;
    }
}

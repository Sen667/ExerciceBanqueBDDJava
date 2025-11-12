package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe pour gérer la connexion à la base de données MySQL
 */
public class ConnexionBD {
    
    // Paramètres de connexion
    private static final String URL = "jdbc:mysql://localhost:3306/banque_db";
    private static final String USER = "root";
    private static final String PASSWORD = "";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    private static Connection connection = null;
    
    /**
     * Établit et retourne une connexion à la base de données
     * @return Connection - objet de connexion à la BD
     * @throws SQLException si la connexion échoue
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                // Chargement du driver MySQL
                Class.forName(DRIVER);
                
                // Établissement de la connexion
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Connexion à la base de données réussie !");
                
            } catch (ClassNotFoundException e) {
                System.err.println("Driver MySQL introuvable : " + e.getMessage());
                throw new SQLException("Erreur de chargement du driver MySQL", e);
            }
        }
        return connection;
    }
    
    /**
     * Ferme la connexion à la base de données
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                System.out.println("Connexion fermée.");
            } catch (SQLException e) {
                System.err.println("Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
    
    /**
     * Teste la connexion à la base de données
     */
    public static void testerConnexion() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("Test de connexion réussi !");
                System.out.println("Base de données : " + conn.getMetaData().getDatabaseProductName());
                System.out.println("Version : " + conn.getMetaData().getDatabaseProductVersion());
            }
        } catch (SQLException e) {
            System.err.println("Échec du test de connexion : " + e.getMessage());
        }
    }
}

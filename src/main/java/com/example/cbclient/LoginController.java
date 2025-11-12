package com.example.cbclient;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;

import model.ConnexionBD;

/**
 * Contrôleur pour l'interface de connexion
 */
public class LoginController {

    @FXML private TextField identifiantField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private Button btnConnect;
    @FXML private Button btnQuit;

    /**
     * Gérer la connexion
     */
    @FXML
    private void handleLogin() {
        String identifiant = identifiantField.getText().trim();
        String password = passwordField.getText();

        // Validation des champs
        if (identifiant.isEmpty() || password.isEmpty()) {
            showError("Veuillez remplir tous les champs");
            return;
        }

        // Vérifier les identifiants
        if (verifierIdentifiants(identifiant, password)) {
            // Connexion réussie
            hideError();
            ouvrirGestionComptes(identifiant);
        } else {
            showError("Identifiant ou mot de passe incorrect");
            passwordField.clear();
        }
    }

    /**
     * Vérifier les identifiants dans la base de données
     * Pour cet exemple, on va créer une table Utilisateur ou utiliser une vérification simple
     */
    private boolean verifierIdentifiants(String identifiant, String password) {
        // Pour le moment, utilisons des identifiants de test
        // Dans une vraie application, on vérifierait dans une table Utilisateur
        
        // Identifiants de test
        if ("admin".equals(identifiant) && "admin123".equals(password)) {
            return true;
        }
        
        // Vérifier si c'est un client dans la BD (format: prenom.nom / mot de passe simple)
        try {
            String sql = "SELECT * FROM Client WHERE LOWER(CONCAT(prenom, '.', nom)) = LOWER(?) LIMIT 1";
            try (Connection conn = ConnexionBD.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {
                
                pstmt.setString(1, identifiant);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        // Pour simplifier, on accepte si le mot de passe = "1234"
                        // Dans une vraie app, on comparerait avec un hash stocké en BD
                        return "1234".equals(password);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la vérification : " + e.getMessage());
        }
        
        return false;
    }

    /**
     * Ouvrir l'interface de gestion des comptes
     */
    private void ouvrirGestionComptes(String identifiant) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("banque-view.fxml"));
            Parent root = loader.load();
            
            // Passer l'identifiant au contrôleur de gestion
            BanqueGUIController controller = loader.getController();
            controller.setUtilisateurConnecte(identifiant);
            
            // Créer une nouvelle scène
            Scene scene = new Scene(root, 900, 650);
            
            // Obtenir la fenêtre actuelle et la remplacer
            Stage stage = (Stage) btnConnect.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Gestion de mes comptes - " + identifiant);
            stage.show();
            
        } catch (IOException e) {
            showError("Erreur lors du chargement de l'interface");
            e.printStackTrace();
        }
    }

    /**
     * Quitter l'application
     */
    @FXML
    private void handleQuit() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Quitter l'application");
        alert.setContentText("Êtes-vous sûr de vouloir quitter ?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Stage stage = (Stage) btnQuit.getScene().getWindow();
                stage.close();
                System.exit(0);
            }
        });
    }

    /**
     * Afficher un message d'erreur
     */
    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    /**
     * Masquer le message d'erreur
     */
    private void hideError() {
        errorLabel.setVisible(false);
    }

    /**
     * Initialisation
     */
    @FXML
    public void initialize() {
        // Focus sur le champ identifiant au démarrage
        identifiantField.requestFocus();
        
        // Permettre de se connecter avec la touche Entrée
        passwordField.setOnAction(e -> handleLogin());
        identifiantField.setOnAction(e -> passwordField.requestFocus());
    }
}

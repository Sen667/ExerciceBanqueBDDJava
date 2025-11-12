package com.example.cbclient;

import model.ConnexionBD;

/**
 * Programme de test pour vérifier la connexion à la base de données
 */
public class TestConnexionBD {
    
    public static void main(String[] args) {
        System.out.println("=== Test de Connexion à la Base de Données ===\n");
        
        // Test de la connexion
        ConnexionBD.testerConnexion();
        
        System.out.println("\nSi le test a réussi, votre base de données est correctement configurée !");
        System.out.println("\nPour lancer l'application graphique, exécutez BanqueGUI.java");
        
        // Fermer la connexion
        ConnexionBD.closeConnection();
    }
}

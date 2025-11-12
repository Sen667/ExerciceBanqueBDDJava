package service;

import model.Client;
import model.Compte;
import model.Operation;
import model.TypeOperation;
import model.ConnexionBD;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Service de gestion des comptes bancaires avec persistance en base de données
 */
public class GestionCompteService {
    private List<Client> clients;
    private List<Compte> tousLesComptes;

    public GestionCompteService() {
        this.clients = new ArrayList<>();
        this.tousLesComptes = new ArrayList<>();
        chargerDonnees();
        synchroniserAvecBD();
    }

    /**
     * Charger les données depuis la base de données
     */
    private void chargerDonnees() {
        try {
            chargerClients();
            chargerComptes();
        } catch (SQLException e) {
            System.err.println("Erreur lors du chargement des données : " + e.getMessage());
        }
    }

    /**
     * Synchroniser les comptes en mémoire avec la base de données
     * - Supprime les comptes qui n'existent pas en BD
     * - Met à jour les soldes depuis la BD
     */
    private void synchroniserAvecBD() {
        try {
            // Récupérer tous les numéros de comptes actifs depuis la BD
            List<String> numeroComptesBD = new ArrayList<>();
            String sql = "SELECT numero_compte, solde FROM Compte WHERE actif = TRUE";
            
            try (Connection conn = ConnexionBD.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {
                
                while (rs.next()) {
                    numeroComptesBD.add(rs.getString("numero_compte"));
                }
            }
            
            // Supprimer les comptes qui ne sont pas en BD
            List<Compte> comptesASupprimer = new ArrayList<>();
            for (Compte compte : tousLesComptes) {
                if (!numeroComptesBD.contains(compte.getNumeroCompte())) {
                    comptesASupprimer.add(compte);
                }
            }
            
            // Supprimer les comptes non présents en BD
            for (Compte compte : comptesASupprimer) {
                tousLesComptes.remove(compte);
                // Supprimer aussi des listes de comptes des clients
                for (Client client : clients) {
                    client.getComptes().remove(compte);
                }
                System.out.println("Compte supprimé (non présent en BD) : " + compte.getNumeroCompte());
            }
            
            // Synchroniser les soldes avec la BD
            for (Compte compte : tousLesComptes) {
                synchroniserSoldeCompte(compte.getNumeroCompte());
            }
            
            System.out.println("Synchronisation terminée : " + comptesASupprimer.size() + " compte(s) supprimé(s)");
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la synchronisation avec la BD : " + e.getMessage());
        }
    }

    /**
     * Synchroniser le solde d'un compte avec la base de données
     */
    private void synchroniserSoldeCompte(String numeroCompte) {
        String sql = "SELECT solde FROM Compte WHERE numero_compte = ? AND actif = TRUE";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroCompte);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    BigDecimal soldeBD = rs.getBigDecimal("solde");
                    Compte compte = rechercherCompte(numeroCompte);
                    if (compte != null && !compte.getSolde().equals(soldeBD)) {
                        // Mettre à jour le solde en mémoire avec celui de la BD
                        compte.setSolde(soldeBD);
                        System.out.println("Solde synchronisé pour le compte " + numeroCompte + " : " + soldeBD + "€");
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la synchronisation du solde : " + e.getMessage());
        }
    }

    /**
     * Charger uniquement les clients qui ont au moins un compte actif
     */
    private void chargerClients() throws SQLException {
        String sql = "SELECT DISTINCT cl.id_client, cl.nom, cl.prenom " +
                    "FROM Client cl " +
                    "INNER JOIN Compte c ON cl.id_client = c.id_client " +
                    "WHERE c.actif = TRUE " +
                    "ORDER BY cl.id_client";
        
        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                Client client = new Client(
                    rs.getString("nom"),
                    rs.getString("prenom")
                );
                clients.add(client);
            }
        }
    }

    /**
     * Charger tous les comptes depuis la BD et les associer aux clients
     */
    private void chargerComptes() throws SQLException {
        String sql = "SELECT c.*, cl.id_client, cl.nom, cl.prenom FROM Compte c " +
                    "INNER JOIN Client cl ON c.id_client = cl.id_client " +
                    "WHERE c.actif = TRUE " +
                    "ORDER BY cl.id_client, c.numero_compte";
        
        try (Connection conn = ConnexionBD.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String numeroCompte = rs.getString("numero_compte");
                String prenom = rs.getString("prenom");
                String nom = rs.getString("nom");
                String proprietaire = prenom + " " + nom;
                BigDecimal solde = rs.getBigDecimal("solde");
                
                // Créer le compte avec le numéro de compte de la BD
                Compte compte = new Compte(numeroCompte, proprietaire, solde);
                tousLesComptes.add(compte);
                
                // Trouver le client correspondant par nom et prénom
                Client clientCorrespondant = trouverClientParNomPrenom(prenom, nom);
                if (clientCorrespondant != null) {
                    clientCorrespondant.ajouterCompte(compte);
                } else {
                    System.err.println("⚠️ Client non trouvé pour le compte : " + compte.getNumeroCompte() + 
                                     " (" + proprietaire + ")");
                }
            }
        }
    }
    
    /**
     * Trouver un client par son nom et prénom
     */
    private Client trouverClientParNomPrenom(String prenom, String nom) {
        for (Client client : clients) {
            String[] parts = client.getNomComplet().split(" ", 2);
            if (parts.length >= 2 && parts[0].equals(prenom) && parts[1].equals(nom)) {
                return client;
            }
        }
        return null;
    }

    /**
     * Créer un nouveau client
     */
    public Client creerClient(String nom, String prenom) {
        return creerClient(nom, prenom, null, null, null);
    }

    /**
     * Créer un nouveau client avec informations complètes
     */
    public Client creerClient(String nom, String prenom, String telephone, String email, String adresse) {
        String sql = "INSERT INTO Client (nom, prenom, telephone, email, adresse) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, nom);
            pstmt.setString(2, prenom);
            pstmt.setString(3, telephone);
            pstmt.setString(4, email);
            pstmt.setString(5, adresse);
            
            pstmt.executeUpdate();
            
            Client client = new Client(nom, prenom);
            clients.add(client);
            
            System.out.println("Client créé avec succès : " + nom + " " + prenom);
            return client;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du client : " + e.getMessage());
            return null;
        }
    }

    /**
     * Créer un compte pour un client
     */
    public Compte creerCompte(Client client, BigDecimal soldeInitial) {
        String sql = "INSERT INTO Compte (numero_compte, id_client, solde, type_compte) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            Compte compte = new Compte(client.getNomComplet(), soldeInitial);
            String numeroCompte = compte.getNumeroCompte();
            
            // Trouver l'ID du client dans la BD (simplifié pour l'exemple)
            int idClient = obtenirIdClient(client.getNomComplet());
            
            pstmt.setString(1, numeroCompte);
            pstmt.setInt(2, idClient);
            pstmt.setBigDecimal(3, soldeInitial);
            pstmt.setString(4, "COURANT");
            
            pstmt.executeUpdate();
            
            // Si solde initial > 0, créer une opération de dépôt
            if (soldeInitial.compareTo(BigDecimal.ZERO) > 0) {
                enregistrerOperation(numeroCompte, TypeOperation.DEPOT, soldeInitial, "Dépôt initial");
            }
            
            client.ajouterCompte(compte);
            tousLesComptes.add(compte);
            
            System.out.println("Compte créé avec succès : " + numeroCompte);
            return compte;
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la création du compte : " + e.getMessage());
            return null;
        }
    }

    /**
     * Obtenir l'ID d'un client par son nom complet
     */
    private int obtenirIdClient(String nomComplet) throws SQLException {
        String[] parts = nomComplet.split(" ");
        String sql = "SELECT id_client FROM Client WHERE prenom = ? AND nom = ? LIMIT 1";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, parts[0]);
            pstmt.setString(2, parts.length > 1 ? parts[1] : "");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id_client");
                }
            }
        }
        return 1; // Par défaut
    }

    /**
     * Enregistrer une opération dans la BD
     */
    private void enregistrerOperation(String numeroCompte, TypeOperation type, BigDecimal montant, String description) {
        String sql = "INSERT INTO Operation (numero_compte, type_operation, montant, description) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroCompte);
            pstmt.setString(2, type.name());
            pstmt.setBigDecimal(3, montant);
            pstmt.setString(4, description);
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de l'enregistrement de l'opération : " + e.getMessage());
        }
    }

    /**
     * Mettre à jour le solde d'un compte dans la BD
     */
    private void mettreAJourSolde(String numeroCompte, BigDecimal nouveauSolde) {
        String sql = "UPDATE Compte SET solde = ? WHERE numero_compte = ?";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setBigDecimal(1, nouveauSolde);
            pstmt.setString(2, numeroCompte);
            
            pstmt.executeUpdate();
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la mise à jour du solde : " + e.getMessage());
        }
    }

    /**
     * Créer un compte avec solde à zéro
     */
    public Compte creerCompte(Client client) {
        return creerCompte(client, BigDecimal.ZERO);
    }

    /**
     * Effectuer un dépôt
     */
    public void effectuerDepot(Compte compte, BigDecimal montant) {
        compte.deposer(montant);
        mettreAJourSolde(compte.getNumeroCompte(), compte.getSolde());
        enregistrerOperation(compte.getNumeroCompte(), TypeOperation.DEPOT, montant, "Dépôt");
    }

    /**
     * Effectuer un retrait
     */
    public void effectuerRetrait(Compte compte, BigDecimal montant) throws Exception {
        compte.retirer(montant);
        mettreAJourSolde(compte.getNumeroCompte(), compte.getSolde());
        enregistrerOperation(compte.getNumeroCompte(), TypeOperation.RETRAIT, montant, "Retrait");
    }

    /**
     * Effectuer un virement
     */
    public void effectuerVirement(Compte source, Compte destination, BigDecimal montant) throws Exception {
        source.virerVers(destination, montant);
        mettreAJourSolde(source.getNumeroCompte(), source.getSolde());
        mettreAJourSolde(destination.getNumeroCompte(), destination.getSolde());
        enregistrerOperation(source.getNumeroCompte(), TypeOperation.RETRAIT, montant, "Virement vers " + destination.getNumeroCompte());
        enregistrerOperation(destination.getNumeroCompte(), TypeOperation.DEPOT, montant, "Virement depuis " + source.getNumeroCompte());
    }

    /**
     * Rechercher un compte par son numéro
     */
    public Compte rechercherCompte(String numeroCompte) {
        for (Compte compte : tousLesComptes) {
            if (compte.getNumeroCompte().equals(numeroCompte)) {
                return compte;
            }
        }
        return null;
    }

    /**
     * Rechercher un client par son ID
     */
    public Client rechercherClient(int id) {
        for (Client client : clients) {
            if (client.getIdentifiant() == id) {
                return client;
            }
        }
        return null;
    }

    /**
     * Obtenir l'historique des opérations d'un compte
     */
    public List<Operation> obtenirHistorique(String numeroCompte) {
        List<Operation> operations = new ArrayList<>();
        String sql = "SELECT * FROM Operation WHERE numero_compte = ? ORDER BY date_operation DESC";
        
        try (Connection conn = ConnexionBD.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, numeroCompte);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    TypeOperation type = TypeOperation.valueOf(rs.getString("type_operation"));
                    BigDecimal montant = rs.getBigDecimal("montant");
                    Operation op = new Operation(type, montant, numeroCompte);
                    operations.add(op);
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération de l'historique : " + e.getMessage());
        }
        
        return operations;
    }

    // Getters
    public List<Client> getClients() { return clients; }
    public List<Compte> getTousLesComptes() { return tousLesComptes; }

    /**
     * Afficher un résumé de tous les clients et leurs comptes
     */
    public String afficherResume() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== BANQUE - RÉSUMÉ ===\n");
        sb.append("Nombre de clients: ").append(clients.size()).append("\n");
        sb.append("Nombre de comptes: ").append(tousLesComptes.size()).append("\n\n");

        for (Client client : clients) {
            sb.append(client.afficherComptes()).append("\n");
        }

        return sb.toString();
    }
}
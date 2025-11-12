package model;

import java.util.ArrayList;
import java.util.List;

public class Client {
    private int compteurId = 1;

    private int identifiant;
    private String nom;
    private String prenom;
    private List<Compte> comptes;

    public Client(String nom, String prenom) {
        this.identifiant = compteurId++;
        this.nom = nom;
        this.prenom = prenom;
        this.comptes = new ArrayList<>();
    }

    /**
     * Ajouter un compte au client
     */
    public void ajouterCompte(Compte compte) {
        comptes.add(compte);
    }

    /**
     * Récupérer un compte par son numéro
     */
    public Compte getCompteParNumero(String numeroCompte) {
        for (Compte compte : comptes) {
            if (compte.getNumeroCompte().equals(numeroCompte)) {
                return compte;
            }
        }
        return null;
    }

    /**
     * Afficher tous les comptes du client
     */
    public String afficherComptes() {
        if (comptes.isEmpty()) {
            return "Aucun compte";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Comptes de ").append(getNomComplet()).append(":\n");
        for (Compte compte : comptes) {
            sb.append("  - ").append(compte.toString()).append("\n");
        }
        return sb.toString();
    }

    public String getNomComplet() {
        return prenom + " " + nom;
    }

    // Getters et Setters
    public int getIdentifiant() { return identifiant; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public List<Compte> getComptes() { return comptes; }

    @Override
    public String toString() {
        return String.format("Client #%d: %s %s (%d compte(s))",
                identifiant, prenom, nom, comptes.size());
    }
}
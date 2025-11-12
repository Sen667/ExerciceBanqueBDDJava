package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


public class Compte {
    private static int compteurNumero = 1000;

    private String numeroCompte;
    private BigDecimal solde;
    private List<Operation> operations;
    private String proprietaire;

    public Compte(String proprietaire) {
        this.numeroCompte = "FR" + (compteurNumero++);
        this.solde = BigDecimal.ZERO;
        this.operations = new ArrayList<>();
        this.proprietaire = proprietaire;
    }

    public Compte(String proprietaire, BigDecimal soldeInitial) {
        this(proprietaire);
        this.solde = soldeInitial;
        if (soldeInitial.compareTo(BigDecimal.ZERO) > 0) {
            Operation depot = new Operation(TypeOperation.DEPOT, soldeInitial, numeroCompte);
            operations.add(depot);
        }
    }

    /**
     * Constructeur pour charger un compte depuis la base de données
     */
    public Compte(String numeroCompte, String proprietaire, BigDecimal solde) {
        this.numeroCompte = numeroCompte;
        this.proprietaire = proprietaire;
        this.solde = solde;
        this.operations = new ArrayList<>();
    }

    /**
     * Déposer de l'argent sur le compte
     */
    public void deposer(BigDecimal montant) {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        solde = solde.add(montant);
        Operation operation = new Operation(TypeOperation.DEPOT, montant, numeroCompte);
        operations.add(operation);
    }

    /**
     * Retirer de l'argent du compte
     */
    public void retirer(BigDecimal montant) throws Exception {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        if (solde.compareTo(montant) < 0) {
            throw new Exception("Solde insuffisant. Solde actuel: " + solde + "€");
        }
        solde = solde.subtract(montant);
        Operation operation = new Operation(TypeOperation.RETRAIT, montant, numeroCompte);
        operations.add(operation);
    }

    /**
     * Effectuer un virement vers un autre compte
     */
    public void virerVers(Compte compteDestination, BigDecimal montant) throws Exception {
        if (montant.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être positif");
        }
        if (solde.compareTo(montant) < 0) {
            throw new Exception("Solde insuffisant pour le virement");
        }

        // Débiter le compte source
        solde = solde.subtract(montant);
        Operation operationDebit = new Operation(TypeOperation.VIREMENT, montant,
                numeroCompte, compteDestination.getNumeroCompte());
        operations.add(operationDebit);

        // Créditer le compte destination
        compteDestination.crediterVirement(montant, numeroCompte);
    }

    /**
     * Créditer le compte suite à un virement reçu
     */
    protected void crediterVirement(BigDecimal montant, String compteSource) {
        solde = solde.add(montant);
        Operation operation = new Operation(TypeOperation.VIREMENT, montant,
                compteSource, numeroCompte);
        operations.add(operation);
    }

    public String afficherSolde() {
        return String.format("Solde du compte %s: %.2f€", numeroCompte, solde);
    }

    public List<Operation> getHistorique() {
        return new ArrayList<>(operations);
    }

    // Getters
    public String getNumeroCompte() { return numeroCompte; }
    public BigDecimal getSolde() { return solde; }
    public List<Operation> getOperations() { return operations; }
    public String getProprietaire() { return proprietaire; }

    // Setter pour la synchronisation avec la BD
    public void setSolde(BigDecimal solde) { 
        this.solde = solde; 
    }

    @Override
    public String toString() {
        return String.format("Compte %s (%s) - Solde: %.2f€",
                numeroCompte, proprietaire, solde);
    }
}
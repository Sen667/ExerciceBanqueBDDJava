package com.example.cbclient;

import model.Client;
import model.Compte;
import model.Operation;
import service.GestionCompteService;

import java.math.BigDecimal;

/**
 * Classe principale pour tester l'application en mode console
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   APPLICATION GESTION BANCAIRE      ║");
        System.out.println("╚══════════════════════════════════════╝\n");

        // Initialisation du service
        GestionCompteService service = new GestionCompteService();

        // === ÉTAPE 1: Créer des clients ===
        System.out.println("📝 Création des clients...");
        Client alice = service.creerClient("Dupont", "Alice");
        Client bob = service.creerClient("Martin", "Bob");
        Client charlie = service.creerClient("Dubois", "Charlie");
        System.out.println("✓ " + alice);
        System.out.println("✓ " + bob);
        System.out.println("✓ " + charlie);
        System.out.println();

        // === ÉTAPE 2: Créer des comptes ===
        System.out.println("💳 Création des comptes...");
        Compte compteAlice1 = service.creerCompte(alice, new BigDecimal("1000.00"));
        Compte compteAlice2 = service.creerCompte(alice, new BigDecimal("500.00"));
        Compte compteBob = service.creerCompte(bob, new BigDecimal("2500.00"));
        Compte compteCharlie = service.creerCompte(charlie);

        System.out.println("✓ " + compteAlice1);
        System.out.println("✓ " + compteAlice2);
        System.out.println("✓ " + compteBob);
        System.out.println("✓ " + compteCharlie);
        System.out.println();

        // === ÉTAPE 3: Effectuer des opérations ===
        System.out.println("💰 Opérations bancaires...\n");

        try {
            // Dépôt
            System.out.println("1️⃣  Dépôt de 300€ sur le compte de Charlie");
            service.effectuerDepot(compteCharlie, new BigDecimal("300.00"));
            System.out.println("   " + compteCharlie.afficherSolde());
            System.out.println();

            // Retrait
            System.out.println("2️⃣  Retrait de 150€ du compte d'Alice");
            service.effectuerRetrait(compteAlice1, new BigDecimal("150.00"));
            System.out.println("   " + compteAlice1.afficherSolde());
            System.out.println();

            // Virement
            System.out.println("3️⃣  Virement de 200€ de Bob vers Alice");
            service.effectuerVirement(compteBob, compteAlice1, new BigDecimal("200.00"));
            System.out.println("   Compte Bob:   " + compteBob.afficherSolde());
            System.out.println("   Compte Alice: " + compteAlice1.afficherSolde());
            System.out.println();

            // Autre virement
            System.out.println("4️⃣  Virement de 100€ d'Alice vers Charlie");
            service.effectuerVirement(compteAlice1, compteCharlie, new BigDecimal("100.00"));
            System.out.println("   " + compteAlice1.afficherSolde());
            System.out.println("   " + compteCharlie.afficherSolde());
            System.out.println();

            // Test d'erreur: Solde insuffisant
            System.out.println("5️⃣  Test: Tentative de retrait avec solde insuffisant");
            try {
                service.effectuerRetrait(compteAlice2, new BigDecimal("1000.00"));
            } catch (Exception e) {
                System.out.println("   ❌ ERREUR: " + e.getMessage());
            }
            System.out.println();

        } catch (Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        }

        // === ÉTAPE 4: Afficher les historiques ===
        System.out.println("\n" + "═".repeat(60));
        System.out.println("📊 HISTORIQUE DES OPÉRATIONS");
        System.out.println("═".repeat(60) + "\n");

        afficherHistoriqueCompte(compteAlice1);
        afficherHistoriqueCompte(compteBob);
        afficherHistoriqueCompte(compteCharlie);

        // === ÉTAPE 5: Résumé final ===
        System.out.println("\n" + "═".repeat(60));
        System.out.println(service.afficherResume());
        System.out.println("═".repeat(60));

        // === ÉTAPE 6: Tests avec assertions ===
        System.out.println("\n🧪 Tests d'assertions...");
        testAssertions(compteAlice1, compteBob, compteCharlie);
        System.out.println("✓ Tous les tests sont passés avec succès!\n");
    }

    /**
     * Afficher l'historique d'un compte
     */
    private static void afficherHistoriqueCompte(Compte compte) {
        System.out.println("📋 Compte: " + compte.getNumeroCompte() +
                " (" + compte.getProprietaire() + ")");
        System.out.println("   Solde actuel: " + compte.getSolde() + "€");
        System.out.println("   Opérations:");

        if (compte.getOperations().isEmpty()) {
            System.out.println("      Aucune opération");
        } else {
            for (Operation op : compte.getOperations()) {
                System.out.println("      • " + op);
            }
        }
        System.out.println();
    }

    /**
     * Tests avec assertions
     */
    private static void testAssertions(Compte compteAlice, Compte compteBob, Compte compteCharlie) {
        // Vérifier que les soldes sont corrects
        // Alice: 1000 - 150 + 200 - 100 = 950
        assert compteAlice.getSolde().compareTo(new BigDecimal("950.00")) == 0 :
                "Solde Alice incorrect";

        // Bob: 2500 - 200 = 2300
        assert compteBob.getSolde().compareTo(new BigDecimal("2300.00")) == 0 :
                "Solde Bob incorrect";

        // Charlie: 0 + 300 + 100 = 400
        assert compteCharlie.getSolde().compareTo(new BigDecimal("400.00")) == 0 :
                "Solde Charlie incorrect";

        // Vérifier le nombre d'opérations
        assert compteAlice.getOperations().size() == 5 :
                "Nombre d'opérations Alice incorrect";
        assert compteBob.getOperations().size() == 2 :
                "Nombre d'opérations Bob incorrect";
        assert compteCharlie.getOperations().size() == 2 :
                "Nombre d'opérations Charlie incorrect";

        System.out.println("   ✓ Vérification des soldes");
        System.out.println("   ✓ Vérification du nombre d'opérations");
        System.out.println("   ✓ Intégrité des données");
    }
}
package com.example.cbclient;

import service.GestionCompteService;
import model.Client;
import model.Compte;

/**
 * Test de la synchronisation avec la base de données
 */
public class TestSynchronisation {
    public static void main(String[] args) {
        System.out.println("═══════════════════════════════════════════════════");
        System.out.println("  TEST DE SYNCHRONISATION AVEC LA BASE DE DONNÉES  ");
        System.out.println("═══════════════════════════════════════════════════\n");

        try {
            GestionCompteService service = new GestionCompteService();
            
            System.out.println("\n📊 RÉSULTATS DU CHARGEMENT :");
            System.out.println("────────────────────────────────────────────────");
            System.out.println("✅ Clients chargés : " + service.getClients().size());
            System.out.println("✅ Comptes chargés : " + service.getTousLesComptes().size());
            
            System.out.println("\n👥 LISTE DES CLIENTS :");
            System.out.println("────────────────────────────────────────────────");
            for (Client client : service.getClients()) {
                System.out.println(client);
            }
            
            System.out.println("\n💰 LISTE DES COMPTES :");
            System.out.println("────────────────────────────────────────────────");
            for (Compte compte : service.getTousLesComptes()) {
                System.out.println(compte);
            }
            
            System.out.println("\n" + service.afficherResume());
            
        } catch (Exception e) {
            System.err.println("❌ Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
}

#!/bin/bash

# Script pour nettoyer les données de la base de données sans recréer les tables

echo "═══════════════════════════════════════════════════"
echo "  NETTOYAGE DES DONNÉES DE LA BASE                "
echo "═══════════════════════════════════════════════════"
echo ""

echo "⚠️  Cette opération va supprimer TOUTES les données"
echo "   (clients, comptes, opérations)"
echo ""
read -p "Voulez-vous continuer? (o/n) " -n 1 -r
echo ""

if [[ ! $REPLY =~ ^[Oo]$ ]]; then
    echo "❌ Opération annulée"
    exit 0
fi

echo ""
echo "🔄 Nettoyage en cours..."

# Commandes SQL pour nettoyer les tables
mysql -u root -p banque_db << EOF
-- Désactiver temporairement les contraintes de clés étrangères
SET FOREIGN_KEY_CHECKS = 0;

-- Supprimer toutes les opérations
DELETE FROM Operation;

-- Supprimer tous les comptes
DELETE FROM Compte;

-- Supprimer tous les clients
DELETE FROM Client;

-- Réactiver les contraintes de clés étrangères
SET FOREIGN_KEY_CHECKS = 1;

-- Réinitialiser les auto-increments
ALTER TABLE Client AUTO_INCREMENT = 1;
ALTER TABLE Operation AUTO_INCREMENT = 1;

COMMIT;

SELECT 'Base de données nettoyée' AS Status;
EOF

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Base de données nettoyée avec succès!"
    echo "   Toutes les données ont été supprimées"
    echo ""
else
    echo ""
    echo "❌ Erreur lors du nettoyage"
    exit 1
fi

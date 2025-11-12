#!/bin/bash

# Script pour réinitialiser complètement la base de données

echo "═══════════════════════════════════════════════════"
echo "  RÉINITIALISATION DE LA BASE DE DONNÉES          "
echo "═══════════════════════════════════════════════════"
echo ""

# Chemins des fichiers SQL
SQL_DIR="src/main/resources/sql"
CREATE_SQL="$SQL_DIR/create_database.sql"
INSERT_SQL="$SQL_DIR/insert_data.sql"

# Vérifier que les fichiers SQL existent
if [ ! -f "$CREATE_SQL" ]; then
    echo "❌ Erreur: Le fichier $CREATE_SQL n'existe pas"
    exit 1
fi

if [ ! -f "$INSERT_SQL" ]; then
    echo "❌ Erreur: Le fichier $INSERT_SQL n'existe pas"
    exit 1
fi

echo "⚠️  ATTENTION: Cette opération va:"
echo "   1. Supprimer toutes les données existantes"
echo "   2. Recréer les tables"
echo "   3. Insérer les données de test"
echo ""
read -p "Voulez-vous continuer? (o/n) " -n 1 -r
echo ""

if [[ ! $REPLY =~ ^[Oo]$ ]]; then
    echo "❌ Opération annulée"
    exit 0
fi

echo ""
echo "🔄 Étape 1/2: Recréation de la base de données..."
mysql -u root -p < "$CREATE_SQL"

if [ $? -ne 0 ]; then
    echo "❌ Erreur lors de la création de la base de données"
    exit 1
fi

echo "✅ Base de données recréée"
echo ""
echo "🔄 Étape 2/2: Insertion des données de test..."
mysql -u root -p banque_db < "$INSERT_SQL"

if [ $? -ne 0 ]; then
    echo "❌ Erreur lors de l'insertion des données"
    exit 1
fi

echo ""
echo "✅ Réinitialisation terminée avec succès!"
echo ""
echo "📊 Récapitulatif:"
echo "   - Base de données: banque_db"
echo "   - 5 clients créés"
echo "   - 7 comptes créés"
echo "   - Opérations bancaires insérées"
echo ""

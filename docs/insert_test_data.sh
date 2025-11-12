#!/bin/bash

# Script pour insérer des données de test dans la base de données

echo "═══════════════════════════════════════════════════"
echo "  INSERTION DE DONNÉES DE TEST DANS LA BASE        "
echo "═══════════════════════════════════════════════════"
echo ""

# Chemins des fichiers SQL
SQL_DIR="src/main/resources/sql"
INSERT_SQL="$SQL_DIR/insert_data.sql"

# Vérifier que le fichier SQL existe
if [ ! -f "$INSERT_SQL" ]; then
    echo "❌ Erreur: Le fichier $INSERT_SQL n'existe pas"
    exit 1
fi

echo "📂 Fichier SQL trouvé: $INSERT_SQL"
echo ""
echo "🔄 Insertion des données de test..."
echo ""

# Exécuter le script SQL
mysql -u root -p banque_db < "$INSERT_SQL"

if [ $? -eq 0 ]; then
    echo ""
    echo "✅ Données insérées avec succès!"
    echo ""
    echo "📊 Récapitulatif:"
    echo "   - 5 clients créés"
    echo "   - 7 comptes créés"
    echo "   - Plusieurs opérations enregistrées"
    echo ""
else
    echo ""
    echo "❌ Erreur lors de l'insertion des données"
    echo "   Vérifiez que:"
    echo "   - MySQL est démarré"
    echo "   - La base de données 'banque_db' existe"
    echo "   - Vos identifiants MySQL sont corrects"
    exit 1
fi

#!/bin/bash

# Script de configuration automatique de la base de données
# Usage: ./setup_database.sh [mot_de_passe_mysql]

echo "╔═══════════════════════════════════════════════════════════╗"
echo "║   CONFIGURATION BASE DE DONNÉES - APPLICATION BANCAIRE    ║"
echo "╚═══════════════════════════════════════════════════════════╝"
echo ""

# Couleurs
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Vérifier que MySQL est installé
echo -e "${BLUE}[1/6]${NC} Vérification de MySQL..."
if ! command -v mysql &> /dev/null; then
    echo -e "${RED}❌ MySQL n'est pas installé !${NC}"
    echo ""
    echo "Installation sur macOS:"
    echo "  brew install mysql"
    echo "  brew services start mysql"
    echo ""
    echo "Installation sur Ubuntu/Debian:"
    echo "  sudo apt install mysql-server"
    echo "  sudo systemctl start mysql"
    exit 1
fi
echo -e "${GREEN}✅ MySQL est installé${NC}"
echo ""

# Demander le mot de passe MySQL
if [ -z "$1" ]; then
    echo -e "${YELLOW}Entrez votre mot de passe MySQL root:${NC}"
    read -s MYSQL_PASSWORD
else
    MYSQL_PASSWORD=$1
fi
echo ""

# Tester la connexion MySQL
echo -e "${BLUE}[2/6]${NC} Test de connexion à MySQL..."
if ! mysql -u root -p"$MYSQL_PASSWORD" -e "SELECT 1;" &> /dev/null; then
    echo -e "${RED}❌ Impossible de se connecter à MySQL${NC}"
    echo "Vérifiez votre mot de passe"
    exit 1
fi
echo -e "${GREEN}✅ Connexion MySQL réussie${NC}"
echo ""

# Créer la base de données et les tables
echo -e "${BLUE}[3/6]${NC} Création de la base de données..."
if mysql -u root -p"$MYSQL_PASSWORD" < src/main/resources/sql/create_database.sql; then
    echo -e "${GREEN}✅ Base de données 'banque_db' créée${NC}"
else
    echo -e "${RED}❌ Erreur lors de la création de la base${NC}"
    exit 1
fi
echo ""

# Insérer les données de test
echo -e "${BLUE}[4/6]${NC} Insertion des données de test..."
if mysql -u root -p"$MYSQL_PASSWORD" < src/main/resources/sql/insert_data.sql; then
    echo -e "${GREEN}✅ Données de test insérées${NC}"
else
    echo -e "${RED}❌ Erreur lors de l'insertion des données${NC}"
    exit 1
fi
echo ""

# Compiler le projet
echo -e "${BLUE}[5/6]${NC} Compilation du projet Maven..."
if ./mvnw clean compile > /dev/null 2>&1; then
    echo -e "${GREEN}✅ Compilation réussie${NC}"
else
    echo -e "${RED}❌ Erreur de compilation${NC}"
    exit 1
fi
echo ""

# Tester la connexion Java
echo -e "${BLUE}[6/6]${NC} Test de connexion Java → MySQL..."
./mvnw compile exec:java -Dexec.mainClass="com.example.cbclient.TestConnexionBD" -q
echo ""

# Résumé
echo "╔═══════════════════════════════════════════════════════════╗"
echo "║           ✅ CONFIGURATION TERMINÉE AVEC SUCCÈS           ║"
echo "╚═══════════════════════════════════════════════════════════╝"
echo ""
echo -e "${GREEN}📊 Base de données créée :${NC}"
echo "   • 5 clients"
echo "   • 7 comptes"
echo "   • ~20 opérations"
echo ""
echo -e "${YELLOW}🔧 Prochaines étapes :${NC}"
echo ""
echo "1. Vérifier la configuration dans model/ConnexionBD.java"
echo "   (Si votre mot de passe MySQL est différent de '')"
echo ""
echo "2. Lancer l'exemple complet :"
echo "   ./mvnw compile exec:java -Dexec.mainClass=\"com.example.cbclient.ExempleUtilisation\""
echo ""
echo "3. Lancer l'interface graphique :"
echo "   ./mvnw javafx:run"
echo ""
echo "4. Vérifier les données dans MySQL :"
echo "   mysql -u root -p"
echo "   USE banque_db;"
echo "   SELECT * FROM Client;"
echo "   SELECT * FROM Compte;"
echo "   SELECT * FROM Operation;"
echo ""
echo -e "${GREEN}🎉 Votre application bancaire est prête !${NC}"

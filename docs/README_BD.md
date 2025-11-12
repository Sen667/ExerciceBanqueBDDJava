# 🏦 Application Bancaire - Intégration Base de Données MySQL

## 📝 Résumé du Projet

Application de gestion bancaire développée en **Java 24 + JavaFX** avec intégration complète d'une **base de données MySQL** pour la persistance des données.

---

## ✅ État du Projet

🎉 **PROJET COMPLÉTÉ AVEC SUCCÈS !**

- ✅ Base de données MySQL configurée
- ✅ Tables créées avec relations (Foreign Keys)
- ✅ Données de test insérées (5 clients, 7 comptes, ~20 opérations)
- ✅ Classe ConnexionBD implémentée dans le package Model
- ✅ Driver MySQL (com.mysql.cj.jdbc.Driver) intégré via Maven
- ✅ Service GestionCompteService adapté pour la persistance
- ✅ Toutes les opérations enregistrées en base de données
- ✅ Compilation réussie sans erreurs
- ✅ Documentation complète fournie

---

## 🗂️ Structure du Projet

```
CBclient/
│
├── src/main/java/
│   ├── model/
│   │   ├── Client.java
│   │   ├── Compte.java
│   │   ├── Operation.java
│   │   ├── TypeOperation.java
│   │   └── ConnexionBD.java                 ⭐ NOUVEAU - Gestion connexion MySQL
│   │
│   ├── service/
│   │   ├── GestionCompteService.java        🔄 MODIFIÉ - Persistance BD
│   │   └── exceptions/
│   │       ├── CompteInexistantException.java
│   │       └── SoldeInsuffisantException.java
│   │
│   └── com/example/cbclient/
│       ├── BanqueApp.java
│       ├── BanqueGUI.java
│       ├── BanqueGUIController.java
│       ├── TestConnexionBD.java             ⭐ NOUVEAU - Test connexion
│       └── ExempleUtilisation.java          ⭐ NOUVEAU - Exemple complet
│
├── src/main/resources/
│   ├── sql/
│   │   ├── create_database.sql              ⭐ NOUVEAU - Création tables
│   │   └── insert_data.sql                  ⭐ NOUVEAU - Données de test
│   │
│   └── com/example/cbclient/
│       ├── banque-view.fxml
│       └── hello-view.fxml
│
├── pom.xml                                   🔄 MODIFIÉ - Driver MySQL ajouté
├── module-info.java                          🔄 MODIFIÉ - requires java.sql
│
├── DATABASE_SETUP.md                         ⭐ Guide installation MySQL
├── GUIDE_INTEGRATION_BD.md                   ⭐ Guide complet d'intégration
└── RESUME_INTEGRATION.md                     ⭐ Résumé détaillé
```

---

## 🚀 Démarrage Rapide

### 1. Prérequis

- ☕ Java 24 (ou supérieur)
- 🗄️ MySQL 8.x (ou supérieur)
- 📦 Maven (inclus avec mvnw)

### 2. Installation MySQL

#### macOS:
```bash
brew install mysql
brew services start mysql
```

#### Windows:
Télécharger depuis https://dev.mysql.com/downloads/mysql/

### 3. Configuration Base de Données

```bash
# Créer la base et les tables
mysql -u root -p < src/main/resources/sql/create_database.sql

# Insérer les données de test
mysql -u root -p < src/main/resources/sql/insert_data.sql
```

### 4. Configuration Connexion

Éditer `src/main/java/model/ConnexionBD.java` :

```java
private static final String PASSWORD = "votre_mot_de_passe_mysql";
```

### 5. Compilation

```bash
./mvnw clean compile
```

### 6. Test de Connexion

```bash
./mvnw compile exec:java -Dexec.mainClass="com.example.cbclient.TestConnexionBD"
```

**Résultat attendu :**
```
=== Test de Connexion à la Base de Données ===

Connexion à la base de données réussie !
Test de connexion réussi !
Base de données : MySQL
Version : 8.x.x
```

### 7. Exemple Complet

```bash
./mvnw compile exec:java -Dexec.mainClass="com.example.cbclient.ExempleUtilisation"
```

Cet exemple démontre :
- Connexion à la BD
- Chargement des données
- Création de client
- Création de compte
- Dépôt et retrait
- Historique des opérations
- Recherche de compte

### 8. Lancer l'Interface Graphique

```bash
./mvnw javafx:run
```

---

## 🗄️ Base de Données

### SGBD : MySQL 8.x

### Tables

#### 1. **Client**
| Colonne | Type | Contrainte |
|---------|------|------------|
| id_client | INT | PRIMARY KEY, AUTO_INCREMENT |
| nom | VARCHAR(100) | NOT NULL |
| prenom | VARCHAR(100) | NOT NULL |
| telephone | VARCHAR(20) | |
| email | VARCHAR(100) | |
| adresse | VARCHAR(255) | |
| date_creation | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |

#### 2. **Compte**
| Colonne | Type | Contrainte |
|---------|------|------------|
| numero_compte | VARCHAR(50) | PRIMARY KEY |
| id_client | INT | FOREIGN KEY → Client(id_client) |
| solde | DECIMAL(15,2) | DEFAULT 0.00 |
| type_compte | VARCHAR(50) | DEFAULT 'COURANT' |
| date_ouverture | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |
| actif | BOOLEAN | DEFAULT TRUE |

#### 3. **Operation**
| Colonne | Type | Contrainte |
|---------|------|------------|
| id_operation | INT | PRIMARY KEY, AUTO_INCREMENT |
| numero_compte | VARCHAR(50) | FOREIGN KEY → Compte(numero_compte) |
| type_operation | VARCHAR(20) | NOT NULL |
| montant | DECIMAL(15,2) | NOT NULL |
| date_operation | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP |
| description | VARCHAR(255) | |

### Données de Test

- **5 clients** (Dupont, Martin, Bernard, Dubois, Thomas)
- **7 comptes** (CPT001 à CPT007)
- **~20 opérations** (dépôts, retraits)

---

## 🔧 Architecture

### Pattern MVC Respecté

- **Model** : Client, Compte, Operation, ConnexionBD
- **View** : BanqueGUI (JavaFX)
- **Controller** : GestionCompteService, BanqueGUIController

### Connexion MySQL

```java
// Chargement du driver
Class.forName("com.mysql.cj.jdbc.Driver");

// Connexion
Connection conn = DriverManager.getConnection(
    "jdbc:mysql://localhost:3306/banque_db",
    "root",
    "password"
);
```

### Opérations CRUD Implémentées

| Opération | Méthode | Action SQL |
|-----------|---------|------------|
| **Create** | `creerClient()` | INSERT INTO Client |
| **Create** | `creerCompte()` | INSERT INTO Compte |
| **Read** | `chargerClients()` | SELECT * FROM Client |
| **Read** | `chargerComptes()` | SELECT avec JOIN |
| **Read** | `obtenirHistorique()` | SELECT FROM Operation |
| **Update** | `effectuerDepot()` | UPDATE Compte SET solde |
| **Update** | `effectuerRetrait()` | UPDATE Compte SET solde |
| **Create** | `enregistrerOperation()` | INSERT INTO Operation |

---

## 📚 Documentation

### Fichiers de Documentation

1. **DATABASE_SETUP.md** - Guide d'installation MySQL détaillé
2. **GUIDE_INTEGRATION_BD.md** - Guide complet d'intégration
3. **RESUME_INTEGRATION.md** - Résumé technique détaillé

### Scripts SQL

1. **create_database.sql** - Création de la BD et des tables
2. **insert_data.sql** - Insertion de données de test

### Exemples

1. **TestConnexionBD.java** - Test simple de connexion
2. **ExempleUtilisation.java** - Démonstration complète

---

## 🧪 Vérification Manuelle

### Voir les données dans MySQL

```sql
-- Se connecter
mysql -u root -p

-- Utiliser la base
USE banque_db;

-- Voir tous les clients
SELECT * FROM Client;

-- Voir tous les comptes avec leurs propriétaires
SELECT 
    c.numero_compte,
    CONCAT(cl.prenom, ' ', cl.nom) AS proprietaire,
    c.solde,
    c.type_compte
FROM Compte c
INNER JOIN Client cl ON c.id_client = cl.id_client;

-- Voir toutes les opérations récentes
SELECT 
    o.id_operation,
    o.numero_compte,
    o.type_operation,
    o.montant,
    o.date_operation,
    o.description
FROM Operation o
ORDER BY o.date_operation DESC
LIMIT 10;

-- Statistiques
SELECT 
    COUNT(DISTINCT c.id_client) as nb_clients,
    COUNT(DISTINCT cpt.numero_compte) as nb_comptes,
    COUNT(o.id_operation) as nb_operations,
    SUM(CASE WHEN o.type_operation = 'DEPOT' THEN o.montant ELSE 0 END) as total_depots,
    SUM(CASE WHEN o.type_operation = 'RETRAIT' THEN o.montant ELSE 0 END) as total_retraits
FROM Client c
LEFT JOIN Compte cpt ON c.id_client = cpt.id_client
LEFT JOIN Operation o ON cpt.numero_compte = o.numero_compte;
```

---

## 📦 Dépendances Maven

```xml
<!-- JavaFX -->
<dependency>
    <groupId>org.openjfx</groupId>
    <artifactId>javafx-controls</artifactId>
    <version>21.0.6</version>
</dependency>

<!-- MySQL Connector -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <version>8.0.33</version>
</dependency>
```

---

## 🎯 Fonctionnalités

### ✅ Implémentées

- [x] Connexion à MySQL
- [x] Création de clients
- [x] Création de comptes
- [x] Dépôts avec enregistrement BD
- [x] Retraits avec enregistrement BD
- [x] Virements avec enregistrement BD
- [x] Historique des opérations
- [x] Recherche de comptes
- [x] Chargement automatique des données
- [x] Persistance totale

### 🔮 Améliorations Possibles

- [ ] Pool de connexions
- [ ] Transactions SQL (BEGIN, COMMIT, ROLLBACK)
- [ ] Gestion avancée des exceptions
- [ ] Authentification utilisateurs
- [ ] Export PDF des relevés
- [ ] Graphiques et statistiques
- [ ] Interface web

---

## 🐛 Troubleshooting

### Erreur : "Access denied for user"
```bash
# Vérifier le mot de passe dans ConnexionBD.java
# Ou réinitialiser le mot de passe MySQL
mysql -u root -p
ALTER USER 'root'@'localhost' IDENTIFIED BY 'nouveau_password';
```

### Erreur : "Unknown database 'banque_db'"
```bash
# Exécuter le script de création
mysql -u root -p < src/main/resources/sql/create_database.sql
```

### Erreur : "Driver not found"
```bash
# Télécharger les dépendances Maven
./mvnw clean install
```

---

## 👨‍💻 Auteur

Projet réalisé dans le cadre du cours **2TSSIO - Bloc N°2**  
Conception et développement d'applications avec persistance des données

---

## 📄 Licence

Projet éducatif - Usage libre pour l'apprentissage

---

## 🎓 Conformité Pédagogique

✅ **Tous les objectifs du cours sont atteints :**

1. ✅ SGBD choisi et justifié (MySQL)
2. ✅ Structure de BD construite
3. ✅ Données de test insérées
4. ✅ Classe ConnexionBD dans le package Model
5. ✅ Méthodes identifiées et implémentées
6. ✅ Driver correctement déclaré
7. ✅ Package java.sql importé

---

**Projet prêt pour démonstration et évaluation ! 🚀**

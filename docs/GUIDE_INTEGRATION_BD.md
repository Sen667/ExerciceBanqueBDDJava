# Guide d'Intégration de la Base de Données

## ✅ Récapitulatif des modifications

L'application bancaire a été mise à jour pour utiliser **MySQL** comme système de gestion de base de données.

## 📁 Fichiers créés/modifiés

### Nouveaux fichiers :
1. **`model/ConnexionBD.java`** - Classe de gestion de la connexion MySQL
2. **`src/main/resources/sql/create_database.sql`** - Script de création de la base
3. **`src/main/resources/sql/insert_data.sql`** - Script d'insertion de données de test
4. **`com/example/cbclient/TestConnexionBD.java`** - Programme de test de connexion
5. **`DATABASE_SETUP.md`** - Guide de configuration détaillé

### Fichiers modifiés :
1. **`pom.xml`** - Ajout du driver MySQL (mysql-connector-j)
2. **`module-info.java`** - Ajout du module `java.sql`
3. **`service/GestionCompteService.java`** - Intégration de la persistance en BD

## 🗄️ Structure de la Base de Données

### SGBD choisi : **MySQL**

### Tables créées :

#### 1. Table `Client`
```sql
- id_client (INT, AUTO_INCREMENT, PRIMARY KEY)
- nom (VARCHAR 100)
- prenom (VARCHAR 100)
- telephone (VARCHAR 20)
- email (VARCHAR 100)
- adresse (VARCHAR 255)
- date_creation (TIMESTAMP)
```

#### 2. Table `Compte`
```sql
- numero_compte (VARCHAR 50, PRIMARY KEY)
- id_client (INT, FOREIGN KEY)
- solde (DECIMAL 15,2)
- type_compte (VARCHAR 50)
- date_ouverture (TIMESTAMP)
- actif (BOOLEAN)
```

#### 3. Table `Operation`
```sql
- id_operation (INT, AUTO_INCREMENT, PRIMARY KEY)
- numero_compte (VARCHAR 50, FOREIGN KEY)
- type_operation (VARCHAR 20)
- montant (DECIMAL 15,2)
- date_operation (TIMESTAMP)
- description (VARCHAR 255)
```

## 🚀 Instructions d'installation

### Étape 1 : Installer MySQL

#### Sur macOS :
```bash
brew install mysql
brew services start mysql
mysql_secure_installation
```

#### Sur Windows :
- Télécharger MySQL Community Server depuis https://dev.mysql.com/downloads/mysql/
- Installer avec l'installeur
- Configurer le mot de passe root

### Étape 2 : Créer la base de données

```bash
# Se connecter à MySQL
mysql -u root -p

# Exécuter le script de création
source /chemin/vers/CBclient/src/main/resources/sql/create_database.sql

# Exécuter le script d'insertion de données
source /chemin/vers/CBclient/src/main/resources/sql/insert_data.sql
```

**OU** via ligne de commande directe :
```bash
mysql -u root -p < src/main/resources/sql/create_database.sql
mysql -u root -p < src/main/resources/sql/insert_data.sql
```

### Étape 3 : Configurer les identifiants

Modifier le fichier `model/ConnexionBD.java` avec vos identifiants MySQL :

```java
private static final String URL = "jdbc:mysql://localhost:3306/banque_db";
private static final String USER = "root";
private static final String PASSWORD = "votre_mot_de_passe";
```

### Étape 4 : Télécharger les dépendances Maven

```bash
./mvnw clean install
```

### Étape 5 : Tester la connexion

```bash
./mvnw compile exec:java -Dexec.mainClass="com.example.cbclient.TestConnexionBD"
```

Si le test réussit, vous devriez voir :
```
=== Test de Connexion à la Base de Données ===

Connexion à la base de données réussie !
Test de connexion réussi !
Base de données : MySQL
Version : 8.x.x
```

### Étape 6 : Lancer l'application

```bash
./mvnw javafx:run
```

## 🔧 Méthodes utilisant la Base de Données

### Classe `ConnexionBD` :
- `getConnection()` - Établit la connexion à la BD
- `closeConnection()` - Ferme la connexion
- `testerConnexion()` - Teste la connexion

### Classe `GestionCompteService` (modifiée) :
- `chargerDonnees()` - Charge tous les clients et comptes au démarrage
- `chargerClients()` - Récupère tous les clients de la BD
- `chargerComptes()` - Récupère tous les comptes de la BD
- `creerClient()` - Crée un client dans la BD
- `creerCompte()` - Crée un compte dans la BD
- `effectuerDepot()` - Enregistre le dépôt et met à jour la BD
- `effectuerRetrait()` - Enregistre le retrait et met à jour la BD
- `effectuerVirement()` - Enregistre le virement et met à jour la BD
- `enregistrerOperation()` - Sauvegarde une opération dans la BD
- `mettreAJourSolde()` - Met à jour le solde d'un compte
- `obtenirHistorique()` - Récupère l'historique des opérations

## 📊 Données de test fournies

Le script `insert_data.sql` crée :
- **5 clients** avec informations complètes
- **7 comptes bancaires** (types COURANT et EPARGNE)
- **~20 opérations** (dépôts, retraits)

### Exemples de comptes :
- **CPT001** : Jean Dupont - 5 000,00€ (Courant)
- **CPT002** : Jean Dupont - 15 000,00€ (Épargne)
- **CPT003** : Marie Martin - 3 500,50€ (Courant)
- etc.

## ⚠️ Points importants

1. **Driver MySQL** : Le driver `com.mysql.cj.jdbc.Driver` est téléchargé automatiquement par Maven
2. **Connexion persistante** : La connexion reste ouverte pendant toute la durée de l'application
3. **Transactions** : Chaque opération (dépôt, retrait, virement) est enregistrée en BD
4. **Synchronisation** : Les données en mémoire sont synchronisées avec la BD

## 🔍 Vérification manuelle

Pour vérifier que tout fonctionne :

```sql
USE banque_db;

-- Voir tous les clients
SELECT * FROM Client;

-- Voir tous les comptes avec leur propriétaire
SELECT c.numero_compte, CONCAT(cl.prenom, ' ', cl.nom) AS client, c.solde, c.type_compte
FROM Compte c
INNER JOIN Client cl ON c.id_client = cl.id_client;

-- Voir toutes les opérations
SELECT o.*, c.id_client
FROM Operation o
INNER JOIN Compte c ON o.numero_compte = c.numero_compte
ORDER BY o.date_operation DESC;

-- Statistiques
SELECT 
    COUNT(DISTINCT id_client) as nb_clients,
    COUNT(DISTINCT numero_compte) as nb_comptes,
    COUNT(*) as nb_operations,
    SUM(CASE WHEN type_operation = 'DEPOT' THEN montant ELSE 0 END) as total_depots,
    SUM(CASE WHEN type_operation = 'RETRAIT' THEN montant ELSE 0 END) as total_retraits
FROM Operation o
INNER JOIN Compte c ON o.numero_compte = c.numero_compte;
```

## 🎯 Prochaines étapes (optionnel)

Pour aller plus loin :
1. Ajouter une gestion des transactions SQL (BEGIN, COMMIT, ROLLBACK)
2. Implémenter un pool de connexions pour améliorer les performances
3. Ajouter des logs détaillés
4. Créer des requêtes préparées pour toutes les opérations
5. Ajouter une gestion d'erreurs plus robuste

## 📝 Exemple d'utilisation dans le code

```java
// Créer un nouveau client
GestionCompteService service = new GestionCompteService();
Client client = service.creerClient("Nom", "Prénom", "0612345678", "email@test.com", "Adresse");

// Créer un compte
Compte compte = service.creerCompte(client, new BigDecimal("1000.00"));

// Effectuer un dépôt (enregistré automatiquement en BD)
service.effectuerDepot(compte, new BigDecimal("500.00"));

// Voir l'historique
List<Operation> historique = service.obtenirHistorique(compte.getNumeroCompte());
```

---

**Félicitations !** Votre application bancaire est maintenant connectée à une base de données MySQL ! 🎉

# Configuration de la Base de Données

## Prérequis
- MySQL Server installé (version 8.0 ou supérieure)
- MySQL Workbench (optionnel, pour l'interface graphique)

## Installation de MySQL

### Sur Windows:
1. Télécharger MySQL Community Server depuis https://dev.mysql.com/downloads/mysql/
2. Installer MySQL avec les paramètres par défaut
3. Noter le mot de passe root configuré lors de l'installation

### Sur macOS:
```bash
brew install mysql
brew services start mysql
```

### Sur Linux (Ubuntu/Debian):
```bash
sudo apt update
sudo apt install mysql-server
sudo systemctl start mysql
```

## Configuration de la Base de Données

### 1. Se connecter à MySQL
```bash
mysql -u root -p
```

### 2. Exécuter le script de création
```bash
mysql -u root -p < src/main/resources/sql/create_database.sql
```

### 3. Insérer les données de test
```bash
mysql -u root -p < src/main/resources/sql/insert_data.sql
```

### Alternative : Via MySQL Workbench
1. Ouvrir MySQL Workbench
2. Se connecter au serveur MySQL
3. Ouvrir le fichier `create_database.sql`
4. Exécuter le script (icône éclair ⚡)
5. Ouvrir le fichier `insert_data.sql`
6. Exécuter le script

## Configuration de la Connexion

Modifier les paramètres dans `model/ConnexionBD.java` si nécessaire:

```java
private static final String URL = "jdbc:mysql://localhost:3306/banque_db";
private static final String USER = "root";
private static final String PASSWORD = ""; // Mettre votre mot de passe MySQL
```

## Structure de la Base de Données

### Table Client
- `id_client` : Identifiant unique (clé primaire)
- `nom` : Nom du client
- `prenom` : Prénom du client
- `telephone` : Numéro de téléphone
- `email` : Adresse email
- `adresse` : Adresse postale
- `date_creation` : Date de création du compte

### Table Compte
- `numero_compte` : Numéro de compte (clé primaire)
- `id_client` : Référence au client (clé étrangère)
- `solde` : Solde du compte
- `type_compte` : Type de compte (COURANT, EPARGNE)
- `date_ouverture` : Date d'ouverture
- `actif` : Statut du compte

### Table Operation
- `id_operation` : Identifiant unique (clé primaire)
- `numero_compte` : Référence au compte (clé étrangère)
- `type_operation` : Type d'opération (DEPOT, RETRAIT)
- `montant` : Montant de l'opération
- `date_operation` : Date de l'opération
- `description` : Description de l'opération

## Vérification

Pour vérifier que tout fonctionne:
```sql
USE banque_db;
SELECT * FROM Client;
SELECT * FROM Compte;
SELECT * FROM Operation;
```

## Données de Test

Le script `insert_data.sql` crée:
- 5 clients
- 7 comptes bancaires
- Environ 20 opérations

Ces données vous permettent de tester immédiatement l'application.

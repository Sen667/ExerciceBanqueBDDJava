# 🔐 Guide de Connexion - Application Bancaire

## Identifiants de connexion

### Option 1 : Administrateur
- **Identifiant** : `admin`
- **Mot de passe** : `admin123`

### Option 2 : Clients (format : prenom.nom)
Tous les clients de la base de données peuvent se connecter avec :
- **Identifiant** : `prenom.nom` (en minuscules)
- **Mot de passe** : `1234`

### Exemples d'identifiants clients :
- **Jean Dupont** : 
  - Identifiant : `jean.dupont`
  - Mot de passe : `1234`
  
- **Marie Martin** : 
  - Identifiant : `marie.martin`
  - Mot de passe : `1234`
  
- **Pierre Bernard** : 
  - Identifiant : `pierre.bernard`
  - Mot de passe : `1234`

- **Sophie Dubois** : 
  - Identifiant : `sophie.dubois`
  - Mot de passe : `1234`

- **Luc Thomas** : 
  - Identifiant : `luc.thomas`
  - Mot de passe : `1234`

## 🚀 Démarrage de l'application

### Avec l'interface de connexion :
```bash
./mvnw clean javafx:run -Djavafx.mainClass="com.example.cbclient.BanqueLoginApp"
```

### Sans interface de connexion (direct) :
```bash
./mvnw clean javafx:run -Djavafx.mainClass="com.example.cbclient.BanqueApp"
```

## 📝 Notes de sécurité

⚠️ **Important** : Dans cet exemple pédagogique :
- Les mots de passe ne sont PAS hashés
- Tous les clients utilisent le même mot de passe (`1234`)
- L'authentification est simplifiée

Dans une application réelle, il faudrait :
- Créer une table `Utilisateur` avec des mots de passe hashés (bcrypt, SHA-256, etc.)
- Implémenter une gestion des sessions
- Ajouter des rôles et permissions
- Limiter les tentatives de connexion
- Utiliser HTTPS pour la transmission des données

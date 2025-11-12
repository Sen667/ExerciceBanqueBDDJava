# 🔑 IDENTIFIANTS DE CONNEXION

## 📋 Vue d'ensemble

L'application bancaire dispose de plusieurs comptes de test pour la connexion.

---

## 👨‍💼 Compte Administrateur

### Admin Principal
- **Identifiant** : `admin`
- **Mot de passe** : `admin123`
- **Accès** : Tous les clients et comptes

---

## 👥 Comptes Clients

### 1️⃣ Jean Dupont
- **Identifiant** : `jean.dupont`
- **Mot de passe** : `1234`
- **Comptes bancaires** : 
  - CPT001 (Courant) - 5 000,00 €
  - CPT002 (Épargne) - 15 000,00 €
- **Total** : 20 000,00 €

### 2️⃣ Marie Martin
- **Identifiant** : `marie.martin`
- **Mot de passe** : `1234`
- **Comptes bancaires** : 
  - CPT003 (Courant) - 3 500,50 €
- **Total** : 3 500,50 €

### 3️⃣ Pierre Bernard
- **Identifiant** : `pierre.bernard`
- **Mot de passe** : `1234`
- **Comptes bancaires** : 
  - CPT004 (Courant) - 12 000,00 €
  - CPT005 (Épargne) - 25 000,00 €
- **Total** : 37 000,00 €

### 4️⃣ Sophie Dubois
- **Identifiant** : `sophie.dubois`
- **Mot de passe** : `1234`
- **Comptes bancaires** : 
  - CPT006 (Courant) - 8 500,75 €
- **Total** : 8 500,75 €

### 5️⃣ Luc Thomas
- **Identifiant** : `luc.thomas`
- **Mot de passe** : `1234`
- **Comptes bancaires** : 
  - CPT007 (Courant) - 2 000,00 €
- **Total** : 2 000,00 €

---

## 📊 Tableau Récapitulatif

| Client          | Identifiant      | Mot de passe | Nb Comptes | Solde Total   |
|-----------------|------------------|--------------|------------|---------------|
| **Admin**       | admin            | admin123     | Tous       | -             |
| Jean Dupont     | jean.dupont      | 1234         | 2          | 20 000,00 €   |
| Marie Martin    | marie.martin     | 1234         | 1          | 3 500,50 €    |
| Pierre Bernard  | pierre.bernard   | 1234         | 2          | 37 000,00 €   |
| Sophie Dubois   | sophie.dubois    | 1234         | 1          | 8 500,75 €    |
| Luc Thomas      | luc.thomas       | 1234         | 1          | 2 000,00 €    |

---

## 🚀 Comment se connecter ?

1. Lancez l'application avec :
   ```bash
   ./run_login_app.sh
   ```
   ou
   ```bash
   ./mvnw clean javafx:run -Djavafx.mainClass="com.example.cbclient.BanqueLoginApp"
   ```

2. Entrez un identifiant et un mot de passe de la liste ci-dessus

3. Cliquez sur "Se Connecter"

---

## 💡 Astuces

- **Format des identifiants** : `prenom.nom` (tout en minuscules)
- **Mot de passe unique** : Tous les clients utilisent `1234` (simplifié pour le TP)
- **Admin** : Accès complet avec `admin` / `admin123`
- **Touche Entrée** : Vous pouvez appuyer sur Entrée après le mot de passe pour vous connecter

---

## ⚠️ Note de sécurité

> 🔒 **Attention** : Cette configuration est à but pédagogique uniquement !
> 
> Dans une application réelle :
> - Les mots de passe seraient hashés (bcrypt, SHA-256)
> - Chaque utilisateur aurait un mot de passe unique
> - Une table `Utilisateur` dédiée serait créée
> - Des sessions sécurisées seraient implémentées
> - HTTPS serait obligatoire

---

## 🎯 Pour tester rapidement

**Connexion rapide recommandée** :
```
Identifiant : jean.dupont
Mot de passe : 1234
```

**Ou en mode admin** :
```
Identifiant : admin
Mot de passe : admin123
```

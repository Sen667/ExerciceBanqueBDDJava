-- Script de création de la base de données Banque

-- Création de la base de données
CREATE DATABASE IF NOT EXISTS banque_db 
CHARACTER SET utf8mb4 
COLLATE utf8mb4_unicode_ci;

USE banque_db;

-- Table Client
CREATE TABLE IF NOT EXISTS Client (
    id_client INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    prenom VARCHAR(100) NOT NULL,
    telephone VARCHAR(20),
    email VARCHAR(100),
    adresse VARCHAR(255),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table Compte
CREATE TABLE IF NOT EXISTS Compte (
    numero_compte VARCHAR(50) PRIMARY KEY,
    id_client INT NOT NULL,
    solde DECIMAL(15, 2) DEFAULT 0.00,
    type_compte VARCHAR(50) DEFAULT 'COURANT',
    date_ouverture TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    actif BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_client) REFERENCES Client(id_client) ON DELETE CASCADE
);

-- Table Operation
CREATE TABLE IF NOT EXISTS Operation (
    id_operation INT AUTO_INCREMENT PRIMARY KEY,
    numero_compte VARCHAR(50) NOT NULL,
    type_operation VARCHAR(20) NOT NULL,
    montant DECIMAL(15, 2) NOT NULL,
    date_operation TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    description VARCHAR(255),
    FOREIGN KEY (numero_compte) REFERENCES Compte(numero_compte) ON DELETE CASCADE
);

-- Index pour améliorer les performances
CREATE INDEX idx_compte_client ON Compte(id_client);
CREATE INDEX idx_operation_compte ON Operation(numero_compte);
CREATE INDEX idx_operation_date ON Operation(date_operation);

COMMIT;

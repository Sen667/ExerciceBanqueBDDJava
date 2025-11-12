-- Script d'insertion de données de test

USE banque_db;

-- Insertion de clients
INSERT INTO Client (nom, prenom, telephone, email, adresse) VALUES
('Dupont', 'Jean', '0612345678', 'jean.dupont@email.com', '12 Rue de Paris, 75001 Paris'),
('Martin', 'Marie', '0623456789', 'marie.martin@email.com', '45 Avenue des Champs, 69001 Lyon'),
('Bernard', 'Pierre', '0634567890', 'pierre.bernard@email.com', '78 Boulevard Victor Hugo, 33000 Bordeaux'),
('Dubois', 'Sophie', '0645678901', 'sophie.dubois@email.com', '23 Rue Nationale, 59000 Lille'),
('Thomas', 'Luc', '0656789012', 'luc.thomas@email.com', '56 Place Bellecour, 69002 Lyon');

-- Insertion de comptes
INSERT INTO Compte (numero_compte, id_client, solde, type_compte) VALUES
('CPT001', 1, 5000.00, 'COURANT'),
('CPT002', 1, 15000.00, 'EPARGNE'),
('CPT003', 2, 3500.50, 'COURANT'),
('CPT004', 3, 12000.00, 'COURANT'),
('CPT005', 3, 25000.00, 'EPARGNE'),
('CPT006', 4, 8500.75, 'COURANT'),
('CPT007', 5, 2000.00, 'COURANT');

-- Insertion d'opérations
INSERT INTO Operation (numero_compte, type_operation, montant, description) VALUES
-- Opérations pour CPT001
('CPT001', 'DEPOT', 1000.00, 'Dépôt initial'),
('CPT001', 'DEPOT', 2000.00, 'Virement salaire'),
('CPT001', 'RETRAIT', 500.00, 'Retrait DAB'),
('CPT001', 'DEPOT', 3000.00, 'Virement externe'),
('CPT001', 'RETRAIT', 500.00, 'Achat en ligne'),

-- Opérations pour CPT002
('CPT002', 'DEPOT', 15000.00, 'Dépôt épargne initial'),

-- Opérations pour CPT003
('CPT003', 'DEPOT', 5000.00, 'Dépôt initial'),
('CPT003', 'RETRAIT', 1500.00, 'Retrait mensuel'),
('CPT003', 'DEPOT', 200.00, 'Remboursement'),
('CPT003', 'RETRAIT', 199.50, 'Frais bancaires'),

-- Opérations pour CPT004
('CPT004', 'DEPOT', 10000.00, 'Dépôt initial'),
('CPT004', 'DEPOT', 2000.00, 'Prime annuelle'),

-- Opérations pour CPT005
('CPT005', 'DEPOT', 25000.00, 'Placement épargne'),

-- Opérations pour CPT006
('CPT006', 'DEPOT', 8000.00, 'Dépôt initial'),
('CPT006', 'DEPOT', 1000.00, 'Virement'),
('CPT006', 'RETRAIT', 500.00, 'Retrait'),
('CPT006', 'DEPOT', 0.75, 'Intérêts'),

-- Opérations pour CPT007
('CPT007', 'DEPOT', 3000.00, 'Dépôt initial'),
('CPT007', 'RETRAIT', 1000.00, 'Retrait mensuel');

COMMIT;

-- Afficher les résultats
SELECT 'Clients créés:' AS Info;
SELECT * FROM Client;

SELECT 'Comptes créés:' AS Info;
SELECT * FROM Compte;

SELECT 'Opérations créées:' AS Info;
SELECT * FROM Operation;

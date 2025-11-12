package com.example.cbclient;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Client;
import model.Compte;
import model.Operation;
import service.GestionCompteService;

import java.math.BigDecimal;

/**
 * Contrôleur pour l'interface graphique FXML
 */
public class BanqueGUIController {

    @FXML private ComboBox<Client> clientCombo;
    @FXML private ComboBox<Compte> compteCombo;
    @FXML private Label soldeLabel;
    @FXML private TextArea historiqueArea;
    @FXML private TextField montantField;
    @FXML private Button btnDepot;
    @FXML private Button btnRetrait;
    @FXML private Button btnVirement;

    private GestionCompteService service;
    private String utilisateurConnecte;

    /**
     * Définir l'utilisateur connecté
     */
    public void setUtilisateurConnecte(String identifiant) {
        this.utilisateurConnecte = identifiant;
        System.out.println("Utilisateur connecté : " + identifiant);
    }

    @FXML
    public void initialize() {
        service = new GestionCompteService();
        // Les données sont automatiquement chargées depuis la BD par GestionCompteService

        // Configurer les événements
        clientCombo.setOnAction(e -> chargerComptesClient());
        compteCombo.setOnAction(e -> afficherInfosCompte());

        // Charger les clients depuis la base de données
        clientCombo.getItems().addAll(service.getClients());
        
        // Afficher un message si aucun client n'est chargé
        if (service.getClients().isEmpty()) {
            System.out.println("⚠️ Aucun client chargé depuis la base de données");
            afficherErreur("Aucune donnée trouvée dans la base de données.\nVérifiez la connexion MySQL.");
        } else {
            System.out.println("✅ " + service.getClients().size() + " clients chargés depuis la BD");
            System.out.println("✅ " + service.getTousLesComptes().size() + " comptes chargés depuis la BD");
        }
    }

    private void chargerComptesClient() {
        Client client = clientCombo.getValue();
        compteCombo.getItems().clear();

        if (client != null) {
            compteCombo.getItems().addAll(client.getComptes());
            if (!client.getComptes().isEmpty()) {
                compteCombo.setValue(client.getComptes().get(0));
            }
        }
    }

    private void afficherInfosCompte() {
        Compte compte = compteCombo.getValue();
        if (compte != null) {
            soldeLabel.setText(String.format("Solde: %.2f€", compte.getSolde()));
            afficherHistorique();
        }
    }

    private void afficherHistorique() {
        Compte compte = compteCombo.getValue();
        if (compte == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("═══════════════════════════════════════════════════\n");
        sb.append(String.format(" Compte: %s\n", compte.getNumeroCompte()));
        sb.append("═══════════════════════════════════════════════════\n\n");

        for (Operation op : compte.getOperations()) {
            sb.append(op.toString()).append("\n");
        }

        if (compte.getOperations().isEmpty()) {
            sb.append("Aucune opération enregistrée.\n");
        }

        historiqueArea.setText(sb.toString());
    }

    @FXML
    private void effectuerDepot() {
        try {
            Compte compte = compteCombo.getValue();
            if (compte == null) {
                afficherErreur("Veuillez sélectionner un compte");
                return;
            }

            BigDecimal montant = new BigDecimal(montantField.getText());
            service.effectuerDepot(compte, montant);

            afficherInfosCompte();
            montantField.clear();
            afficherSucces("Dépôt de " + montant + "€ effectué avec succès!");

        } catch (NumberFormatException e) {
            afficherErreur("Montant invalide");
        } catch (Exception e) {
            afficherErreur(e.getMessage());
        }
    }

    @FXML
    private void effectuerRetrait() {
        try {
            Compte compte = compteCombo.getValue();
            if (compte == null) {
                afficherErreur("Veuillez sélectionner un compte");
                return;
            }

            BigDecimal montant = new BigDecimal(montantField.getText());
            service.effectuerRetrait(compte, montant);

            afficherInfosCompte();
            montantField.clear();
            afficherSucces("Retrait de " + montant + "€ effectué avec succès!");

        } catch (NumberFormatException e) {
            afficherErreur("Montant invalide");
        } catch (Exception e) {
            afficherErreur(e.getMessage());
        }
    }

    @FXML
    private void effectuerVirement() {
        try {
            Compte compteSource = compteCombo.getValue();
            if (compteSource == null) {
                afficherErreur("Veuillez sélectionner un compte source");
                return;
            }

            // Dialog pour sélectionner le compte destination
            Dialog<Compte> dialog = new Dialog<>();
            dialog.setTitle("Virement");
            dialog.setHeaderText("Sélectionnez le compte destination");

            ComboBox<Compte> destCombo = new ComboBox<>();
            destCombo.getItems().addAll(service.getTousLesComptes());
            destCombo.getItems().remove(compteSource);

            dialog.getDialogPane().setContent(destCombo);
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            dialog.setResultConverter(buttonType -> {
                if (buttonType == ButtonType.OK) {
                    return destCombo.getValue();
                }
                return null;
            });

            dialog.showAndWait().ifPresent(compteDest -> {
                try {
                    BigDecimal montant = new BigDecimal(montantField.getText());
                    service.effectuerVirement(compteSource, compteDest, montant);

                    afficherInfosCompte();
                    montantField.clear();
                    afficherSucces("Virement de " + montant + "€ effectué vers " +
                            compteDest.getNumeroCompte());
                } catch (Exception e) {
                    afficherErreur(e.getMessage());
                }
            });

        } catch (NumberFormatException e) {
            afficherErreur("Montant invalide");
        }
    }

    private void afficherSucces(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void afficherErreur(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erreur");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

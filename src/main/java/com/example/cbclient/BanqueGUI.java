package ui;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Client;
import model.Compte;
import model.Operation;
import service.GestionCompteService;

import java.math.BigDecimal;

/**
 * Interface graphique de l'application bancaire
 */
public class BanqueGUI extends Application {

    private GestionCompteService service;
    private ComboBox<Client> clientCombo;
    private ComboBox<Compte> compteCombo;
    private Label soldeLabel;
    private TextArea historiqueArea;
    private TextField montantField;

    @Override
    public void start(Stage primaryStage) {
        service = new GestionCompteService();
        initialiserDonnees();

        primaryStage.setTitle("🏦 Gestion Bancaire");

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));

        // Partie supérieure - Sélection client/compte
        VBox topBox = creerPanneauSelection();
        root.setTop(topBox);

        // Centre - Opérations
        VBox centerBox = creerPanneauOperations();
        root.setCenter(centerBox);

        // Bas - Historique
        VBox bottomBox = creerPanneauHistorique();
        root.setBottom(bottomBox);

        Scene scene = new Scene(root, 800, 700);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox creerPanneauSelection() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.setStyle("-fx-background-color: #f0f0f0; -fx-background-radius: 5;");

        Label titre = new Label("Sélection Client & Compte");
        titre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        HBox selectionBox = new HBox(15);
        selectionBox.setAlignment(Pos.CENTER_LEFT);

        // ComboBox Client
        Label clientLabel = new Label("Client:");
        clientCombo = new ComboBox<>();
        clientCombo.getItems().addAll(service.getClients());
        clientCombo.setOnAction(e -> chargerComptesClient());
        clientCombo.setPrefWidth(200);

        // ComboBox Compte
        Label compteLabel = new Label("Compte:");
        compteCombo = new ComboBox<>();
        compteCombo.setOnAction(e -> afficherInfosCompte());
        compteCombo.setPrefWidth(200);

        // Label Solde
        soldeLabel = new Label("Solde: -- €");
        soldeLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2196F3;");

        selectionBox.getChildren().addAll(
                clientLabel, clientCombo,
                compteLabel, compteCombo,
                soldeLabel
        );

        vbox.getChildren().addAll(titre, selectionBox);
        return vbox;
    }

    private VBox creerPanneauOperations() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));

        Label titre = new Label("Opérations Bancaires");
        titre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Champ montant
        HBox montantBox = new HBox(10);
        montantBox.setAlignment(Pos.CENTER_LEFT);
        Label montantLabel = new Label("Montant (€):");
        montantField = new TextField();
        montantField.setPromptText("0.00");
        montantField.setPrefWidth(150);
        montantBox.getChildren().addAll(montantLabel, montantField);

        // Boutons d'opérations
        HBox boutonsBox = new HBox(10);
        boutonsBox.setAlignment(Pos.CENTER);

        Button btnDepot = new Button("💰 Dépôt");
        btnDepot.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14px;");
        btnDepot.setPrefWidth(120);
        btnDepot.setOnAction(e -> effectuerDepot());

        Button btnRetrait = new Button("💸 Retrait");
        btnRetrait.setStyle("-fx-background-color: #FF9800; -fx-text-fill: white; -fx-font-size: 14px;");
        btnRetrait.setPrefWidth(120);
        btnRetrait.setOnAction(e -> effectuerRetrait());

        Button btnVirement = new Button("🔄 Virement");
        btnVirement.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white; -fx-font-size: 14px;");
        btnVirement.setPrefWidth(120);
        btnVirement.setOnAction(e -> effectuerVirement());

        boutonsBox.getChildren().addAll(btnDepot, btnRetrait, btnVirement);

        vbox.getChildren().addAll(titre, montantBox, boutonsBox);
        return vbox;
    }

    private VBox creerPanneauHistorique() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));

        Label titre = new Label("Historique des Opérations");
        titre.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        historiqueArea = new TextArea();
        historiqueArea.setEditable(false);
        historiqueArea.setPrefHeight(250);
        historiqueArea.setStyle("-fx-font-family: 'Courier New'; -fx-font-size: 12px;");

        vbox.getChildren().addAll(titre, historiqueArea);
        return vbox;
    }

    private void initialiserDonnees() {
        // Créer des clients de démonstration
        Client alice = service.creerClient("Dupont", "Alice");
        Client bob = service.creerClient("Martin", "Bob");
        Client charlie = service.creerClient("Dubois", "Charlie");

        // Créer des comptes
        service.creerCompte(alice, new BigDecimal("1000.00"));
        service.creerCompte(alice, new BigDecimal("500.00"));
        service.creerCompte(bob, new BigDecimal("2500.00"));
        service.creerCompte(charlie, new BigDecimal("750.00"));
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

    public static void main(String[] args) {
        launch(args);
    }
}
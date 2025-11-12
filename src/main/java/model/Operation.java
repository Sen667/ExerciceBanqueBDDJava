package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Classe représentant une opération bancaire
 */
public class Operation {
    private static int compteurId = 1;

    private int id;
    private LocalDateTime date;
    private TypeOperation type;
    private BigDecimal montant;
    private String compteSource;
    private String compteDestination;
    private String description;

    public Operation(TypeOperation type, BigDecimal montant, String compteSource) {
        this(type, montant, compteSource, null);
    }

    public Operation(TypeOperation type, BigDecimal montant, String compteSource, String compteDestination) {
        this.id = compteurId++;
        this.date = LocalDateTime.now();
        this.type = type;
        this.montant = montant;
        this.compteSource = compteSource;
        this.compteDestination = compteDestination;
        this.description = genererDescription();
    }

    private String genererDescription() {
        switch (type) {
            case DEPOT:
                return "Dépôt de " + montant + "€";
            case RETRAIT:
                return "Retrait de " + montant + "€";
            case VIREMENT:
                return "Virement de " + montant + "€ vers " + compteDestination;
            default:
                return "Opération";
        }
    }

    // Getters
    public int getId() { return id; }
    public LocalDateTime getDate() { return date; }
    public TypeOperation getType() { return type; }
    public BigDecimal getMontant() { return montant; }
    public String getCompteSource() { return compteSource; }
    public String getCompteDestination() { return compteDestination; }
    public String getDescription() { return description; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return String.format("[%s] %s - %s - %.2f€",
                date.format(formatter),
                type.getLibelle(),
                description,
                montant);
    }
}
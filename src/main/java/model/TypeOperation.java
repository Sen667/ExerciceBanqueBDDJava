package model;

/**
 * Énumération des types d'opérations bancaires
 */
public enum TypeOperation {
    DEPOT("Dépôt"),
    RETRAIT("Retrait"),
    VIREMENT("Virement");

    private final String libelle;

    TypeOperation(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    @Override
    public String toString() {
        return libelle;
    }
}
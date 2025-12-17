package fr.insa.beuvron.vaadin.projets.tournoi.model;

public enum SexeEnum {
    MASCULIN("M", "Masculin"),
    FEMININ("F", "Féminin"),
    NON_SPECIFIE(null, "Non spécifié");

    private final String code;
    private final String nom;

    SexeEnum(String code, String nom) {
        this.code = code;
        this.nom = nom;
    }

    public static SexeEnum getByCode(String code) {
        for (SexeEnum sexe : SexeEnum.values()) {
            if (sexe.getCode() != null && sexe.getCode().equals(code)) {
                return sexe;
            }
        }
        return NON_SPECIFIE;
    }

    public String getCode() {
        return code;
    }

    public String getNom() {
        return nom;
    }

}

package fr.insa.beuvron.vaadin.projets.tournoi.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public enum RoleEnum {
    ROLE_ADMIN(1, "administrateur", "Rôle donnant tous les droits sur l'application", 1),
    ROLE_CREATEUR(2, "createur", "Rôle permettant de créer de nouveaux tournois", 1),
    ROLE_JOUEUR(3, "joueur", "Rôle de base de toute personne connectée sur le site", 1),
    ROLE_GESTIONNAIRE(4, "gestionnaire", "Rôle permettant de gérer un tournoi", 2),
    ROLE_ARBITRE(5, "arbitre", "Rôle permettant de gérer les rondes et les matchs d'un tournoi", 2);

    private int id;
    private String nom;
    private String description;
    /**
     * portée du rôle.
     * <ul>
     * <li>1 pour les rôles généraux (administrateur,createur,joueur)</li>
     * <li>2 pour les rôles associés à un tournoi (gestionnaire,arbitre)</li>
     * </ul>
     */
    private int portee;

    private RoleEnum(int id, String nom, String description, int portee) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.portee = portee;
    }

    public List<RoleEnum> getAllRoles() {
        return List.of(RoleEnum.values());
    }

    public List<RoleEnum> getAllGeneralRoles() {
        return Arrays.stream(RoleEnum.values())
                .filter(role -> role.getPortee() == 1)
                .toList();
    }

    public static RoleEnum getById(int id) {
        for (RoleEnum role : RoleEnum.values()) {
            if (role.getId() == id) {
                return role;
            }
        }
        return null; // ou lever une exception si l'id n'est pas trouvé
    }

    protected void saveInDB(Connection con) throws SQLException {
        PreparedStatement insert = con.prepareStatement(
                "insert into role (id,nom,description,portee) values (?,?,?,?)");
        insert.setInt(1, this.getId());
        insert.setString(2, this.getNom());
        insert.setString(3, this.getDescription());
        insert.setInt(4, this.getPortee());
        insert.executeUpdate();
    }

        /**
     * On ne va conserver que l'identificateur du rôle dans la session active.
     * C'est pourquoi on a des méthodes statiques avec en paramètre l'identificateur.
     */
    public static boolean isAdmin(int idRole) {
        return idRole == ROLE_ADMIN.id;
    }

    /**
     * On ne va conserver que l'identificateur du rôle dans la session active.
     * C'est pourquoi on a des méthodes statiques avec en paramètre l'identificateur.
     */
    public static boolean isCreateur(int idRole) {
        return isAdmin(idRole) || idRole == ROLE_CREATEUR.id;
    }


    public int getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getDescription() {
        return description;
    }

    public int getPortee() {
        return portee;
    }

}

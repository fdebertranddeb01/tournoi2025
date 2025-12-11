/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.beuvron.vaadin.projets.tournoi.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Les rôles dans le logiciel.
 * Ce pourrait être une énumération puisqu'il n'est pas possible de créer de
 * nouveaux roles sans modifier le programme.
 * Néanmoins, nous restons avec des classes dans model très proche de l'implantation
 * sous forme de tables relationnelles.
 * Les ids de cette classe/table sont explicitement gérés puisque les tests
 * portent directement sur les identificateurs :
 * <ul>
 *   <li> rôles globaux : rôles ayant un sens global sur l'application
 *   <ul>
 *     <li> 1 : administrateur : rôle donnant tout pouvoir sur l'application </li>
 *     <li> 2 : createur : rôle permettant de créer un nouveau tournoi.
 *          Le créateur du tournoi devient le premier gestionnaire du tournoi.
 *     </li>
 *     <li> 3 : joueur : le rôle de base de toute personne connectée sur le site </li>
 *   </ul>
 *   </li>
 *   <li> rôles associés à un tournoi
 *   <ul>
 *     <li> 4 : gestionnaire : permet de fixer tous les paramètres du tournoi </li>
 *     <li> 5 : arbitre : permet de gérer les rondes et les matchs
 *   </ul>
 *   </li>
 * </ul>
 * Les rôles sont hiérarchisés : 
 * {@code administrateur > createur > joueur} : un administrateur, en plus de ses
 * attributions spécifiques a tous les droit d'un créateur qui a lui-même tous les droits
 * d'un joueur.
 * {@code administrateur > gestionnaire > arbitre} : un administrateur est
 * gestionnaire de tous les tournois. Un gestionnaire est également un arbitre du tournoi.
 * @author fdebertranddeb01
 */
public class Role {

    public static final int ID_ROLE_ADMIN = 1;
    public static final int ID_ROLE_CREATEUR = 2;
    public static final int ID_ROLE_JOUEUR = 3;
    public static final int ID_ROLE_GESTIONNAIRE = 4;
    public static final int ID_ROLE_ARBITRE = 5;

    public static final Role ROLE_ADMIN = new Role(ID_ROLE_ADMIN, "administrateur", "Rôle donnant tous les droits sur l'application",1);
    public static final Role ROLE_CREATEUR = new Role(ID_ROLE_CREATEUR, "createur", "Rôle permettant de créer de nouveaux tournois",1);
    public static final Role ROLE_JOUEUR = new Role(ID_ROLE_JOUEUR, "joueur", "Rôle de base de toute personne connectée sur le site",1);
    public static final Role ROLE_GESTIONNAIRE = new Role(ID_ROLE_GESTIONNAIRE, "gestionnaire", "Rôle permettant de gérer un tournoi",2);
    public static final Role ROLE_ARBITRE = new Role(ID_ROLE_ARBITRE, "arbitre", "Rôle permettant de gérer les rondes et les matchs d'un tournoi",2);

    public static final List<Role> ALL_ROLES = List.of(
        ROLE_ADMIN,
        ROLE_CREATEUR,
        ROLE_JOUEUR,
        ROLE_GESTIONNAIRE,
        ROLE_ARBITRE
    );
    
    private int id;
    private String nom;
    private String description;
    /** portée du rôle.
     * <ul>
     *   <li> 1 pour les rôles généraux (administrateur,createur,joueur) </li>
     *   <li> 2 pour les rôles associés à un tournoi (gestionnaire,arbitre) </li>
     * </ul>
     */
    private int portee;

    public Role(int id, String nom, String description,int portee) {
        this.id = id;
        this.nom = nom;
        this.description = description;
        this.portee = portee;
    }

    @Override
    public String toString() {
        return "Role{" + "id=" + getId() + ", nom=" + getNom() + ", portee=" + getPortee() + '}';
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
        return idRole == ID_ROLE_ADMIN;
    }

    /**
     * On ne va conserver que l'identificateur du rôle dans la session active.
     * C'est pourquoi on a des méthodes statiques avec en paramètre l'identificateur.
     */
    public static boolean isCreateur(int idRole) {
        return isAdmin(idRole) || idRole == ID_ROLE_CREATEUR;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return the portee
     */
    public int getPortee() {
        return portee;
    }
    
    
    
}

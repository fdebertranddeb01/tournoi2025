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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import fr.insa.beuvron.utils.database.ClasseMiroir;

/**
 * Une petite classe "miroir" de la table des joueurs.
 * 
 * @author fdebertranddeb01
 */
public class Joueur extends ClasseMiroir {

    private String surnom;
    private String pass;
    private int idRole;
    private String sexe;
    private Date dateNaissance;
    private byte[] photo;
    private String photoType;

    @Override
    public String toString() {
        return "Joueur{" + getSurnom() + " (sexe : " + sexe
                + ") " + dateNaissance + " (idRole=" + getIdRole() + ")}";
    }

    public Joueur(int id, String surnom, String pass, String sexe, Date dateNaissance, int idRole, byte[] photo,
            String photoType) {
        super(id);
        this.surnom = surnom;
        this.pass = pass;
        this.sexe = sexe;
        this.dateNaissance = dateNaissance;
        this.idRole = idRole;
        this.photo = photo;
        this.photoType = photoType;
    }

    public Joueur(String surnom, String pass, String sexe, Date dateNaissance, int idRole, byte[] photo,
            String photoType) {
        this(-1, surnom, pass, sexe, dateNaissance, idRole, photo, photoType);
    }

    public static Joueur nouveauJoueur() {
        return new Joueur(-1, "", "", null,null, Role.ID_ROLE_JOUEUR, null, null);
    }

    @Override
    protected PreparedStatement saveSansId(Connection con) throws SQLException {
        PreparedStatement insert = con.prepareStatement(
                "insert into joueur ("+ allFiedsNotId() +") values (?,?,?,?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS);
                int i = 1;
        insert.setString(i++, this.getSurnom());
        insert.setString(i++, this.getPass());
        insert.setString(i++, this.sexe);
        insert.setDate(i++, this.dateNaissance);
        insert.setInt(i++, this.getIdRole());
        insert.setBlob(i++, this.photo == null ? null : new java.io.ByteArrayInputStream(this.photo));
        insert.setString(i++, this.photoType);
        return insert;
    }

    private static String allFiedsNotId() {
        return "surnom, pass,sexe,datenaissance, idrole, photo, phototype";
    }

    private static String allFieds() {
        return "id," + allFiedsNotId();
    }

    private static Joueur fromCurLine(ResultSet rs) throws SQLException {
        return new Joueur(
                rs.getInt("id"),
                rs.getString("surnom"),
                rs.getString("pass"),
                rs.getString("sexe"),
                rs.getDate("datenaissance"),
                rs.getInt("idrole"),
                rs.getBytes("photo"),
                rs.getString("phototype"));
    }

    /** retrouve un joueur avec son identificateur */
    public static Optional<Joueur> getById(Connection con, int idJoueur) throws SQLException {
        try (PreparedStatement req = con.prepareStatement(
                "select " + allFieds() + " from joueur where id=?")) {
            req.setInt(1, idJoueur);
            ResultSet res = req.executeQuery();
            if (res.next()) {
                return Optional.of(fromCurLine(res));
            } else {
                return Optional.empty();
            }
        }
    }

    /** crée une liste de tous les joueurs */
    public static List<Joueur> getAllJoueurs(Connection con) throws SQLException {
        List<Joueur> resList = new ArrayList<>();
        try (PreparedStatement req = con
                .prepareStatement("select " + allFieds() + " from joueur")) {
            ResultSet res = req.executeQuery();
            while (res.next()) {
                resList.add(fromCurLine(res));
            }
        }
        return resList;
    }

    /**
     * @return the surnom
     */
    public String getSurnom() {
        return surnom;
    }

    /**
     * @param surnom the surnom to set
     */
    public void setSurnom(String surnom) {
        this.surnom = surnom;
    }

    /**
     * @return the pass
     */
    public String getPass() {
        return pass;
    }

    /**
     * @param pass the pass to set
     */
    public void setPass(String pass) {
        this.pass = pass;
    }

    /**
     * @return the sexe
     */
    public String getSexe() {
        return sexe;
    }

    /**
     * @param sexe the sexe to set
     */
    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    /**
     * @return the dateNaissance
     */
    public Date getDateNaissance() {
        return dateNaissance;
    }

    /**
     * @param dateNaissance the dateNaissance to set
     */
    public void setDateNaissance(Date dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    /**
     * @return the idRole
     */
    public int getIdRole() {
        return idRole;
    }

    /**
     * @param idRole the idRole to set
     */
    public void setIdRole(int idRole) {
        this.idRole = idRole;
    }

    /**
     * @return the photo
     */
    public byte[] getPhoto() {
        return photo;
    }

    public void setPhoto(byte[] photo) {
        this.photo = photo;
    }

    /**
     * @return the photoType
     */
    public String getPhotoType() {
        return photoType;
    }

    public void setPhotoType(String photoType) {
        this.photoType = photoType;
    }

}

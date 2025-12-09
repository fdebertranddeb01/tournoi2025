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
    private byte[] photo;
    private String photoType;

    @Override
    public String toString() {
        return "Joueur{" + "surnom=" + getSurnom() + ", idRole=" + getIdRole() + '}';
    }

    public Joueur(String surnom, String pass, int idRole, byte[] photo, String photoType) {
        this.surnom = surnom;
        this.pass = pass;
        this.idRole = idRole;
        this.photo = photo;
        this.photoType = photoType;
    }

    @Override
    protected PreparedStatement saveSansId(Connection con) throws SQLException {
        PreparedStatement insert = con.prepareStatement(
                "insert into joueur (surnom,pass,idrole,photo,phototype) values (?,?,?,?,?)",
                PreparedStatement.RETURN_GENERATED_KEYS);
        insert.setString(1, this.getSurnom());
        insert.setString(2, this.getPass());
        insert.setInt(3, this.getIdRole());
        insert.setBlob(4, this.photo == null ? null : new java.io.ByteArrayInputStream(this.photo));
        insert.setString(5, this.photoType);
        return insert;
    }

    private static String allFieds() {
        return "surnom, pass, idrole, photo, phototype";
    }

    private static Joueur fromCurLine(ResultSet rs) throws SQLException {
        return new Joueur(
                rs.getString("surnom"),
                rs.getString("pass"),
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

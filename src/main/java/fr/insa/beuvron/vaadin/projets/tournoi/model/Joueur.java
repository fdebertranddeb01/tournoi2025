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

import fr.insa.beuvron.utils.database.ClasseMiroir;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Une petite classe "miroir" de la table des joueurs. 
 * @author fdebertranddeb01
 */
public class Joueur extends ClasseMiroir{

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

    /** retrouve un joueur avec son identificateur */
    // public static Joueur getById(Connection con,int idJoueur) throws SQLException {
    //     try (PreparedStatement req = con.prepareStatement(
    //             "select surnom, pass, idrole from joueur where id=?");
    //     req.setInt(1, idJoueur);
    //     return ClasseMiroir.getOneFromPreparedRequest(
    //             req,
    //             (rs) -> new Joueur(
    //                     rs.getString("surnom"),
    //                     rs.getString("pass"),
    //                     rs.getInt("idrole"),
    //                     null,
    //                     null
    //             )
    //     );
    // }


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
    
}

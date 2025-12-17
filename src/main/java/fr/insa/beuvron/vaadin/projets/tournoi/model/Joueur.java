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
import java.util.Random;

import fr.insa.beuvron.utils.database.ClasseMiroir;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.SmallImage;

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

    /**
     * crée un nouveau joueur vide (id = -1, champs vides ou par défaut).
     * 
     * @return
     */
    public static Joueur nouveauJoueur() {
        return new Joueur(-1, "", "", null, null, Role.ID_ROLE_JOUEUR, null, null);
    }

    /**
     * crée une liste de joueurs aléatoires.
     * 
     * @param nbr               le nombre de joueurs à créer
     * @param debutNumerotation le numéro à utiliser pour le premier joueur
     * @param prefixSurnom      le préfixe des surnoms
     * @param prefixPass        le préfixe des mots de passe
     * @param minAge            age minimum des joueurs
     * @param maxAge            age maximum des joueurs
     * @param probaHomme        la probabilité qu'un joueur soit de sexe masculin
     *                          (0.0 à 1.0)
     *                          (1 - probaHomme - probaFemme = probabilité sexe
     *                          indéfini)
     * @param probaFemme        la probabilité qu'un joueur soit de sexe féminin
     *                          (0.0 à 1.0)
     *                          (1 - probaHomme - probaFemme = probabilité sexe
     *                          indéfini)
     * @param probaContent      la probabilité qu'un joueur ait une photo "content"
     *                          (1 - probaContent - probaPasContent = probabilité
     *                          pas de photo)
     * @param probaPasContent   la probabilité qu'un joueur ait une photo "pas
     *                          content"
     *                          (1 - probaContent - probaPasContent = probabilité
     *                          pas de photo)
     * @param rand              le générateur de nombres aléatoires à utiliser
     * @return
     */
    public static List<Joueur> joueursAlea(int nbr, int debutNumerotation, String prefixSurnom, String prefixPass,
            double probaSansDateNais, int minAge, int maxAge, double probaHomme, double probaFemme,
            double probaContent, double probaPasContent, Random rand) {
        List<Joueur> res = new ArrayList<>();
         // compte le nombre de digits nécessaires pour le numéro le plus élevé
        int maxNumero = (nbr > 0) ? (debutNumerotation + nbr - 1) : debutNumerotation;
        int maxAbs = Math.abs(maxNumero);
        int nbDigits = (maxAbs == 0) ? 1 : (int) Math.floor(Math.log10(maxAbs)) + 1;
        String numeroFormat = "%0" + nbDigits + "d";
        for (int i = 0; i < nbr; i++) {
            String surnom = prefixSurnom + String.format(numeroFormat,debutNumerotation + i);
            String pass = prefixPass + String.format(numeroFormat,debutNumerotation + i);
            double pDateNais = rand.nextDouble();
            Date dateNaissance = null;
            if (pDateNais >= probaSansDateNais) {
                java.time.LocalDate today = java.time.LocalDate.now();
                java.time.LocalDate earliest = today.minusYears(maxAge);
                java.time.LocalDate latest = today.minusYears(minAge);
                long daysBetween = java.time.temporal.ChronoUnit.DAYS.between(earliest, latest);
                long offset = daysBetween > 0 ? rand.nextLong(daysBetween + 1) : 0;
                dateNaissance = Date.valueOf(earliest.plusDays(offset));
            }
            int roleID = Role.ID_ROLE_JOUEUR;
            double p = rand.nextDouble();
            String sexe = null;
            if (p < probaHomme) {
                sexe = "H";
            } else if (p < probaHomme + probaFemme) {
                sexe = "F";
            } else {
                sexe = null;
            }
            double pImage = rand.nextDouble();
            byte[] photo;
            String photoType;
            if (pImage < probaContent) {
                photo = SmallImage.PETIT_SMILEY_CONTENT_PNG.getImageData();
                photoType = SmallImage.PETIT_SMILEY_CONTENT_PNG.getImageType();
            } else if (pImage < probaContent + probaPasContent) {
                photo = SmallImage.PETIT_SMILEY_PAS_CONTENT_PNG.getImageData();
                photoType = SmallImage.PETIT_SMILEY_PAS_CONTENT_PNG.getImageType();
            } else {
                photo = null;
                photoType = null;
            }
            res.add(new Joueur(surnom, pass, sexe, dateNaissance, roleID, photo, photoType));
        }
        return res;
    }

    @Override
    protected PreparedStatement saveSansId(Connection con) throws SQLException {
        PreparedStatement insert = con.prepareStatement(
                "insert into joueur (" + allFiedsNotId() + ") values (?,?,?,?,?,?,?)",
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

    /**
     * met à jour ce joueur dans la base de données (en utilisant son ID).
     */
    public void updateSaufID(Connection con) throws SQLException {
        try (PreparedStatement update = con.prepareStatement(
                "update joueur set "
                        + "surnom=?, pass=?, sexe=?, datenaissance=?, idrole=?, photo=?, phototype=? "
                        + "where id=?")) {
            int i = 1;
            update.setString(i++, this.getSurnom());
            update.setString(i++, this.getPass());
            update.setString(i++, this.sexe);
            update.setDate(i++, this.dateNaissance);
            update.setInt(i++, this.getIdRole());
            update.setBlob(i++, this.photo == null ? null : new java.io.ByteArrayInputStream(this.photo));
            update.setString(i++, this.photoType);
            update.setInt(i++, this.getId());
            update.executeUpdate();
        }
    }

    public void delete(Connection con) throws SQLException {
        try (PreparedStatement delete = con.prepareStatement(
                "delete from joueur where id=?")) {
            delete.setInt(1, this.getId());
            delete.executeUpdate();
        }
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

    /**
     * cherche un joueur par son ID.
     * 
     * @param con      la connexion à utiliser
     * @param idJoueur l'ID du joueur à chercher
     * @return un Optional contenant le joueur si trouvé, ou vide sinon
     * @throws SQLException en cas de problème lors de la requête
     */
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

    /**
     * récupère tous les joueurs de la base de données.
     * 
     * @param con la connexion à utiliser
     * @return la liste des joueurs
     * @throws SQLException en cas de problème lors de la requête
     */
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

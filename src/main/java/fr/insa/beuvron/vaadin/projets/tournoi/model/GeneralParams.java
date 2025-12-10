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
import java.sql.Statement;

import fr.insa.beuvron.utils.database.ConnectionSimpleSGBD;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.Application;

/**
 * Gestion des paramètres généraux de l'application.
 *
 * @author fdebertranddeb01
 */
public class GeneralParams {

  /** taille maximale des fichiers de photo que l'utilisateur peut uploader */
  public static int maxSizePhotoFileInKo = 1024;
  /**
   * la largeur des photos en pixel quand représentés dans une liste de joueurs.
   */
  public static int widthOfPhotoInJoueurList = 40;
  /**
   * la hauteur des photos en pixel quand représentés dans une liste de joueurs.
   */
  public static int heightOfPhotoInJoueurList = 60;
  /**
   * largeur des photos en pixel quand représentés dans un panneau joueur (ex: vue
   * détaillée)
   */
  public static int widthOfPhotoInJoueurPanel = 120;
  /**
   * hauteur des photos en pixel quand représentés dans un panneau joueur (ex: vue
   * détaillée)
   */
  public static int heightOfPhotoInJoueurPanel = 180;
  /**
   * taille (largeur) des photos sauvegardées dans la BdD. Les photos sont
   * automatiquement
   * redimensionnées à cette taille lors de l'upload.
   */
  public static int photoHeightInPixelInDB = 300;
  /**
   * taille (largeur) des photos sauvegardées dans la BdD. Les photos sont
   * automatiquement
   * redimensionnées à cette taille lors de l'upload.
   */
  public static int photoWidthInPixelInDB = 200;

  public static void creeTableParams(Connection con) throws SQLException {
    try (Statement st = con.createStatement()) {
      // creation des tables
      st.executeUpdate(
          "create table generalparams ( "
              + "maxsizephotofileinko integer not null,"
              // la taille des vignettes des photos des joueurs
              // quand is sont dans une liste
              // la chaîne de caractère doit être une mesure css valide
              + "widthofphotoinjoueurlist integer not null,"
              + "heightofphotoinjoueurlist integer not null,"
              + "widthofphotoinjoueurpanel integer not null,"
              + "heightofphotoinjoueurpanel integer not null,"
              + "photoheightinpixelindb integer not null,"
              + "photowidthinpixelindb integer not null"
              + ") ");
    }
  }

  public static void dropTableParams(Connection con) {
    try (Statement st = con.createStatement()) {
      // creation des tables
      st.executeUpdate(
          "drop table generalparams ");
    } catch (SQLException e) {
      // rien à faire, la table n'existait pas
    }
  }

  public static void loadGeneralParams(Connection con) throws SQLException {
    try (PreparedStatement pst = con.prepareStatement(
        "select maxsizephotofileinko,"
            + "widthofphotoinjoueurlist,heightofphotoinjoueurlist,"
            + "photoheightinpixelindb,photowidthinpixelindb,"
            + "widthofphotoinjoueurpanel,heightofphotoinjoueurpanel "
            + " from generalparams")) {
      ResultSet res = pst.executeQuery();
      res.next();
      maxSizePhotoFileInKo = res.getInt(1);
      widthOfPhotoInJoueurList = res.getInt(2);
      heightOfPhotoInJoueurList = res.getInt(3);
      photoHeightInPixelInDB = res.getInt(4);
      photoWidthInPixelInDB = res.getInt(5);
      widthOfPhotoInJoueurPanel = res.getInt(6);
      heightOfPhotoInJoueurPanel = res.getInt(7);
    }
  }

  public static void saveGeneralParams(Connection con) throws SQLException {
    try (PreparedStatement pst = con.prepareStatement(
        "update generalparams set "
            + "maxsizephotofileinko=?,"
            + "widthofphotoinjoueurlist=?,heightofphotoinjoueurlist=?,"
            + " photoheightinpixelindb=?,photowidthinpixelindb=?,"
            + "widthofphotoinjoueurpanel=?,heightofphotoinjoueurpanel=?")) {
      pst.setInt(1, maxSizePhotoFileInKo);
      pst.setInt(2, widthOfPhotoInJoueurList);
      pst.setInt(3, heightOfPhotoInJoueurList);
      pst.setInt(4, photoHeightInPixelInDB);
      pst.setInt(5, photoWidthInPixelInDB);
      pst.setInt(6, widthOfPhotoInJoueurPanel);
      pst.setInt(7, heightOfPhotoInJoueurPanel);
      pst.executeUpdate();
    }
  }

  public static void initDefaultGeneralParams(Connection con) throws SQLException {
    try (PreparedStatement pst = con.prepareStatement(
        "insert into generalparams (maxsizephotofileinko,"
            + "widthofphotoinjoueurlist,heightofphotoinjoueurlist,"
            + "photoheightinpixelindb,photowidthinpixelindb,"
            + "widthofphotoinjoueurpanel,heightofphotoinjoueurpanel) values (?,?,?,?,?,?,?)")) {
      pst.setInt(1, 1024);
      pst.setInt(2, 40);
      pst.setInt(3, 60);
      pst.setInt(4, 300);
      pst.setInt(5, 200);
      pst.setInt(6, 120);
      pst.setInt(7, 180);
      pst.executeUpdate();
    }
    // debug à supprimer
    if (Application.DEBUG) {
      maxSizePhotoFileInKo = 2048;
      saveGeneralParams(con);
      maxSizePhotoFileInKo = 512;
      loadGeneralParams(con);
      System.out.println("GeneralParams.initDefaultGeneralParams: "
          + " maxSizePhotoFileInKo=" + maxSizePhotoFileInKo);
    }
  }

}

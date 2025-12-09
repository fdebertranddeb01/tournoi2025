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

/**
 * Gestion des paramètres généraux de l'application.
 *
 * @author fdebertranddeb01
 */
public class GeneralParams {

  public static int maxSizePhotoInKo = 1024;
  public static String widthOfPhotoInJoueurList = "4em";
  public static String heightOfPhotoInJoueurList = "6em";
  /** Hauteur (en pixels) utilisée pour redimensionner les photos lors d'un upload/affichage */
  public static int resizePhotoHeightInPixel = 300;
  /** Largeur (en pixels) utilisée pour redimensionner les photos lors d'un upload/affichage */
  public static int resizePhotoWidthInPixel = 200;

  public static void loadGeneralParams(Connection con) throws SQLException {
    try (PreparedStatement pst = con.prepareStatement(
      "select maxsizephotoinko,widthofphotoinjoueurlist,heightofphotoinjoueurlist,resizephotoheightinpixel,resizephotowidthinpixel "
        + " from generalparams")) {
      ResultSet res = pst.executeQuery();
      res.next();
      maxSizePhotoInKo = res.getInt(1);
      widthOfPhotoInJoueurList = res.getString(2);
      heightOfPhotoInJoueurList = res.getString(3);
      resizePhotoHeightInPixel = res.getInt(4);
      resizePhotoWidthInPixel = res.getInt(5);
    }
  }

  public static void saveGeneralParams(Connection con) throws SQLException {
    try (PreparedStatement pst = con.prepareStatement(
      "update generalparams set maxsizephotoinko=?,widthofphotoinjoueurlist=?,"
        + "heightofphotoinjoueurlist=?,resizephotoheightinpixel=?,resizephotowidthinpixel=?")) {
      pst.setInt(1, maxSizePhotoInKo);
      pst.setString(2, widthOfPhotoInJoueurList);
      pst.setString(3, heightOfPhotoInJoueurList);
      pst.setInt(4, resizePhotoHeightInPixel);
      pst.setInt(5, resizePhotoWidthInPixel);
      pst.executeUpdate();
    }
  }

  public static void initDefaultGeneralParams(Connection con) throws SQLException {
    try (PreparedStatement pst = con.prepareStatement(
      "insert into generalparams (maxsizephotoinko,widthofphotoinjoueurlist,"
        + "heightofphotoinjoueurlist,resizephotoheightinpixel,resizephotowidthinpixel) values (?,?,?,?,?)")) {
      pst.setInt(1, 1024);
      pst.setString(2, "4em");
      pst.setString(3, "6em");
      pst.setInt(4, 300);
      pst.setInt(5, 200);
      pst.executeUpdate();
    }
  }

}

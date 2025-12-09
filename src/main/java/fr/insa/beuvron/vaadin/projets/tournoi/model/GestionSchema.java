/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is ecole of CoursBeuvron.

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

import fr.insa.beuvron.utils.database.ConnectionSimpleSGBD;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * @author francois
 */
public class GestionSchema {

  /**
   * @param con
   * @throws SQLException
   */
  public static void creeSchema(Connection con) throws SQLException {
    try {
      con.setAutoCommit(false);
      try (Statement st = con.createStatement()) {
        // creation des tables
        st.executeUpdate(
            "create table generalparams ( "
                + "maxsizephotoinko integer not null,"
                // la taille des vignettes des photos des joueurs
                // quand is sont dans une liste
                // la chaîne de caractère doit être une mesure css valide
                + "widthofphotoinjoueurlist varchar(20) not null,"
                + "heightofphotoinjoueurlist varchar(20) not null"
                + ") ");
        st.executeUpdate(
            "create table role ( "
                + "id integer primary key,"
                + " nom varchar(30) not null unique,"
                + " description text not null,"
                // portee : générale=1 ou tournoi=2
                + " portee integer not null"
                + ") ");
        st.executeUpdate(
            "create table joueur ( "
                + ConnectionSimpleSGBD.sqlForGeneratedKeys(con, "id")
                + ","
                + " surnom varchar(30) not null unique,"
                + " pass varchar(20) not null,"
                + " idrole integer not null,"
                + " photo blob,"
                + " phototype varchar(20)"
                + ") ");
        st.executeUpdate(
            "alter table joueur\n"
                + "  add constraint fk_joueur_idrole\n"
                + "  foreign key (idrole) references role(id)");
        con.commit();
      }
    } catch (SQLException ex) {
      con.rollback();
      throw ex;
    } finally {
      con.setAutoCommit(true);
    }
  }

  /**
   * @param con
   * @throws SQLException
   */
  public static void deleteSchema(Connection con) throws SQLException {
    try (Statement st = con.createStatement()) {
      try {
        st.executeUpdate("alter table joueur " + "drop constraint fk_joueur_idrole");
      } catch (SQLException ex) {
      }
      try {
        st.executeUpdate("drop table joueur");
      } catch (SQLException ex) {
      }
      try {
        st.executeUpdate("drop table role");
      } catch (SQLException ex) {
      }
      try {
        st.executeUpdate("drop table generalparams");
      } catch (SQLException ex) {
      }
    }
  }

  public static void createBdDVide(Connection con) throws SQLException {
    try (PreparedStatement pst =
        con.prepareStatement(
            "insert into generalparams ("
                + "maxsizephotoinko,"
                + "widthofphotoinjoueurlist,"
                + "heightofphotoinjoueurlist) values (?,?,?)")) {
      pst.setInt(1, 1024);
      pst.setString(2, "4em");
      pst.setString(3, "6em");
      pst.executeUpdate();
    }
    List<Role> roles =
        List.of(
            new Role(1, "admin", "tous les droits sur l'ensemble de l'application", 1),
            new Role(
                2,
                "createur",
                "droit de créer un nouveau tournoi ; devient gestionnaire de ce tournoi",
                1),
            new Role(3, "joueur", "rôle de base des utilisateurs connectés", 1));
    for (var r : roles) {
      r.saveInDB(con);
    }
    List<Joueur> joueurs =
        List.of(
            new Joueur(
                "admin",
                "admin",
                1,
                DefaultPhoto.getPhotoBytes(DefaultPhoto.PETIT_SMILEY_CONTENT_BASE64_PNG),
                DefaultPhoto.PNG_TYPE));
    for (var u : joueurs) {
      u.saveInDB(con);
    }
  }

  /**
   * @param con
   * @throws SQLException
   */
  public static void razBdd(Connection con) throws SQLException {
    deleteSchema(con);
    creeSchema(con);
    createBdDVide(con);
  }

  /**
   * @param args
   */
  public static void main(String[] args) {
    try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
      razBdd(con);
      System.out.println("RAZ BdD : OK");
    } catch (SQLException ex) {
      throw new Error(ex);
    }
  }
}

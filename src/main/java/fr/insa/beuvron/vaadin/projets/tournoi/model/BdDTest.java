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

import fr.insa.beuvron.utils.database.ConnectionSimpleSGBD;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.SmallImage;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @author francois
 */
public class BdDTest {

  public static void createBdDTest(Connection con) throws SQLException {
    SmallImage content = SmallImage.PETIT_SMILEY_CONTENT_PNG;
    List<Joueur> users =
        List.of(
            new Joueur("toto", "p1", 1, null, null),
            new Joueur("titi", "p2", 2, null, null),
            new Joueur("tutu", "p3", 3, null, null));
    for (var u : users) {
      u.saveInDB(con);
    }
  }

  public static void main(String[] args) {
    try (Connection con = ConnectionSimpleSGBD.defaultCon()) {
      GestionSchema.razBdd(con);
      System.out.println("RAZ BdD : OK");
      createBdDTest(con);
      System.out.println("Création BdD test : OK");
    } catch (SQLException ex) {
      throw new Error(ex);
    }
  }
}

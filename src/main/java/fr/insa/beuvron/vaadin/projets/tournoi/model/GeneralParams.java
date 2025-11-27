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
 * @author fdebertranddeb01
 */
public class GeneralParams {
    
    public static int maxSizePhotoInKo(Connection con) throws SQLException{
        try (PreparedStatement pst = con.prepareStatement(
                "select maxsizephotoinko from generalparams")) {
            ResultSet res = pst.executeQuery();
            res.next();
            return res.getInt(1);
        }
    }
    
    public static String widthOfPhotoInJoueurList(Connection con) throws SQLException{
        try (PreparedStatement pst = con.prepareStatement(
                "select widthofphotoinjoueurlist from generalparams")) {
            ResultSet res = pst.executeQuery();
            res.next();
            return res.getString(1);
        }
    }
            
    public static String heightOfPhotoInJoueurList(Connection con) throws SQLException{
        try (PreparedStatement pst = con.prepareStatement(
                "select heightofphotoinjoueurlist from generalparams")) {
            ResultSet res = pst.executeQuery();
            res.next();
            return res.getString(1);
        }
    }
            
    
}

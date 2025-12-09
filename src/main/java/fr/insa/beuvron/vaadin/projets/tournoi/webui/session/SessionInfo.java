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
package fr.insa.beuvron.vaadin.projets.tournoi.webui.session;

import com.vaadin.flow.server.VaadinSession;
import fr.insa.beuvron.vaadin.projets.tournoi.model.Role;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
/**
 * L'ensemble des informations associées à la session.
 * @author francois
 */
public class SessionInfo implements Serializable {

    /**
     * Classe représentant seulement une partie des caractéristiques d'un joueur
     * que l'on conserve comme information de session.
     */
    public static class ConnectedUser implements Serializable {

        private static final long serialVersionUID = 1L;

        private int id;
        private int role;
        private String surnom;

        public ConnectedUser(int id,String surnom, int role) {
            this.id = id;
            this.role = role;
            this.surnom = surnom;
        }

        public static Optional<ConnectedUser> fromLogin(Connection con, String surnom, String pass) throws SQLException {
            try (PreparedStatement pst = con.prepareStatement(
                    "select id,surnom,idrole from joueur where surnom = ? and pass = ?")) {
                pst.setString(1, surnom);
                pst.setString(2, pass);
                ResultSet res = pst.executeQuery();
                if (res.next()) {
                    return Optional.of(new ConnectedUser(res.getInt(1),res.getString(2), res.getInt(3)));
                } else {
                    return Optional.empty();
                }
            }
        }

        /**
         * @return the id
         */
        public int getId() {
            return id;
        }

        /**
         * @return the role
         */
        public int getRole() {
            return role;
        }

        public String getSurnom() {
            return surnom;
        }

    }

    private Optional<ConnectedUser> curUser;

    public SessionInfo() {
        this.curUser = Optional.empty();
    }
    
    public static SessionInfo getOrCreate() {
        VaadinSession curSession = VaadinSession.getCurrent();
        SessionInfo curInfo = curSession.getAttribute(SessionInfo.class);
        if (curInfo == null) {
            curInfo = new SessionInfo();
            curSession.setAttribute(SessionInfo.class, curInfo);
        }
        return curInfo;
    }

    public static boolean tryLogin(Connection con, String surnom, String pass) throws SQLException {
        SessionInfo curInfo = getOrCreate();
        curInfo.curUser = ConnectedUser.fromLogin(con, surnom, pass);
        return curInfo.curUser.isPresent();
    }

    public static void logout() {
        SessionInfo curInfo = getOrCreate();
        curInfo.curUser = Optional.empty();
    }
    
    public static String curSurnom() {
        SessionInfo curInfo = getOrCreate();
        if (curInfo.curUser.isPresent()) {
            return curInfo.curUser.get().getSurnom();
        } else {
            return "";
        }
        
    }
    
    public static boolean userConnected() {
        SessionInfo curInfo = getOrCreate();
        return curInfo.curUser.isPresent();
    }

    public static boolean createurConnected() {
        SessionInfo curInfo = getOrCreate();
        return curInfo.curUser.isPresent() && Role.isCreateur(curInfo.curUser.get().getRole());
    }

    public static boolean adminConnected() {
        SessionInfo curInfo = getOrCreate();
        return curInfo.curUser.isPresent() && Role.isAdmin(curInfo.curUser.get().getRole());
    }

}

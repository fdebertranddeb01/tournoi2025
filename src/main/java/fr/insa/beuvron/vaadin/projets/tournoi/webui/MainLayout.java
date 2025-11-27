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
package fr.insa.beuvron.vaadin.projets.tournoi.webui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.session.LoginEntete;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.session.LogoutEntete;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.session.SessionInfo;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.Utils;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author francois
 */
public class MainLayout extends AppLayout implements AfterNavigationObserver {

    // en mode debug, il est utile de se logger automatiquement comme admin
    // pour tester toutes les fonctionnalités
    // à mettre évidement à false quand debug fini
    public static final boolean AUTO_LOG_AS_ADMIN = true;

    private LoginEntete curLogin;
    private LogoutEntete curLogout;

    public MainLayout() {
        this.addToDrawer(new MainMenu());
        DrawerToggle toggle = new DrawerToggle();
        this.addToNavbar(toggle);
        if (SessionInfo.userConnected()) {
            this.curLogout = new LogoutEntete();
            this.addToNavbar(this.curLogout);
        } else if (AUTO_LOG_AS_ADMIN) {
            try (Connection con = ConnectionPool.getConnection()) {
                SessionInfo.tryLogin(con, "admin", "admin");
                this.curLogout = new LogoutEntete();
                this.addToNavbar(this.curLogout);
            } catch (SQLException ex) {
                Utils.outErrorAsNotification("Problème interne : " + ex.getLocalizedMessage());
                this.curLogin = new LoginEntete();
                this.addToNavbar(this.curLogin);
            }
        } else {
            this.curLogin = new LoginEntete();
            this.addToNavbar(this.curLogin);
        }
    }

    public void refresh() {
        if (this.curLogin != null) {
            this.remove(this.curLogin);
        }
        if (this.curLogout != null) {
            this.remove(this.curLogout);
        }
        if (SessionInfo.userConnected()) {
            this.curLogout = new LogoutEntete();
            this.addToNavbar(this.curLogout);
        } else {
            this.curLogin = new LoginEntete();
            this.addToNavbar(this.curLogin);
        }
    }

    @Override
    public void afterNavigation(AfterNavigationEvent ane) {
//        Location loc = ane.getLocation();
        this.refresh();
    }

}

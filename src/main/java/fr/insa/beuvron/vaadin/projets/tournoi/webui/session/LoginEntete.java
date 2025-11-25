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

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import fr.insa.beuvron.utils.database.ConnectionPool;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author francois
 */
public class LoginEntete extends FlexLayout {

    public TextField tfSurnom;
    public PasswordField pfPass;
    public Button bLogin;
    public Button bInscription;

    public LoginEntete() {
        this.setWidthFull();
        this.setAlignItems(Alignment.END);
        this.setFlexDirection(FlexDirection.ROW);
        this.setFlexWrap(FlexWrap.WRAP);
        this.getStyle().set("gap", "5px");
        this.tfSurnom = new TextField("surnom : ");
        this.pfPass = new PasswordField("pass : ");
        this.bLogin = new Button("se connecter");
        this.bLogin.addClickListener((t) -> {
            this.doLogin();
        });
        this.bInscription = new Button("s'inscrire");
        this.bInscription.addClickListener((t) -> {
            //TODO
//            UI.getCurrent().navigate(Inscription.class);
        });
        this.add(this.tfSurnom, this.pfPass, this.bLogin, this.bInscription);
    }

    public void doLogin() {
        String surnom = this.tfSurnom.getValue();
        String pass = this.pfPass.getValue();
        try (Connection con = ConnectionPool.getConnection()) {
            boolean ok = SessionInfo.tryLogin(con, surnom, pass);
            if (!ok) {
                Notification.show("Surnom ou pass incorrect");
            } else {
                UI.getCurrent().refreshCurrentRoute(true);
            }
        } catch (SQLException ex) {
            Notification.show("Problème " + ex.getLocalizedMessage());
        }
    }

}

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

import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;

import fr.insa.beuvron.vaadin.projets.tournoi.webui.admin.EditGeneralParams;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.joueur.InscriptionJoueur;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.joueur.ListeJoueur;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.tests.TestIntegerTextField;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.tests.TestSmallImageLoader;

/**
 * @author francois
 */
public class MainMenu extends SideNav {

  public MainMenu() {
    SideNavItem accueil = new SideNavItem("accueil", VuePrincipale.class);
    SideNavItem joueurs = new SideNavItem("joueurs");
    SideNavItem listeJoueurs = new SideNavItem("liste", ListeJoueur.class);
    SideNavItem inscriptionJoueurs = new SideNavItem("inscription", InscriptionJoueur.class);
    joueurs.addItem(listeJoueurs, inscriptionJoueurs);
    SideNavItem admin = new SideNavItem("admin");
    SideNavItem editParams = new SideNavItem("edit params", EditGeneralParams.class);
    admin.addItem(editParams);
    SideNavItem tests = new SideNavItem("tests");
    SideNavItem testupload = new SideNavItem("upload image", TestSmallImageLoader.class);
    SideNavItem testIField = new SideNavItem("Integer field", TestIntegerTextField.class);
    tests.addItem(testupload, testIField);
    this.addItem(accueil, joueurs,admin,tests);
  }
}

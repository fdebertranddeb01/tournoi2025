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
package fr.insa.beuvron.vaadin.projets.tournoi.webui.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.MainLayout;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.session.OnlyAdmin;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.MyIntegerField;

/**
 *
 * @author fdebertranddeb01
 */
@Route(value = "admin/edit",layout = MainLayout.class)
public class EditGeneralParams extends FormLayout implements OnlyAdmin {
    
    private MyIntegerField ifMaxSizePhotoInKo;
    private TextField tfWidthOfPhotoInJoueurList;
    private TextField tfHeightOfPhotoInJoueurList;
    private Button bSave;
    
    public EditGeneralParams() {
        this.add(new H3("taille max fichiers photo en Ko"));
        this.ifMaxSizePhotoInKo = new MyIntegerField();
        this.add(new H3("taille (largeur) d'affichage des vignettes photo"));
        this.add(new Paragraph("cette taille doit être exprimée comme en css"));
        this.add(new Paragraph("exemples : 200px , 5em , 3cm"));
        this.tfWidthOfPhotoInJoueurList = new TextField();
        this.add(new H3("taille (hauteur) d'affichage des vignettes photo"));
        this.add(new Paragraph("cette taille doit être exprimée comme en css"));
        this.add(new Paragraph("exemples : 200px , 5em , 3cm"));
        this.tfHeightOfPhotoInJoueurList = new TextField();
        
        
    }
    
}

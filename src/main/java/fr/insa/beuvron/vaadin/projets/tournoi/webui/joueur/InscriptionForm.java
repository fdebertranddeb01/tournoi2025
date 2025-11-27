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
package fr.insa.beuvron.vaadin.projets.tournoi.webui.joueur;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.SmallImageWithUploader;

/**
 *
 * @author fdebertranddeb01
 */
public class InscriptionForm extends FormLayout{
    
    private TextField tfSurnom;
    private PasswordField pfPass;
    private PasswordField pfAgain;
    private SmallImageWithUploader imgUploader;
    private Button btnSubmit;
    
    public InscriptionForm() {
        this.tfSurnom = new TextField("surnom");
        this.pfPass = new PasswordField("pass : ");
        this.pfAgain = new PasswordField("pass (confirm) : ");
        
        

    }
    
}

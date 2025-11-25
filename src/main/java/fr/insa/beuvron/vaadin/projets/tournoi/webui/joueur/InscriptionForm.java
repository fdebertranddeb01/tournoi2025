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

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.server.streams.UploadMetadata;

/**
 *
 * @author fdebertranddeb01
 */
public class InscriptionForm extends FormLayout{
    
    private TextField tfSurnom;
    private PasswordField pfPass;
    private PasswordField pfAgain;
    private VerticalLayout vlPreview;
    private Image iPreview;
    
    public InscriptionForm() {
        this.tfSurnom = new TextField("surnom");
        this.pfPass = new PasswordField("pass : ");
        this.pfAgain = new PasswordField("pass (confirm) : ");
        this.iPreview = new Image();
        this.iPreview.setMaxWidth("200px");
        this.iPreview.setMaxHeight("300px");
        
        UploadHandler inMemoryHandler = UploadHandler.inMemory(
                (UploadMetadata metadata, byte[] fileBytes) -> {

                    // Vérification du type MIME
                    String mime = metadata.contentType();
                    if (!mime.equals("image/jpeg") && !mime.equals("image/png")) {
                        Notification.show("Seules les images JPEG ou PNG sont acceptées");
                        return;
                    }

                    // Vérification de la taille max (ex : 5 Mo)
                    if (fileBytes.length > 5 * 1024 * 1024) {
                        Notification.show("Fichier trop volumineux (max 5 Mo)");
                        return;
                    }

                    // Affichage dans un composant Image
//                    preview.setSrc(resource);

                    Notification.show("Image chargée : " + metadata.fileName());
                }
        );
        

    }
    
}

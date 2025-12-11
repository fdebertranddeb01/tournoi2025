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

import java.sql.Connection;
import java.sql.Date;
import java.util.Optional;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.beuvron.vaadin.projets.tournoi.model.GeneralParams;
import fr.insa.beuvron.vaadin.projets.tournoi.model.Joueur;
import fr.insa.beuvron.vaadin.projets.tournoi.model.Role;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.MainLayout;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.SmallImage;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.SmallImageWithUploader;

/**
 * @author fdebertranddeb01
 */
@Route(value ="joueur/inscription", layout = MainLayout.class)
public class InscriptionForm extends FormLayout {

  private TextField tfSurnom;
  private PasswordField pfPass;
  private PasswordField pfAgain;
  private ComboBox<String> cbSexe;
  private DatePicker dpDateNaissance;
  private SmallImageWithUploader imgUploader;
  private Button btnSubmit;

  public InscriptionForm() {
    this.tfSurnom = new TextField("surnom : ");
    this.pfPass = new PasswordField("pass : ");
    this.pfAgain = new PasswordField("pass (confirm) : ");
    this.cbSexe = new ComboBox<>("sexe : ");
    this.cbSexe.setItems("M","F","Autre");
    this.dpDateNaissance = new DatePicker("date de naissance : ");
    // rendre les champs obligatoires visuellement
    this.tfSurnom.setRequiredIndicatorVisible(true);
    this.pfPass.setRequiredIndicatorVisible(true);
    this.pfAgain.setRequiredIndicatorVisible(true);
    // effacer l'état d'erreur quand l'utilisateur modifie les champs
    this.tfSurnom.addValueChangeListener(e -> {
      this.tfSurnom.setInvalid(false);
      this.tfSurnom.setErrorMessage((String) null);
    });
    this.pfPass.addValueChangeListener(e -> {
      this.pfPass.setInvalid(false);
      this.pfPass.setErrorMessage((String) null);
      this.pfAgain.setInvalid(false);
      this.pfAgain.setErrorMessage((String) null);
    });
    this.pfAgain.addValueChangeListener(e -> {
      this.pfPass.setInvalid(false);
      this.pfPass.setErrorMessage((String) null);
      this.pfAgain.setInvalid(false);
      this.pfAgain.setErrorMessage((String) null);
    });
    int width = GeneralParams.widthOfPhotoInJoueurPanel;
    int height = GeneralParams.heightOfPhotoInJoueurPanel;
    int maxsize = GeneralParams.maxSizePhotoFileInKo;
    this.imgUploader = new SmallImageWithUploader(maxsize * 1024, width, height);
    this.btnSubmit = new Button("S'inscrire");
    this.btnSubmit.addClickListener(e -> {
      this.createJoueur();
    });
    this.add(this.tfSurnom, this.pfPass, this.pfAgain,this.cbSexe,this.dpDateNaissance, this.imgUploader, this.btnSubmit);

  }

  public void createJoueur() {
    // clear previous error states
    this.tfSurnom.setInvalid(false);
    this.tfSurnom.setErrorMessage((String) null);
    this.pfPass.setInvalid(false);
    this.pfPass.setErrorMessage((String) null);
    this.pfAgain.setInvalid(false);
    this.pfAgain.setErrorMessage((String) null);

    String surnom = this.tfSurnom.getValue() == null ? "" : this.tfSurnom.getValue().trim();
    String pass = this.pfPass.getValue() == null ? "" : this.pfPass.getValue();
    String passAgain = this.pfAgain.getValue() == null ? "" : this.pfAgain.getValue();

    if (surnom.isEmpty()) {
      this.tfSurnom.setInvalid(true);
      this.tfSurnom.setErrorMessage("Le surnom est requis");
      this.tfSurnom.focus();
      return;
    }
    if (pass.isEmpty()) {
      this.pfPass.setInvalid(true);
      this.pfPass.setErrorMessage("Le mot de passe est requis");
      this.pfPass.focus();
      return;
    }
    if (passAgain.isEmpty()) {
      this.pfAgain.setInvalid(true);
      this.pfAgain.setErrorMessage("La confirmation du mot de passe est requise");
      this.pfAgain.focus();
      return;
    }
    if (!pass.equals(passAgain)) {
      this.pfPass.setInvalid(true);
      this.pfPass.setErrorMessage("Les mots de passe ne correspondent pas");
      this.pfAgain.setInvalid(true);
      this.pfAgain.setErrorMessage("Les mots de passe ne correspondent pas");
      this.pfAgain.focus();
      return;
    }

    String sexe = this.cbSexe.getValue();
    if (sexe == null || ! (sexe.equalsIgnoreCase("M")) || sexe.equalsIgnoreCase("F")) {
      sexe = null;
    }

    Date naissance = this.dpDateNaissance.getValue() == null ? null
        : Date.valueOf(this.dpDateNaissance.getValue());

    byte[] imgData = null;
    String imgType = null;
    Optional<SmallImage> imgOpt = this.imgUploader.getCurrentResizedImage();
    ;
    if (imgOpt.isPresent()) {
      imgData = imgOpt.get().getImageData();
      imgType = imgOpt.get().getImageType();
    }
    // le nouveau joueur est toujours un joueur simple
    // un admin pourra le promouvoir plus tard s'il le souhaite
    Joueur nouveauJoueur = new Joueur(surnom, pass,sexe,naissance, Role.ID_ROLE_JOUEUR, imgData, imgType);
    try (Connection con = ConnectionPool.getConnection()) {
      nouveauJoueur.saveInDB(con);
      Notification.show("Inscription " + nouveauJoueur.getSurnom() + " : OK", 2500, Position.MIDDLE);
    } catch (Exception e) {
      Notification.show("Erreur lors de l'inscription : " + e.getMessage(), 5000, Position.MIDDLE);
      return;
    }
  }
}

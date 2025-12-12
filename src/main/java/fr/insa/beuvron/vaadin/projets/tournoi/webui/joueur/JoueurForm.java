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

import java.sql.Date;
import java.util.Optional;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

import fr.insa.beuvron.vaadin.projets.tournoi.model.GeneralParams;
import fr.insa.beuvron.vaadin.projets.tournoi.model.Joueur;
import fr.insa.beuvron.vaadin.projets.tournoi.model.Role;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.MyIntegerField;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.SmallImage;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.SmallImageWithUploader;

/**
 * @author fdebertranddeb01
 */
public class JoueurForm extends FormLayout {

  private MyIntegerField tfID;
  private TextField tfSurnom;
  private PasswordField pfPass;
  private PasswordField pfAgain;
  private ComboBox<String> cbSexe;
  private DatePicker dpDateNaissance;
  private SmallImageWithUploader imgUploader;
  private ComboBox<Role> cbRole;

  private boolean forAdmin = false;
  private Joueur joueurEnCours = null;

  public JoueurForm(Joueur joueurEnCours, boolean forAdmin) {
    this.joueurEnCours = joueurEnCours;
    this.forAdmin = forAdmin;
    // sur une seule colonne
    this.setResponsiveSteps(new ResponsiveStep("0", 1));
    this.tfID = new MyIntegerField("ID");
    this.tfID.setReadOnly(true);
    this.tfSurnom = new TextField();
    this.pfPass = new PasswordField();
    this.pfAgain = new PasswordField();
    this.cbSexe = new ComboBox<>();
    this.cbSexe.setItems("M", "F", "Autre");
    this.dpDateNaissance = new DatePicker();
    // rendre les champs obligatoires visuellement
    this.tfSurnom.setRequiredIndicatorVisible(true);
    this.pfPass.setRequiredIndicatorVisible(true);
    this.pfAgain.setRequiredIndicatorVisible(true);
    // effacer l'état d'erreur quand l'utilisateur modifie les champs
    this.tfSurnom.addValueChangeListener(e -> {
      this.testValidity();
    });
    this.pfPass.addValueChangeListener(e -> {
      this.testValidity();
    });
    this.pfAgain.addValueChangeListener(e -> {
      this.testValidity();
    });
    int width = GeneralParams.widthOfPhotoInJoueurPanel;
    int height = GeneralParams.heightOfPhotoInJoueurPanel;
    int maxsize = GeneralParams.maxSizePhotoFileInKo;
    this.imgUploader = new SmallImageWithUploader(maxsize * 1024, width, height);
    this.cbRole = new ComboBox<>("Rôle");
    this.cbRole.setItemLabelGenerator(r -> r.getNom());
    this.cbRole.setItems(Role.ALL_ROLES_GENERAUX);
    this.cbRole.setRequiredIndicatorVisible(true);
    if (this.forAdmin) {
      this.addFormItem(this.tfID, "ID : ");
    }
    this.addFormItem(this.tfSurnom, "surnom : ");
    this.addFormItem(this.pfPass, "mot de passe : ");
    this.addFormItem(this.pfAgain, "mot de passe (confirmation) : ");
    this.addFormItem(this.cbSexe, "sexe : ");
    this.addFormItem(this.dpDateNaissance, "date de naissance : ");
    if (this.forAdmin) {
      this.addFormItem(this.cbRole, "rôle : ");
    }
    this.addFormItem(this.imgUploader, "photo : ");
    this.updateViewFromJoueur();
  }

  public void setJoueurEnCours(Joueur joueurEnCours) {
    this.joueurEnCours = joueurEnCours;
    this.updateViewFromJoueur();
  }

  public void updateViewFromJoueur() {
    if (this.joueurEnCours != null) {
      this.tfID.setValue(Integer.toString(this.joueurEnCours.getId()));
      this.tfSurnom.setValue(this.joueurEnCours.getSurnom());
      this.cbSexe.setValue(this.joueurEnCours.getSexe());
      if (this.joueurEnCours.getDateNaissance() != null) {
        this.dpDateNaissance.setValue(this.joueurEnCours.getDateNaissance().toLocalDate());
      }
      Optional<Role> roleOpt = Role.ALL_ROLES_GENERAUX.stream()
          .filter(r -> r.getId() == this.joueurEnCours.getIdRole())
          .findFirst();
      roleOpt.ifPresent(r -> this.cbRole.setValue(r));
      if (this.joueurEnCours.getPhoto() != null) {
        SmallImage img = new SmallImage(this.joueurEnCours.getPhoto(), this.joueurEnCours.getPhotoType());
        this.imgUploader.setCurImage(img);
      } else {
        this.imgUploader.setCurImage(null);
      }
    }
  }

  public boolean testValidity() {
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
    boolean valid = true;
    if (surnom.isEmpty()) {
      this.tfSurnom.setInvalid(true);
      this.tfSurnom.setErrorMessage("Le surnom est requis");
      valid = false;
    }
    if (pass.isEmpty()) {
      this.pfPass.setInvalid(true);
      this.pfPass.setErrorMessage("Le mot de passe est requis");
      valid = false;
    }
    if (passAgain.isEmpty()) {
      this.pfAgain.setInvalid(true);
      this.pfAgain.setErrorMessage("La confirmation du mot de passe est requise");
      valid = false;
    }
    if (!pass.equals(passAgain)) {
      this.pfPass.setInvalid(true);
      this.pfPass.setErrorMessage("Les mots de passe ne correspondent pas");
      this.pfAgain.setInvalid(true);
      this.pfAgain.setErrorMessage("Les mots de passe ne correspondent pas");
      valid = false;
    }
    if (this.cbRole.getValue() == null) {
      this.cbRole.setInvalid(true);
      this.cbRole.setErrorMessage("Le rôle est requis");
      valid = false;
    }
    return valid;
  }

  public boolean updateModelFromView() {
    if (!this.testValidity()) {
      return false;
    }
    String surnom = this.tfSurnom.getValue() == null ? "" : this.tfSurnom.getValue().trim();
    String pass = this.pfPass.getValue() == null ? "" : this.pfPass.getValue();
    String passAgain = this.pfAgain.getValue() == null ? "" : this.pfAgain.getValue();

    String sexe = this.cbSexe.getValue();
    if (sexe == null || !(sexe.equalsIgnoreCase("M")) || sexe.equalsIgnoreCase("F")) {
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
    this.joueurEnCours.setSurnom(surnom);
    this.joueurEnCours.setPass(pass);
    if (this.cbRole.getValue() != null) {
      this.joueurEnCours.setIdRole(this.cbRole.getValue().getId());
    } else {
      this.joueurEnCours.setIdRole(Role.ID_ROLE_JOUEUR);
    }
    this.joueurEnCours.setIdRole(this.cbRole.getValue().getId());
    this.joueurEnCours.setSexe(sexe);
    this.joueurEnCours.setDateNaissance(naissance);
    this.joueurEnCours.setPhoto(imgData);
    this.joueurEnCours.setPhotoType(imgType);
    return true;

  }
}

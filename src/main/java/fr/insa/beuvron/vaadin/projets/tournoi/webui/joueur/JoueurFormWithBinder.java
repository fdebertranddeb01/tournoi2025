package fr.insa.beuvron.vaadin.projets.tournoi.webui.joueur;

import java.sql.Date;
import java.time.LocalDate;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.Result;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.data.converter.Converter;

import fr.insa.beuvron.vaadin.projets.tournoi.model.GeneralParams;
import fr.insa.beuvron.vaadin.projets.tournoi.model.Joueur;
import fr.insa.beuvron.vaadin.projets.tournoi.model.RoleEnum;
import fr.insa.beuvron.vaadin.projets.tournoi.model.SexeEnum;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.SmallImage;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.SmallImageWithUploader;

public class JoueurFormWithBinder extends FormLayout {

        private Binder<Joueur> binder;
        private Joueur curJoueur;

        private TextField idField;
        private TextField surnomField;
        private TextField passField;
        private TextField confirmPassField;
        private RoleEnumCombo roleCombo;
        private SexeEnumCombo sexeCombo;
        private DatePicker dateNaissancePicker;
        private SmallImageWithUploader photoUploader;

        public JoueurFormWithBinder(Joueur joueur) {
                this.curJoueur = joueur;
                this.idField = new TextField("ID");
                this.idField.setReadOnly(true);
                this.surnomField = new TextField("Surnom");
                this.passField = new TextField("Mot de passe");
                this.confirmPassField = new TextField("Confirmer mot de passe");
                this.roleCombo = new RoleEnumCombo();
                this.sexeCombo = new SexeEnumCombo();
                this.dateNaissancePicker = new DatePicker("Date de naissance");
                int width = GeneralParams.widthOfPhotoInJoueurPanel;
                int height = GeneralParams.heightOfPhotoInJoueurPanel;
                int maxsize = GeneralParams.maxSizePhotoFileInKo;
                this.photoUploader = new SmallImageWithUploader(maxsize * 1024, width, height);

                this.binder = new BeanValidationBinder<>(Joueur.class);
                this.binder.forField(surnomField)
                                .bind(Joueur::getSurnom, Joueur::setSurnom);
                this.binder.forField(passField)
                                .bind(Joueur::getPass, Joueur::setPass);
                this.binder.forField(confirmPassField)
                                .withValidator(pass -> pass == null || pass.isEmpty()
                                                || !pass.equals(passField.getValue()),
                                                "Les mots de passe ne correspondent pas");
                this.binder.forField(this.roleCombo)
                                .withConverter(new Converter<RoleEnum, Integer>() {

                                        @Override
                                        public Result<Integer> convertToModel(RoleEnum value, ValueContext context) {
                                                if (value == null) {
                                                        return Result.error("Role non défini");
                                                }
                                                return Result.ok(value.getId());
                                        }

                                        @Override
                                        public RoleEnum convertToPresentation(Integer value, ValueContext context) {
                                                if (value == null) {
                                                        return null;
                                                }
                                                return RoleEnum.getById(value);
                                        }

                                })
                                .bind(Joueur::getIdRole, Joueur::setIdRole);
                this.binder.forField(this.sexeCombo)
                                .withConverter(new Converter<SexeEnum, String>() {
                                        @Override
                                        public Result<String> convertToModel(SexeEnum value, ValueContext context) {
                                                if (value == null) {
                                                        return Result.error("Sexe non défini");
                                                }
                                                return Result.ok(value.getCode());
                                        }

                                        @Override
                                        public SexeEnum convertToPresentation(String value, ValueContext context) {
                                                if (value == null) {
                                                        return null;
                                                }
                                                return SexeEnum.getByCode(value);
                                        }
                                })
                                .bind(Joueur::getSexe, Joueur::setSexe);
                this.binder.forField(this.dateNaissancePicker)
                                .withConverter(new Converter<LocalDate, Date>() {
                                        @Override
                                        public Result<Date> convertToModel(LocalDate value, ValueContext context) {
                                                if (value == null) {
                                                        return Result.ok(null);
                                                }
                                                return Result.ok(Date.valueOf(value));
                                        }

                                        @Override
                                        public LocalDate convertToPresentation(Date value, ValueContext context) {
                                                if (value == null) {
                                                        return null;
                                                }
                                                return value.toLocalDate();
                                        }
                                })
                                .bind(Joueur::getDateNaissance, Joueur::setDateNaissance);
                this.binder.forField(this.photoUploader)
                                .bind((Joueur j) -> {
                                        if (j.getPhoto() != null) {
                                                return new SmallImage(j.getPhoto(), j.getPhotoType());
                                        } else {
                                                return null;
                                        }
                                }, (Joueur j, SmallImage img) -> {
                                        if (img != null) {
                                                j.setPhotoType(img.getImageType());
                                                j.setPhoto(img.getImageData());
                                        } else {
                                                j.setPhoto(null);
                                                j.setPhotoType(null);
                                        }
                                });
                                this.addFormItem(idField, "id");
                this.addFormItem(surnomField, "surnom");
                this.addFormItem(passField, "mot de passe");
                this.addFormItem(confirmPassField, "confirmer mot de passe");
                this.addFormItem(roleCombo, "rôle");
                this.addFormItem(sexeCombo, "sexe");
                this.addFormItem(dateNaissancePicker, "date de naissance");
                this.addFormItem(photoUploader, "photo");

                // this.binder.setBean(this.curJoueur);
        }

}

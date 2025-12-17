package fr.insa.beuvron.vaadin.projets.tournoi.webui.joueur;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;

import fr.insa.beuvron.vaadin.projets.tournoi.model.GeneralParams;
import fr.insa.beuvron.vaadin.projets.tournoi.model.Joueur;
import fr.insa.beuvron.vaadin.projets.tournoi.model.RoleEnum;
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

    public JoueurFormWithBinder() {
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
                .withValidator(pass -> pass == null || pass.isEmpty() || ! pass.equals(passField.getValue()),
                        "Les mots de passe ne correspondent pas");
        // this.binder.forField(roleCombo)
        // .withConverter(null)
        //         .bind(j -> RoleEnum.getById(j.getIdRole())
        //         , (Joueur j, RoleEnum r) -> j.setIdRole(r.getId()));

                //utiliser binder.readBean pour associer le binder à un joueur

    }

}

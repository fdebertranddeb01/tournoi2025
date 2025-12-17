package fr.insa.beuvron.vaadin.projets.tournoi.webui.joueur;

import com.vaadin.flow.component.combobox.ComboBox;

import fr.insa.beuvron.vaadin.projets.tournoi.model.SexeEnum;

public class SexeEnumCombo extends ComboBox<SexeEnum> {

    public SexeEnumCombo() {
        this.setLabel("Sexe");
        this.setItems(SexeEnum.values());
        this.setItemLabelGenerator(SexeEnum::getNom);
        this.setValue(SexeEnum.NON_SPECIFIE);
        this.setClearButtonVisible(true);
     }
     
     /**
      * Retourne le sexe sélectionné sous forme de "M", "F" ou null si non spécifié.
      * @return
      */
     public String getSelectedSexe() {
         SexeEnum selected = this.getValue();
         if (selected != null) {
             return selected.getCode();
         } else {
             return null;
         }
     }

}

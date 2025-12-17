package fr.insa.beuvron.vaadin.projets.tournoi.webui.joueur;

import com.vaadin.flow.component.combobox.ComboBox;

public class SexeCombo extends ComboBox<String> {

    public SexeCombo() {
        this.setLabel("Sexe");
        this.setItems("Masculin", "Féminin", "Non spécifié");
        this.setValue("Non spécifié");
     }
     
     /**
      * Retourne le sexe sélectionné sous forme de "M", "F" ou null si non spécifié.
      * @return
      */
     public String getSelectedSexe() {
         String selected = this.getValue();
         if (selected != null) {
             if (selected.equals("Masculin")) {
                 return "M";
             } else if (selected.equals("Féminin")) {
                 return "F";
             } else {
                 return null;
             }
         } else {
             return null;
         }
     }

}

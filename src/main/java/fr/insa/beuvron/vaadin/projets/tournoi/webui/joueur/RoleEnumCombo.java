package fr.insa.beuvron.vaadin.projets.tournoi.webui.joueur;

import com.vaadin.flow.component.combobox.ComboBox;

import fr.insa.beuvron.vaadin.projets.tournoi.model.RoleEnum;

public class RoleEnumCombo extends ComboBox<RoleEnum> {

    public RoleEnumCombo() {
        this.setLabel("Rôle");
        this.setItems(RoleEnum.values());
        this.setItemLabelGenerator(RoleEnum::getNom);
        this.setValue(RoleEnum.ROLE_JOUEUR);
        this.addValueChangeListener(evt -> {
            if (evt.getValue() == null && evt.getOldValue() != null) {
                this.setValue(evt.getOldValue());
            }
        });
    }

    public int getSelectedRoleId() {
        RoleEnum selected = this.getValue();
        if (selected != null) {
            return selected.getId();
        } else {
            return RoleEnum.ROLE_JOUEUR.getId(); // ne devrait jamais arriver
        }
    }

}

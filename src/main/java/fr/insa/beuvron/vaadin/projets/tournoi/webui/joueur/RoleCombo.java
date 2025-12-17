package fr.insa.beuvron.vaadin.projets.tournoi.webui.joueur;

import com.vaadin.flow.component.combobox.ComboBox;

import fr.insa.beuvron.vaadin.projets.tournoi.model.Role;

public class RoleCombo extends ComboBox<Role> {

    public RoleCombo() {
        this.setLabel("Rôle");
        this.setItems(Role.ALL_ROLES_GENERAUX);
        this.setItemLabelGenerator(Role::getNom);
        this.setValue(Role.ROLE_JOUEUR);
        this.addValueChangeListener(evt -> {
            if (evt.getValue() == null && evt.getOldValue() != null) {
                this.setValue(evt.getOldValue());
            }
        });
    }

    public int getSelectedRoleId() {
        Role selected = this.getValue();
        if (selected != null) {
            return selected.getId();
        } else {
            return Role.ID_ROLE_JOUEUR; // ne devrait jamais arriver
        }
    }

}

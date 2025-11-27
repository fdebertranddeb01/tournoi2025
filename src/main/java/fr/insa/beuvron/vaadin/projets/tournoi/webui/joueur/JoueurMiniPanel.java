package fr.insa.beuvron.vaadin.projets.tournoi.webui.joueur;

import java.sql.Connection;
import java.sql.SQLException;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import fr.insa.beuvron.utils.database.ConnectionPool;

public class JoueurMiniPanel extends HorizontalLayout{

    public JoueurMiniPanel() {
        try (Connection con = ConnectionPool.getConnection()) {
            
        } catch (SQLException ex) {
            throw new Error(ex);
        }
    }

}

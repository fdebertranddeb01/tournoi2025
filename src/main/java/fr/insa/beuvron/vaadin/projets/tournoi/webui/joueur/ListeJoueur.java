package fr.insa.beuvron.vaadin.projets.tournoi.webui.joueur;

import java.sql.Connection;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.beuvron.vaadin.projets.tournoi.model.Joueur;

@Route("joueurs/liste")
public class ListeJoueur extends VerticalLayout {

    public ListeJoueur() {
        this.add(new H2("Liste des joueurs"));
        Grid<Joueur> grid = new Grid<>(Joueur.class);
        try (Connection con = ConnectionPool.getConnection()) {
            grid.setItems(Joueur.getAllJoueurs(con));
        } catch (Exception e) {
            Notification.show("Erreur lors du chargement de la liste des joueurs : " + e, 5000,
                    Notification.Position.MIDDLE);
        }
        grid.addComponentColumn(j -> new JoueurMiniPanel(j)).setHeader("Joueur");
    }

}

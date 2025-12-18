package fr.insa.beuvron.vaadin.projets.tournoi.webui.tests;

import java.sql.Connection;
import java.util.Optional;

import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.beuvron.vaadin.projets.tournoi.model.Joueur;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.MainLayout;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.joueur.JoueurFormWithBinder;

@Route(value = "tests/joueurformbinder", layout = MainLayout.class)
public class TestJoueurFormWithBinder extends VerticalLayout {

    public TestJoueurFormWithBinder() {
        JoueurFormWithBinder form = new JoueurFormWithBinder(Joueur.nouveauJoueur(), false);
        form.getStyle().set("border", "1px solid black");
        form.getStyle().set("margin-bottom", "20px");
        this.add(form);
        try (Connection conn = ConnectionPool.getConnection()) {
            Optional<Joueur> optJoueur = Joueur.getBySurnom(conn, "toto");
            if (!optJoueur.isEmpty()) {
                JoueurFormWithBinder form2 = new JoueurFormWithBinder(optJoueur.get(), true);
                form2.getStyle().set("border", "1px solid black");
                this.add(form2);

            } else {
                this.add(new H3("Joueur 'toto' non trouvé"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

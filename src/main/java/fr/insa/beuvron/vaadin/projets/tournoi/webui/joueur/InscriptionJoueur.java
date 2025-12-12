package fr.insa.beuvron.vaadin.projets.tournoi.webui.joueur;

import java.sql.Connection;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.beuvron.vaadin.projets.tournoi.model.Joueur;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.MainLayout;

@Route(value = "joueur/inscription", layout = MainLayout.class)
public class InscriptionJoueur extends VerticalLayout {

    private JoueurForm form;
    private Button btnInscrire;
    private Joueur nouveauJoueur;

    public InscriptionJoueur() {
        this.add(new H3("Inscription Joueur"));
        this.nouveauJoueur = Joueur.nouveauJoueur();
        this.form = new JoueurForm(this.nouveauJoueur, false);
        this.btnInscrire = new Button("S'inscrire", event -> {
            this.doInscription();
        });
        this.add(this.form,this.btnInscrire);
    }

    public void doInscription() {
        boolean valid = this.form.updateModelFromView();
        if (valid) {
            try (Connection con = ConnectionPool.getConnection()) {
                nouveauJoueur.saveInDB(con);
                Notification.show("Inscription " + this.nouveauJoueur.getSurnom() + " : OK", 2500,
                        Notification.Position.BOTTOM_END);
            } catch (Exception e) {
                Notification.show("Erreur lors de l'inscription : " + e.getMessage(), 5000,
                        Notification.Position.MIDDLE);
                return;
            }
        }
    }

}

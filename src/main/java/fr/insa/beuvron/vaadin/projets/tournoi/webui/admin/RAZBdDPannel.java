package fr.insa.beuvron.vaadin.projets.tournoi.webui.admin;

import java.sql.Connection;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.beuvron.vaadin.projets.tournoi.model.BdDTest;
import fr.insa.beuvron.vaadin.projets.tournoi.model.GestionSchema;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.MainLayout;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.Utils;

@Route(value = "admin/razbdd", layout = MainLayout.class)
public class RAZBdDPannel extends VerticalLayout {

    public RAZBdDPannel() {
        this.add(new H3("Remise à zéro de la base de données"));
        this.add(new Paragraph("Cette opération supprime toutes les données de la base et recrée une base de test."));
        Button razButton = new Button("Remise à zéro", evt -> onRAZ());
        this.add(razButton);

    }

    public void onRAZ() {
        Utils.confirm("RAZ BdD","Confirmer la remise à zéro de la base de données ?","RAZ", () -> {

            try (Connection con = ConnectionPool.getConnection()) {
                GestionSchema.razBdd(con);
                BdDTest.createBdDTest(con);
                Utils.outSuccess("Base de données remise à zéro : OK");
            } catch (Exception ex) {
                Utils.outError("Erreur lors de la remise à zéro de la base de données : " + ex.getLocalizedMessage());
            }
        });
    }

}

package fr.insa.beuvron.vaadin.projets.tournoi.webui;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.beuvron.vaadin.projets.tournoi.model.BdDTest;
import fr.insa.beuvron.vaadin.projets.tournoi.model.GestionSchema;
import java.sql.Connection;
import java.sql.SQLException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@Theme("default")
public class Application extends SpringBootServletInitializer implements AppShellConfigurator {

    // en mode debug, il est utile par exemple
    // de se logger automatiquement comme admin
    // pour tester toutes les fonctionnalités
    // à mettre évidement à false quand debug fini
    public static final boolean DEBUG = true;

    public static void main(String[] args) {
        // seulement si bdd h2 en memoire
        try (Connection con = ConnectionPool.getConnection()) {
            GestionSchema.razBdd(con);
            BdDTest.createBdDTest(con);
        } catch (SQLException ex) {
            throw new Error(ex);
        }
        SpringApplication.run(Application.class, args);
    }

}

package fr.insa.beuvron.vaadin.projets.tournoi.webui.joueur;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.Grid.Column;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;

import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.beuvron.vaadin.projets.tournoi.model.Joueur;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.MainLayout;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.session.OnlyAdmin;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.Utils;

/**
 * Vue principale pour gérer les joueurs : grille à gauche, éditeur à droite.
 */
@Route(value = "joueurs/browse", layout = MainLayout.class)
public class JoueurBrowser extends SplitLayout implements OnlyAdmin {

	private static enum Mode {
		MODIFICATION,
		CREATION
	}

	private Grid<Joueur> grid = new Grid<>(Joueur.class, false);
	private JoueurForm form;
	private Button bSave;
	private Button bReload;
	private Button bDelete;
	private Button bCreate;

	private List<Joueur> allJoueurs;

	public JoueurBrowser() {
		// left: grid
		VerticalLayout left = new VerticalLayout();
		left.setPadding(false);
		left.setSpacing(false);
		left.add(new H3("Liste des joueurs"));

		grid.addColumn(Joueur::getId).setHeader("ID").setSortable(true);
		grid.addColumn(Joueur::getSurnom).setHeader("Surnom").setSortable(true);
		grid.addColumn(Joueur::getSexe).setHeader("Sexe");
		grid.addColumn(j -> j.getDateNaissance() == null ? "" : j.getDateNaissance().toString()).setHeader("Naissance");
		grid.addColumn(Joueur::getIdRole).setHeader("Role");
		grid.addColumn((Joueur j) -> j.getPhoto() == null ? "N" : "O").setHeader("Photo?");
		grid.getColumns().forEach(col -> col.setAutoWidth(true));
		this.setSplitterPosition(33);
		// grid.setWidth("min-content");
		// grid.getElement().getStyle().set("flex-shrink", "1");
		// grid.getElement().getStyle().set("min-width", "150px");
		left.add(grid);

		// right: form
		VerticalLayout right = new VerticalLayout();

		this.form = new JoueurForm(Joueur.nouveauJoueur(), true);
		right.add(new H3("Éditer joueur courant"));
		right.add(form);

		HorizontalLayout actions = new HorizontalLayout();
		this.bSave = new Button("Modifier", e -> onSave());
		this.bReload = new Button("Recharger version sauvegardée", e -> onReload());
		this.bDelete = new Button("Supprimer", e -> onDelete());
		this.bCreate = new Button("Créer nouveau joueur", e -> this.onCreate());
		actions.add(bSave, bReload, bDelete, bCreate);
		right.add(actions);

		// When a grid item is selected, load full joueur and bind
		grid.asSingleSelect().addValueChangeListener(evt -> {
			Joueur sel = evt.getValue();
			if (sel != null) {
				form.setJoueurEnCours(sel);
				this.enable(true);
			}
		});

		// populate grid
		refreshGrid();
		this.enable(true);

		this.addToPrimary(left);
		this.addToSecondary(right);
	}

	private void enable(boolean enabled) {
		this.form.setEnabled(enabled);
		this.bSave.setEnabled(enabled);
		this.bReload.setEnabled(enabled);
		this.bDelete.setEnabled(enabled);
	}

	private void refreshGridAndReselect(int idJoueur) {
		try (Connection con = ConnectionPool.getConnection()) {
			this.allJoueurs = Joueur.getAllJoueurs(con);
			grid.setItems(this.allJoueurs);
			boolean found = false;
			for (Joueur jj : this.allJoueurs) {
				if (jj.getId() == idJoueur) {
					grid.asSingleSelect().setValue(jj);
					this.form.setJoueurEnCours(jj);
					found = true;
					break;
				}
			}
			if (!found) {
				this.enable(false);
				;
			}
		} catch (SQLException ex) {
			Utils.outError("Erreur lecture joueurs : " + ex.getLocalizedMessage());
		}
	}

	private void refreshGrid() {
		try (Connection con = ConnectionPool.getConnection()) {
			this.allJoueurs = Joueur.getAllJoueurs(con);
			grid.setItems(this.allJoueurs);
			this.enable(false);
		} catch (SQLException ex) {
			Utils.outError("Erreur lecture joueurs : " + ex.getLocalizedMessage());
		}
	}

	private void onSave() {
		boolean ok = this.form.updateModelFromView();
		if (ok) {
			try (Connection con = ConnectionPool.getConnection()) {
				Joueur j = this.form.getJoueurEnCours();
				j.updateSaufID(con);
				Utils.outSuccess("Joueur " + j.getSurnom() + " modifié : OK");
				this.refreshGridAndReselect(j.getId());
			} catch (SQLException ex) {
				Utils.outError("Erreur sauvegarde joueur : " + ex.getLocalizedMessage());
			}
		}
	}

	public void onCreate() {
		boolean ok = this.form.updateModelFromView();
		if (ok) {
			Joueur j = this.form.getJoueurEnCours();
			j.setId(-1);
			try (Connection con = ConnectionPool.getConnection()) {
				j.saveInDB(con);
				Utils.outSuccess("Joueur " + j.getSurnom() + " créé : OK");
				this.refreshGridAndReselect(j.getId());
			} catch (SQLException ex) {
				Utils.outError("Erreur création joueur : " + ex.getLocalizedMessage());
			}
		}
	}

	private void onReload() {
		try (Connection con = ConnectionPool.getConnection()) {
			Joueur j = this.form.getJoueurEnCours();
			Joueur reloaded = Joueur.getById(con, j.getId()).orElseThrow();
			this.form.setJoueurEnCours(reloaded);
			Utils.outSuccess("Joueur " + j.getSurnom() + " rechargé : OK");
		} catch (SQLException ex) {
			Utils.outError("Erreur rechargement joueur : " + ex.getLocalizedMessage());
		}
	}

	private void onDelete() {
		Joueur j = this.form.getJoueurEnCours();
		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setHeader("Confirmation de suppression");
		dialog.setText("Confirmez-vous la suppression du joueur " + j.getSurnom() + " ?");
		dialog.setCancelable(true);
		dialog.setConfirmText("Supprimer");
		dialog.addConfirmListener(e -> {
			try (Connection con = ConnectionPool.getConnection()) {
				j.delete(con);
				Utils.outSuccess("Joueur " + j.getSurnom() + " supprimé : OK");
				this.refreshGrid();
			} catch (SQLException ex) {
				Utils.outError("Erreur suppression joueur : " + ex.getLocalizedMessage());
			}
		});
		dialog.open();
	}

}

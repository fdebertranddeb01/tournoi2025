package fr.insa.beuvron.vaadin.projets.tournoi.webui.joueur;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.converter.StringToIntegerConverter;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.beuvron.vaadin.projets.tournoi.model.Joueur;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.MainLayout;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.session.OnlyAdmin;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.Utils;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.MyIntegerField;

import java.io.ByteArrayInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Base64;

/**
 * Vue principale pour gérer les joueurs : grille à gauche, éditeur à droite.
 */
@Route(value = "joueurs", layout = MainLayout.class)
public class JoueurBrowser extends SplitLayout implements OnlyAdmin {

	private Grid<Joueur> grid = new Grid<>(Joueur.class, false);

	// editor fields
	private TextField idField = new TextField("ID");
	private TextField surnom = new TextField("Surnom");
	private TextField pass = new TextField("Mot de passe");
	private ComboBox<String> sexe = new ComboBox<>("Sexe");
	private DatePicker dateNaissance = new DatePicker("Date de naissance");
	private MyIntegerField idRole = new MyIntegerField();
	private TextArea photoBase64 = new TextArea("Photo (Base64)");
	private TextField photoType = new TextField("Photo type");

	private Binder<Joueur> binder = new Binder<>(Joueur.class);

	public JoueurBrowser() {
		// left: grid
		VerticalLayout left = new VerticalLayout();
		left.setPadding(false);
		left.setSpacing(false);
		left.add(new H3("Liste des joueurs"));

		grid.addColumn(Joueur::getId).setHeader("ID").setKey("id");
		grid.addColumn(Joueur::getSurnom).setHeader("Surnom");
		grid.addColumn(Joueur::getSexe).setHeader("Sexe");
		grid.addColumn(j -> j.getDateNaissance() == null ? "" : j.getDateNaissance().toString()).setHeader("Naissance");
		grid.addColumn(Joueur::getIdRole).setHeader("Role");

		left.add(grid);

		// right: form
		FormLayout form = new FormLayout();
		idField.setReadOnly(true);
		idRole.setWidth("100px");
		photoBase64.setWidthFull();
		photoBase64.setHeight("150px");

		form.addFormItem(idField, "ID");
		form.addFormItem(surnom, "Surnom");
		form.addFormItem(pass, "Mot de passe");
		sexe.setItems("M", "F", "X");
		form.addFormItem(sexe, "Sexe");
		form.addFormItem(dateNaissance, "Date de naissance");
		form.addFormItem(idRole, "ID Role");
		form.addFormItem(photoType, "Photo type");
		form.addFormItem(photoBase64, "Photo (Base64)");

		HorizontalLayout actions = new HorizontalLayout();
		Button save = new Button("Sauvegarder", e -> onSave());
		Button newBtn = new Button("Nouveau", e -> newJoueur());
		Button delete = new Button("Supprimer", e -> onDelete());
		actions.add(save, newBtn, delete);
		form.add(actions);

		// binder bindings
		binder.forField(surnom).bind(Joueur::getSurnom, Joueur::setSurnom);
		binder.forField(pass).bind(Joueur::getPass, Joueur::setPass);
		binder.forField(sexe).bind(Joueur::getSexe, Joueur::setSexe);
		binder.forField(dateNaissance)
				.withConverter(d -> d == null ? null : java.sql.Date.valueOf(d),
						sqlDate -> sqlDate == null ? null : sqlDate.toLocalDate())
				.bind(Joueur::getDateNaissance, Joueur::setDateNaissance);
		binder.forField(idRole).withConverter(new StringToIntegerConverter("doit être un entier"))
				.bind(Joueur::getIdRole, Joueur::setIdRole);
		binder.forField(photoType).bind(Joueur::getPhotoType, Joueur::setPhotoType);
		// photo: convert between byte[] and base64 string
		binder.forField(photoBase64).bind(
				j -> j.getPhoto() == null ? "" : Base64.getEncoder().encodeToString(j.getPhoto()),
				(j, val) -> j.setPhoto(val == null || val.isEmpty() ? null : Base64.getDecoder().decode(val)));

		// When a grid item is selected, load full joueur and bind
		grid.asSingleSelect().addValueChangeListener(evt -> {
			Joueur sel = evt.getValue();
			if (sel == null) {
				binder.setBean(null);
				idField.clear();
			} else {
				try (Connection con = ConnectionPool.getConnection()) {
					Optional<Joueur> full = Joueur.getById(con, sel.getId());
					if (full.isPresent()) {
						Joueur j = full.get();
						binder.setBean(j);
						idField.setValue(Integer.toString(j.getId()));
						// idRole field is MyIntegerField; set its internal value
						idRole.setValue(Integer.toString(j.getIdRole()));
					}
				} catch (SQLException ex) {
					Utils.outErrorAsNotification("Erreur chargement joueur : " + ex.getLocalizedMessage());
				}
			}
		});

		// populate grid
		refreshGrid();

		VerticalLayout right = new VerticalLayout();
		right.add(new H3("Éditeur joueur"));
		right.add(form);

		this.addToPrimary(left);
		this.addToSecondary(right);
	}

	private void refreshGrid() {
		try (Connection con = ConnectionPool.getConnection()) {
			List<Joueur> all = Joueur.getAllJoueurs(con);
			grid.setItems(all);
		} catch (SQLException ex) {
			Utils.outErrorAsNotification("Erreur lecture joueurs : " + ex.getLocalizedMessage());
		}
	}

	private void onSave() {
		Joueur bean = binder.getBean();
		if (bean == null) {
			Utils.outErrorAsNotification("Aucun joueur sélectionné.");
			return;
		}
		try (Connection con = ConnectionPool.getConnection()) {
			if (bean.getId() == -1) {
				// new joueur
				bean.saveInDB(con);
			} else {
				try (PreparedStatement pst = con.prepareStatement(
						"update joueur set surnom=?, pass=?, sexe=?, datenaissance=?, idrole=?, photo=?, phototype=? where id=?")) {
					int i = 1;
					pst.setString(i++, bean.getSurnom());
					pst.setString(i++, bean.getPass());
					pst.setString(i++, bean.getSexe());
					pst.setDate(i++, bean.getDateNaissance());
					pst.setInt(i++, bean.getIdRole());
					pst.setBlob(i++, bean.getPhoto() == null ? null : new ByteArrayInputStream(bean.getPhoto()));
					pst.setString(i++, bean.getPhotoType());
					pst.setInt(i++, bean.getId());
					pst.executeUpdate();
				}
			}
			Utils.outErrorAsNotification("Sauvegarde OK");
			refreshGrid();
		} catch (SQLException ex) {
			Utils.outErrorAsNotification("Erreur sauvegarde : " + ex.getLocalizedMessage());
		}
	}

	private void newJoueur() {
		Joueur j = new Joueur("", "", "", null, 0, null, "");
		binder.setBean(j);
		idField.clear();
	}

	private void onDelete() {
		Joueur bean = binder.getBean();
		if (bean == null || bean.getId() == -1) {
			Utils.outErrorAsNotification("Aucun joueur sélectionné ou joueur non sauvegardé.");
			return;
		}
		try (Connection con = ConnectionPool.getConnection();
				PreparedStatement pst = con.prepareStatement("delete from joueur where id=?")) {
			pst.setInt(1, bean.getId());
			pst.executeUpdate();
			binder.setBean(null);
			refreshGrid();
			Utils.outErrorAsNotification("Joueur supprimé");
		} catch (SQLException ex) {
			Utils.outErrorAsNotification("Erreur suppression : " + ex.getLocalizedMessage());
		}
	}

}

/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.beuvron.vaadin.projets.tournoi.webui.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.MainLayout;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.session.OnlyAdmin;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.MyIntegerField;
import fr.insa.beuvron.utils.database.ConnectionPool;
import fr.insa.beuvron.vaadin.projets.tournoi.model.GeneralParams;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.Utils;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author fdebertranddeb01
 */
@Route(value = "admin/editParams", layout = MainLayout.class)
public class EditGeneralParams extends FormLayout implements OnlyAdmin {

	private MyIntegerField maxSizePhotoFileInKo = new MyIntegerField();
	private MyIntegerField widthOfPhotoInJoueurList = new MyIntegerField();
	private MyIntegerField heightOfPhotoInJoueurList = new MyIntegerField();
	private MyIntegerField widthOfPhotoInJoueurPanel = new MyIntegerField();
	private MyIntegerField heightOfPhotoInJoueurPanel = new MyIntegerField();
	private MyIntegerField photoHeightInPixelInDB = new MyIntegerField();
	private MyIntegerField photoWidthInPixelInDB = new MyIntegerField();

	public EditGeneralParams() {
    // une seule colonne
    this.setResponsiveSteps(new ResponsiveStep("0", 1));
		H3 titre = new H3("Paramètres généraux");
		Paragraph help = new Paragraph("Modifiez les paramètres puis cliquez sur 'Sauvegarder' pour les enregistrer en base.");

		this.add(titre);
		this.add(help);

		// add fields to the form
		this.addFormItem(this.maxSizePhotoFileInKo, "Taille maximale fichier photo (Ko)");
		this.addFormItem(this.widthOfPhotoInJoueurList, "Largeur vignette liste (px)");
		this.addFormItem(this.heightOfPhotoInJoueurList, "Hauteur vignette liste (px)");
		this.addFormItem(this.widthOfPhotoInJoueurPanel, "Largeur photo panneau (px)");
		this.addFormItem(this.heightOfPhotoInJoueurPanel, "Hauteur photo panneau (px)");
		this.addFormItem(this.photoHeightInPixelInDB, "Hauteur sauvegardée BDD (px)");
		this.addFormItem(this.photoWidthInPixelInDB, "Largeur sauvegardée BDD (px)");

		Button save = new Button("Sauvegarder", evt -> this.onSave());
		Button reload = new Button("Recharger depuis BD", evt -> this.onReload());

		this.add(save, reload);

		// populate with current values
		this.fillFieldsFromModel();
	}

	private void fillFieldsFromModel() {
		this.maxSizePhotoFileInKo.setValue(Integer.toString(GeneralParams.maxSizePhotoFileInKo));
		this.widthOfPhotoInJoueurList.setValue(Integer.toString(GeneralParams.widthOfPhotoInJoueurList));
		this.heightOfPhotoInJoueurList.setValue(Integer.toString(GeneralParams.heightOfPhotoInJoueurList));
		this.widthOfPhotoInJoueurPanel.setValue(Integer.toString(GeneralParams.widthOfPhotoInJoueurPanel));
		this.heightOfPhotoInJoueurPanel.setValue(Integer.toString(GeneralParams.heightOfPhotoInJoueurPanel));
		this.photoHeightInPixelInDB.setValue(Integer.toString(GeneralParams.photoHeightInPixelInDB));
		this.photoWidthInPixelInDB.setValue(Integer.toString(GeneralParams.photoWidthInPixelInDB));
	}

	private void onSave() {
		try {
			// validate and set model
			GeneralParams.maxSizePhotoFileInKo = this.getIntOrThrow(this.maxSizePhotoFileInKo, "Taille max photo (Ko)");
			GeneralParams.widthOfPhotoInJoueurList = this.getIntOrThrow(this.widthOfPhotoInJoueurList, "Largeur photo liste (px)");
			GeneralParams.heightOfPhotoInJoueurList = this.getIntOrThrow(this.heightOfPhotoInJoueurList, "Hauteur photo liste (px)");
			GeneralParams.widthOfPhotoInJoueurPanel = this.getIntOrThrow(this.widthOfPhotoInJoueurPanel, "Largeur photo panneau (px)");
			GeneralParams.heightOfPhotoInJoueurPanel = this.getIntOrThrow(this.heightOfPhotoInJoueurPanel, "Hauteur photo panneau (px)");
			GeneralParams.photoHeightInPixelInDB = this.getIntOrThrow(this.photoHeightInPixelInDB, "Hauteur photo BDD (px)");
			GeneralParams.photoWidthInPixelInDB = this.getIntOrThrow(this.photoWidthInPixelInDB, "Largeur photo BDD (px)");

			// persist to DB
			try (Connection con = ConnectionPool.getConnection()) {
				GeneralParams.saveGeneralParams(con);
			}
			Utils.outSuccess("Paramètres sauvegardés avec succès.");
		} catch (NumberFormatException ex) {
			Utils.outError("Valeur entière attendue : " + ex.getMessage());
		} catch (SQLException ex) {
			Utils.outError("Erreur de sauvegarde : " + ex.getLocalizedMessage());
		}
	}

	private void onReload() {
		try (Connection con = ConnectionPool.getConnection()) {
			GeneralParams.loadGeneralParams(con);
			this.fillFieldsFromModel();
			Utils.outError("Paramètres rechargés depuis la base.");
		} catch (SQLException ex) {
			Utils.outError("Erreur de lecture depuis la base : " + ex.getLocalizedMessage());
		}
	}

	private int getIntOrThrow(TextField f, String fieldName) {
		try {
			return Integer.parseInt(f.getValue());
		} catch (NumberFormatException ex) {
			throw new NumberFormatException(fieldName);
		}
	}
}
 

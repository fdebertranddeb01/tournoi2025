package fr.insa.beuvron.vaadin.projets.tournoi.webui.joueur;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import fr.insa.beuvron.vaadin.projets.tournoi.model.GeneralParams;
import fr.insa.beuvron.vaadin.projets.tournoi.model.Joueur;
import fr.insa.beuvron.vaadin.projets.tournoi.webui.utils.SmallImage;

public class JoueurMiniPanel extends HorizontalLayout {

  private SmallImage photoJoueur;

  public JoueurMiniPanel(Joueur joueur) {
    Div vaadImage = new Div();
    // ajouter une bordure autour de vaadImage
    vaadImage.getStyle().set("border", "1px solid black");
    // ajouter un padding de 2px autour de l'image
    vaadImage.getStyle().set("padding", "2px");
    // utiliser un flex layout pour centrer l'image
    vaadImage.getStyle().set("display", "flex");
    vaadImage.getStyle().set("justify-content", "center");
    vaadImage.getStyle().set("align-items", "center");
    vaadImage.setWidth(GeneralParams.widthOfPhotoInJoueurList + "px");
    vaadImage.setHeight(GeneralParams.heightOfPhotoInJoueurList + "px");
    if (joueur.getPhoto() != null) {
      try {
        this.photoJoueur = new SmallImage(joueur.getPhoto(), joueur.getPhotoType())
            .resizeToPNG(GeneralParams.widthOfPhotoInJoueurList, GeneralParams.heightOfPhotoInJoueurList);
      } catch (Exception e) {
        vaadImage.add(VaadinIcon.BAN.create());;
      }
      vaadImage.add(this.photoJoueur.toVaadinImage("Problème Affichage photo joueur"));
    } else {
      vaadImage.add(VaadinIcon.BAN.create());
    }
    this.add(vaadImage);
  }
}

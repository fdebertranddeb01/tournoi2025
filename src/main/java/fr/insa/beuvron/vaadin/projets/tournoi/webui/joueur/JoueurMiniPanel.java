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
    vaadImage.setWidth(GeneralParams.widthOfPhotoInJoueurList);
    vaadImage.setHeight(GeneralParams.heightOfPhotoInJoueurList);
    if (joueur.getPhoto() != null) {
      this.photoJoueur = new SmallImage(joueur.getPhoto(), joueur.getPhotoType());
      vaadImage.add(this.photoJoueur.toVaadinImage("Problem loading player photo"));
    } else {
      vaadImage.add(VaadinIcon.BAN.create());
    }
    this.add(vaadImage);
  }
}

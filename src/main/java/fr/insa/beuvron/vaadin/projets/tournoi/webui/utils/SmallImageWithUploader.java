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
package fr.insa.beuvron.vaadin.projets.tournoi.webui.utils;

import java.util.Optional;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
/**
 * @author fdebertranddeb01
 */
public class SmallImageWithUploader extends HorizontalLayout {

  private SmallImageUploader uploader;
  private Div imagePlace;

  private SmallImage curImage;

  public SmallImageWithUploader(int maxFileSize, String imgWidth, String imgHeight) {
    this.imagePlace = new Div();
    this.imagePlace.getStyle().set("border", "1px solid black");
    this.imagePlace.setMaxWidth(imgWidth);
    this.imagePlace.setMaxHeight(imgHeight);
    this.imagePlace.setMinWidth(imgWidth);
    this.imagePlace.setMinHeight(imgHeight);
    this.uploader = new SmallImageUploader(maxFileSize);
    this.uploader.addAllFinishedListener(
        (t) -> {
          this.changeImage();
        });
    this.imagePlace.add(VaadinIcon.BAN.create());
    this.add(this.imagePlace, this.uploader);
  }

  public void changeImage() {
    this.imagePlace.removeAll();
    byte[] imgData = this.uploader.getImageData();
    if (imgData == null) {
      Icon icon = VaadinIcon.BAN.create();
      icon.setSize("2cm");
      this.imagePlace.add();
      this.curImage = null;
    } else {
      this.curImage = new SmallImage(imgData, this.uploader.getImageType());
      Image img = this.curImage.toVaadinImage("Problem loading image");
      this.imagePlace.add(img);
    }
  }

  public SmallImageUploader getUploader() {
    return this.uploader;
  }

  public Optional<SmallImage> getCurrentImage() {
    return Optional.ofNullable(this.curImage);
  }

}

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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;

/**
 * @author fdebertranddeb01
 */
public class SmallImageWithUploader extends HorizontalLayout
    implements HasValue<HasValue.ValueChangeEvent<SmallImage>, SmallImage> {

  private SmallImageUploader uploader;
  private Div imagePlace;
  private int imgWidthInPixel;
  private int imgHeightInPixel;

  private SmallImage curImage;
  private SmallImage curImageResized;

  public SmallImageWithUploader(int maxFileSize, int imgWidthInPixel, int imgHeightInPixel) {
    this.imgWidthInPixel = imgWidthInPixel;
    this.imgHeightInPixel = imgHeightInPixel;
    this.imagePlace = new Div();
    this.imagePlace.getStyle().set("border", "1px solid black");
    // centrer le contenu de imagePlace
    this.imagePlace.getStyle().set("display", "flex");
    this.imagePlace.getStyle().set("justify-content", "center");
    this.imagePlace.getStyle().set("align-items", "center");
    this.imagePlace.setMaxWidth(imgWidthInPixel + "px");
    this.imagePlace.setMaxHeight(imgHeightInPixel + "px");
    this.imagePlace.setMinWidth(imgWidthInPixel + "px");
    this.imagePlace.setMinHeight(imgHeightInPixel + "px");
    this.uploader = new SmallImageUploader(maxFileSize);
    this.uploader.addAllFinishedListener(
        (t) -> {
          this.changeImage();
        });
    this.imagePlace.add(VaadinIcon.BAN.create());
    this.add(this.imagePlace, this.uploader);
  }

  public void setCurImage(SmallImage img) {
    this.curImage = img;
    if (img != null) {
      try {
        this.curImageResized = this.curImage.resizeToPNG(this.imgWidthInPixel, this.imgHeightInPixel);
      } catch (Exception e) {
        this.noImage();
      }
      Image vaadinImg = this.curImageResized.toVaadinImage("Problem affichage image");
      this.imagePlace.removeAll();
      this.imagePlace.add(vaadinImg);
    } else {
      this.noImage();
    }
  }

  private void noImage() {
    this.imagePlace.removeAll();
    Icon icon = VaadinIcon.BAN.create();
    icon.setSize(this.imgWidthInPixel + "px");
    this.imagePlace.add(icon);
    this.curImage = null;
    this.curImageResized = null;
  }

  public void changeImage() {
    this.imagePlace.removeAll();
    byte[] imgData = this.uploader.getImageData();
    if (imgData == null) {
      Icon icon = VaadinIcon.BAN.create();
      icon.setSize(this.imgWidthInPixel + "px");
      this.imagePlace.add(icon);
      this.curImage = null;
    } else {
      try {
        this.curImage = new SmallImage(imgData, this.uploader.getImageType());
        this.curImageResized = this.curImage.resizeToPNG(this.imgWidthInPixel, this.imgHeightInPixel);
        Image img = this.curImageResized.toVaadinImage("Problem affichage image");
        this.imagePlace.add(img);
      } catch (Exception e) {
        this.noImage();
      }

    }
  }

  public SmallImageUploader getUploader() {
    return this.uploader;
  }

  public Optional<SmallImage> getCurrentImage() {
    return Optional.ofNullable(this.curImage);
  }

  public Optional<SmallImage> getCurrentResizedImage() {
    return Optional.ofNullable(this.curImageResized);
  }

  private List<ValueChangeListener<? super ValueChangeEvent<SmallImage>>> listeners = new ArrayList<>();

  @Override
  public Registration addValueChangeListener(ValueChangeListener<? super ValueChangeEvent<SmallImage>> listener) {
    this.listeners.add(listener);
    return () -> this.listeners.remove(listener);
  }

  @Override
  public SmallImage getValue() {
    return this.curImage;
  }

  private boolean readOnly = false;

  @Override
  public boolean isReadOnly() {
    return this.readOnly;
  }

  private boolean requiredIndicatorVisible = false;

  @Override
  public boolean isRequiredIndicatorVisible() {
    return this.requiredIndicatorVisible;
  }

  @Override
  public void setReadOnly(boolean readOnly) {
    this.readOnly = readOnly;
    this.uploader.setEnabled(!readOnly);
  }

  @Override
  public void setRequiredIndicatorVisible(boolean requiredIndicatorVisible) {
    this.requiredIndicatorVisible = requiredIndicatorVisible;
  }

  @Override
  public void setValue(SmallImage arg0) {
    this.setCurImage(arg0);
  }

}

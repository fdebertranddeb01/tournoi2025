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

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

/**
 *
 * @author fdebertranddeb01
 */
public class SmallImageWithUploader extends HorizontalLayout{
    
    private SmallImageUploader uploader;
    private Div imagePlace;
    
    public SmallImageWithUploader(int maxFileSize,String imgWidth,String imgHeight) {
        this.imagePlace = new Div();
        this.imagePlace.setMaxWidth(imgWidth);
        this.imagePlace.setMaxHeight(imgWidth);
        this.imagePlace.setMinWidth(imgWidth);
        this.imagePlace.setMinHeight(imgWidth);
        this.uploader = new SmallImageUploader(maxFileSize);
        this.imagePlace.add(this.uploader.getImage());
        this.add(this.imagePlace,this.uploader);
    }
    
}

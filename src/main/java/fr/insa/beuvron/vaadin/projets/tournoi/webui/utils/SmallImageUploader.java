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

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.InMemoryUploadHandler;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.server.streams.UploadMetadata;
import java.util.Base64;

/**
 *
 * @author fdebertranddeb01
 */
public class SmallImageUploader extends Upload {

    private byte[] imageData;
    private String imageType;

    public SmallImageUploader(int maxFileSize) {
        InMemoryUploadHandler inMemoryHandler = UploadHandler.inMemory(
                (UploadMetadata metadata, byte[] data) -> {
                    // Get other information about the file.
                    String fileName = metadata.fileName();
                    this.imageType = metadata.contentType();
                    long contentLength = metadata.contentLength();
                    this.imageData = data;
                });
        this.setUploadHandler(inMemoryHandler);
        this.setAcceptedFileTypes("image/png", "image/jpeg", "image/jpg");
        this.setMaxFiles(1);
        this.setMaxFileSize(maxFileSize);
    }


    public Image getImage() {
        if (this.imageData == null) {
            return new Image("noImage", "pas d'image");
        } else {
            String dataUri = "data:"+this.imageType+";base64," +
                    Base64.getEncoder().encodeToString(this.imageData);
            return new Image(dataUri,"image invalide");
        }
    }

}

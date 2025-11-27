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

import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.server.streams.InMemoryUploadHandler;
import com.vaadin.flow.server.streams.UploadHandler;
import com.vaadin.flow.server.streams.UploadMetadata;

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
                    String type = metadata.contentType().trim();
                    long contentLength = metadata.contentLength();
                    if (!type.equals("image/png")
                    && !type.equals("image/jpeg")
                    && !type.equals("image/jpg")) {
                        Utils.outErrorAsNotification(
                                "type de fichier invalide : \"" + type
                                + "\"\ndevrait être \"image/png\", \"image/jpeg\", ou \"image/jpg\"");
                    } else if (contentLength > maxFileSize ) {
                        Utils.outErrorAsNotification(
                                "fichier trop volumineux");
                    } else {
                        this.imageType = metadata.contentType();
                        this.imageData = data;
                    }
                });
        this.setUploadHandler(inMemoryHandler);
        this.setAcceptedFileTypes("image/png", "image/jpeg", "image/jpg");
        this.setMaxFiles(1);
        this.setMaxFileSize(maxFileSize);
        this.addFileRejectedListener((t) -> {
            Utils.outErrorAsNotification("fichier trop volumineux : taille max : "
                    +maxFileSize + "o (~ "+ (maxFileSize/1024) + "ko)");
        });
    }

    /**
     * @return the imageData
     */
    public byte[] getImageData() {
        return imageData;
    }

    /**
     * @return the imageType
     */
    public String getImageType() {
        return imageType;
    }

}

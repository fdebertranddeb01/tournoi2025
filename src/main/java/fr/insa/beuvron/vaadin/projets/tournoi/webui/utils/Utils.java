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

import java.sql.Connection;
import java.sql.SQLException;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

import fr.insa.beuvron.utils.database.ConnectionPool;

/**
 * @author fdebertranddeb01
 */
public class Utils {

  public static void outSuccess(String successMessage) {
    Notification notification = Notification.show(successMessage, 3000, Notification.Position.BOTTOM_START);
    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
  }

  public static void outInfo(String infoMessage) {
    Notification.show(infoMessage, 3000, Notification.Position.BOTTOM_START);
  }

  public static void outError(String errMessage) {
    Notification notification = Notification.show(errMessage, 5000, Notification.Position.MIDDLE);
    notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
  }

  public static void outWarning(String warningMessage) {
    Notification notification = Notification.show(warningMessage, 5000, Notification.Position.MIDDLE);
    notification.addThemeVariants(NotificationVariant.LUMO_WARNING);
  }

  public static void confirm(String entete,String corpsMessage, String confirmText,Runnable onConfirm) {
		ConfirmDialog dialog = new ConfirmDialog();
		dialog.setHeader(entete);
		dialog.setText(corpsMessage);
		dialog.setCancelable(true);
		dialog.setConfirmText(confirmText);
		dialog.addConfirmListener(e -> onConfirm.run());
		dialog.open();
  }

}

package fr.insa.beuvron.vaadin.projets.tournoi.webui.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import com.vaadin.flow.theme.lumo.LumoUtility.AlignItems.Breakpoint.Small;

/** généré par chatGPT 5.0 */
public class ImageResizer {

    public static BufferedImage resizeKeepAspectRatio(BufferedImage source, int targetWidth, int targetHeight) {

        // Calcul du ratio de l’image d’origine
        double ratio = Math.min(
                (double) targetWidth / source.getWidth(),
                (double) targetHeight / source.getHeight()
        );

        // Dimensions finales en conservant les proportions
        int newWidth = (int) (source.getWidth() * ratio);
        int newHeight = (int) (source.getHeight() * ratio);

        // Création d’une image transparente de la taille finale
        BufferedImage output = new BufferedImage(targetWidth, targetHeight, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = output.createGraphics();

        // Active un rendu haute qualité
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Calcul du positionnement pour centrer l’image
        int x = (targetWidth - newWidth) / 2;
        int y = (targetHeight - newHeight) / 2;

        // Dessine l’image redimensionnée
        g2d.drawImage(
                source.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH),
                x, y, null
        );

        g2d.dispose();

        return output;
    }

   
}

package clases;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;

/**
 *
 * @author Claudio Loncharich
 */

public class AnimacionDelLogo {

    private ImageIcon logo;
    private JLabel logoLabel;

    public AnimacionDelLogo(JPanel containerPanel) {
        // Crear un JLabel con máscara circular
        logoLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, getWidth(), getHeight());
                g2.setClip(shape);
                super.paintComponent(g2);
                g2.dispose();
            }
        };

        logo = new ImageIcon(getClass().getResource("/imagenes/alLogin.jpg"));

        // Verificar que la imagen se haya cargado correctamente
        if (logo.getImageLoadStatus() == java.awt.MediaTracker.ERRORED) {
            System.err.println("Error cargando la imagen");
            return;
        }

        logoLabel.setIcon(applyCircularMask(logo.getImage()));
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Añadir la animación directamente al JLabel
        animateLogo();

        // Agregar el JLabel al JPanel proporcionado como parámetro
        containerPanel.setLayout(new BorderLayout());
        containerPanel.add(logoLabel, BorderLayout.CENTER);
    }

    private void animateLogo() {
        Timer animationTimer = new Timer(16, new ActionListener() {
            double angle = 0;
            double frequency = 0.1; // Frecuencia con la que salta el logo
            double jumpHeight = 30; // Ajusta este valor para controlar la altura del salto

            @Override
            public void actionPerformed(ActionEvent e) {
                angle += Math.toRadians(5);
                if (angle >= Math.toRadians(360)) {
                    angle = 0;
                }

                Image rotatedImage = rotateImage(logo.getImage(), angle);
                ImageIcon circularImage = applyCircularMask(rotatedImage);
                logoLabel.setIcon(circularImage);

                // Calcular la altura del salto limitándola para que no sea demasiado alta
                int yOffset = (int) Math.min(jumpHeight * Math.sin(frequency * angle), jumpHeight);

                // Ajustar la posición X e Y para centrar horizontal y verticalmente
                int centerX = (logoLabel.getParent().getWidth() - circularImage.getIconWidth()) / 2;
                int centerY = (logoLabel.getParent().getHeight() - circularImage.getIconHeight()) / 2;

                logoLabel.setBounds(
                        centerX,
                        centerY, //+ yOffset,
                        circularImage.getIconWidth(),
                        circularImage.getIconHeight()
                );
            }
        });
        animationTimer.setRepeats(true);
        animationTimer.start();
    }

    private ImageIcon applyCircularMask(Image image) {
        int maxDiameter = 50; // Diámetro máximo deseado

        int originalWidth = image.getWidth(null);
        int originalHeight = image.getHeight(null);

        // Calcular el nuevo ancho y alto manteniendo la proporción original
        double scaleFactor = Math.min(1.0, (double) maxDiameter / Math.max(originalWidth, originalHeight));
        int newWidth = (int) (originalWidth * scaleFactor);
        int newHeight = (int) (originalHeight * scaleFactor);

        // Crear una nueva imagen con el tamaño calculado
        BufferedImage maskedImage = new BufferedImage(maxDiameter, maxDiameter, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = maskedImage.createGraphics();
        Ellipse2D.Double shape = new Ellipse2D.Double(0, 0, maxDiameter, maxDiameter);
        g2d.setClip(shape);

        // Dibujar la imagen escalada y centrada en la nuevo espacio circular
        int x = (maxDiameter - newWidth) / 2;
        int y = (maxDiameter - newHeight) / 2;
        g2d.drawImage(image, x, y, newWidth, newHeight, null);

        g2d.dispose();
        return new ImageIcon(maskedImage);
    }

    private Image rotateImage(Image image, double angle) {
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        BufferedImage rotatedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotatedImage.createGraphics();

        g2d.rotate(angle, width / 2, height / 2);
        g2d.drawImage(image, 0, 0, null);

        g2d.dispose();
        return rotatedImage;
    }

  
}

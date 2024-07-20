package clases;

import java.awt.Component;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileView;

/**
 *
 * @author Claudio Loncharich
 */
public class FileChooserHandler {

    private static LookAndFeel previousLF;

    public static void verArchivos(String initialDir) {
        LookAndFeel previousLF = UIManager.getLookAndFeel();

        try {
            // Establecer el LookAndFeel del sistema operativo solo para el JFileChooser
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JFileChooser fileChooser = new JFileChooser(initialDir) {
            @Override
            public void approveSelection() {
                File selectedFile = getSelectedFile();
                if (selectedFile.isFile()) {
                    super.approveSelection();
                } else {
                    JOptionPane.showMessageDialog(this, "Por favor, selecciona un archivo válido.");
                }
            }

            @Override
            public void cancelSelection() {
                super.cancelSelection();
            }
        };

        // Configurar el filtro para restringir la navegación
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setAcceptAllFileFilterUsed(false);

        // Deshabilitar opciones de navegación
        fileChooser.setFileView(new FileView() {
            @Override
            public Boolean isTraversable(File f) {
                // Permitir la navegación solo dentro del directorio inicial
                return f.getAbsolutePath().equals(initialDir);
            }

            @Override
            public Icon getIcon(File f) {
                return UIManager.getIcon(f.isDirectory() ? "FileView.directoryIcon" : "FileView.fileIcon");
            }
        });

        fileChooser.addPropertyChangeListener(event -> {
            if (JFileChooser.DIRECTORY_CHANGED_PROPERTY.equals(event.getPropertyName())
                    || JFileChooser.SELECTED_FILE_CHANGED_PROPERTY.equals(event.getPropertyName())) {
                try {
                    UIManager.setLookAndFeel(previousLF);
                } catch (UnsupportedLookAndFeelException ex) {
                    ex.printStackTrace();
                }
            }
        });

        // Deshabilitar la edición manual de la ruta
        for (Component c : fileChooser.getComponents()) {
            disableNavigation(c);
        }

        // Mostrar el JFileChooser
        int result = fileChooser.showOpenDialog(null);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            try {
                // Abrir el archivo con el programa predeterminado de Windows
                Desktop.getDesktop().open(selectedFile);
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(null, "Error al abrir el archivo: " + ioException.getMessage());
            }
        }

        // Restaurar el LookAndFeel anterior después de cerrar el JFileChooser
        try {
            UIManager.setLookAndFeel(previousLF);
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
    }

    private static void disableNavigation(Component c) {
        if (c instanceof JTextField) {
            c.setEnabled(false);
        } else if (c instanceof JPanel) {
            for (Component child : ((JPanel) c).getComponents()) {
                disableNavigation(child);
            }
        }
    }
    
    public static String cargarArchivo(String ci,String tipo,String ruta){
        String newFileName="";
        LookAndFeel previousLF = UIManager.getLookAndFeel();

        try {
            // Establecer el LookAndFeel del sistema operativo solo para el JFileChooser
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = fileChooser.showDialog(null, "Seleccionar archivo");

        // Restaurar el LookAndFeel anterior
        try {
            UIManager.setLookAndFeel(previousLF);
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            File targetDir = new File(ruta+ "/" + ci);
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }

            // Obtener la fecha y hora actuales
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
            String timestamp = now.format(formatter);

            // Extraer la extensión del archivo original
            String fileName = selectedFile.getName();
            String fileExtension = "";
            int dotIndex = fileName.lastIndexOf('.');
            if (dotIndex > 0 && dotIndex < fileName.length() - 1) {
                fileExtension = fileName.substring(dotIndex);
            }

            newFileName = ci + tipo + timestamp + fileExtension; // Cambia este nombre según tus necesidades
            File targetFile = new File(targetDir, newFileName);

            try {
                Files.copy(selectedFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                JOptionPane.showMessageDialog(null, "Archivo copiado a: " + targetFile.getAbsolutePath());
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(null, "Error al copiar el archivo: " + ioException.getMessage());
            }
        }
        return newFileName;
    }
    
}

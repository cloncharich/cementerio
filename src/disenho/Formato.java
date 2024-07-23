package disenho;

import com.sun.imageio.plugins.common.ImageUtil;
import com.toedter.calendar.JDateChooser;
import java.awt.Component;
import java.awt.Container;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JTextField;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import javax.swing.text.NumberFormatter;

/**
 *
 * @author Claudio loncharich
 */
public class Formato {

    public static void NomenclaturaTelefono(JCheckBox box1, JCheckBox box2, JFormattedTextField field) {
        try {
            if (box1.isSelected()) {
                field.setValue("");
                MaskFormatter formatter1 = new MaskFormatter("(+###) ####-###-###");
                field.setFormatterFactory(new DefaultFormatterFactory(formatter1));
            } else if (box2.isSelected()) {
                field.setValue("");
                MaskFormatter formatter2 = new MaskFormatter("(+##) ####-###-###");
                field.setFormatterFactory(new DefaultFormatterFactory(formatter2));
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    public static void NomenclaturaNumero(JFormattedTextField field) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols();
        symbols.setGroupingSeparator('.');
        symbols.setDecimalSeparator(',');
        DecimalFormat format = new DecimalFormat("#,##0.###", symbols);

        NumberFormatter formatter = new NumberFormatter(format) {
            @Override
            public Object stringToValue(String text) throws ParseException {
                if (text == null || text.isEmpty()) {
                    return null;
                }
                text = text.replaceAll("[^0-9.,]", "");
                return super.stringToValue(text);
            }

            @Override
            public String valueToString(Object value) throws ParseException {
                if (value == null) {
                    return "";
                }
                return super.valueToString(value);
            }
        };

        formatter.setValueClass(Long.class);
        formatter.setAllowsInvalid(false);

        field.setFormatterFactory(new DefaultFormatterFactory(formatter));

        /* field.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                SwingUtilities.invokeLater(() -> {
                    field.setValue(null);
                });
            }
        });*/
    }

    public static void limpiarCampos(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTextField) {
                JTextField textField = (JTextField) comp;
                textField.setText("");
            } else if (comp instanceof Container) {
                limpiarCampos((Container) comp); // Recursivamente limpiar campos dentro de sub-contenedores
            }
        }
    }

    public static Icon icono(String path, int width, int height) {
        Icon img = new ImageIcon(new ImageIcon(ImageUtil.class.getResource(path)).getImage().getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH));
        return img;
    }

    public static boolean verificarCampos(Container container) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTextField) {
                JTextField textField = (JTextField) comp;
                if (textField.getText().trim().isEmpty()) {
                    return false;
                }
            } else if (comp instanceof Container) {
                if (!verificarCampos((Container) comp)) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void habilitarCampos(Container container, boolean habilitar) {
        for (Component comp : container.getComponents()) {
            if (comp instanceof JTextField) {
                JTextField textField = (JTextField) comp;
                textField.setEnabled(habilitar);
            } else if (comp instanceof JFormattedTextField) {
                JFormattedTextField formattedTextField = (JFormattedTextField) comp;
                formattedTextField.setEnabled(habilitar);
            } else if (comp instanceof JCheckBox) {
                JCheckBox checkBox = (JCheckBox) comp;
                checkBox.setEnabled(habilitar);
            } else if (comp instanceof JComboBox) {
                JComboBox<?> comboBox = (JComboBox<?>) comp;
                comboBox.setEnabled(habilitar);
            } else if (comp instanceof JDateChooser) {
                JDateChooser dateChooser = (JDateChooser) comp;
                dateChooser.setEnabled(habilitar);
            } else if (comp instanceof Container) {
                habilitarCampos((Container) comp, habilitar);
            }
        }
    }

}

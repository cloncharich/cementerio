
package clases;

import java.sql.SQLException;
import javax.swing.JOptionPane;

/**
 *
 * @author Claudio Loncharich
 */
public class CustomSQLExceptionHandler {
    
     /**
     * Extrae y retorna el mensaje personalizado de una excepción SQL.
     *
     * @param e La excepción SQL.
     * @return El mensaje personalizado extraído.
     */
    public static String extractCustomMessage(SQLException e) {
        String errorMessage = e.getMessage();
        int whereIndex = errorMessage.indexOf("Where: ");
        if (whereIndex != -1) {
            return errorMessage.substring(0, whereIndex).trim();
        } else {
            return errorMessage;
        }
    }

    /**
     * Muestra el mensaje personalizado de una excepción SQL.
     *
     * @param e La excepción SQL.
     */
    public static void showCustomMessage(SQLException e) {
        String customMessage = extractCustomMessage(e);
        JOptionPane.showMessageDialog(null, customMessage, "Error SQL", JOptionPane.ERROR_MESSAGE);
    }
    
}

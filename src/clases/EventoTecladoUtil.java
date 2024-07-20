
package clases;

import java.awt.event.KeyEvent;


/**
 *
 * @author Claudio Loncharich
 */

public class EventoTecladoUtil {

    public static void convertirAMayusculas(KeyEvent evt) {
       char c = evt.getKeyChar();
        if (Character.isLetter(c) || Character.isSpaceChar(c)) {
            if (Character.isLowerCase(c)) {
                String cad = ("" + c).toUpperCase();
                c = cad.charAt(0);
                evt.setKeyChar(c);
            }
        } else {
            evt.consume();
        }
    
    }

    // Método para restringir la entrada solo a dígitos
    public static void permitirSoloDigitos(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (c < '0' || c > '9') {
            evt.consume();
        }
    }
    
    
    public static void permitirMayusculasYNumeros(KeyEvent evt) {
        char c = evt.getKeyChar();
        if (Character.isLetter(c)) {
            if (Character.isLowerCase(c)) {
                String cad = ("" + c).toUpperCase();
                c = cad.charAt(0);
                evt.setKeyChar(c);
            }
        } else if (!Character.isDigit(c) && c != '.' && c != '-') {
            evt.consume();
        }
    }
    
      public static void permitirNumerosComasYGuiones(KeyEvent evt) {
        char c = evt.getKeyChar();
 
        if (!Character.isDigit(c) && c != ',' && c != '-' && c != 'x') {
            evt.consume();  // Consumir el evento si no es un número, una coma o un guion
        }
    }
    
       
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 *
 * @author Claudio Loncharich
 */
public class EmailValidator {

    public static boolean isEmailValid(String email) {
        // Patrón de expresión regular para validar un correo electrónico
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        
        // Crear un patrón a partir de la expresión regular
        Pattern pattern = Pattern.compile(emailRegex);
        
        // Si el email está vacío, se considera no válido
        if (email == null || email.isEmpty()) {
            return false;
        }

        // Comparar el email con el patrón
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static void validateEmail(JTextField emailField) {
        String email = emailField.getText();
        
        if (isEmailValid(email)) {
            JOptionPane.showMessageDialog(null, "El correo electrónico es válido.", "Validación Exitosa", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "El correo electrónico no es válido. Por favor, ingrese un correo válido.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    
}

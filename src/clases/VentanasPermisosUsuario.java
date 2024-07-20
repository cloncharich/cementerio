/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;


import formularios.principalMenu;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Claudio Loncharich
 */

public class VentanasPermisosUsuario {

    public static List<String> obtenerUsuarioVentana(){
        List<String> ventanas = new ArrayList<>();
        String query = "SELECT nombre_ventana FROM ventana_permiso_usuario(?)";

        try (Connection conn = DatabaseConnector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, principalMenu.usuarioBase);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nombreVentana = rs.getString("nombre_ventana");
                    ventanas.add(nombreVentana);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return ventanas;
    }

    // Método para verificar si el usuario tiene permiso para una ventana específica
    public static boolean permisosVentana(String nombreVentana, List<String> ventanas) {
        return ventanas.contains(nombreVentana);
    }
}



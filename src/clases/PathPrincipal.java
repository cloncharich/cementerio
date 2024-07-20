package clases;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Claudio Loncharich
 */
public class PathPrincipal {

    public static String obtenerPathStorage() {
        String query = "SELECT path_storage "
                + "FROM empresa "
                + "ORDER BY cod_empresa "
                + "LIMIT 1";

        try (Connection conn = DatabaseConnector.getConnection();
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) {
                String valor = rs.getString("path_storage");
                return valor;
            } else {
              
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al ejecutar la consulta SQL: " + e.getMessage());
            return null;
        }
    }

}

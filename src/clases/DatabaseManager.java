
package clases;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


/**
 *
 * @author Claudio Loncharich
 */
public class DatabaseManager {
 private static Connection getConnection() throws SQLException {
        return DatabaseConnector.getConnection(); 
    }

    public static int executeUpdate(String sql, Object... params) throws SQLException {
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Setear los parámetros en la consulta preparada
            for (int i = 0; i < params.length; i++) {
                stmt.setObject(i + 1, params[i]);
            }
            
            // Ejecutar la consulta
            return stmt.executeUpdate();
        }
    }

    public static int insert(String sql, Object... params) throws SQLException {
        return executeUpdate(sql, params);
    }

    public static int update(String sql, Object... params) throws SQLException {
        return executeUpdate(sql, params);
    }

    public static int delete(String sql, Object... params) throws SQLException {
        return executeUpdate(sql, params);
    }
}

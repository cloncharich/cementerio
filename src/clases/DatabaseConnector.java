/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clases;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Claudio Loncharich
 */
public class DatabaseConnector {
    
    private static String url;
    private static String user;
    private static String password;

    public static void initializeConnection(String dbUrl, String dbUser, String dbPassword) {
        url = dbUrl;
        user = dbUser;
        password = dbPassword;
    }

    public static Connection getConnection() throws SQLException {
        if (url == null || user == null || password == null) {
            throw new SQLException("La conexión no está inicializada.");
        }
        return DriverManager.getConnection(url, user, password);
    }
    
}

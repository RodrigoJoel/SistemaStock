package com.lobo24.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL = "jdbc:mysql://localhost:3306/lobo";
    private static final String USER ="rodrigo";      // Usuario admin
    private static final String PASSWORD = "lobo24";  // Contraseña correcta

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}



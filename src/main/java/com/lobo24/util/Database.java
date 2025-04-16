package com.lobo24.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    // Configura con tus credenciales (¡usa el puerto correcto!)
    private static final String URL = "jdbc:mysql://localhost:3506/lobo"; // Puerto 3506 según tu Workbench
    private static final String USER = "root"; // Cambia si usas otro usuario
    private static final String PASSWORD = ""; // Tu contraseña de MySQL

    public static Connection getConnection() throws SQLException {
        try {
            // Carga el driver de MySQL (versión 8.x)
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error al cargar el driver de MySQL", e);
        }
    }

    // Método de prueba para verificar la conexión
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Error de conexión: " + e.getMessage());
            return false;
        }
    }
}
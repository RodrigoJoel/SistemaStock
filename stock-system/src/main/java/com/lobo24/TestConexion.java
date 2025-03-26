package com.lobo24;

import java.sql.*;

public class TestConexion {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/lobo?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String usuario = "root"; // Ajusta si tienes otro usuario
        String contraseña = "Ro372143";  // Ajusta si tienes una contraseña

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conexion = DriverManager.getConnection(url, usuario, contraseña);
            System.out.println("✅ ¡Conexión exitosa a la base de datos!");

            // Prueba: Obtener y mostrar las tablas de la base de datos
            Statement stmt = conexion.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW TABLES");

            System.out.println("📌 Tablas en la base de datos 'lobo':");
            while (rs.next()) {
                System.out.println(" - " + rs.getString(1));
            }

            // Cerrar conexión
            rs.close();
            stmt.close();
            conexion.close();
        } catch (ClassNotFoundException e) {
            System.err.println("❌ Error: Driver no encontrado.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Error SQL: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

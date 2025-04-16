package com.lobo24.database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        System.out.println("=== Probando conexión con usuario admin ===");
        try (Connection conn = DatabaseConnection.getConnection()) {
            System.out.println("Conexión exitosa.");
            System.out.println("Tablas disponibles:");

            DatabaseMetaData metaData = conn.getMetaData();
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            while (tables.next()) {
                System.out.println(" - " + tables.getString("TABLE_NAME"));
            }

        } catch (SQLException e) {
            System.out.println("Error al conectar: " + e.getMessage());
            e.printStackTrace();
        }
    }
}


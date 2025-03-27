package com.lobo24.dao;

import com.lobo24.models.Producto;
import com.lobo24.util.Database;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;

public class ProductoDAO {
    public static ObservableList<Producto> listarProductos() {
        ObservableList<Producto> productos = FXCollections.observableArrayList();
        String sql = "SELECT id, nombre, stock FROM productos"; // Asegúrate de que tu tabla tenga estos campos

        try (Connection conn = Database.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                productos.add(new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getInt("stock")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al cargar productos: " + e.getMessage());
        }
        return productos;
    }
}
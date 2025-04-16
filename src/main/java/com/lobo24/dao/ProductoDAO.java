package com.lobo24.dao;

import com.lobo24.database.DatabaseConnection;
import com.lobo24.models.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductoDAO {
    // Consultas SQL (adaptadas a tu estructura de BD)
    private static final String INSERT_SQL = "INSERT INTO productos (" +
            "codigo_barras, codigo_barras_alternativo, automatico, nombre, " +
            "categoria, marca, precio_venta, precio_compra, porcentaje_incremento, " +
            "control_stock, stock, stock_minimo, unidad_medida, " +
            "permite_fraccionamiento, imagen_path, observaciones" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL = "SELECT * FROM productos";
    private static final String UPDATE_SQL = "UPDATE productos SET ... WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM productos WHERE id = ?";

    // Crear un nuevo producto
    public static boolean crearProducto(Producto producto) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            // Setear parámetros (orden debe coincidir con INSERT_SQL)
            pstmt.setString(1, producto.getCodigoBarras());
            pstmt.setString(2, producto.getCodigoExtra());
            pstmt.setBoolean(3, producto.isAutomatico());
            pstmt.setString(4, producto.getDescripcion());
            pstmt.setString(5, producto.getCategoria());
            pstmt.setString(6, producto.getMarca());
            pstmt.setDouble(7, producto.getPrecioVenta());
            pstmt.setDouble(8, producto.getPrecioCosto());
            pstmt.setDouble(9, producto.getPorcentajeIncremento());
            pstmt.setBoolean(10, producto.isControlStock());
            pstmt.setInt(11, producto.getStockActual());
            pstmt.setInt(12, producto.getStockMinimo());
            pstmt.setString(13, producto.getUnidadMedida());
            pstmt.setBoolean(14, producto.isPermiteFraccionamiento());
            pstmt.setString(15, producto.getImagenPath());
            pstmt.setString(16, producto.getObservaciones());

            int affectedRows = pstmt.executeUpdate();

            // Obtener ID generado
            if (affectedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        producto.setId(rs.getInt(1));
                        return true;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al crear producto: " + e.getMessage());
        }
        return false;
    }


    // Actualizar producto (método parcial, completa los campos necesarios)
    public static boolean actualizarProducto(Producto producto) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {

            pstmt.setString(1, producto.getDescripcion());
            // ... setear otros campos
            pstmt.setInt(10, producto.getId()); // WHERE id=?

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
        }
        return false;
    }

    // Eliminar producto
    public static boolean eliminarProducto(int id) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(DELETE_SQL)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
        }
        return false;
    }
}

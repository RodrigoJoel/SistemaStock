
package com.lobo24.dao;

import com.lobo24.database.DatabaseConnection;
import com.lobo24.models.Producto;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

public class ProductoDAO {
    // Consultas SQL
    private static final String INSERT_SQL = "INSERT INTO productos (" +
            "nombre, codigo_barras, codigo_barras_alternativo, precio_compra, " +
            "precio_venta, stock, stock_minimo, categoria, porcentaje_incremento, automatico" +
            ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ALL_SQL = "SELECT * FROM productos";
    private static final String UPDATE_SQL = "UPDATE productos SET " +
            "nombre = ?, codigo_barras = ?, codigo_barras_alternativo = ?, precio_compra = ?, " +
            "precio_venta = ?, stock = ?, stock_minimo = ?, categoria = ?, porcentaje_incremento = ?, automatico = ? " +
            "WHERE id = ?";
    private static final String DELETE_SQL = "DELETE FROM productos WHERE id = ?";

    // Crear un nuevo producto
    public static boolean crearProducto(Producto producto) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, producto.getDescripcion());
            pstmt.setString(2, producto.getCodigoBarras());
            pstmt.setString(3, producto.getCodigoExtra());
            pstmt.setBigDecimal(4, producto.getPrecioCoste());
            pstmt.setBigDecimal(5, producto.getPrecioVenta());
            pstmt.setInt(6, producto.getStock());
            pstmt.setInt(7, producto.getStockMinimo());
            pstmt.setString(8, producto.getCategoria());
            pstmt.setBigDecimal(9, producto.getIncremento());
            pstmt.setBoolean(10, producto.isAutomatico());

            int affectedRows = pstmt.executeUpdate();

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
            e.printStackTrace();
        }
        return false;
    }

    // Actualizar un producto
    public static boolean actualizarProducto(Producto producto) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(UPDATE_SQL)) {

            pstmt.setString(1, producto.getDescripcion());
            pstmt.setString(2, producto.getCodigoBarras());
            pstmt.setString(3, producto.getCodigoExtra());
            pstmt.setBigDecimal(4, producto.getPrecioCoste());
            pstmt.setBigDecimal(5, producto.getPrecioVenta());
            pstmt.setInt(6, producto.getStock());
            pstmt.setInt(7, producto.getStockMinimo());
            pstmt.setString(8, producto.getCategoria());
            pstmt.setBigDecimal(9, producto.getIncremento());
            pstmt.setBoolean(10, producto.isAutomatico());
            pstmt.setInt(11, producto.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar producto: " + e.getMessage());
            e.printStackTrace();
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
            e.printStackTrace();
        }
        return false;
    }

    // Obtener todos los productos
    public static List<Producto> obtenerProductos() {
        List<Producto> productos = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setDescripcion(rs.getString("nombre"));
                producto.setCodigoBarras(rs.getString("codigo_barras"));
                producto.setCodigoExtra(rs.getString("codigo_barras_alternativo"));
                producto.setPrecioCoste(rs.getBigDecimal("precio_compra"));
                producto.setPrecioVenta(rs.getBigDecimal("precio_venta"));
                producto.setStock(rs.getInt("stock"));
                producto.setStockMinimo(rs.getInt("stock_minimo"));
                producto.setCategoria(rs.getString("categoria"));
                producto.setIncremento(rs.getBigDecimal("porcentaje_incremento"));
                producto.setAutomatico(rs.getBoolean("automatico"));
                productos.add(producto);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener productos: " + e.getMessage());
            e.printStackTrace();
        }
        return productos;
    }

    // Obtener producto por ID
    public static Producto obtenerProductoPorId(int id) {
        String sql = SELECT_ALL_SQL + " WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Producto producto = new Producto();
                    producto.setId(rs.getInt("id"));
                    producto.setDescripcion(rs.getString("nombre"));
                    producto.setCodigoBarras(rs.getString("codigo_barras"));
                    producto.setCodigoExtra(rs.getString("codigo_barras_alternativo"));
                    producto.setPrecioCoste(rs.getBigDecimal("precio_compra"));
                    producto.setPrecioVenta(rs.getBigDecimal("precio_venta"));
                    producto.setStock(rs.getInt("stock"));
                    producto.setStockMinimo(rs.getInt("stock_minimo"));
                    producto.setCategoria(rs.getString("categoria"));
                    producto.setIncremento(rs.getBigDecimal("porcentaje_incremento"));
                    producto.setAutomatico(rs.getBoolean("automatico"));
                    return producto;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener producto por ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
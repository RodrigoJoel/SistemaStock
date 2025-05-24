package com.lobo24.dao;

import com.lobo24.models.Cliente;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {
    private static final String URL = "jdbc:mysql://localhost:3306/lobo";
    private static final String USER = "rodrigo";
    private static final String PASS = "lobo24";

    // Crear un nuevo cliente
    public static boolean crearCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, apellido, codigo, dni, telefono, email, saldo, tipo_cliente, direccion, localidad, provincia, recibe_notificaciones, ultima_compra, observaciones) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellido());
            stmt.setString(3, cliente.getCodigo());
            stmt.setString(4, cliente.getDni());
            stmt.setString(5, cliente.getTelefono());
            stmt.setString(6, cliente.getEmail());
            stmt.setDouble(7, cliente.getSaldo());
            stmt.setString(8, cliente.getTipoCliente());
            stmt.setString(9, cliente.getDireccion());
            stmt.setString(10, cliente.getLocalidad());
            stmt.setString(11, cliente.getProvincia());

            // Campo booleano con valor por defecto si es null
            boolean recibeNotif = cliente.isRecibeNotificaciones();
            stmt.setBoolean(12, recibeNotif);

            // Campo fecha (convertir String a Date o manejar null)
            if (cliente.getUltimaCompra() != null && !cliente.getUltimaCompra().isEmpty()) {
                stmt.setDate(13, Date.valueOf(LocalDate.parse(cliente.getUltimaCompra())));
            } else {
                stmt.setNull(13, Types.DATE);
            }

            stmt.setString(14, cliente.getObservaciones());

            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al crear cliente: " + e.getMessage());
            return false;
        }
    }

    // Obtener todos los clientes
    public static List<Cliente> obtenerTodosClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes ORDER BY nombre";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("nombre"),
                        rs.getString("codigo"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getDouble("saldo"),
                        rs.getString("tipo_cliente"),
                        rs.getString("ultima_compra"),
                        rs.getString("direccion")
                );
                cliente.setApellido(rs.getString("apellido"));
                cliente.setDni(rs.getString("dni"));
                cliente.setLocalidad(rs.getString("localidad"));
                cliente.setProvincia(rs.getString("provincia"));
                cliente.setFechaNacimiento(rs.getString("fecha_nacimiento"));
                cliente.setRecibeNotificaciones(rs.getBoolean("recibe_notificaciones"));
                cliente.setObservaciones(rs.getString("observaciones"));
                cliente.setId(rs.getInt("id"));

                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener clientes: " + e.getMessage());
        }
        return clientes;
    }

    // Buscar clientes por nombre, código o teléfono
    public static List<Cliente> buscarClientes(String criterio) {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes WHERE nombre LIKE ? OR codigo LIKE ? OR telefono LIKE ? ORDER BY nombre";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String likeCriterio = "%" + criterio + "%";
            stmt.setString(1, likeCriterio);
            stmt.setString(2, likeCriterio);
            stmt.setString(3, likeCriterio);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("nombre"),
                        rs.getString("codigo"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getDouble("saldo"),
                        rs.getString("tipo_cliente"),
                        rs.getString("ultima_compra"),
                        rs.getString("direccion")
                );
                cliente.setApellido(rs.getString("apellido"));
                cliente.setDni(rs.getString("dni"));
                cliente.setLocalidad(rs.getString("localidad"));
                cliente.setProvincia(rs.getString("provincia"));
                cliente.setFechaNacimiento(rs.getString("fecha_nacimiento"));
                cliente.setRecibeNotificaciones(rs.getBoolean("recibe_notificaciones"));
                cliente.setObservaciones(rs.getString("observaciones"));
                cliente.setId(rs.getInt("id"));

                clientes.add(cliente);
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar clientes: " + e.getMessage());
        }
        return clientes;
    }

    public static boolean actualizarCliente(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre = ?, apellido = ?, dni = ?, telefono = ?, "
                + "email = ?, saldo = ?, tipo_cliente = ?, direccion = ?, localidad = ?, "
                + "provincia = ?, fecha_nacimiento = ?, recibe_notificaciones = ?, "
                + "observaciones = ? WHERE codigo = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Establecer parámetros
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellido());
            stmt.setString(3, cliente.getDni());
            stmt.setString(4, cliente.getTelefono());
            stmt.setString(5, cliente.getEmail());
            stmt.setDouble(6, cliente.getSaldo());
            stmt.setString(7, cliente.getTipoCliente());
            stmt.setString(8, cliente.getDireccion());
            stmt.setString(9, cliente.getLocalidad());
            stmt.setString(10, cliente.getProvincia());
            stmt.setDate(11, cliente.getFechaNacimiento() != null ?
                    Date.valueOf(cliente.getFechaNacimiento()) : null);
            stmt.setBoolean(12, cliente.isRecibeNotificaciones());
            stmt.setString(13, cliente.getObservaciones());
            stmt.setString(14, cliente.getCodigo());

            int filasAfectadas = stmt.executeUpdate();

            if (filasAfectadas == 0) {
                System.err.println("Advertencia: Ningún registro fue actualizado para código: " + cliente.getCodigo());
            }

            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error SQL al actualizar cliente:");
            e.printStackTrace();
            return false;
        }
    }

    // Eliminar cliente por código
    public static boolean eliminarClientePorCodigo(String codigo) {
        String sql = "DELETE FROM clientes WHERE codigo = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente por código: " + e.getMessage());
            return false;
        }
    }

    // Obtener cliente por código
    public static Cliente obtenerClientePorCodigo(String codigo) {
        String sql = "SELECT * FROM clientes WHERE codigo = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigo);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getString("nombre"),
                        rs.getString("codigo"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getDouble("saldo"),
                        rs.getString("tipo_cliente"),
                        rs.getString("ultima_compra"),
                        rs.getString("direccion")
                );
                cliente.setApellido(rs.getString("apellido"));
                cliente.setDni(rs.getString("dni"));
                cliente.setLocalidad(rs.getString("localidad"));
                cliente.setProvincia(rs.getString("provincia"));
                cliente.setFechaNacimiento(rs.getString("fecha_nacimiento"));
                cliente.setRecibeNotificaciones(rs.getBoolean("recibe_notificaciones"));
                cliente.setObservaciones(rs.getString("observaciones"));
                cliente.setId(rs.getInt("id"));

                return cliente;
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener cliente: " + e.getMessage());
        }
        return null;
    }

    // Obtener el próximo ID disponible
    public static int obtenerProximoId() {
        String sql = "SELECT MAX(id) FROM clientes";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            return rs.next() ? rs.getInt(1) + 1 : 1;

        } catch (SQLException e) {
            System.err.println("Error al obtener próximo ID: " + e.getMessage());
            return 1;
        }
    }

    // Actualizar saldo del cliente
    public static boolean actualizarSaldo(int idCliente, double nuevoSaldo) {
        String sql = "UPDATE clientes SET saldo = ? WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDouble(1, nuevoSaldo);
            stmt.setInt(2, idCliente);
            return stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            System.err.println("Error al actualizar saldo: " + e.getMessage());
            return false;
        }
    }
}

package com.lobo24.auth;

import com.lobo24.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthService {

    public static String autenticarUsuario(String username, String password) {
        String sql = "SELECT role FROM usuarios WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getString("role"); // Retorna el rol del usuario autenticado
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Retorna null si la autenticación falla
    }
}

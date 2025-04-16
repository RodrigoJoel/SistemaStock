package com.lobo24.auth;

import com.lobo24.database.DatabaseConnection;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class RecoveryCodeGenerator {

    public static String generateCode() {
        Random random = new Random();
        int code = 100000 + random.nextInt(900000); // Código de 6 dígitos
        return String.valueOf(code);
    }

    public static boolean saveRecoveryCode(int userId, String code) {
        String sql = "INSERT INTO recovery_codes (user_id, code) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("Intentando insertar código: " + code + " para el usuario: " + userId);

            stmt.setInt(1, userId);
            stmt.setString(2, code);
            int rowsInserted = stmt.executeUpdate();

            if (rowsInserted > 0) {
                System.out.println("Código guardado correctamente.");
            } else {
                System.out.println("No se insertó ningún registro.");
            }

            return rowsInserted > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}

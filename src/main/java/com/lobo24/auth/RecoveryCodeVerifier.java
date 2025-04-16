package com.lobo24.auth;

import com.lobo24.database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;

public class RecoveryCodeVerifier {

    public static Integer verifyCode(String code) {
        String sql = "SELECT user_id, created_at FROM recovery_codes WHERE code = ? ORDER BY created_at DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, code);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp createdAt = rs.getTimestamp("created_at");
                    LocalDateTime createdTime = createdAt.toLocalDateTime();
                    LocalDateTime now = LocalDateTime.now();

                    if (Duration.between(createdTime, now).toMinutes() <= 10) {
                        return rs.getInt("user_id"); // Código válido
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null; // Código inválido o vencido
    }
}

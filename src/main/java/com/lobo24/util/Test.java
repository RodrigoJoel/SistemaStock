package com.lobo24.util;

import com.lobo24.auth.RecoveryCodeGenerator;

public class Test {
    public static void main(String[] args) {
        int testUserId = 1;  // Asegúrate de que este usuario exista en la BD
        String recoveryCode = RecoveryCodeGenerator.generateCode();

        boolean success = RecoveryCodeGenerator.saveRecoveryCode(testUserId, recoveryCode);

        if (success) {
            System.out.println("🎉 Prueba exitosa: el código se guardó.");
        } else {
            System.out.println("❌ Error: el código no se guardó.");
        }
    }
}

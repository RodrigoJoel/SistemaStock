package com.lobo24.auth;

import java.util.Scanner;

public class TestPasswordRecovery {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Ingrese el código de recuperación recibido: ");
        String code = scanner.nextLine();

        Integer userId = RecoveryCodeVerifier.verifyCode(code);
        if (userId != null) {
            System.out.println("Código válido. Ahora puede ingresar una nueva contraseña.");
            System.out.print("Nueva contraseña: ");
            String nuevaClave = scanner.nextLine();

            boolean updated = PasswordUpdater.updatePassword(userId, nuevaClave);
            if (updated) {
                System.out.println("Contraseña actualizada con éxito ✅");
            } else {
                System.out.println("❌ Error al actualizar la contraseña");
            }

        } else {
            System.out.println("⚠️ Código inválido o vencido");
        }

        scanner.close();
    }
}

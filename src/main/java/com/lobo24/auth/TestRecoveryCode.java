package com.lobo24.auth;

public class TestRecoveryCode {
    public static void main(String[] args) {
        String code = RecoveryCodeGenerator.generateCode();
        System.out.println("Código de recuperación generado: " + code);
    }
}

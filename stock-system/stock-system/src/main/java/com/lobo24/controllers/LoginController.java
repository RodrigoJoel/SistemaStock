package com.lobo24.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Button btnEntrar;

    @FXML
    private void initialize() {
        btnEntrar.setOnAction(event -> validarLogin());
    }

    private void validarLogin() {
        String usuario = txtUsuario.getText();
        String password = txtPassword.getText();

        // Aquí iría la lógica para validar con la BD
        System.out.println("Intento de login: " + usuario + "/" + password);

        // Ejemplo básico de validación
        if (!usuario.isEmpty() && !password.isEmpty()) {
            // Abrir la ventana principal (MainView)
            System.out.println("Login exitoso!");
        } else {
            new Alert(Alert.AlertType.ERROR, "Usuario/contraseña incorrectos").show();
        }
    }
}
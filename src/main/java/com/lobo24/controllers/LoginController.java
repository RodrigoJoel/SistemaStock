package com.lobo24.controllers;

import com.lobo24.auth.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;
    @FXML private Label lblIP;

    @FXML
    public void initialize() {
        mostrarDireccionIP();

        // ✅ Permitir login con Enter en los campos de usuario y contraseña
        txtUsuario.setOnKeyPressed(this::handleEnterKey);
        txtPassword.setOnKeyPressed(this::handleEnterKey);
    }

    private void mostrarDireccionIP() {
        try {
            String ip = InetAddress.getLocalHost().getHostAddress();
            lblIP.setText("Conectado desde: " + ip);
        } catch (UnknownHostException e) {
            lblIP.setText("Dirección IP no disponible");
        }
    }

    @FXML
    private void handleLogin() {
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText().trim();

        if (camposValidos(usuario, password)) {
            String role = AuthService.autenticarUsuario(usuario, password);

            if (role != null) {
                abrirVentanaPrincipal(usuario, role);
            } else {
                mostrarError("Credenciales incorrectas");
            }
        }
    }

    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }

    private boolean camposValidos(String usuario, String password) {
        if (usuario.isEmpty() || password.isEmpty()) {
            mostrarError("Complete todos los campos");
            return false;
        }
        return true;
    }

    private void abrirVentanaPrincipal(String usuario, String role) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MainView.fxml"));
            Parent root = loader.load();

            MainController mainController = loader.getController();
            mainController.initData(usuario, role);

            Stage stage = new Stage();
            mainController.setStage(stage);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Panel Principal - Sistema de Ventas Lobo24");
            stage.setMaximized(true);
            stage.setMinWidth(800);
            stage.setMinHeight(600);

            ((Stage) txtUsuario.getScene().getWindow()).close();
            stage.show();
        } catch (IOException e) {
            mostrarError("Error al cargar la interfaz principal");
            e.printStackTrace();
        }
    }

    private void mostrarError(String mensaje) {
        lblError.setText(mensaje);
    }

    @FXML
    private void handleRecuperarPassword() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/RecuperarPassword.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Recuperar Contraseña");
            stage.setScene(new Scene(root));
            stage.setResizable(false);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

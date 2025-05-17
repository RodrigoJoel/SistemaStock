package com.lobo24.controllers;

import com.lobo24.util.RecoveryCodeStore;
import com.lobo24.database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConfirmarCodigoController {

    @FXML private TextField txtCodigo;
    @FXML private PasswordField txtNuevaClave;
    @FXML private PasswordField txtConfirmarClave;
    @FXML private Label lblMensaje;

    private String usuarioCorreo; // Se debe pasar desde la ventana anterior

    public void setUsuarioCorreo(String usuarioCorreo) {
        this.usuarioCorreo = usuarioCorreo;
    }

    @FXML
    private void handleCambiarClave() {
        String codigoIngresado = txtCodigo.getText().trim();
        String nuevaClave = txtNuevaClave.getText();
        String confirmarClave = txtConfirmarClave.getText();

        if (!RecoveryCodeStore.validarCodigo(usuarioCorreo, codigoIngresado)) {
            lblMensaje.setText("Código incorrecto.");
            return;
        }

        if (nuevaClave.isEmpty() || confirmarClave.isEmpty()) {
            lblMensaje.setText("Complete los campos de contraseña.");
            return;
        }

        if (!nuevaClave.equals(confirmarClave)) {
            lblMensaje.setText("Las contraseñas no coinciden.");
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "UPDATE usuarios SET password = ? WHERE username = ? OR correo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nuevaClave);
            stmt.setString(2, usuarioCorreo);
            stmt.setString(3, usuarioCorreo);
            int filas = stmt.executeUpdate();

            if (filas > 0) {
                lblMensaje.setStyle("-fx-text-fill: #28a745;");
                lblMensaje.setText("Contraseña actualizada con éxito.");
                RecoveryCodeStore.borrarCodigo(usuarioCorreo);

                // Cerramos la ventana actual
                Stage currentStage = (Stage) lblMensaje.getScene().getWindow();
                currentStage.close();

                // Abrimos ventana login
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Login");
                stage.setScene(new Scene(root));
                stage.show();

            } else {
                lblMensaje.setText("No se encontró el usuario.");
            }
        } catch (Exception e) {
            lblMensaje.setText("Error al actualizar la contraseña.");
            e.printStackTrace();
        }
    }

}



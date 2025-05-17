package com.lobo24.controllers;

import com.lobo24.util.EmailSender;
import com.lobo24.util.RecoveryCodeStore;
import com.lobo24.database.DatabaseConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RecuperarPasswordController {

    @FXML private TextField txtUsuarioCorreo;
    @FXML private Label lblMensaje;

    @FXML
    private void handleEnviarCodigo() {
        String usuarioCorreo = txtUsuarioCorreo.getText().trim();

        if (usuarioCorreo.isEmpty()) {
            lblMensaje.setText("Ingrese su usuario o correo.");
            return;
        }

        // Obtener correo real
        String correoReal = obtenerCorreoPorUsuarioOCorreo(usuarioCorreo);
        if (correoReal == null) {
            lblMensaje.setText("Usuario o correo no encontrado.");
            return;
        }

        String codigo = generarCodigo();

        boolean enviado = EmailSender.enviarCodigo(correoReal, codigo);
        if (enviado) {
            lblMensaje.setText("Código enviado. Revise su correo.");

            RecoveryCodeStore.guardarCodigo(correoReal, codigo);

            try {
                URL location = getClass().getResource("/views/confirmarCodigo.fxml");
                if (location == null) {
                    throw new IOException("No se pudo encontrar el archivo FXML: /views/confirmarCodigo.fxml");
                }

                FXMLLoader loader = new FXMLLoader(location);
                Parent root = loader.load();
                ConfirmarCodigoController ctrl = loader.getController();
                ctrl.setUsuarioCorreo(correoReal); // Pasamos el correo real

                // Cerramos la ventana actual
                Stage currentStage = (Stage) txtUsuarioCorreo.getScene().getWindow();
                currentStage.close();

                Stage stage = new Stage();
                stage.setTitle("Confirmar Código");
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                lblMensaje.setText("Error al cargar la ventana: " + e.getMessage());
            }

        } else {
            lblMensaje.setText("Error al enviar el código.");
        }
    }

    private String generarCodigo() {
        int codigo = (int) (Math.random() * 900000) + 100000; // Código de 6 dígitos
        return String.valueOf(codigo);
    }

    private String obtenerCorreoPorUsuarioOCorreo(String usuarioCorreo) {
        String sql = "SELECT correo FROM usuarios WHERE username = ? OR correo = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuarioCorreo);
            stmt.setString(2, usuarioCorreo);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("correo");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}



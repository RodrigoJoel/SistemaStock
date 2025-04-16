package com.lobo24.controllers;

import com.lobo24.util.EmailSender;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RecuperarPasswordController {

    @FXML private TextField txtUsuarioCorreo;
    @FXML private Label lblMensaje;

    @FXML
    private void handleEnviarCodigo() {
        String usuarioCorreo = txtUsuarioCorreo.getText().trim();

        if (usuarioCorreo.isEmpty()) {
            lblMensaje.setText("Ingrese su usuario o correo");
        } else {
            String codigo = generarCodigo();

            boolean enviado = EmailSender.enviarCodigo(usuarioCorreo, codigo);

            if (enviado) {
                lblMensaje.setText("Código enviado. Revise su correo.");
                // Aquí luego podemos abrir una nueva ventana para que ingrese el código
            } else {
                lblMensaje.setText("Error al enviar el código.");
            }
        }
    }

    private String generarCodigo() {
        int codigo = (int) (Math.random() * 900000) + 100000; // Código de 6 dígitos
        return String.valueOf(codigo);
    }

}

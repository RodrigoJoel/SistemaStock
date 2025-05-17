package com.lobo24.controllers;

import com.lobo24.database.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.scene.control.ComboBox;



import com.lobo24.auth.AuthService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class LoginController {

    @FXML private ComboBox<String> cmbUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblError;
    @FXML private Label lblIP;
    @FXML private ComboBox<String> cmbServidor;



    @FXML
    public void initialize() {
        mostrarDireccionIP();
        cmbUsuario.setEditable(false);
        cargarUsuarios();

        // Permitir login con Enter en usuario y contraseña
        cmbUsuario.setOnKeyPressed(this::handleEnterKey);
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

    private void cargarUsuarios() {
        String query = "SELECT username FROM usuarios ORDER BY username ASC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                String nombre = rs.getString("username");
                cmbUsuario.getItems().add(nombre);
            }

        } catch (SQLException e) {
            lblError.setText("Error al cargar usuarios");
            e.printStackTrace();
        }
    }


    @FXML
    private void handleLogin() {
        String usuario = cmbUsuario.getValue(); // antes: txtUsuario.getText().trim();
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

            // Cerrar ventana de login
            ((Stage) cmbUsuario.getScene().getWindow()).close();
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

            // Cerrar ventana actual de login
            Stage currentStage = (Stage) cmbUsuario.getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarOpcionesServidor() {
        Stage ventana = new Stage();
        ventana.initModality(Modality.APPLICATION_MODAL);
        ventana.setTitle("¿Dónde se encuentra el servidor del programa?");

        VBox layout = new VBox(10);
        layout.setPadding(new javafx.geometry.Insets(15));

        ToggleGroup opciones = new ToggleGroup();

        RadioButton opcion1 = new RadioButton("En esta misma computadora");
        RadioButton opcion2 = new RadioButton("Localizar el servidor automáticamente");
        RadioButton opcion3 = new RadioButton("Informe el nombre de red o IP del servidor");

        opcion1.setToggleGroup(opciones);
        opcion2.setToggleGroup(opciones);
        opcion3.setToggleGroup(opciones);

        TextField txtIP = new TextField();
        txtIP.setPromptText("Ingrese IP o nombre del servidor");
        txtIP.setDisable(true);

        opcion3.selectedProperty().addListener((obs, oldVal, newVal) -> {
            txtIP.setDisable(!newVal);
        });

        Button btnAceptar = new Button("Aceptar");
        btnAceptar.setOnAction(e -> {
            if (opcion1.isSelected()) {
                cmbServidor.setValue("Local");
            } else if (opcion2.isSelected()) {
                cmbServidor.setValue("Auto");
            } else if (opcion3.isSelected()) {
                cmbServidor.setValue(txtIP.getText());
            }
            ventana.close();
        });

        layout.getChildren().addAll(
                new Label("¿Dónde se encuentra el servidor del programa?"),
                opcion1, opcion2, opcion3, txtIP, btnAceptar
        );

        Scene scene = new Scene(layout);
        ventana.setScene(scene);
        ventana.showAndWait();
    }
}

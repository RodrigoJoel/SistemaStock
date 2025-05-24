package com.lobo24.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class NuevoUsuarioController {

    @FXML private TextField txtInicial;
    @FXML private TextField txtNombre;
    @FXML private TextField txtEmail;
    @FXML private ComboBox<String> cmbRol;

    private Stage dialogStage;
    private boolean guardado = false;

    @FXML
    public void initialize() {
        // Llenar combo box de roles
        cmbRol.getItems().addAll("Admin", "Usuario", "Vendedor", "Gerente");
        cmbRol.getSelectionModel().selectFirst();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public boolean isGuardado() {
        return guardado;
    }

    @FXML
    private void guardarUsuario() {
        if (esValido()) {
            guardado = true;
            dialogStage.close();
        }
    }

    @FXML
    private void cancelar() {
        dialogStage.close();
    }

    @FXML
    private void gestionarPermisos() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PermisosUsuario.fxml"));
            Parent root = loader.load();

            // Configurar el diálogo
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Gestión de Permisos");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(txtInicial.getScene().getWindow());
            dialogStage.setScene(new Scene(root));

            // Configurar el controlador
            PermisosUsuarioController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setUsuario(txtNombre.getText().isEmpty() ? "Nuevo Usuario" : txtNombre.getText());

            // Mostrar diálogo
            dialogStage.showAndWait();

        } catch (IOException e) {
            mostrarAlerta("Error", "No se pudo abrir la ventana de permisos", Alert.AlertType.ERROR);
        }
    }

    private boolean esValido() {
        String error = "";

        if (txtInicial.getText() == null || txtInicial.getText().isEmpty()) {
            error += "Inicial no válida!\n";
        }

        if (txtNombre.getText() == null || txtNombre.getText().isEmpty()) {
            error += "Nombre no válido!\n";
        }

        if (txtEmail.getText() == null || !txtEmail.getText().contains("@")) {
            error += "Email no válido!\n";
        }

        if (error.isEmpty()) {
            return true;
        } else {
            mostrarAlerta("Error", error, Alert.AlertType.ERROR);
            return false;
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Métodos para obtener los datos del formulario
    public String getInicial() {
        return txtInicial.getText();
    }

    public String getNombre() {
        return txtNombre.getText();
    }

    public String getEmail() {
        return txtEmail.getText();
    }

    public String getRol() {
        return cmbRol.getValue();
    }
}
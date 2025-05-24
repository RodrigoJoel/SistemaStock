package com.lobo24.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class PermisosUsuarioController {

    // Controles de pestañas
    @FXML private TabPane tabPane;

    // Controles de permisos
    @FXML private CheckBox chkPermitirUso;
    @FXML private CheckBox chkLimitarDescuentos;
    @FXML private TextField txtMaxDescuento;
    @FXML private CheckBox chkEsAdmin;

    // Recursos
    @FXML private CheckBox chkCaja;
    @FXML private CheckBox chkClientes;
    @FXML private CheckBox chkGeneral;
    @FXML private CheckBox chkEstadisticas;
    @FXML private CheckBox chkProveedores;
    @FXML private CheckBox chkProductos;
    @FXML private CheckBox chkStock;
    @FXML private CheckBox chkVentas;
    @FXML private CheckBox chkCotizacion;
    @FXML private CheckBox chkCuentasPagar;

    private Stage dialogStage;
    private boolean guardado = false;
    private String usuarioActual;

    @FXML
    public void initialize() {
        // Configurar comportamiento de los controles
        chkEsAdmin.selectedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                deshabilitarTodosChecks();
            } else {
                habilitarTodosChecks();
            }
        });

        chkLimitarDescuentos.selectedProperty().addListener((obs, oldVal, newVal) -> {
            txtMaxDescuento.setDisable(!newVal);
        });

        txtMaxDescuento.setDisable(true);
    }

    private void deshabilitarTodosChecks() {
        chkCaja.setDisable(true);
        chkClientes.setDisable(true);
        chkGeneral.setDisable(true);
        chkEstadisticas.setDisable(true);
        chkProveedores.setDisable(true);
        chkProductos.setDisable(true);
        chkStock.setDisable(true);
        chkVentas.setDisable(true);
        chkCotizacion.setDisable(true);
        chkCuentasPagar.setDisable(true);
    }

    private void habilitarTodosChecks() {
        chkCaja.setDisable(false);
        chkClientes.setDisable(false);
        chkGeneral.setDisable(false);
        chkEstadisticas.setDisable(false);
        chkProveedores.setDisable(false);
        chkProductos.setDisable(false);
        chkStock.setDisable(false);
        chkVentas.setDisable(false);
        chkCotizacion.setDisable(false);
        chkCuentasPagar.setDisable(false);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setUsuario(String usuario) {
        this.usuarioActual = usuario;
        cargarPermisosUsuario(usuario);
    }

    private void cargarPermisosUsuario(String usuario) {
        // Aquí deberías cargar los permisos reales desde tu base de datos
        // Estos son valores de ejemplo

        // Ejemplo: Si es admin, marcar la casilla correspondiente
        if (usuario.equals("Admin")) {
            chkEsAdmin.setSelected(true);
            deshabilitarTodosChecks();
        } else {
            // Simular algunos permisos para usuarios normales
            chkCaja.setSelected(true);
            chkVentas.setSelected(true);
            chkProductos.setSelected(true);
        }
    }

    @FXML
    private void guardarPermisos() {
        if (validarDatos()) {
            // Aquí deberías guardar los permisos en tu base de datos
            System.out.println("Guardando permisos para: " + usuarioActual);

            // Configuración general
            System.out.println("Permitir uso: " + chkPermitirUso.isSelected());
            System.out.println("Limitar descuentos: " + chkLimitarDescuentos.isSelected());
            if (chkLimitarDescuentos.isSelected()) {
                System.out.println("Máximo descuento: " + txtMaxDescuento.getText() + "%");
            }
            System.out.println("Es admin: " + chkEsAdmin.isSelected());

            // Recursos
            System.out.println("\nRecursos permitidos:");
            System.out.println("Caja: " + chkCaja.isSelected());
            System.out.println("Clientes: " + chkClientes.isSelected());
            System.out.println("General: " + chkGeneral.isSelected());
            System.out.println("Estadísticas: " + chkEstadisticas.isSelected());
            System.out.println("Proveedores: " + chkProveedores.isSelected());
            System.out.println("Productos: " + chkProductos.isSelected());
            System.out.println("Stock: " + chkStock.isSelected());
            System.out.println("Ventas: " + chkVentas.isSelected());
            System.out.println("Cotización: " + chkCotizacion.isSelected());
            System.out.println("Cuentas a pagar: " + chkCuentasPagar.isSelected());

            guardado = true;
            dialogStage.close();
        }
    }

    private boolean validarDatos() {
        if (chkLimitarDescuentos.isSelected()) {
            try {
                double descuento = Double.parseDouble(txtMaxDescuento.getText());
                if (descuento < 0 || descuento > 100) {
                    mostrarAlerta("Error", "El porcentaje de descuento debe estar entre 0 y 100", Alert.AlertType.ERROR);
                    return false;
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Error", "Porcentaje de descuento no válido", Alert.AlertType.ERROR);
                return false;
            }
        }
        return true;
    }

    @FXML
    private void cancelar() {
        dialogStage.close();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public boolean isGuardado() {
        return guardado;
    }
}
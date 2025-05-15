package com.lobo24.controllers;

import com.lobo24.models.Factura;

import com.lobo24.util.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.function.Consumer;

public class NuevaFacturaDialogController {

    @FXML private ComboBox<String> comboStatus;
    @FXML private DatePicker dateFechaVencimiento;
    @FXML private TextField txtValor;
    @FXML private TextField txtReferente;
    @FXML private TextField txtProveedor;
    @FXML private ComboBox<String> comboTipo;
    @FXML private TextArea txtNotas;
    @FXML private Button btnCancelar;
    @FXML private Button btnGuardar;

    private Consumer<Factura> onFacturaGuardada;

    @FXML
    public void initialize() {
        // Inicializar valores por defecto
        comboStatus.setValue("Pendiente");
        dateFechaVencimiento.setValue(LocalDate.now().plusDays(30)); // Por defecto a 30 días
        comboTipo.setValue("Factura");

        // Configurar validación de valor (solo números y punto decimal)
        txtValor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                txtValor.setText(oldValue);
            }
        });

        // Configurar botones
        btnCancelar.setOnAction(event -> cerrarVentana());
        btnGuardar.setOnAction(event -> guardarFactura());
    }

    /**
     * Establece el callback a invocar cuando se guarda una factura
     * @param consumer Consumer que recibirá la factura guardada
     */
    public void setOnFacturaGuardada(Consumer<Factura> consumer) {
        this.onFacturaGuardada = consumer;
    }

    /**
     * Guarda la factura con los datos ingresados
     */
    private void guardarFactura() {
        if (!validarDatos()) {
            return;
        }

        try {
            String status = comboStatus.getValue();
            LocalDate fechaVencimiento = dateFechaVencimiento.getValue();
            double valor = Double.parseDouble(txtValor.getText());
            String referente = txtReferente.getText();
            String proveedor = txtProveedor.getText();
            String tipo = comboTipo.getValue();

            // Calcular días hasta vencimiento
            int diasVencimiento = 0; // Por simplicidad, se puede implementar el cálculo real

            // Crear nueva factura
            Factura nuevaFactura = new Factura(status, fechaVencimiento, valor,
                    referente, proveedor, tipo, diasVencimiento);

            // Notificar que se ha guardado la factura
            if (onFacturaGuardada != null) {
                onFacturaGuardada.accept(nuevaFactura);
            }

            // Cerrar ventana
            cerrarVentana();

        } catch (NumberFormatException e) {
            AlertUtils.mostrarError("Error", "Valor inválido",
                    "Por favor, ingrese un número válido para el valor.");
        } catch (Exception e) {
            AlertUtils.mostrarError("Error", "Error al guardar", e.getMessage());
        }
    }

    /**
     * Valida que los datos ingresados sean correctos y completos
     * @return true si los datos son válidos, false en caso contrario
     */
    private boolean validarDatos() {
        if (dateFechaVencimiento.getValue() == null) {
            AlertUtils.mostrarError("Datos incompletos", "Fecha requerida",
                    "Por favor, seleccione una fecha de vencimiento.");
            return false;
        }

        if (txtValor.getText() == null || txtValor.getText().isEmpty()) {
            AlertUtils.mostrarError("Datos incompletos", "Valor requerido",
                    "Por favor, ingrese el valor de la factura.");
            return false;
        }

        if (txtReferente.getText() == null || txtReferente.getText().isEmpty()) {
            AlertUtils.mostrarError("Datos incompletos", "Referente requerido",
                    "Por favor, ingrese el referente de la factura.");
            return false;
        }

        if (txtProveedor.getText() == null || txtProveedor.getText().isEmpty()) {
            AlertUtils.mostrarError("Datos incompletos", "Proveedor requerido",
                    "Por favor, ingrese el proveedor de la factura.");
            return false;
        }

        return true;
    }

    /**
     * Cierra la ventana del diálogo
     */
    private void cerrarVentana() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
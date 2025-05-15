package com.lobo24.controllers;

import com.lobo24.models.Factura;
import com.lobo24.util.AlertUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Consumer;

public class EditarFacturaDialogController {

    @FXML private ComboBox<String> comboStatus;
    @FXML private DatePicker dateFechaVencimiento;
    @FXML private TextField txtValor;
    @FXML private TextField txtReferente;
    @FXML private TextField txtProveedor;
    @FXML private ComboBox<String> comboTipo;
    @FXML private TextArea txtNotas;
    @FXML private Button btnEliminar;
    @FXML private Button btnCancelar;
    @FXML private Button btnGuardar;

    private Factura factura;
    private Consumer<Factura> onFacturaEditada;

    @FXML
    public void initialize() {
        // Configurar validación de valor (solo números y punto decimal)
        txtValor.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*\\.?\\d*")) {
                txtValor.setText(oldValue);
            }
        });

        // Configurar botones
        btnCancelar.setOnAction(event -> cerrarVentana());
        btnGuardar.setOnAction(event -> guardarCambios());
        btnEliminar.setOnAction(event -> eliminarFactura());
    }

    /**
     * Establece la factura que se va a editar
     * @param factura Factura a editar
     */
    public void setFactura(Factura factura) {
        this.factura = factura;

        // Cargar datos de la factura en los controles
        comboStatus.setValue(factura.getStatus());
        dateFechaVencimiento.setValue(factura.getFechaVencimiento());
        txtValor.setText(String.valueOf(factura.getValor()));
        txtReferente.setText(factura.getReferente());
        txtProveedor.setText(factura.getProveedor());
        comboTipo.setValue(factura.getTipo());
    }

    /**
     * Establece el callback a invocar cuando se guarda una factura editada
     * @param consumer Consumer que recibirá la factura editada
     */
    public void setOnFacturaEditada(Consumer<Factura> consumer) {
        this.onFacturaEditada = consumer;
    }

    /**
     * Guarda los cambios realizados a la factura
     */
    private void guardarCambios() {
        if (!validarDatos()) {
            return;
        }

        try {
            // Actualizar datos de la factura
            factura.setStatus(comboStatus.getValue());
            factura.setFechaVencimiento(dateFechaVencimiento.getValue());
            factura.setValor(Double.parseDouble(txtValor.getText()));
            factura.setReferente(txtReferente.getText());
            factura.setProveedor(txtProveedor.getText());
            factura.setTipo(comboTipo.getValue());

            // Notificar que se ha editado la factura
            if (onFacturaEditada != null) {
                onFacturaEditada.accept(factura);
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
     * Elimina la factura actual
     */
    private void eliminarFactura() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar factura");
        confirmacion.setHeaderText("¿Está seguro de eliminar esta factura?");
        confirmacion.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            // Aquí se implementaría la lógica para eliminar la factura de la base de datos
            // Por ahora, simplemente cerraremos la ventana
            cerrarVentana();
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
package com.lobo24.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.lobo24.dao.ClienteDAO;
import com.lobo24.models.Cliente;

public class NuevoClienteController {

    // Campos del formulario

    @FXML private TextField nombreField;
    @FXML private TextField apellidoField;
    @FXML private TextField codigoField;
    @FXML private CheckBox codigoAutoCheck;
    @FXML private TextField dniField;
    @FXML private TextField telefonoField;
    @FXML private TextField emailField;
    @FXML
    private ComboBox<String> tipoClientComboBox;
    @FXML private TextField direccionField;
    @FXML private TextField localidadField;
    @FXML private TextField provinciaField;
    @FXML private TextField saldoField;
    @FXML private DatePicker fechaNacimientoPicker;
    @FXML private CheckBox notificacionesCheck;
    @FXML private TextArea observacionesArea;


    private ClientesController.ClienteTabla clienteEnEdicion;
    private ClientesController clientesController;

    private boolean esModoEdicion = false;


    @FXML
    public void initialize() {
        System.out.println("Inicializado. ComboBox tipoClienteComboBox: " + tipoClientComboBox);
    }




    public void setClientesController(ClientesController controller) {
        this.clientesController = controller;
    }

    @FXML
    private void guardarCliente() {
        try {
            // Validaciones básicas
            if (nombreField.getText().isEmpty() || codigoField.getText().isEmpty()) {
                mostrarError("Error", "Nombre y código son campos obligatorios");
                return;
            }

            // Crear objeto Cliente
            Cliente cliente = new Cliente(
                    nombreField.getText(),
                    codigoField.getText(),
                    telefonoField.getText(),
                    emailField.getText(),
                    parseDoubleOrDefault(saldoField.getText(), 0.0),
                    tipoClientComboBox.getValue(),
                    "N/A", // Última compra se actualizará después
                    direccionField.getText()
            );

            // Establecer atributos adicionales
            cliente.setApellido(apellidoField.getText());
            cliente.setDni(dniField.getText());
            cliente.setLocalidad(localidadField.getText());
            cliente.setProvincia(provinciaField.getText());
            if (fechaNacimientoPicker.getValue() != null) {
                cliente.setFechaNacimiento(fechaNacimientoPicker.getValue().toString());
            }
            cliente.setObservaciones(observacionesArea.getText());
            cliente.setRecibeNotificaciones(notificacionesCheck.isSelected());

            // Guardar en la base de datos
            boolean insertado = ClienteDAO.crearCliente(cliente);

            if (insertado) {
                // Actualizar la tabla principal
                clientesController.agregarCliente(new ClientesController.ClienteTabla(
                        cliente.getNombreCompleto(),
                        cliente.getCodigo(),
                        cliente.getTelefono(),
                        cliente.getEmail(),
                        cliente.getSaldo(),
                        cliente.getTipoCliente(),
                        "", // Última compra
                        cliente.getDireccionCompleta()
                ));
                cerrarVentana();
            } else {
                mostrarError("Error", "No se pudo guardar el cliente en la base de datos");
            }

        } catch (Exception e) {
            mostrarError("Error inesperado", e.getMessage());
            e.printStackTrace();
        }
    }


    @FXML
    private void generarCodigoAutomatico() {
        if (codigoAutoCheck.isSelected()) {
            // Lógica para generar código automático (ej: CLI-001)
            String nuevoCodigo = "CLI-" + String.format("%03d", ClienteDAO.obtenerProximoId());
            codigoField.setText(nuevoCodigo);
            codigoField.setDisable(true);
        } else {
            codigoField.setDisable(false);
            codigoField.clear();
        }
    }

    @FXML
    private void cerrarVentana() {
        nombreField.getScene().getWindow().hide();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private double parseDoubleOrDefault(String value, double defaultValue) {
        if (value == null || value.isEmpty()) return defaultValue;
        return Double.parseDouble(value);
    }
}
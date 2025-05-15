package com.lobo24.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.lobo24.dao.ClienteDAO;
import com.lobo24.models.Cliente;
import javafx.stage.Stage;

public class EditarClienteController {

    // Campos del formulario (deben coincidir con los del FXML)
    @FXML private TextField nombreField;
    @FXML private TextField apellidoField;
    @FXML private TextField codigoField;
    @FXML private TextField dniField;
    @FXML private TextField telefonoField;
    @FXML private TextField emailField;
    @FXML private ComboBox<String> tipoClienteComboBox;
    @FXML private TextField direccionField;
    @FXML private TextField localidadField;
    @FXML private TextField provinciaField;
    @FXML private TextField saldoField;
    @FXML private DatePicker fechaNacimientoPicker;
    @FXML private CheckBox notificacionesCheck;
    @FXML private TextArea observacionesArea;

    private ClientesController.ClienteTabla clienteSeleccionado;
    private ClientesController clientesController;

    public void setCliente(ClientesController.ClienteTabla cliente) {
        this.clienteSeleccionado = cliente;
        cargarDatosCliente();
    }

    public void setClientesController(ClientesController controller) {
        this.clientesController = controller;
    }

    private void cargarDatosCliente() {
        // Aquí deberías cargar los datos completos del cliente desde la base de datos
        // usando el código del clienteSeleccionado
        Cliente cliente = ClienteDAO.obtenerClientePorCodigo(clienteSeleccionado.getCodigo());

        // Rellenar los campos con los datos del cliente
        nombreField.setText(cliente.getNombre());
        apellidoField.setText(cliente.getApellido());
        codigoField.setText(cliente.getCodigo());
        dniField.setText(cliente.getDni());
        telefonoField.setText(cliente.getTelefono());
        emailField.setText(cliente.getEmail());
        tipoClienteComboBox.setValue(cliente.getTipoCliente());
        direccionField.setText(cliente.getDireccion());
        localidadField.setText(cliente.getLocalidad());
        provinciaField.setText(cliente.getProvincia());
        saldoField.setText(String.valueOf(cliente.getSaldo()));
        // fechaNacimientoPicker.setValue(...); // Necesitas convertir String a LocalDate
        notificacionesCheck.setSelected(cliente.isRecibeNotificaciones());
        observacionesArea.setText(cliente.getObservaciones());
    }

    @FXML
    private void guardarCambios() {
        try {
            // Crear objeto Cliente con los datos modificados
            Cliente clienteActualizado = new Cliente(
                    nombreField.getText(),
                    codigoField.getText(),
                    telefonoField.getText(),
                    emailField.getText(),
                    Double.parseDouble(saldoField.getText()),
                    tipoClienteComboBox.getValue(),
                    clienteSeleccionado.getUltimaCompra(),
                    direccionField.getText()
            );

            // Establecer otros atributos
            clienteActualizado.setApellido(apellidoField.getText());
            clienteActualizado.setDni(dniField.getText());
            clienteActualizado.setLocalidad(localidadField.getText());
            clienteActualizado.setProvincia(provinciaField.getText());
            clienteActualizado.setObservaciones(observacionesArea.getText());
            clienteActualizado.setRecibeNotificaciones(notificacionesCheck.isSelected());

            // Actualizar en la base de datos
            boolean actualizado = ClienteDAO.actualizarCliente(clienteActualizado);

            if (actualizado) {
                // Cerrar la ventana de edición
                ((Stage) nombreField.getScene().getWindow()).close();

                // Actualizar la tabla en el controlador principal
                clientesController.cargarClientes();
            } else {
                mostrarError("Error", "No se pudo actualizar el cliente");
            }

        } catch (NumberFormatException e) {
            mostrarError("Error", "El saldo debe ser un número válido");
        } catch (Exception e) {
            mostrarError("Error inesperado", e.getMessage());
        }
    }

    @FXML
    private void cancelar() {
        ((Stage) nombreField.getScene().getWindow()).close();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
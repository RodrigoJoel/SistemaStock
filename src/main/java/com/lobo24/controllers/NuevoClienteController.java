package com.lobo24.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import com.lobo24.dao.ClienteDAO;
import com.lobo24.models.Cliente;
import javafx.stage.Stage;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class NuevoClienteController {

    // Campos del formulario


    @FXML
    private CheckBox codigoAutoCheck;

    @FXML
    private TextField txtCodigo;
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
    @FXML
    private DatePicker ultimaPicker;
    @FXML private TextArea observacionesArea;
    @FXML private Button guardarButton;
    @FXML private Label tituloLabel; // Para cambiar el título dinámicamente

    private ClientesController.ClienteTabla clienteSeleccionado;
    private ClientesController clientesController;
    private boolean modoEdicion = false;


    public void initialize(URL location, ResourceBundle resources) {
        if (tipoClienteComboBox != null) {
            tipoClienteComboBox.getItems().addAll(/* your items */);
        }
    }
    public void setClientesController(ClientesController controller) {
        this.clientesController = controller;
    }

    // Método para configurar el modo edición
    public void setCliente(ClientesController.ClienteTabla cliente) {
        this.clienteSeleccionado = cliente;
        this.modoEdicion = true;

        // Cambiar el título y texto del botón
        if (tituloLabel != null) {
            tituloLabel.setText("Editar Cliente");
        }
        if (guardarButton != null) {
            guardarButton.setText("Actualizar");
        }

        cargarDatosCliente();
    }

    private void cargarDatosCliente() {
        if (clienteSeleccionado != null) {
            // Cargar los datos completos del cliente desde la base de datos
            Cliente cliente = ClienteDAO.obtenerClientePorCodigo(clienteSeleccionado.getCodigo());

            if (cliente != null) {
                nombreField.setText(cliente.getNombre());
                apellidoField.setText(cliente.getApellido());
                codigoField.setText(cliente.getCodigo());
                codigoField.setDisable(true);  // No permitir editar el código
                codigoAutoCheck.setDisable(true);
                dniField.setText(cliente.getDni());
                telefonoField.setText(cliente.getTelefono());
                emailField.setText(cliente.getEmail());
                tipoClienteComboBox.setValue(cliente.getTipoCliente());
                direccionField.setText(cliente.getDireccion());
                localidadField.setText(cliente.getLocalidad());
                provinciaField.setText(cliente.getProvincia());
                saldoField.setText(String.valueOf(cliente.getSaldo()));
                notificacionesCheck.setSelected(cliente.isRecibeNotificaciones());
                observacionesArea.setText(cliente.getObservaciones());
            }
        }
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {
        // Tu código aquí
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void guardarCliente() {
        if (!validarCampos()) {
            return;
        }

        try {
            Cliente cliente = crearClienteDesdeFormulario();
            System.out.println("Intentando guardar cliente con código: " + cliente.getCodigo());

            boolean resultado;
            String mensajeExito;
            String mensajeError;

            if (modoEdicion) {
                resultado = ClienteDAO.actualizarCliente(cliente);
                mensajeExito = "Cliente actualizado correctamente";
                mensajeError = "No se pudo actualizar el cliente. Verifica:";
            } else {
                resultado = ClienteDAO.crearCliente(cliente);
                mensajeExito = "Cliente creado correctamente";
                mensajeError = "No se pudo crear el cliente. Verifica:";
            }

            if (resultado) {
                mostrarInfo("Éxito", mensajeExito);
                clientesController.cargarClientes();

                // Cerrar solo si es edición, mantener abierto para nuevo cliente
                if (modoEdicion) {
                    cerrarVentana();
                } else {
                    cerrarVentana();
                }
            } else {
                mostrarError("Error", mensajeError +
                        "\n- Que el código no esté duplicado" +
                        "\n- Que todos los campos requeridos tengan valores válidos");
            }

        } catch (NumberFormatException e) {
            mostrarError("Error de formato", "El saldo debe ser un número válido");
        } catch (Exception e) {
            mostrarError("Error inesperado", "Detalles: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private Cliente crearClienteDesdeFormulario() {
        Cliente cliente = new Cliente(
                nombreField.getText().trim(),
                codigoField.getText().trim(),
                telefonoField.getText().trim(),
                emailField.getText().trim(),
                Double.parseDouble(saldoField.getText().trim()),
                tipoClienteComboBox.getValue(),
                "", // Última compra se maneja por separado
                direccionField.getText().trim()
        );

        // Establecer otros atributos
        cliente.setApellido(apellidoField.getText().trim());
        cliente.setDni(dniField.getText().trim());
        cliente.setLocalidad(localidadField.getText().trim());
        cliente.setProvincia(provinciaField.getText().trim());
        cliente.setObservaciones(observacionesArea.getText().trim());
        cliente.setRecibeNotificaciones(notificacionesCheck.isSelected());

        return cliente;
    }

    private boolean validarCampos() {
        if (nombreField.getText().trim().isEmpty()) {
            mostrarError("Campo requerido", "El nombre es obligatorio");
            nombreField.requestFocus();
            return false;
        }

        if (codigoField.getText().trim().isEmpty()) {
            mostrarError("Campo requerido", "El código es obligatorio");
            codigoField.requestFocus();
            return false;
        }

        if (telefonoField.getText().trim().isEmpty()) {
            mostrarError("Campo requerido", "El teléfono es obligatorio");
            telefonoField.requestFocus();
            return false;
        }

        try {
            Double.parseDouble(saldoField.getText().trim());
        } catch (NumberFormatException e) {
            mostrarError("Error", "El saldo debe ser un número válido");
            saldoField.requestFocus();
            return false;
        }

        // Verificar si el código ya existe (solo en modo creación)
        if (!modoEdicion && ClienteDAO.obtenerClientePorCodigo(codigoField.getText().trim()) != null) {
            mostrarError("Error", "Ya existe un cliente con este código");
            codigoField.requestFocus();
            return false;
        }

        return true;
    }
    @FXML
    private void generarCodigoAutomatico() {
        if (codigoAutoCheck.isSelected()) {
            String codigoAutomatico = generarCodigoUnico();
            codigoField.setText(codigoAutomatico);
            codigoField.setDisable(true); // Deshabilitar edición si es automático
        } else {
            codigoField.clear();
            codigoField.setDisable(false); // Habilitar edición manual
        }
    }

    private String generarCodigoUnico() {
        String prefix = "CLI";
        int numeroAleatorio = (int) (Math.random() * 10000);
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(8);

        return prefix + String.format("%04d", numeroAleatorio) + timestamp;
    }


    private void limpiarFormulario() {
        nombreField.clear();
        apellidoField.clear();
        codigoField.clear();
        dniField.clear();
        telefonoField.clear();
        emailField.clear();
        tipoClienteComboBox.setValue("Regular");
        direccionField.clear();
        localidadField.clear();
        provinciaField.clear();
        saldoField.clear();
        notificacionesCheck.setSelected(false);
        observacionesArea.clear();
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    private void cerrarVentana() {
        Stage stage = (Stage) nombreField.getScene().getWindow();
        stage.close();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void mostrarInfo(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}

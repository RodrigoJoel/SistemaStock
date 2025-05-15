package com.lobo24.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.lobo24.dao.ClienteDAO;
import com.lobo24.models.Cliente;
import java.io.IOException;
import java.util.List;

public class ClientesController {

    // Componentes de la interfaz
    @FXML private TableColumn<ClienteTabla, Void> colAcciones;
    @FXML private TableView<ClienteTabla> tablaClientes;
    @FXML private TableColumn<ClienteTabla, String> colNombre;
    @FXML private TableColumn<ClienteTabla, String> colCodigo;
    @FXML private TableColumn<ClienteTabla, String> colTelefono;
    @FXML private TableColumn<ClienteTabla, String> colEmail;
    @FXML private TableColumn<ClienteTabla, Double> colSaldo;
    @FXML private TableColumn<ClienteTabla, String> colTipo;
    @FXML private TableColumn<ClienteTabla, String> colUltimaCompra;
    @FXML private TableColumn<ClienteTabla, String> colDireccion;
    @FXML private TextField buscarField;
    @FXML private Label contadorLabel;

    // Clase interna para manejar los datos de la tabla
    public static class ClienteTabla {
        private final String nombre;
        private final String codigo;
        private final String telefono;
        private final String email;
        private final double saldo;
        private final String tipo;
        private final String ultimaCompra;
        private final String direccion;

        public ClienteTabla(String nombre, String codigo, String telefono, String email,
                            double saldo, String tipo, String ultimaCompra, String direccion) {
            this.nombre = nombre;
            this.codigo = codigo;
            this.telefono = telefono;
            this.email = email;
            this.saldo = saldo;
            this.tipo = tipo;
            this.ultimaCompra = ultimaCompra;
            this.direccion = direccion;
        }

        // Getters
        public String getNombre() { return nombre; }
        public String getCodigo() { return codigo; }
        public String getTelefono() { return telefono; }
        public String getEmail() { return email; }
        public double getSaldo() { return saldo; }
        public String getTipo() { return tipo; }
        public String getUltimaCompra() { return ultimaCompra; }
        public String getDireccion() { return direccion; }
    }

    @FXML
    public void initialize() {
        // Configurar las columnas de la tabla
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colUltimaCompra.setCellValueFactory(new PropertyValueFactory<>("ultimaCompra"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        // Formatear columna de saldo para mostrar moneda
        colSaldo.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double saldo, boolean empty) {
                super.updateItem(saldo, empty);
                if (empty || saldo == null) {
                    setText(null);
                } else {
                    setText(String.format("$ %.2f", saldo));
                    // Color rojo para saldos negativos
                    if (saldo < 0) {
                        setStyle("-fx-text-fill: red;");
                    } else {
                        setStyle("-fx-text-fill: green;");
                    }
                }
            }
        });

        // Cargar datos iniciales
        cargarClientes();

        // Configurar búsqueda en tiempo real
        buscarField.textProperty().addListener((obs, oldVal, newVal) -> filtrarClientes(newVal));

        colAcciones.setCellFactory(col -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");
            private final Button btnEliminar = new Button("Eliminar");

            {
                btnEditar.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
                btnEliminar.setStyle("-fx-background-color: #f44336; -fx-text-fill: white;");

                btnEditar.setOnAction(event -> {
                    ClienteTabla cliente = getTableView().getItems().get(getIndex());
                    abrirEdicionCliente(cliente);
                });

                btnEliminar.setOnAction(event -> {
                    ClienteTabla cliente = getTableView().getItems().get(getIndex());
                    eliminarClienteDesdeTabla(cliente);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10, btnEditar, btnEliminar);
                    setGraphic(hbox);
                }
            }
        });

    }

    private void abrirEdicionCliente(ClienteTabla clienteSeleccionado) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/nuevocliente.fxml"));
            Parent root = loader.load();

            EditarClienteController controller = loader.getController();
            controller.setCliente(clienteSeleccionado);
            controller.setClientesController(this);

            Stage stage = new Stage();
            stage.setTitle("Editar Cliente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            mostrarError("Error", "No se pudo cargar la ventana de edición");
            e.printStackTrace();
        }
    }

    private void eliminarClienteDesdeTabla(ClienteTabla clienteSeleccionado) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de eliminar al cliente " + clienteSeleccionado.getNombre() + "?");

        if (confirmacion.showAndWait().get() == ButtonType.OK) {
            boolean eliminado = ClienteDAO.eliminarClientePorCodigo(clienteSeleccionado.getCodigo());

            if (eliminado) {
                tablaClientes.getItems().remove(clienteSeleccionado);
                actualizarContador();
                mostrarInfo("Éxito", "Cliente eliminado correctamente");
            } else {
                mostrarError("Error", "No se pudo eliminar el cliente");
            }
        }
    }


    void cargarClientes() {
        tablaClientes.getItems().clear();
        List<Cliente> clientes = ClienteDAO.obtenerTodosClientes();

        for (Cliente cliente : clientes) {
            tablaClientes.getItems().add(new ClienteTabla(
                    cliente.getNombreCompleto(),
                    cliente.getCodigo(),
                    cliente.getTelefono(),
                    cliente.getEmail(),
                    cliente.getSaldo(),
                    cliente.getTipoCliente(),
                    cliente.getUltimaCompra(),
                    cliente.getDireccionCompleta()
            ));
        }

        actualizarContador();
    }

    private void filtrarClientes(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            cargarClientes();
        } else {
            tablaClientes.getItems().clear();
            List<Cliente> clientes = ClienteDAO.buscarClientes(filtro);

            for (Cliente cliente : clientes) {
                tablaClientes.getItems().add(new ClienteTabla(
                        cliente.getNombreCompleto(),
                        cliente.getCodigo(),
                        cliente.getTelefono(),
                        cliente.getEmail(),
                        cliente.getSaldo(),
                        cliente.getTipoCliente(),
                        cliente.getUltimaCompra(),
                        cliente.getDireccionCompleta()
                ));
            }

            actualizarContador();
        }
    }

    private void actualizarContador() {
        contadorLabel.setText("Total de clientes: " + tablaClientes.getItems().size());
    }

    @FXML
    private void abrirNuevoCliente() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/nuevocliente.fxml"));
            Parent root = loader.load();


            NuevoClienteController controller = loader.getController();
            //  controller.setClientesController(this);

            Stage stage = new Stage();
            stage.setTitle("Nuevo Cliente");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            mostrarError("Error", "No se pudo cargar la ventana de nuevo cliente");
            e.printStackTrace();
        }
    }

    public void agregarCliente(ClienteTabla nuevoCliente) {
        tablaClientes.getItems().add(nuevoCliente);
        actualizarContador();
    }

    @FXML
    private void editarCliente() {
        ClienteTabla clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/nuevocliente.fxml"));
                Parent root = loader.load();

                EditarClienteController controller = loader.getController();
                controller.setCliente(clienteSeleccionado);
                controller.setClientesController(this);

                Stage stage = new Stage();
                stage.setTitle("Editar Cliente");
                stage.setScene(new Scene(root));
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();

            } catch (IOException e) {
                mostrarError("Error", "No se pudo cargar la ventana de edición");
                e.printStackTrace();
            }
        } else {
            mostrarError("Selección requerida", "Por favor seleccione un cliente para editar");
        }
    }

    @FXML
    private void eliminarCliente() {
        ClienteTabla clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();

        if (clienteSeleccionado != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText(null);
            confirmacion.setContentText("¿Está seguro de eliminar al cliente " + clienteSeleccionado.getNombre() + "?");

            if (confirmacion.showAndWait().get() == ButtonType.OK) {
                boolean eliminado = ClienteDAO.eliminarClientePorCodigo(clienteSeleccionado.getCodigo());

                if (eliminado) {
                    tablaClientes.getItems().remove(clienteSeleccionado);
                    actualizarContador();
                    mostrarInfo("Éxito", "Cliente eliminado correctamente");
                } else {
                    mostrarError("Error", "No se pudo eliminar el cliente");
                }
            }
        } else {
            mostrarError("Selección requerida", "Por favor seleccione un cliente para eliminar");
        }
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

package com.lobo24.controllers;

import com.itextpdf.text.Font;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import com.lobo24.dao.ClienteDAO;
import com.lobo24.models.Cliente;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ClientesController {

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

    public static class ClienteTabla {
        private final String nombre, codigo, telefono, email, tipo, ultimaCompra, direccion;
        private final double saldo;

        public ClienteTabla(String nombre, String codigo, String telefono, String email,
                            double saldo, String tipo,String ultimaCompra, String direccion) {
            this.nombre = nombre;
            this.codigo = codigo;
            this.telefono = telefono;
            this.email = email;
            this.saldo = saldo;
            this.tipo = tipo;
            this.ultimaCompra = ultimaCompra;
            this.direccion = direccion;
        }

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
        configurarColumnas();
        configurarAcciones();
        cargarClientes();
        buscarField.textProperty().addListener((obs, oldVal, newVal) -> filtrarClientes(newVal));
    }

    private void configurarColumnas() {
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colSaldo.setCellValueFactory(new PropertyValueFactory<>("saldo"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colUltimaCompra.setCellValueFactory(new PropertyValueFactory<>("ultimaCompra"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));

        colSaldo.setCellFactory(tc -> new TableCell<>() {
            @Override
            protected void updateItem(Double saldo, boolean empty) {
                super.updateItem(saldo, empty);
                if (empty || saldo == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(String.format("$ %.2f", saldo));
                    setStyle("-fx-text-fill: " + (saldo < 0 ? "red" : "green") + ";");
                }
            }
        });
    }

    private void configurarAcciones() {
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
                setGraphic(empty ? null : new HBox(10, btnEditar, btnEliminar));
            }
        });
    }

    void cargarClientes() {
        cargarClientesDesdeLista(ClienteDAO.obtenerTodosClientes());
    }

    private void filtrarClientes(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            cargarClientes();
        } else {
            cargarClientesDesdeLista(ClienteDAO.buscarClientes(filtro));
        }
    }

    private void cargarClientesDesdeLista(List<Cliente> clientes) {
        tablaClientes.getItems().clear();
        for (Cliente cliente : clientes) {
            tablaClientes.getItems().add(new ClienteTabla(
                    cliente.getNombreCompleto(),
                    cliente.getCodigo(),
                    cliente.getTelefono(),
                    cliente.getEmail(),
                    cliente.getSaldo(),
                    cliente.getTipoCliente(),
                    cliente.getDni(),
                    cliente.getDireccionCompleta()
            ));
        }
        actualizarContador();
    }

    private void actualizarContador() {
        contadorLabel.setText("Total de clientes: " + tablaClientes.getItems().size());
    }

    @FXML
    private void abrirNuevoCliente() {
        abrirFormularioCliente("/views/nuevocliente.fxml", "Nuevo Cliente", null);
    }

    private void abrirEdicionCliente(ClienteTabla clienteSeleccionado) {
        abrirFormularioCliente("/views/nuevocliente.fxml", "Editar Cliente", clienteSeleccionado);
    }

    private void abrirFormularioCliente(String ruta, String titulo, ClienteTabla cliente) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(ruta));
            Parent root = loader.load();

            NuevoClienteController controller = loader.getController();
            controller.setClientesController(this);

            if (cliente != null) {
                controller.setCliente(cliente);
            }

            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();

        } catch (IOException e) {
            mostrarError("Error", "No se pudo cargar la ventana de cliente");
            e.printStackTrace();
        }
    }

    @FXML
    private void cerrarVentana(javafx.event.ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void agregarCliente(ClienteTabla nuevoCliente) {
        tablaClientes.getItems().add(nuevoCliente);
        actualizarContador();
    }

    @FXML
    private void editarCliente() {
        ClienteTabla clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            abrirEdicionCliente(clienteSeleccionado);
        } else {
            mostrarError("Selección requerida", "Por favor seleccione un cliente para editar");
        }
    }

    @FXML
    private void eliminarCliente() {
        ClienteTabla clienteSeleccionado = tablaClientes.getSelectionModel().getSelectedItem();
        if (clienteSeleccionado != null) {
            eliminarClienteDesdeTabla(clienteSeleccionado);
        } else {
            mostrarError("Selección requerida", "Por favor seleccione un cliente para eliminar");
        }
    }

    private void eliminarClienteDesdeTabla(ClienteTabla clienteSeleccionado) {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar eliminación");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Está seguro de eliminar al cliente " + clienteSeleccionado.getNombre() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
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

    public void actualizarClienteEnTabla(Cliente clienteActualizado) {
        for (int i = 0; i < tablaClientes.getItems().size(); i++) {
            ClienteTabla c = tablaClientes.getItems().get(i);
            if (c.getCodigo().equals(clienteActualizado.getCodigo())) {
                ClienteTabla actualizado = new ClienteTabla(
                        clienteActualizado.getNombreCompleto(),
                        clienteActualizado.getCodigo(),
                        clienteActualizado.getTelefono(),
                        clienteActualizado.getEmail(),
                        clienteActualizado.getSaldo(),
                        clienteActualizado.getTipoCliente(),
                        clienteActualizado.getDni(),
                        clienteActualizado.getDireccionCompleta()
                );
                tablaClientes.getItems().set(i, actualizado);
                break;
            }
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

    // ------------------- Exportar a Excel -------------------

    @FXML
    private void exportarTablaExcel() {
        if (tablaClientes.getItems().isEmpty()) {
            mostrarError("Error", "No hay datos para exportar");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo Excel (*.xlsx)", "*.xlsx"));
        File file = fileChooser.showSaveDialog(tablaClientes.getScene().getWindow());

        if (file != null) {
            try (Workbook workbook = new XSSFWorkbook()) {
                Sheet sheet = workbook.createSheet("Clientes");

                // Crear fila de encabezado
                Row header = sheet.createRow(0);
                String[] columnas = {"Nombre", "Código", "Teléfono", "Email", "Saldo", "Tipo", "DNI/CUIT", "Dirección"};
                for (int i = 0; i < columnas.length; i++) {
                    Cell cell = header.createCell(i);
                    cell.setCellValue(columnas[i]);
                }

                // Rellenar filas con datos
                int rowIndex = 1;
                for (ClienteTabla c : tablaClientes.getItems()) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(c.getNombre());
                    row.createCell(1).setCellValue(c.getCodigo());
                    row.createCell(2).setCellValue(c.getTelefono());
                    row.createCell(3).setCellValue(c.getEmail());
                    row.createCell(4).setCellValue(c.getSaldo());
                    row.createCell(5).setCellValue(c.getTipo());
                    row.createCell(6).setCellValue(c.getUltimaCompra());
                    row.createCell(7).setCellValue(c.getDireccion());
                }

                // Autoajustar columnas
                for (int i = 0; i < columnas.length; i++) {
                    sheet.autoSizeColumn(i);
                }

                try (FileOutputStream fos = new FileOutputStream(file)) {
                    workbook.write(fos);
                }

                mostrarInfo("Exportación exitosa", "Archivo Excel guardado correctamente.");

            } catch (IOException e) {
                mostrarError("Error", "No se pudo guardar el archivo Excel.");
                e.printStackTrace();
            }
        }
    }

    // ------------------- Exportar a PDF -------------------

    @FXML
    private void exportarTablaPDF() {
        if (tablaClientes.getItems().isEmpty()) {
            mostrarError("Error", "No hay datos para exportar");
            return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar archivo PDF");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivo PDF (*.pdf)", "*.pdf"));
        File file = fileChooser.showSaveDialog(tablaClientes.getScene().getWindow());

        if (file != null) {
            Document document = new Document(PageSize.A4.rotate());
            try {
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();

                Font fontTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
                Paragraph titulo = new Paragraph("Listado de Clientes", fontTitulo);
                titulo.setAlignment(Element.ALIGN_CENTER);
                titulo.setSpacingAfter(20);
                document.add(titulo);

                PdfPTable table = new PdfPTable(8);
                table.setWidthPercentage(100);
                table.setWidths(new int[]{3, 2, 3, 4, 2, 2, 3, 4});

                // Agregar encabezados
                String[] columnas = {"Nombre", "Código", "Teléfono", "Email", "Saldo", "Tipo", "DNI/CUIT", "Dirección"};
                for (String col : columnas) {
                    table.addCell(new Phrase(col, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
                }

                // Agregar filas de datos
                for (ClienteTabla c : tablaClientes.getItems()) {
                    table.addCell(c.getNombre());
                    table.addCell(c.getCodigo());
                    table.addCell(c.getTelefono());
                    table.addCell(c.getEmail());
                    table.addCell(String.format("$ %.2f", c.getSaldo()));
                    table.addCell(c.getTipo());
                    table.addCell(c.getUltimaCompra());
                    table.addCell(c.getDireccion());
                }

                document.add(table);
                mostrarInfo("Exportación exitosa", "Archivo PDF guardado correctamente.");

            } catch (Exception e) {
                mostrarError("Error", "No se pudo guardar el archivo PDF.");
                e.printStackTrace();
            } finally {
                document.close();
            }
        }
    }
}



package com.lobo24.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class ProductosController {

    @FXML
    private TableView<Producto> tablaProductos;
    @FXML
    private TextField buscarField;
    @FXML
    private Label contadorLabel;

    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarTabla();
        cargarDatosEjemplo();
        configurarBusqueda();
    }

    private void configurarTabla() {
        tablaProductos.setTableMenuButtonVisible(true);
    }

    private void cargarDatosEjemplo() {
        listaProductos.addAll(
                new Producto("Editar", "□", "CIGARRILLO PHILIP MORRIS CAPS 12 BOX", "077971913", "$2.400,00", 7, 0, 7, "77971")
        );
        tablaProductos.setItems(listaProductos);
        actualizarContador();
    }

    private void configurarBusqueda() {
        buscarField.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarProductos(newValue);
        });
    }

    private void filtrarProductos(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tablaProductos.setItems(listaProductos);
        } else {
            ObservableList<Producto> productosFiltrados = FXCollections.observableArrayList();
            String filtroLower = filtro.toLowerCase();
            for (Producto producto : listaProductos) {
                if (producto.getDescripcion().toLowerCase().contains(filtroLower) ||
                        producto.getCodigo().toLowerCase().contains(filtroLower)) {
                    productosFiltrados.add(producto);
                }
            }
            tablaProductos.setItems(productosFiltrados);
        }
        actualizarContador();
    }

    private void actualizarContador() {
        contadorLabel.setText("Total de productos: " + tablaProductos.getItems().size());
    }

    @FXML
    private void abrirNuevoProducto() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/nuevoProducto.fxml"));
            Parent root = loader.load();

            NuevoProductoController nuevoController = loader.getController();
            nuevoController.setProductosController(this);

            Stage stage = new Stage();
            stage.setTitle("Nuevo Producto");
            stage.setScene(new Scene(root));
            stage.showAndWait(); // Espera que se cierre para seguir

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo cargar el formulario",
                    "Ruta incorrecta o archivo no encontrado: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void agregarProducto(Producto nuevoProducto) {
        listaProductos.add(nuevoProducto);
        tablaProductos.setItems(listaProductos);
        tablaProductos.refresh();
        actualizarContador();
    }

    private void mostrarAlerta(String titulo, String encabezado, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    // Clase interna para el modelo de datos Producto
    public static class Producto {
        private final String operacion;
        private final String imagen;
        private final String descripcion;
        private final String codigo;
        private final String precio;
        private final int stockActual;
        private final int stockReservado;
        private final int stockDisponible;
        private final String codigoAlterno;

        public Producto(String operacion, String imagen, String descripcion, String codigo,
                        String precio, int stockActual, int stockReservado,
                        int stockDisponible, String codigoAlterno) {
            this.operacion = operacion;
            this.imagen = imagen;
            this.descripcion = descripcion;
            this.codigo = codigo;
            this.precio = precio;
            this.stockActual = stockActual;
            this.stockReservado = stockReservado;
            this.stockDisponible = stockDisponible;
            this.codigoAlterno = codigoAlterno;
        }

        public String getOperacion() { return operacion; }
        public String getImagen() { return imagen; }
        public String getDescripcion() { return descripcion; }
        public String getCodigo() { return codigo; }
        public String getPrecio() { return precio; }
        public int getStockActual() { return stockActual; }
        public int getStockReservado() { return stockReservado; }
        public int getStockDisponible() { return stockDisponible; }
        public String getCodigoAlterno() { return codigoAlterno; }
    }
}

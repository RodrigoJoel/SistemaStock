package com.lobo24.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ProductosController {

    @FXML
    private TableView<Producto> tablaProductos;
    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarTabla();
        cargarDatosEjemplo();
    }

    private void configurarTabla() {
        tablaProductos.setTableMenuButtonVisible(true);
    }

    private void cargarDatosEjemplo() {
        listaProductos.addAll(
                new Producto("Editar - F2", "□", "CIGARRILLO MASTER COMUN 20", "77941558", "$1.600,00", 11, 0, 11, "77941"),
                new Producto("Editar", "□", "CIGARRILLO PARLIAMENT BOX 20", "77929891", "$5.200,00", 5, 0, 5, "77929"),
                new Producto("Editar", "□", "CIGARRILLO PARLIAMENT SUPERSLIMS BOX 20", "77974389", "$5.200,00", 3, 0, 3, "77974"),
                new Producto("Editar", "□", "CIGARRILLO PHILIP MORRIS BOX 12", "77981813", "$2.400,00", 7, 0, 7, "77981"),
                new Producto("Editar", "□", "CIGARRILLO PHILIP MORRIS CAPS 12 BOX", "077971913", "$2.400,00", 7, 0, 7, "77971")
        );
        tablaProductos.setItems(listaProductos);
    }

    @FXML
    private void abrirNuevoProducto() {
        try {
            // Cambia esta línea según donde tengas realmente el FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/nuevoProducto.fxml"));

            // O si está directamente en views:
            // FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/nuevoProducto.fxml"));

            Parent root = loader.load();

            NuevoProductoController controller = loader.getController();
            controller.setProductosController(this);

            Stage stage = new Stage();
            stage.setTitle("Nuevo Producto");
          //  stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));

            NuevoProductoController nuevoController = loader.getController();
            nuevoController.setProductosController(this);

            stage.showAndWait();

        } catch (Exception e) {
            mostrarAlerta("Error", "No se pudo cargar el formulario",
                    "Ruta incorrecta o archivo no encontrado: " + e.getMessage(),
                    Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    public void agregarProducto(Producto nuevoProducto) {
        listaProductos.add(nuevoProducto);
        tablaProductos.refresh();
    }

    private void mostrarAlerta(String titulo, String encabezado, String contenido, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(encabezado);
        alert.setContentText(contenido);
        alert.showAndWait();
    }

    // Clase modelo para los productos
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

        // Getters (necesarios para PropertyValueFactory)
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
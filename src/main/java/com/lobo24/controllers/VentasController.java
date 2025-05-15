package com.lobo24.controllers;

import com.lobo24.util.ProductoVenta;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.lobo24.models.VentaHistorico;

import java.io.IOException;

public class VentasController {


    @FXML private TableView<Venta> tablaVentas;
    @FXML private TableColumn<Venta, String> colOperacion;
    @FXML private TableColumn<Venta, String> colNumero;
    @FXML private TableColumn<Venta, String> colResumen;
    @FXML private TableColumn<Venta, String> colTipo;
    @FXML private TableColumn<Venta, String> colFecha;
    @FXML private TableColumn<Venta, String> colHora;
    @FXML private TableColumn<Venta, String> colOrigen;
    @FXML private TableColumn<Venta, String> colArticulos;
    @FXML private TableColumn<Venta, String> colCliente;
    @FXML private TableColumn<Venta, String> colObservaciones;

    private ObservableList<Venta> listaVentas = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colOperacion.setCellValueFactory(new PropertyValueFactory<>("operacion"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colResumen.setCellValueFactory(new PropertyValueFactory<>("resumen"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colOrigen.setCellValueFactory(new PropertyValueFactory<>("origen"));
        colArticulos.setCellValueFactory(new PropertyValueFactory<>("articulos"));
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));
        colObservaciones.setCellValueFactory(new PropertyValueFactory<>("observaciones"));

        tablaVentas.setItems(listaVentas);
        tablaVentas.setStyle("-fx-font-size: 14px;");
        tablaVentas.setRowFactory(tv -> {
            TableRow<Venta> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    Venta ventaSeleccionada = row.getItem();
                    mostrarDetalleVenta(ventaSeleccionada);
                }
            });
            return row;
        });
    }
    private void mostrarDetalleVenta(Venta venta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/detalleVenta.fxml"));
            Parent root = loader.load();

            // Obtén el controlador y pasa los productos
            DetalleVentaController controller = loader.getController();
            controller.mostrarDetalle(venta.getProductos()); // Ahora usa el nuevo método

            Stage stage = new Stage();
            stage.setTitle("Detalle de Venta #" + venta.getNumero());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  /* @FXML
    private void registrarVenta() {
        Venta nueva = new Venta(
                "Venta", "001", "Producto x", "Contado", "2025-04-05", "12:34",
                "Caja 1", "3", "Juan Pérez", "Sin observaciones"
        );
        listaVentas.add(nueva);
    }*/
    public void abrirVentanaNuevaVenta() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/nuevaVenta.fxml"));
            Parent root = loader.load();

            NuevaVentaController nuevoController = loader.getController();
            nuevoController.setVentasController(this); // ¡Pasa esta misma instancia!



            Stage stage = new Stage();
            stage.setTitle("Nueva Venta");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tablaVentas.getScene().getWindow()); // Establece dueño
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método clave para agregar ventas
    public void agregarVentaAlHistorico(Venta nuevaVenta) {
        Platform.runLater(() -> listaVentas.add(nuevaVenta));
    }


    @FXML private Label lblUsuario;

    public void setUsuario(String usuario) {
        lblUsuario.setText("Usuario: " + usuario);
    }

    // Clase interna
    public static class Venta {
        private ObservableList<ProductoVenta> productos; // Lista de productos vendidos
        private final String operacion;
        private final String numero;
        private final String resumen;
        private final String tipo;
        private final String fecha;
        private final String hora;
        private final String origen;
        private final String articulos;
        private final String cliente;
        private final String observaciones;

        public Venta(String operacion, String numero, String resumen, String tipo, String fecha,
                     String hora, String origen, String articulos, String cliente, String observaciones,ObservableList<ProductoVenta> productos) {
            this.operacion = operacion;
            this.numero = numero;
            this.resumen = resumen;
            this.tipo = tipo;
            this.fecha = fecha;
            this.hora = hora;
            this.origen = origen;
            this.articulos = articulos;
            this.cliente = cliente;
            this.observaciones = observaciones;
            this.productos = FXCollections.observableArrayList(productos); // Copia la lista
        }

        public String getOperacion() { return operacion; }
        public String getNumero() { return numero; }
        public String getResumen() { return resumen; }
        public String getTipo() { return tipo; }
        public String getFecha() { return fecha; }
        public String getHora() { return hora; }
        public String getOrigen() { return origen; }
        public String getArticulos() { return articulos; }
        public String getCliente() { return cliente; }
        public String getObservaciones() { return observaciones; }
        public ObservableList<ProductoVenta> getProductos() {
            return productos;
        }
    }
}

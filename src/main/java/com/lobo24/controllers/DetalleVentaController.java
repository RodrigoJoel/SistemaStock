package com.lobo24.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import com.lobo24.util.ProductoVenta; // Asegúrate de que esta clase existe
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DetalleVentaController {

    @FXML private TableView<ProductoVenta> tablaProductos;
    @FXML private TableColumn<ProductoVenta, String> colNombre;
    @FXML private TableColumn<ProductoVenta, Integer> colCantidad;
    @FXML private TableColumn<ProductoVenta, Double> colPrecio;

    @FXML private Label labelTotal; // Nuevo Label para el total
    @FXML
    public void initialize() {
        // Configuración de columnas (versión compatible con tu estructura)
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colCantidad.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty().asObject());
        colPrecio.setCellValueFactory(cellData -> cellData.getValue().precioProperty().asObject());
    }

    public void mostrarProductos(ObservableList<ProductoVenta> productos) {
        tablaProductos.setItems(productos);
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) tablaProductos.getScene().getWindow();
        stage.close();
    }
    public void mostrarDetalle(ObservableList<ProductoVenta> productos) {
        tablaProductos.setItems(productos);

        // Calcula el total
        BigDecimal total = productos.stream()
                .map(p -> p.getPrecioBigDecimal().multiply(BigDecimal.valueOf(p.getCantidad())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        labelTotal.setText("Total: $" + total.setScale(2, RoundingMode.HALF_UP));
    }
}

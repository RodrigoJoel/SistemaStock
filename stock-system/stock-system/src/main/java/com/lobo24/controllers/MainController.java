package com.lobo24.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class MainController {
    @FXML
    private TableView<String> productosTable;
    @FXML
    private TableColumn<String, String> colId, colNombre, colStock;
    @FXML
    private Button btnAgregar, btnRecargar;

    @FXML
    private void initialize() {
        // Configuración inicial
        System.out.println("¡Controlador inicializado!");

        // Configurar columnas (versión corregida)
        colId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()));
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty("Ejemplo"));
        colStock.setCellValueFactory(cellData -> new SimpleStringProperty("10"));

        // Datos de prueba (sin caracteres extraños)
        productosTable.getItems().addAll("1", "2", "3");
    }

    @FXML
    private void agregarProducto() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mensaje"); // Corregido "Hensaje"
        alert.setHeaderText("Botón funcional");
        alert.setContentText("¡El botón 'Agregar' funciona!"); // Corregido "funcional"
        alert.showAndWait();
    }

    @FXML
    private void recargarDatos() {
        productosTable.getItems().clear();
        productosTable.getItems().addAll("4", "5", "6"); // Sin _es
    }
}
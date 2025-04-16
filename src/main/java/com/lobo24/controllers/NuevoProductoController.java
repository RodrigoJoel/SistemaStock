package com.lobo24.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class NuevoProductoController {

    @FXML private TextField descripcionField;
    @FXML private TextField codigoField;
    @FXML private TextField precioField;
    @FXML private TextField stockField;

    private ProductosController productosController;

    // Método setter para la inyección de dependencias
    public void setProductosController(ProductosController controller) {
        this.productosController = controller;
    }

    @FXML
    private void abrirNuevoProducto() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/nuevoProducto.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setTitle("Nuevo Producto");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(root));
    }

    @FXML
    private void guardarProducto() {
        try {
            // Validar campos
            if (descripcionField.getText().isEmpty() || codigoField.getText().isEmpty() ||
                    precioField.getText().isEmpty() || stockField.getText().isEmpty()) {
                mostrarError("Error", "Todos los campos son obligatorios");
                return;
            }

            // Crear nuevo producto
            ProductosController.Producto nuevoProducto = new ProductosController.Producto(
                    "Editar",
                    "□",
                    descripcionField.getText(),
                    codigoField.getText(),
                    "$" + precioField.getText(),
                    Integer.parseInt(stockField.getText()),
                    0,
                    Integer.parseInt(stockField.getText()),
                    codigoField.getText().substring(0, Math.min(5, codigoField.getText().length()))
            );

            // Agregar a la lista principal
            productosController.agregarProducto(nuevoProducto);

            // Cerrar la ventana
            cerrarVentana();

        } catch (NumberFormatException e) {
            mostrarError("Error", "El stock y el precio deben ser números válidos");
        }
    }

    @FXML
    private void cancelar() {
        cerrarVentana();
    }

    @FXML
    private void cerrarVentana() {
        descripcionField.getScene().getWindow().hide();
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


}
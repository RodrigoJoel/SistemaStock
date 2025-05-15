package com.lobo24.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.lobo24.dao.ProductoDAO;
import com.lobo24.models.Producto;

public class NuevoProductoController {

    @FXML private TextField descripcionField;
    @FXML private TextField codigoField;
    @FXML private CheckBox codigoAutoCheck;
    @FXML private TextField codigoExtraField;
    @FXML private TextField eanField;
    @FXML private ComboBox<String> categoriaComboBox;
    @FXML private TextField marcaField;
    @FXML private TextField precioField;
    @FXML private CheckBox precioAutoCheck;
    @FXML private CheckBox precioEditableCheck;
    @FXML private TextField precioCosteField;
    @FXML private TextField incrementoField;
    @FXML private CheckBox consultaStockCheck;
    @FXML private TextField stockField;
    @FXML private TextField stockMinimoField;
    @FXML private CheckBox precioEnVentaCheck;
    @FXML private CheckBox observacionCheck;

    private ProductosController productosController;

    public void setProductosController(ProductosController controller) {
        this.productosController = controller;
    }

    @FXML
    private void guardarProducto() {
        try {
            // Validaciones mínimas obligatorias
            if (descripcionField.getText().isEmpty() || codigoField.getText().isEmpty() ||
                    precioField.getText().isEmpty() || stockField.getText().isEmpty()) {
                mostrarError("Error", "Todos los campos obligatorios deben estar completos.");
                return;
            }

            // Crear un objeto Producto usando el constructor adecuado
            Producto producto = new Producto(
                    codigoField.getText(),                        // código de barras
                    descripcionField.getText(),                   // nombre
                    codigoExtraField.getText(),                   // código extra (alternativo)
                    parseDoubleOrDefault(precioCosteField.getText(), 0.0), // precio de coste
                    parseDoubleOrDefault(precioField.getText(), 0.0),  // precio de venta
                    parseDoubleOrDefault(incrementoField.getText(), 0.0)  // porcentaje de incremento
            );

            // Establecer otros atributos opcionales
            producto.setMarca(marcaField.getText());
            producto.setCategoria(categoriaComboBox.getValue() != null ? categoriaComboBox.getValue() : "Sin categoría");
            producto.setEan(eanField.getText());
            producto.setStock(Integer.parseInt(stockField.getText()));
            producto.setStockMinimo(parseIntOrDefault(stockMinimoField.getText(), 0));
            producto.setAutomatico(codigoAutoCheck.isSelected());

            // Guardar el producto usando el DAO
            boolean insertado = ProductoDAO.crearProducto(producto);

            if (insertado) {
                // Crear un nuevo objeto Producto para el controlador
                ProductosController.Producto nuevoProducto = new ProductosController.Producto(
                        "Editar",
                        "□",
                        producto.getDescripcion(),
                        producto.getCodigoBarras(),
                        "$" + producto.getPrecioVenta(),
                        producto.getStock(),
                        producto.getStockMinimo(),
                        Producto.getStockDisponible(),
                        producto.getCodigoBarras().substring(0, Math.min(5, producto.getCodigoBarras().length()))
                );
                productosController.agregarProducto(nuevoProducto);
                cerrarVentana();
            } else {
                mostrarError("Error", "No se pudo guardar el producto en la base de datos.");
            }

        } catch (NumberFormatException e) {
            mostrarError("Error", "Revisá que los campos numéricos estén bien completados.");
        } catch (Exception e) {
            mostrarError("Error inesperado", e.getMessage());
            e.printStackTrace();
        }
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

    // Métodos auxiliares para evitar errores por campos vacíos
    private double parseDoubleOrDefault(String value, double defaultValue) {
        if (value == null || value.isEmpty()) return defaultValue;
        return Double.parseDouble(value);
    }

    private int parseIntOrDefault(String value, int defaultValue) {
        if (value == null || value.isEmpty()) return defaultValue;
        return Integer.parseInt(value);
    }
}

package com.lobo24.controllers;

import com.lobo24.models.ProductoStock;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.net.URL;
import java.util.ResourceBundle;

public class StockController implements Initializable {


    @FXML
    private VBox stockView;
    @FXML
    private VBox operationsView;
    @FXML
    private VBox expirationView;

    @FXML
    private BorderPane stockMainPane;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<ProductoStock> stockTable;

    @FXML
    private TableColumn<ProductoStock, String> operacionColumn;

    @FXML
    private TableColumn<ProductoStock, ImageView> imagenColumn;

    @FXML
    private TableColumn<ProductoStock, String> productoColumn;

    @FXML
    private TableColumn<ProductoStock, Integer> stockActualColumn;

    @FXML
    private TableColumn<ProductoStock, Integer> pedidosColumn;

    @FXML
    private TableColumn<ProductoStock, Integer> disponibleColumn;

    @FXML
    private TableColumn<ProductoStock, String> proveedorColumn;

    @FXML
    private TableColumn<ProductoStock, String> categoriaColumn;

    @FXML
    private Label totalItemsLabel;

    @FXML
    private TabPane mainTabPane;

    @FXML
    private Tab stockTab;

    @FXML
    private Tab operationsTab;

    @FXML
    private Tab expirationTab;

    @FXML
    private TableView<?> operacionesTable;

    @FXML
    private TableView<?> vencimientoTable;

    private ObservableList<ProductoStock> productosData;
    private FilteredList<ProductoStock> filteredData;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurar columnas de la tabla
        configurarColumnas();

        // Inicializar datos de ejemplo (remplazar con tu lógica real)
        productosData = FXCollections.observableArrayList();
        filteredData = new FilteredList<>(productosData, p -> true);
        stockTable.setItems(filteredData);

        // Cargar datos de ejemplo
        cargarDatosEjemplo();

        // Configurar búsqueda
        configurarBusqueda();
    }

    private void configurarColumnas() {
        operacionColumn.setCellFactory(createButtonCellFactory());
        imagenColumn.setCellValueFactory(new PropertyValueFactory<>("imagenView"));
        productoColumn.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));
        stockActualColumn.setCellValueFactory(new PropertyValueFactory<>("stockActual"));
        pedidosColumn.setCellValueFactory(new PropertyValueFactory<>("pedidos"));
        disponibleColumn.setCellValueFactory(new PropertyValueFactory<>("disponible"));
        proveedorColumn.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
        categoriaColumn.setCellValueFactory(new PropertyValueFactory<>("categoria"));
    }

    private void cargarDatosEjemplo() {
        // Aquí deberías cargar los datos reales desde tu servicio
        // Esto es solo para demostración
        productosData.addAll(
                // Agrega tus objetos ProductoStock aquí
        );

        actualizarContadorTotal();
    }

    private void actualizarContadorTotal() {
        int total = productosData.stream()
                .mapToInt(ProductoStock::getDisponible)
                .sum();
        totalItemsLabel.setText("Total: " + total);
    }

    private void configurarBusqueda() {
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(producto -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();
                return producto.getNombre().toLowerCase().contains(lowerCaseFilter) ||
                        producto.getCodigo().toLowerCase().contains(lowerCaseFilter);
            });
            actualizarContadorTotal();
        });
    }

    @FXML
    private void handleSearch() {
        // La búsqueda ya está manejada por el listener
    }


    @FXML
    private void handleAddStock(ActionEvent event) {
        // Implementar lógica para agregar nuevo stock
        System.out.println("Agregar nuevo stock");
    }
    @FXML
    private void showStockTab(ActionEvent event) {
        stockView.setVisible(true);
        operationsView.setVisible(false);
        expirationView.setVisible(false);
    }

    @FXML
    private void showOperationsTab(ActionEvent event) {
        stockView.setVisible(false);
        operationsView.setVisible(true);
        expirationView.setVisible(false);
    }

    @FXML
    private void showExpirationTab(ActionEvent event) {
        stockView.setVisible(false);
        operationsView.setVisible(false);
        expirationView.setVisible(true);
    }



    private Callback<TableColumn<ProductoStock, String>, TableCell<ProductoStock, String>> createButtonCellFactory() {
        return param -> new TableCell<>() {
            final Button btn = new Button("Abrir");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                    setText(null);
                } else {
                    ProductoStock producto = getTableView().getItems().get(getIndex());
                    btn.setText(getIndex() == 0 ? "Abrir - F2" : "Abrir");
                    btn.setOnAction(event -> abrirDetalleProducto(producto));
                    setGraphic(btn);
                    setText(null);
                }
            }
        };
    }



    private void abrirDetalleProducto(ProductoStock producto) {
        // Implementar lógica para abrir detalle del producto
        System.out.println("Abriendo producto: " + producto.getNombre());
    }

    private void mostrarError(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }


}
package com.lobo24.controllers;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.input.KeyCode;
import com.lobo24.util.ProductoVenta;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

public class NuevaVentaController {


    // Componentes UI
    @FXML private TextField campoBusqueda;
    @FXML private TableView<ProductoVenta> tablaProductos;
    @FXML private TableColumn<ProductoVenta, String> colCodigo;
    @FXML private TableColumn<ProductoVenta, String> colNombre;
    @FXML private TableColumn<ProductoVenta, Integer> colCantidad;
    @FXML private TableColumn<ProductoVenta, BigDecimal> colPrecio;
    @FXML private TableColumn<ProductoVenta, BigDecimal> colSubtotal;
    @FXML private Label labelTotal;
    @FXML private RadioButton pagoEfectivo, pagoTarjeta, pagoMP, pagoTransferencia, pagoPedidosYaEfectivo;
    @FXML private TextArea campoDetalles;
    @FXML
    private TableColumn<ProductoVenta, Void> colEliminar;
    private VentasController ventasController; // Referencia al histórico


    // ToggleGroup inicializado directamente
    private final ToggleGroup grupoPago = new ToggleGroup();

    // Datos
    private final ObservableList<ProductoVenta> productosAgregados = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configurar el toggleGroup para los RadioButtons
        configurarMetodosPago();
        configurarColumnas();
        configurarEventos();
        agregarBotonEliminar();
    }

    private void agregarBotonEliminar() {
        colEliminar.setCellFactory(param -> new TableCell<>() {
            private final Button btn = new Button("❌");

            {
                btn.setStyle("-fx-background-color: transparent; -fx-text-fill: red; -fx-font-size: 14px;");
                btn.setOnAction(event -> {
                    ProductoVenta producto = getTableView().getItems().get(getIndex());
                    getTableView().getItems().remove(producto);
                    actualizarTotal(); // si tenés un método para recalcular el total
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(btn);
                }
            }
        });
    }

    private void configurarMetodosPago() {
        pagoEfectivo.setToggleGroup(grupoPago);
        pagoPedidosYaEfectivo.setToggleGroup(grupoPago);
        pagoTarjeta.setToggleGroup(grupoPago);
        pagoMP.setToggleGroup(grupoPago);
        pagoTransferencia.setToggleGroup(grupoPago);

        // Opcional: Seleccionar un método por defecto
        pagoEfectivo.setSelected(true);
    }

    private void configurarColumnas() {
        colCodigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        colSubtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        tablaProductos.setItems(productosAgregados);
    }

    private void configurarEventos() {
        // Buscar con Enter
        campoBusqueda.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER && !campoBusqueda.getText().isEmpty()) {
                agregarProducto();
            }
        });

        // Editar cantidad con doble clic
        tablaProductos.setRowFactory(tv -> {
            TableRow<ProductoVenta> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    editarCantidad(row.getItem());
                }
            });
            return row;
        });
    }

    @FXML
    private void agregarProducto() {
        String busqueda = campoBusqueda.getText().trim();
        if (busqueda.isEmpty()) {
            mostrarAlerta("Debe ingresar un código o nombre de producto", "Venta agregada al histórico");
            return;
        }

        ProductoVenta producto = buscarProductoEnBD(busqueda);

        if (producto != null) {
            productosAgregados.add(producto);
            actualizarTotal();
            campoBusqueda.clear();
            campoBusqueda.requestFocus();
        } else {
            mostrarAlerta("Producto no encontrado", "Venta agregada al histórico");
        }
    }

    private ProductoVenta buscarProductoEnBD(String busqueda) {
        // Datos de ejemplo (simulando búsqueda)
        String codigo = "PRD-" + System.currentTimeMillis() % 1000;
        String nombre = "Producto " + busqueda;
        int cantidad = 1; // Cantidad inicial
        BigDecimal precio = new BigDecimal("150.50");

        return new ProductoVenta(
                codigo,
                nombre,
                cantidad,
                precio
        );
    }

    private void editarCantidad(ProductoVenta producto) {
        TextInputDialog dialog = new TextInputDialog(String.valueOf(producto.getCantidad()));
        dialog.setTitle("Editar cantidad");
        dialog.setHeaderText("Modificar cantidad para: " + producto.getNombre());
        dialog.setContentText("Nueva cantidad:");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(cantidad -> {
            try {
                int nuevaCantidad = Integer.parseInt(cantidad);
                if (nuevaCantidad > 0) {
                    producto.setCantidad(nuevaCantidad);
                    actualizarTotal();
                    tablaProductos.refresh();
                } else {
                    mostrarAlerta("La cantidad debe ser mayor a cero", "Venta agregada al histórico");
                }
            } catch (NumberFormatException e) {
                mostrarAlerta("Debe ingresar un número válido", "Venta agregada al histórico");
            }
        });
    }
/*
    @FXML
    private void finalizarVenta() {
        // Validaciones
        if (productosAgregados.isEmpty()) {
            mostrarAlerta("No hay productos en la venta");
            return;
        }

        if (grupoPago.getSelectedToggle() == null) {
            mostrarAlerta("Seleccione un método de pago");
            return;
        }

        RadioButton metodoSeleccionado = (RadioButton) grupoPago.getSelectedToggle();
        String detalles = campoDetalles.getText();

        // Confirmación
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar venta");
        confirmacion.setHeaderText("Total: " + labelTotal.getText());
        confirmacion.setContentText("¿Confirmar venta con " + metodoSeleccionado.getText() + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            guardarVentaEnBD();
            limpiarFormulario();
        }
        crearVentaFicticia(); // <-- Aquí se envía al histórico
        limpiarFormulario();
        mostrarAlerta("Venta registrada en el histórico");
    }*/

/*
    @FXML
    private void finalizarVenta() {
        // 1. Crear objeto Venta con datos ficticios (ajusta esto)
        VentasController.Venta nuevaVenta = new VentasController.Venta(
                "Venta",
                "VEN-" + (System.currentTimeMillis() % 1000),
                "Resumen de productos", // Usa productosAgregados para esto
                "Efectivo", // Método de pago real
                LocalDate.now().toString(),
                LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                "Caja 1",
                String.valueOf(productosAgregados.size()),
                "Cliente General",
                campoDetalles.getText(),
                productosAgregados // ← Lista de productos de la venta
        );

        // 2. Enviar al histórico
        if (ventasController != null) {
            ventasController.agregarVentaAlHistorico(nuevaVenta);
            mostrarAlerta("Confirmar Venta?", "Venta agregada al histórico");
        } else {
            mostrarAlerta("Error", "No se pudo conectar con el histórico");
        }

        // 3. Limpiar formulario
        limpiarFormulario();
    }
*/
@FXML
private void finalizarVenta() {
    // Validaciones básicas
    if (productosAgregados.isEmpty()) {
        mostrarAlerta("Error", "No hay productos en la venta");
        return;
    }

    if (grupoPago.getSelectedToggle() == null) {
        mostrarAlerta("Error", "Seleccione un método de pago");
        return;
    }

    // 1. Obtener método de pago seleccionado
    RadioButton metodoSeleccionado = (RadioButton) grupoPago.getSelectedToggle();
    String metodoPago = metodoSeleccionado.getText(); // Captura "Efectivo", "Mercado Pago", etc.

    // 2. Crear resumen de productos
    String resumen = productosAgregados.stream()
            .map(p -> p.getNombre() + " (x" + p.getCantidad() + ")")
            .collect(Collectors.joining(", "));

    // 3. Crear objeto Venta
    VentasController.Venta nuevaVenta = new VentasController.Venta(
            "Venta",
            "VEN-" + (System.currentTimeMillis() % 1000),
            resumen,
            metodoPago, // ← Usa el método capturado, no hardcodeado
            LocalDate.now().toString(),
            LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
            "Caja 1",
            String.valueOf(productosAgregados.size()),
            "Cliente General",
            campoDetalles.getText(),
            FXCollections.observableArrayList(productosAgregados) // Copia de la lista
    );

    // 4. Enviar al histórico
    if (ventasController != null) {
        ventasController.agregarVentaAlHistorico(nuevaVenta);
        mostrarAlerta("Éxito", "Venta registrada correctamente");
    } else {
        mostrarAlerta("Error", "No se pudo conectar con el histórico");
    }

    // 5. Limpiar formulario
    limpiarFormulario();
}
    private void guardarVentaEnBD() {
        // TODO: Implementar persistencia real
        System.out.println("Venta guardada:");
        System.out.println("Total: " + labelTotal.getText());
        System.out.println("Método: " + ((RadioButton)grupoPago.getSelectedToggle()).getText());
        System.out.println("Detalles: " + campoDetalles.getText());
        productosAgregados.forEach(p ->
                System.out.println(p.getCodigo() + " - " + p.getNombre() + " x" + p.getCantidad())
        );

    }


    @FXML
    private void cancelarVenta() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cancelar venta");
        confirmacion.setHeaderText("¿Está seguro de cancelar esta venta?");
        confirmacion.setContentText("Se perderán todos los productos agregados");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            limpiarFormulario();
        }
    }

    private void limpiarFormulario() {
        productosAgregados.clear();
        campoBusqueda.clear();
        campoDetalles.clear();
        grupoPago.selectToggle(null);
        pagoEfectivo.setSelected(true); // Resetear selección
        actualizarTotal();
        campoBusqueda.requestFocus();
    }

  /*  private void actualizarTotal() {
        BigDecimal total = productosAgregados.stream()
                .map(ProductoVenta::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        labelTotal.setText("$" + total.setScale(2, BigDecimal.ROUND_HALF_UP));
    }*/
  private void actualizarTotal() {
      BigDecimal total = productosAgregados.stream()
              .map(p -> new BigDecimal(p.getPrecio()).multiply(new BigDecimal(p.getCantidad())))
              .reduce(BigDecimal.ZERO, BigDecimal::add);

      labelTotal.setText("$" + total.setScale(2, RoundingMode.HALF_UP));
  }

    private void mostrarAlerta(String mensaje, String venta_agregada_al_histórico) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Atención");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void crearVentaFicticia() {
        // Datos básicos de ejemplo (ajusta según necesites)
        String metodoPago = ((RadioButton) grupoPago.getSelectedToggle()).getText();
        String resumen = productosAgregados.stream()
                .map(p -> p.getNombre() + " x" + p.getCantidad())
                .collect(Collectors.joining(", "));

        VentasController.Venta nuevaVenta = new VentasController.Venta(
                "Venta",                          // Operación
                "VEN-" + (System.currentTimeMillis() % 1000), // Número único
                resumen,                          // Resumen de productos
                metodoPago,                       // Tipo de pago
                java.time.LocalDate.now().toString(), // Fecha actual
                java.time.LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm")), // Hora
                "Caja 1",                         // Origen
                String.valueOf(productosAgregados.size()), // Cant. artículos
                "Cliente Default",                // Cliente
                campoDetalles.getText(),           // Observaciones
                productosAgregados // ← Lista de productos de la venta
        );
    }

    public void setVentasController(VentasController ventasController) {
        this.ventasController = ventasController;
    }

}

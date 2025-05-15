package com.lobo24.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class EstadisticasController implements Initializable {

    @FXML
    private Button btnRegresar;

    @FXML
    private Label lblTitulo;

    // Menú de navegación
    @FXML private Label btnRentabilidad;
    @FXML private Label btnIngresosGastos;
    @FXML private Label btnPorProductoVendido;
    @FXML private Label btnProveedores;
    @FXML private Label btnComprasPorProducto;
    @FXML private Label btnVentasAnalitico;
    @FXML private Label btnComprasPorProveedor;
    @FXML private Label btnVentasSintetico;
    @FXML private Label btnVentas;
    @FXML private Label btnMediosPago;
    @FXML private Label btnComisionPorVendedor;
    @FXML private Label btnHorarioPunta;
    @FXML private Label btnPorCategoriaProducto;
    @FXML private Label btnPorVendedor;
    @FXML private Label btnPorProducto;
    @FXML private Label btnPorMarca;
    @FXML private Label btnPorClienteCategoria;
    @FXML private Label btnPorClienteProducto;
    @FXML private Label btnPorCombo;
    @FXML private Label btnVentasRetencion;
    @FXML private Label btnPorTasa;

    @FXML
    private ComboBox<String> comboFechaTransaccion;

    @FXML
    private ComboBox<String> comboHora;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Label lblFacturacion;

    @FXML
    private Label lblCantVentas;

    @FXML
    private Label lblTicketMedio;

    @FXML
    private Label lblGanancias;

    @FXML
    private BarChart<String, Number> barChartVentas;

    @FXML
    private CategoryAxis xAxisHora;

    @FXML
    private NumberAxis yAxisVentas;

    @FXML
    private TableView<VentasPorHora> tableVentas;

    @FXML
    private TableColumn<VentasPorHora, String> colHora;

    @FXML
    private TableColumn<VentasPorHora, Integer> colVentas;

    @FXML
    private TableColumn<VentasPorHora, String> colTicketMedio;

    @FXML
    private TableColumn<VentasPorHora, String> colGanancias;

    @FXML
    private TableColumn<VentasPorHora, String> colFacturacion;

    private ObservableList<VentasPorHora> ventasPorHoraData = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Configurar filtros
        setupFiltros();

        // Configurar tabla
        setupTabla();

        // Configurar gráfico
        setupGrafico();

        // Configurar estilo de cursor para los menús (para que se vean como botones)
        setupMenuStyles();

        // Cargar datos de ejemplo (estos serían reemplazados por datos reales)
        cargarDatosEjemplo();
    }

    private void setupFiltros() {
        // Configurar fechas
        datePicker.setValue(LocalDate.now());

        // Configurar comboboxes
        comboFechaTransaccion.setItems(FXCollections.observableArrayList(
                "por fecha de transacción",
                "por fecha de creación",
                "por fecha de pago"
        ));
        comboFechaTransaccion.getSelectionModel().selectFirst();

        comboHora.setItems(FXCollections.observableArrayList(
                "Hora",
                "Día",
                "Semana",
                "Mes"
        ));
        comboHora.getSelectionModel().selectFirst();
    }

    private void setupTabla() {
        // Configurar columnas
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        colVentas.setCellValueFactory(new PropertyValueFactory<>("ventas"));
        colTicketMedio.setCellValueFactory(new PropertyValueFactory<>("ticketMedio"));
        colGanancias.setCellValueFactory(new PropertyValueFactory<>("ganancias"));
        colFacturacion.setCellValueFactory(new PropertyValueFactory<>("facturacion"));

        // Asignar datos a la tabla
        tableVentas.setItems(ventasPorHoraData);
    }

    private void setupGrafico() {
        // Configurar ejes
        xAxisHora.setLabel("Hora");
        yAxisVentas.setLabel("Ventas");

        // Otras configuraciones del gráfico
        barChartVentas.setAnimated(false);
        barChartVentas.setLegendVisible(false);
    }

    private void setupMenuStyles() {
        // Configurar todos los elementos del menú para que se vean como elementos clicables
        Label[] menuItems = {
                btnRentabilidad, btnIngresosGastos, btnPorProductoVendido, btnProveedores,
                btnComprasPorProducto, btnVentasAnalitico, btnComprasPorProveedor, btnVentasSintetico,
                btnVentas, btnMediosPago, btnComisionPorVendedor, btnHorarioPunta,
                btnPorCategoriaProducto, btnPorVendedor, btnPorProducto, btnPorMarca,
                btnPorClienteCategoria, btnPorClienteProducto, btnPorCombo, btnVentasRetencion, btnPorTasa
        };

        for (Label item : menuItems) {
            item.setStyle("-fx-cursor: hand;");
        }
    }

    private void cargarDatosEjemplo() {
        // Limpiar datos existentes
        ventasPorHoraData.clear();
        barChartVentas.getData().clear();

        // Datos de resumen
        lblFacturacion.setText("$ 17.682.241,34");
        lblCantVentas.setText("5386");
        lblTicketMedio.setText("$ 3.283,00");
        lblGanancias.setText("$ 4.905.239,94");

        // Datos de tabla
        ventasPorHoraData.addAll(
                new VentasPorHora("0:00", 293, "$ 4.203,14", "$ 358.986,83", "$ 1.231.520,55"),
                new VentasPorHora("1:00", 207, "$ 3.283,77", "$ 210.615,37", "$ 679.740,33"),
                new VentasPorHora("2:00", 168, "$ 3.254,23", "$ 180.751,90", "$ 546.710,45"),
                new VentasPorHora("3:00", 120, "$ 3.708,67", "$ 136.036,97", "$ 445.040,14"),
                new VentasPorHora("4:00", 67, "$ 3.035,82", "$ 65.015,53", "$ 203.400,09"),
                new VentasPorHora("5:00", 42, "$ 2.987,56", "$ 39.842,31", "$ 125.477,52"),
                new VentasPorHora("6:00", 53, "$ 3.125,91", "$ 48.275,26", "$ 165.673,23"),
                new VentasPorHora("7:00", 87, "$ 3.421,38", "$ 98.745,65", "$ 297.660,06"),
                new VentasPorHora("8:00", 156, "$ 3.768,52", "$ 205.674,32", "$ 587.889,12"),
                new VentasPorHora("9:00", 243, "$ 3.894,61", "$ 328.942,17", "$ 946.190,23"),
                new VentasPorHora("10:00", 325, "$ 4.102,37", "$ 467.583,29", "$ 1.333.270,25"),
                new VentasPorHora("11:00", 378, "$ 4.237,85", "$ 532.908,76", "$ 1.601.906,30"),
                new VentasPorHora("12:00", 402, "$ 4.318,54", "$ 578.641,92", "$ 1.736.053,08"),
                new VentasPorHora("13:00", 387, "$ 4.256,93", "$ 547.832,15", "$ 1.647.433,91"),
                new VentasPorHora("14:00", 342, "$ 4.123,71", "$ 486.521,37", "$ 1.410.308,82"),
                new VentasPorHora("15:00", 298, "$ 3.956,84", "$ 412.875,23", "$ 1.179.138,32"),
                new VentasPorHora("16:00", 265, "$ 3.745,19", "$ 345.672,81", "$ 992.475,35"),
                new VentasPorHora("17:00", 238, "$ 3.628,37", "$ 301.542,68", "$ 863.551,66"),
                new VentasPorHora("18:00", 278, "$ 3.852,73", "$ 372.461,59", "$ 1.071.058,94"),
                new VentasPorHora("19:00", 326, "$ 4.075,82", "$ 456.932,74", "$ 1.328.717,32"),
                new VentasPorHora("20:00", 349, "$ 4.183,26", "$ 502.745,38", "$ 1.459.957,74"),
                new VentasPorHora("21:00", 321, "$ 4.097,45", "$ 459.873,21", "$ 1.315.281,45"),
                new VentasPorHora("22:00", 276, "$ 3.842,91", "$ 368.743,52", "$ 1.060.643,16"),
                new VentasPorHora("23:00", 237, "$ 3.612,58", "$ 297.625,44", "$ 856.181,46")
        );

        // Datos de gráfico
        XYChart.Series<String, Number> series = new XYChart.Series<>();

        // Agregar datos al gráfico para todas las horas (0-23)
        for (int i = 0; i < 24; i++) {
            String hora = i + ":00";

            // Buscar los datos correspondientes en la tabla de ventas por hora
            int ventas = 0;
            for (VentasPorHora vph : ventasPorHoraData) {
                if (vph.getHora().equals(hora)) {
                    ventas = vph.getVentas();
                    break;
                }
            }

            // Si no se encuentra en la tabla, usar un valor aleatorio
            if (ventas == 0) {
                ventas = (int)(Math.random() * 100) + 50;
            }

            series.getData().add(new XYChart.Data<>(hora, ventas));
        }

        barChartVentas.getData().add(series);

        // Personalizar barras del gráfico (opcional)
        for (XYChart.Data<String, Number> data : series.getData()) {
            // Aquí se podría personalizar cada barra si fuera necesario
            // Por ejemplo: data.getNode().setStyle("-fx-bar-fill: #4CAF50;");
        }
    }

    @FXML
    private void onMenuItemClicked(MouseEvent event) {
        // Identificar qué elemento del menú fue clicado
        Label source = (Label) event.getSource();
        String menuId = source.getId();

        System.out.println("Menú clicado: " + menuId);

        // Implementar la lógica para cada elemento del menú
        switch (menuId) {
            case "btnRentabilidad":
                cargarDatosRentabilidad();
                break;
            case "btnIngresosGastos":
                cargarDatosIngresosGastos();
                break;
            case "btnPorProductoVendido":
                cargarDatosPorProductoVendido();
                break;
            case "btnProveedores":
                cargarDatosProveedores();
                break;
            case "btnComprasPorProducto":
                cargarDatosComprasPorProducto();
                break;
            case "btnVentasAnalitico":
                cargarDatosVentasAnalitico();
                break;
            case "btnComprasPorProveedor":
                cargarDatosComprasPorProveedor();
                break;
            case "btnVentasSintetico":
                cargarDatosVentasSintetico();
                break;
            case "btnVentas":
                cargarDatosVentas();
                break;
            case "btnMediosPago":
                cargarDatosMediosPago();
                break;
            case "btnComisionPorVendedor":
                cargarDatosComisionPorVendedor();
                break;
            case "btnHorarioPunta":
                cargarDatosHorarioPunta();
                break;
            case "btnPorCategoriaProducto":
                cargarDatosPorCategoriaProducto();
                break;
            case "btnPorVendedor":
                cargarDatosPorVendedor();
                break;
            case "btnPorProducto":
                cargarDatosPorProducto();
                break;
            case "btnPorMarca":
                cargarDatosPorMarca();
                break;
            case "btnPorClienteCategoria":
                cargarDatosPorClienteCategoria();
                break;
            case "btnPorClienteProducto":
                cargarDatosPorClienteProducto();
                break;
            case "btnPorCombo":
                cargarDatosPorCombo();
                break;
            case "btnVentasRetencion":
                cargarDatosVentasRetencion();
                break;
            case "btnPorTasa":
                cargarDatosPorTasa();
                break;
            default:
                System.out.println("Opción no implementada");
        }
    }

    // Métodos para cargar diferentes tipos de datos
    private void cargarDatosRentabilidad() {
        System.out.println("Cargando datos de Rentabilidad");
        actualizarTituloDashboard("Rentabilidad");

        // Limpiar datos existentes
        ventasPorHoraData.clear();
        barChartVentas.getData().clear();

        // Datos de ejemplo para rentabilidad
        lblFacturacion.setText("$ 19.347.852,45");
        lblCantVentas.setText("4820");
        lblTicketMedio.setText("$ 4.013,87");
        lblGanancias.setText("$ 5.804.355,73");

        // Aquí se cargarían los datos específicos para la sección de Rentabilidad
        // ...
    }

    private void cargarDatosIngresosGastos() {
        System.out.println("Cargando datos de Ingresos / Gastos");
        actualizarTituloDashboard("Ingresos / Gastos");

        // Limpiar datos existentes
        ventasPorHoraData.clear();
        barChartVentas.getData().clear();

        // Actualizar etiquetas y títulos de gráficos
        xAxisHora.setLabel("Periodo");
        yAxisVentas.setLabel("Monto");

        // Aquí se cargarían los datos específicos para la sección de Ingresos / Gastos
        // ...
    }

    private void cargarDatosPorProductoVendido() {
        System.out.println("Cargando datos por Producto vendido");
        actualizarTituloDashboard("Por Producto vendido");

        // Limpiar datos existentes
        ventasPorHoraData.clear();
        barChartVentas.getData().clear();

        // Actualizar etiquetas y títulos de gráficos
        xAxisHora.setLabel("Producto");
        yAxisVentas.setLabel("Unidades vendidas");

        // Aquí se cargarían los datos específicos para la sección de Por Producto vendido
        // ...
    }

    private void cargarDatosProveedores() {
        System.out.println("Cargando datos de Proveedores");
        actualizarTituloDashboard("Proveedores");

        // Implementación para cargar datos específicos de Proveedores
        // ...
    }

    private void cargarDatosComprasPorProducto() {
        System.out.println("Cargando datos de Compras por Producto");
        actualizarTituloDashboard("Compras por Producto");

        // Implementación para cargar datos específicos de Compras por Producto
        // ...
    }

    private void cargarDatosVentasAnalitico() {
        System.out.println("Cargando datos de Ventas Analítico");
        actualizarTituloDashboard("Ventas Analítico");

        // Implementación para cargar datos específicos de Ventas Analítico
        // ...
    }

    private void cargarDatosComprasPorProveedor() {
        System.out.println("Cargando datos de Compras por Proveedor");
        actualizarTituloDashboard("Compras por Proveedor");

        // Implementación para cargar datos específicos de Compras por Proveedor
        // ...
    }

    private void cargarDatosVentasSintetico() {
        System.out.println("Cargando datos de Ventas Sintético");
        actualizarTituloDashboard("Ventas Sintético");

        // Implementación para cargar datos específicos de Ventas Sintético
        // ...
    }

    private void cargarDatosVentas() {
        System.out.println("Cargando datos de Ventas");
        actualizarTituloDashboard("Ventas");

        // Restablecer valores predeterminados y cargar datos de ejemplo
        cargarDatosEjemplo();
    }

    private void cargarDatosMediosPago() {
        System.out.println("Cargando datos de Medios de pago");
        actualizarTituloDashboard("Medios de pago");

        // Limpiar datos existentes
        ventasPorHoraData.clear();
        barChartVentas.getData().clear();

        // Actualizar etiquetas y títulos de gráficos
        xAxisHora.setLabel("Medio de pago");
        yAxisVentas.setLabel("Monto");

        // Crear datos de ejemplo para medios de pago
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.getData().add(new XYChart.Data<>("Efectivo", 6548230.45));
        series.getData().add(new XYChart.Data<>("Tarjeta Débito", 5230147.89));
        series.getData().add(new XYChart.Data<>("Tarjeta Crédito", 4125689.32));
        series.getData().add(new XYChart.Data<>("Transferencia", 1547823.56));
        series.getData().add(new XYChart.Data<>("QR", 230350.12));

        barChartVentas.getData().add(series);
    }

    private void cargarDatosComisionPorVendedor() {
        System.out.println("Cargando datos de Comisión por vendedor");
        actualizarTituloDashboard("Comisión por vendedor");

        // Implementación para cargar datos específicos de Comisión por vendedor
        // ...
    }

    private void cargarDatosHorarioPunta() {
        System.out.println("Cargando datos de Horario punta");
        actualizarTituloDashboard("Horario punta");

        // Implementación similar a los datos por hora pero enfocado en horas punta
        // ...
    }

    private void cargarDatosPorCategoriaProducto() {
        System.out.println("Cargando datos por Categoría y producto");
        actualizarTituloDashboard("Por Categoría y producto");

        // Implementación para cargar datos específicos de Por Categoría y producto
        // ...
    }

    private void cargarDatosPorVendedor() {
        System.out.println("Cargando datos por Vendedor");
        actualizarTituloDashboard("Por Vendedor");

        // Implementación para cargar datos específicos de Por Vendedor
        // ...
    }

    private void cargarDatosPorProducto() {
        System.out.println("Cargando datos por Producto");
        actualizarTituloDashboard("Por Producto");

        // Implementación para cargar datos específicos de Por Producto
        // ...
    }

    private void cargarDatosPorMarca() {
        System.out.println("Cargando datos por Marca");
        actualizarTituloDashboard("Por Marca");

        // Implementación para cargar datos específicos de Por Marca
        // ...
    }

    private void cargarDatosPorClienteCategoria() {
        System.out.println("Cargando datos por Cliente y categoría");
        actualizarTituloDashboard("Por Cliente y categoría");

        // Implementación para cargar datos específicos de Por Cliente y categoría
        // ...
    }

    private void cargarDatosPorClienteProducto() {
        System.out.println("Cargando datos por Cliente y producto");
        actualizarTituloDashboard("Por Cliente y producto");

        // Implementación para cargar datos específicos de Por Cliente y producto
        // ...
    }

    private void cargarDatosPorCombo() {
        System.out.println("Cargando datos por Combo");
        actualizarTituloDashboard("Por Combo");

        // Implementación para cargar datos específicos de Por Combo
        // ...
    }

    private void cargarDatosVentasRetencion() {
        System.out.println("Cargando datos de Ventas / Retención");
        actualizarTituloDashboard("Ventas / Retención");

        // Implementación para cargar datos específicos de Ventas / Retención
        // ...
    }

    private void cargarDatosPorTasa() {
        System.out.println("Cargando datos por Tasa");
        actualizarTituloDashboard("Por Tasa");

        // Implementación para cargar datos específicos de Por Tasa
        // ...
    }

    // Método para actualizar el título del dashboard según la sección seleccionada
    private void actualizarTituloDashboard(String titulo) {
        // Actualizar el título de la sección en la interfaz
        lblTitulo.setText("Estadísticas - " + titulo);
    }

    @FXML
    private void onRegresarClick(ActionEvent event) {
        // Aquí va la lógica para regresar a la vista anterior
        System.out.println("Regresar a Resumen");
        // SceneManager.loadScene("resumen.fxml"); // Implementar según tu arquitectura
    }

    // Clase para manejar los datos de la tabla
    public static class VentasPorHora {
        private String hora;
        private int ventas;
        private String ticketMedio;
        private String ganancias;
        private String facturacion;

        public VentasPorHora(String hora, int ventas, String ticketMedio, String ganancias, String facturacion) {
            this.hora = hora;
            this.ventas = ventas;
            this.ticketMedio = ticketMedio;
            this.ganancias = ganancias;
            this.facturacion = facturacion;
        }

        public String getHora() {
            return hora;
        }

        public void setHora(String hora) {
            this.hora = hora;
        }

        public int getVentas() {
            return ventas;
        }

        public void setVentas(int ventas) {
            this.ventas = ventas;
        }

        public String getTicketMedio() {
            return ticketMedio;
        }

        public void setTicketMedio(String ticketMedio) {
            this.ticketMedio = ticketMedio;
        }

        public String getGanancias() {
            return ganancias;
        }

        public void setGanancias(String ganancias) {
            this.ganancias = ganancias;
        }

        public String getFacturacion() {
            return facturacion;
        }

        public void setFacturacion(String facturacion) {
            this.facturacion = facturacion;
        }
    }
}
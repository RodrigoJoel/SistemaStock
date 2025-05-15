package com.lobo24.controllers;


import com.lobo24.models.Factura;
import com.lobo24.util.AlertUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class CuentasAPagarController {

    @FXML
    private TableView<Factura> tablaCuentas;
    @FXML
    private TableColumn<Factura, String> colOpciones;
    @FXML
    private TableColumn<Factura, String> colStatus;
    @FXML
    private TableColumn<Factura, LocalDate> colVencimiento;
    @FXML
    private TableColumn<Factura, Double> colValor;
    @FXML
    private TableColumn<Factura, String> colReferente;
    @FXML
    private TableColumn<Factura, String> colProveedor;
    @FXML
    private TableColumn<Factura, String> colTipo;
    @FXML
    private TableColumn<Factura, Integer> colDias;
    @FXML
    private TableColumn<Factura, String> colMes;

    @FXML
    private TextField txtBuscar;
    @FXML
    private Button btnNuevo;
    @FXML
    private MenuButton btnFiltroEstado;
    @FXML
    private Button btnFiltros;
    @FXML
    private Button btnOpciones;
    @FXML
    private Button btnConfig;
    @FXML
    private MenuButton menuPlan;
    @FXML
    private Button btnPerfil;
    @FXML
    private Button btnAyuda;

    private ObservableList<Factura> listaFacturas = FXCollections.observableArrayList();
    private FilteredList<Factura> facturasFiltradas;

    @FXML
    public void initialize() {
        // Configurar columnas
        configureTableColumns();

        // Cargar datos de ejemplo
        cargarDatosDeEjemplo();

        // Configurar filtro de búsqueda
        configurarBuscador();

        // Configurar eventos de botones
      //  configurarEventosBotones();
    }

    private void configureTableColumns() {
        // Configurar celdas de opciones con botones de editar
        colOpciones.setCellFactory(param -> new TableCell<>() {
            private final Button btnEditar = new Button("Editar");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    btnEditar.setOnAction(event -> {
                        Factura factura = getTableView().getItems().get(getIndex());
                       // mostrarDialogoEdicion(factura);
                    });
                    setGraphic(btnEditar);
                }
            }
        });

        // Configurar celdas de estado con checkmark
        colStatus.setCellFactory(param -> new TableCell<>() {
            private final CheckBox checkBox = new CheckBox();

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    Factura factura = getTableView().getItems().get(getIndex());
                    checkBox.setSelected("Pago".equals(factura.getStatus()));
                    Label label = new Label("Pago");
                    HBox box = new HBox(5, checkBox, label);
                    setGraphic(box);
                }
            }
        });

        // Configurar las demás columnas
        colVencimiento.setCellValueFactory(new PropertyValueFactory<>("fechaVencimiento"));
        colVencimiento.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                }
            }
        });

        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colValor.setCellFactory(column -> new TableCell<>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("$ %.2f", item));
                }
            }
        });

        colReferente.setCellValueFactory(new PropertyValueFactory<>("referente"));
        colProveedor.setCellValueFactory(new PropertyValueFactory<>("proveedor"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colDias.setCellValueFactory(new PropertyValueFactory<>("diasVencimiento"));

        colMes.setCellValueFactory(cellData -> {
            LocalDate fecha = cellData.getValue().getFechaVencimiento();
            String mes = fecha.format(DateTimeFormatter.ofPattern("MM/yyyy"));
            String diasText = cellData.getValue().getDiasVencimiento() + "seg";
            return new SimpleStringProperty(mes + " " + diasText);
        });

        // Asignar datos a la tabla
        tablaCuentas.setItems(listaFacturas);
    }

    private void cargarDatosDeEjemplo() {
        // Crear facturas de ejemplo basadas en la imagen
        listaFacturas.add(new Factura("Pago", LocalDate.of(2023, 11, 24), 19110.00, "CABALGATA", "cabalgata", "Factura", 0));

        // Crear lista filtrable
        facturasFiltradas = new FilteredList<>(listaFacturas);
        tablaCuentas.setItems(facturasFiltradas);
    }

    private void configurarBuscador() {
        txtBuscar.textProperty().addListener((observable, oldValue, newValue) -> {
            facturasFiltradas.setPredicate(factura -> {
                // Si el campo de búsqueda está vacío, muestra todos los registros
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                // Buscar en diferentes campos
                if (factura.getReferente().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (factura.getProveedor().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (String.valueOf(factura.getValor()).contains(lowerCaseFilter)) {
                    return true;
                }

                return false;
            });
        });
    }
/*

        private void configurarEventosBotones() {
            // Configurar evento para botón nuevo
            btnNuevo.setOnAction(event -> mostrarDialogoNuevaFactura());

            // Configurar evento para botón de filtro de estado
            btnFiltroEstado.getItems().forEach(menuItem -> {
                menuItem.setOnAction(event -> {
                    String filtroSeleccionado = menuItem.getText();
                    aplicarFiltroEstado(filtroSeleccionado);
                    btnFiltroEstado.setText(filtroSeleccionado);
                });
            });

            // Configurar evento para botón de filtros avanzados
            btnFiltros.setOnAction(event -> mostrarFiltrosAvanzados());

            // Configurar evento para botón de ayuda
            btnAyuda.setOnAction(event -> mostrarAyuda());
        }
    */
    private void aplicarFiltroEstado(String filtroEstado) {
        facturasFiltradas.setPredicate(factura -> {
            if ("Todos los estados".equals(filtroEstado)) {
                return true;
            }
            return factura.getStatus().equals(filtroEstado);
        });
    }

    private void mostrarDialogoNuevaFactura() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tuapp/views/NuevaFacturaDialog.fxml"));
            Parent root = loader.load();

            NuevaFacturaDialogController controller = loader.getController();
            controller.setOnFacturaGuardada(factura -> {
                listaFacturas.add(factura);
                tablaCuentas.refresh();
            });

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Nueva Factura");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.mostrarError("Error", "Error al abrir el diálogo de nueva factura", e.getMessage());
        }
    }

    private void mostrarDialogoEdicion(Factura factura) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tuapp/views/EditarFacturaDialog.fxml"));
            Parent root = loader.load();

            EditarFacturaDialogController controller = loader.getController();
            controller.setFactura(factura);
            controller.setOnFacturaEditada(facturaEditada -> {
                tablaCuentas.refresh();
            });

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Editar Factura");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.mostrarError("Error", "Error al abrir el diálogo de edición", e.getMessage());
        }
    }
/*
    private void mostrarFiltrosAvanzados() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/tuapp/views/FiltrosAvanzadosDialog.fxml"));
            Parent root = loader.load();

            FiltrosAvanzadosDialogController controller = loader.getController();
            controller.setOnFiltrosAplicados(predicado -> {
                facturasFiltradas.setPredicate(predicado);
            });

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Filtros Avanzados");
            stage.setScene(new Scene(root));
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            AlertUtils.mostrarError("Error", "Error al abrir el diálogo de filtros", e.getMessage());
        }
    }
*/
    private void mostrarAyuda() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Ayuda - Cuentas a Pagar");
        alert.setHeaderText("Módulo de Cuentas a Pagar");
        alert.setContentText("Este módulo le permite gestionar todas las facturas a pagar.\n\n" +
                "- Use el botón + para agregar nuevas facturas\n" +
                "- Use el campo de búsqueda para filtrar por cualquier dato\n" +
                "- Use el botón de filtro para aplicar filtros avanzados\n" +
                "- Haga clic en 'Editar' para modificar una factura existente");
        alert.showAndWait();
    }
}
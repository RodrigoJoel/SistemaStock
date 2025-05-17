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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.geometry.Bounds;

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

    // Nuevos componentes para el menú de usuario
    @FXML private StackPane userMenuContainer;
    @FXML private Circle userCircle;
    @FXML private Label userInitial;
    @FXML private Label lblUsuario;

    private ObservableList<Venta> listaVentas = FXCollections.observableArrayList();
    private Popup userPopup;

    // Datos del usuario actual
    private String nombreUsuario = "German";
   // private String emailUsuario = "germanveganaga@gmail.com";

    @FXML
    public void initialize() {
        // Inicialización de la tabla
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

        // Inicialización del menú de usuario
        setupUserMenu();
    }

    private void setupUserMenu() {
        // Si alguno de los componentes es null, salimos del método
        if (userMenuContainer == null || userCircle == null || userInitial == null) {
            System.out.println("Alguno de los componentes del menú de usuario no está definido en el FXML");
            return;
        }

        // Configurar colores del círculo de usuario
        userCircle.setFill(Color.web("#3498db"));

        // Mostrar inicial del usuario
        String inicial = nombreUsuario != null && !nombreUsuario.isEmpty() ?
                nombreUsuario.substring(0, 1).toUpperCase() : "G";
        userInitial.setText(inicial);

        // Configurar evento de clic
        userMenuContainer.setOnMouseClicked(event -> mostrarMenuUsuario());
    }

    public void mostrarMenuUsuario() {
        if (userPopup == null) {
            crearMenuUsuario();
        }

        if (!userPopup.isShowing()) {
            Bounds bounds = userMenuContainer.localToScreen(userMenuContainer.getBoundsInLocal());
            userPopup.show(userMenuContainer.getScene().getWindow(),
                    bounds.getMinX() - 150, // Alineamos el popup a la derecha del icono
                    bounds.getMaxY());
        } else {
            userPopup.hide();
        }
    }

    private void crearMenuUsuario() {
        userPopup = new Popup();
        userPopup.setAutoHide(true);

        // Crear el contenido del menú
        VBox menuContent = new VBox();
        menuContent.setStyle("-fx-background-color: white; -fx-border-color: #ddd; -fx-border-width: 1; " +
                "-fx-padding: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 10, 0, 0, 5);");
        menuContent.setPrefWidth(220);

        // Información de usuario
        Label userName = new Label(nombreUsuario);
        userName.setStyle("-fx-font-size: 16; -fx-font-weight: bold;");

   //     Label userEmail = new Label(emailUsuario);
        //userEmail.setStyle("-fx-font-size: 12; -fx-text-fill: #666;");

        Separator separator1 = new Separator();
        separator1.setPrefWidth(200);

        // Opciones del menú
        Button btnCambiarUsuario = new Button("CAMBIAR USUARIO");
        btnCambiarUsuario.setStyle("-fx-background-color: transparent; -fx-text-fill: #333; " +
                "-fx-padding: 5 0; -fx-alignment: CENTER;");
        btnCambiarUsuario.setPrefWidth(200);
        btnCambiarUsuario.setOnAction(e -> cambiarUsuario());

        Button btnCambiarContrasena = new Button("Cambiar Contraseña");
        btnCambiarContrasena.setStyle("-fx-background-color: transparent; -fx-text-fill: #333; " +
                "-fx-padding: 5 0; -fx-alignment: CENTER_LEFT;");
        btnCambiarContrasena.setPrefWidth(200);
        btnCambiarContrasena.setOnAction(e -> cambiarContrasena());

        Button btnGestionarUsuarios = new Button("Gestionar Usuarios");
        btnGestionarUsuarios.setStyle("-fx-background-color: transparent; -fx-text-fill: #3498db; " +
                "-fx-padding: 5 0; -fx-alignment: CENTER_LEFT;");
        btnGestionarUsuarios.setPrefWidth(200);
        btnGestionarUsuarios.setOnAction(e -> gestionarUsuarios());

        // Agregar los componentes al menú
        menuContent.getChildren().addAll(
                userName,
                separator1,
                btnCambiarUsuario,
                btnCambiarContrasena,
                btnGestionarUsuarios
        );

        userPopup.getContent().add(menuContent);
    }

    private void mostrarDetalleVenta(Venta venta) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/detalleVenta.fxml"));
            Parent root = loader.load();

            // Obtén el controlador y pasa los productos
            DetalleVentaController controller = loader.getController();
            controller.mostrarDetalle(venta.getProductos());

            Stage stage = new Stage();
            stage.setTitle("Detalle de Venta #" + venta.getNumero());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
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

    public void setUsuario(String usuario) {
        nombreUsuario = usuario;
        if (lblUsuario != null) {
            lblUsuario.setText("Usuario: " + usuario);
        }

        // Actualizar la inicial en el círculo
        if (userInitial != null) {
            String inicial = usuario != null && !usuario.isEmpty() ?
                    usuario.substring(0, 1).toUpperCase() : "G";
            userInitial.setText(inicial);
        }
    }


    private void cambiarUsuario() {
        // Implementar lógica para cambiar usuario
        System.out.println("Cambiar usuario");
        userPopup.hide();

        try {
            // Cerrar la ventana actual
            Stage currentStage = (Stage) userMenuContainer.getScene().getWindow();

            // Cargar la pantalla de login
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Parent root = loader.load();

            // Crear nueva escena y establecer en la misma ventana
          //  Scene scene = new Scene(root);
           // currentStage.setScene(scene);
          //  currentStage.setTitle("Login");

            // Opcionalmente, puedes ajustar el tamaño de la ventana para adaptarse mejor al formulario de login
            currentStage.sizeToScene();

            // Alternativamente, si prefieres cerrar completamente la aplicación y abrir una nueva ventana:
             currentStage.close();
             Stage loginStage = new Stage();
             loginStage.setTitle("Login");
             loginStage.setScene(new Scene(root));
             loginStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void cambiarContrasena() {
        // Implementar lógica para cambiar contraseña
        System.out.println("Cambiar contraseña");
        userPopup.hide();

        // Aquí podrías abrir un diálogo para cambiar la contraseña
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/RecuperarPassword.fxml"));
                Parent root = loader.load();

                Stage stage = new Stage();
                stage.setTitle("Recuperar Contraseña");
                stage.setScene(new Scene(root));
                stage.setResizable(false);
                stage.show();

                // Cerrar ventana actual de login
                Stage currentStage = (Stage) userMenuContainer.getScene().getWindow();
                currentStage.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    private void gestionarUsuarios() {
        // Implementar lógica para gestionar usuarios
        System.out.println("Gestionar usuarios");
        userPopup.hide();

        // Aquí podrías abrir una ventana de gestión de usuarios
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
                     String hora, String origen, String articulos, String cliente, String observaciones,
                     ObservableList<ProductoVenta> productos) {
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
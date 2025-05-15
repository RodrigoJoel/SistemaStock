package com.lobo24.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainController {
    private Stage stage;
    private boolean isMaximized = true;

    @FXML private StackPane contentArea;
    @FXML private Label lblUsuario;
    @FXML private VBox menuVertical;

    // Mapa que relaciona cada opción del menú con su archivo FXML
    private final Map<String, String> vistas = new HashMap<>();

    @FXML
    public void initialize() {
        // Configurar las rutas de las vistas
        configurarRutasVistas();
    }

    private void configurarRutasVistas() {
        vistas.put("Ventas", "/views/ventas.fxml");
        vistas.put("Clientes", "/views/clientes.fxml");
        vistas.put("Productos", "/views/ProductosView.fxml");
        vistas.put("Stock", "/views/Stock.fxml");
        vistas.put("Catálogo Online", "/views/catalogo.fxml");
        vistas.put("Cuentas a Pagar", "/views/CuentasAPagar.fxml");
        vistas.put("Caja", "/views/Caja.fxml");
        vistas.put("Estadísticas", "/views/estadisticas.fxml");
        vistas.put("Tutoriales", "/views/tutoriales.fxml");
    }

    public void setStage(Stage stage) {
        this.stage = stage;
        stage.setMaximized(true);
    }

    public void initData(String usuario, String role) {
        lblUsuario.setText("Usuario: " + usuario + " | Rol: " + role);
        aplicarPermisos(role);
    }

    private void aplicarPermisos(String role) {
        if ("empleado".equals(role)) {
            // Ocultar botones según el rol
            menuVertical.getChildren().forEach(node -> {
                if (node instanceof Button) {
                    Button boton = (Button) node;
                    String userData = (String) boton.getUserData();
                    if (!"Ventas".equals(userData) && !"Caja".equals(userData)) {
                        boton.setVisible(false);
                    }
                }
            });
        }
    }

    @FXML
    private void handleMenuAction(ActionEvent event) {
        Button botonPresionado = (Button) event.getSource();
        String opcion = (String) botonPresionado.getUserData();

        System.out.println("Se seleccionó: " + opcion);

        if (vistas.containsKey(opcion)) {
            cargarVista(vistas.get(opcion));
        } else {
            mostrarVistaNoDisponible(opcion);
        }
    }

    private void cargarVista(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent vista = loader.load();
            contentArea.getChildren().setAll(vista);

            // Opcional: Pasar datos al controlador de la vista cargada
            Object controller = loader.getController();
            if (controller instanceof VentasController) {
                ((VentasController) controller).setUsuario(lblUsuario.getText());
            }
            // Agregar más casos para otros controladores

        } catch (IOException e) {
            mostrarError("Error al cargar la vista: " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void mostrarVistaNoDisponible(String opcion) {
        Label mensaje = new Label("El módulo '" + opcion + "' no está disponible aún");
        mensaje.setStyle("-fx-font-size: 16px; -fx-text-fill: #e74c3c;");
        contentArea.getChildren().setAll(mensaje);
    }

    @FXML
    private void minimizeWindow() {
        stage.setIconified(true);
    }

    @FXML
    private void toggleMaximizeWindow() {
        isMaximized = !isMaximized;
        stage.setMaximized(isMaximized);
    }

    @FXML
    private void closeWindow() {
        stage.close();
    }

    private void mostrarError(String mensaje) {
        Label errorLabel = new Label(mensaje);
        errorLabel.setStyle("-fx-text-fill: red; -fx-font-size: 14px;");
        contentArea.getChildren().setAll(errorLabel);
    }


}
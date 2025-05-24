package com.lobo24.controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.layout.StackPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GestionUsuariosController implements Initializable {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, String> colInicial;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colEmail;
    @FXML private TableColumn<Usuario, String> colRol;
    @FXML private TableColumn<Usuario, String> colAcciones;
    @FXML private TextField txtBuscar;

    private ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Configurar columnas
        colNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        colEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        colRol.setCellValueFactory(cellData -> cellData.getValue().rolProperty());

        // Columna personalizada para iniciales
        colInicial.setCellFactory(column -> new TableCell<Usuario, String>() {
            private final StackPane stackPane = new StackPane();
            private final Circle circle = new Circle(15);
            private final Label label = new Label();

            {
                circle.setFill(Color.web("#3498db"));
                stackPane.getChildren().addAll(circle, label);
                stackPane.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    Usuario usuario = getTableRow().getItem();
                    label.setText(usuario.getInicial());
                    setGraphic(stackPane);
                }
            }
        });

        // Cargar datos de ejemplo (deberías reemplazar esto con datos reales de tu base de datos)
        cargarUsuariosEjemplo();

        tablaUsuarios.setItems(listaUsuarios);
    }

    private void cargarUsuariosEjemplo() {
        listaUsuarios.add(new Usuario("Q", "Notable", "admin@lobo24.com", "Admin"));
        listaUsuarios.add(new Usuario("2", "250", "germanvegaaaa@gmail.com", "Usuario"));
        listaUsuarios.add(new Usuario("F1", "FRANCO ITURALDE", "franco@lobo24.com", "Vendedor"));
        listaUsuarios.add(new Usuario("G", "GenMan", "german@lobo24.com", "Gerente"));
        listaUsuarios.add(new Usuario("LA", "Lucas Acevedo", "lucas@lobo24.com", "Vendedor"));
    }

    // Agrega este método al controlador existente
    @FXML
    private void nuevoUsuario() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/NuevoUsuario.fxml"));
            Parent root = loader.load();

            // Configurar el diálogo
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Nuevo Usuario");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tablaUsuarios.getScene().getWindow());
            dialogStage.setScene(new Scene(root));

            // Configurar el controlador
            NuevoUsuarioController controller = loader.getController();
            controller.setDialogStage(dialogStage);

            // Mostrar diálogo y esperar hasta que el usuario lo cierre
            dialogStage.showAndWait();

            // Si el usuario hizo clic en Guardar
            if (controller.isGuardado()) {
                // Crear nuevo usuario con los datos del formulario
                Usuario nuevoUsuario = new Usuario(
                        controller.getInicial(),
                        controller.getNombre(),
                        controller.getEmail(),
                        controller.getRol()
                );

                // Agregar a la lista
                listaUsuarios.add(nuevoUsuario);

            }
        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error", "No se pudo abrir la ventana de nuevo usuario", Alert.AlertType.ERROR);
        }
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    // Clase modelo para usuarios
    public static class Usuario {
        private final StringProperty inicial;
        private final StringProperty nombre;
        private final StringProperty email;
        private final StringProperty rol;

        public Usuario(String inicial, String nombre, String email, String rol) {
            this.inicial = new SimpleStringProperty(inicial);
            this.nombre = new SimpleStringProperty(nombre);
            this.email = new SimpleStringProperty(email);
            this.rol = new SimpleStringProperty(rol);
        }

        // Getters y properties
        public String getInicial() { return inicial.get(); }
        public StringProperty inicialProperty() { return inicial; }

        public String getNombre() { return nombre.get(); }
        public StringProperty nombreProperty() { return nombre; }

        public String getEmail() { return email.get(); }
        public StringProperty emailProperty() { return email; }

        public String getRol() { return rol.get(); }
        public StringProperty rolProperty() { return rol; }
    }
}
package com.lobo24;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        // Asegúrate de usar esta ruta exacta
        Parent root = FXMLLoader.load(getClass().getResource("/views/MainView.fxml"));
        stage.setScene(new Scene(root, 600, 400)); // Tamaño opcional
        stage.setTitle("Sistema de Stock");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
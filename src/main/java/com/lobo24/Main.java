package com.lobo24;

import com.lobo24.util.ExcelReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        // Ruta del archivo Excel
        String path = "C:/Users/rodri/OneDrive/Desktop/stock/Exportar.xls";

// Establecer la conexión a la base de datos
        String url ="jdbc:mysql://localhost:3306/lobo";


        try (Connection connection = DriverManager.getConnection(url, "rodrigo", "lobo24")) {
            // Llamar a la función para leer el Excel y guardar los productos
            ExcelReader.leerYGuardarProductos(path, connection);
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        }


        // Cargar el archivo FXML
        Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        Scene scene = new Scene(root, 450, 400);

        primaryStage.setTitle("Login - Sistema de Ventas Lobo24");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

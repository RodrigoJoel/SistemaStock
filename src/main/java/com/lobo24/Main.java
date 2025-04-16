package com.lobo24;/*package com.lobo24;*/
/*
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
/*
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Cargar el archivo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Login.fxml"));
        Scene scene = new Scene(loader.load());

        // Configurar la ventana
        primaryStage.setTitle("Sistema de ventas Lobo24");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false); // Evita que la ventana cambie de tamaño

        // Centrar la ventana en la pantalla
        primaryStage.centerOnScreen();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

*/

/*
package com.lobo24;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
        Scene scene = new Scene(root, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Sistema de Ventas Lobo24");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
*/


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws IOException {
        // Usa esta ruta que coincide con tu estructura actual
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
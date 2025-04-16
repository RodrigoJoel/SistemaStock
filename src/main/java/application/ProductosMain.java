package application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class ProductosMain extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Carga el archivo FXML
            Parent root = FXMLLoader.load(getClass().getResource("/view/ProductosView.fxml"));
            Scene scene = new Scene(root);
            primaryStage.setTitle("Gestión de Productos");
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

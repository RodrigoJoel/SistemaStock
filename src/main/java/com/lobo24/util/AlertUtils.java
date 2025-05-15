package com.lobo24.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

/**
 * Clase utilitaria para mostrar alertas y diálogos de confirmación
 */
public class AlertUtils {

    /**
     * Muestra un mensaje de error con título, cabecera y mensaje
     * @param titulo Título de la ventana
     * @param cabecera Cabecera del mensaje
     * @param mensaje Mensaje detallado
     */
    public static void mostrarError(String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje de error con detalles de excepción
     * @param titulo Título de la ventana
     * @param cabecera Cabecera del mensaje
     * @param excepcion Excepción a mostrar
     */
    public static void mostrarError(String titulo, String cabecera, Exception excepcion) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(excepcion.getMessage());

        // Crear área de texto expandible para mostrar el stack trace
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        excepcion.printStackTrace(pw);
        String exceptionText = sw.toString();

        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        alert.getDialogPane().setExpandableContent(expContent);
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje de información
     * @param titulo Título de la ventana
     * @param cabecera Cabecera del mensaje
     * @param mensaje Mensaje detallado
     */
    public static void mostrarInformacion(String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un mensaje de advertencia
     * @param titulo Título de la ventana
     * @param cabecera Cabecera del mensaje
     * @param mensaje Mensaje detallado
     */
    public static void mostrarAdvertencia(String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    /**
     * Muestra un diálogo de confirmación y devuelve el resultado
     * @param titulo Título de la ventana
     * @param cabecera Cabecera del mensaje
     * @param mensaje Mensaje detallado
     * @return Optional con el botón presionado
     */
    public static Optional<ButtonType> mostrarConfirmacion(String titulo, String cabecera, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(titulo);
        alert.setHeaderText(cabecera);
        alert.setContentText(mensaje);
        return alert.showAndWait();
    }

    /**
     * Método de conveniencia para verificar si se confirmó una acción
     * @param resultado El resultado devuelto por mostrarConfirmacion()
     * @return true si se presionó OK, false en caso contrario
     */
    public static boolean seConfirmo(Optional<ButtonType> resultado) {
        return resultado.isPresent() && resultado.get() == ButtonType.OK;
    }
}
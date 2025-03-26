package com.lobo24.models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Producto {
    private final SimpleIntegerProperty id;
    private final SimpleStringProperty nombre;
    private final SimpleIntegerProperty stock;

    public Producto(int id, String nombre, int stock) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.stock = new SimpleIntegerProperty(stock);
    }

    // Getters (usando propiedades para JavaFX)
    public int getId() { return id.get(); }
    public String getNombre() { return nombre.get(); }
    public int getStock() { return stock.get(); }
}
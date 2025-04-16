package com.lobo24.util;

import javafx.beans.property.*;

import java.math.BigDecimal;

public class ProductoVenta {
    private final StringProperty nombre;
    private final IntegerProperty cantidad;
    private final DoubleProperty precio;
    private final DoubleProperty subtotal;
    private final SimpleStringProperty codigo;

    public ProductoVenta(String codigo, String nombre, int cantidad, BigDecimal precio) {
        this.codigo = new SimpleStringProperty(codigo);
        this.nombre = new SimpleStringProperty(nombre);
        this.cantidad = new SimpleIntegerProperty(cantidad);
        this.precio = new SimpleDoubleProperty(precio.doubleValue());
        this.subtotal = new SimpleDoubleProperty(precio.doubleValue() * cantidad);
    }

    // Métodos property (necesarios para TableView)
    public StringProperty nombreProperty() { return nombre; }
    public IntegerProperty cantidadProperty() { return cantidad; }
    public DoubleProperty precioProperty() { return precio; }
    public DoubleProperty subtotalProperty() { return subtotal; }

    // Getters estándar
    public String getNombre() { return nombre.get(); }
    public int getCantidad() { return cantidad.get(); }
    public double getPrecio() { return precio.get(); }
    public double getSubtotal() { return subtotal.get(); } // ← ¡Método que faltaba!
    public String getCodigo() {
        return codigo.get();
    }

    // Setters (opcionales)
    public void setNombre(String nombre) { this.nombre.set(nombre); }
    public void setCantidad(int cantidad) {
        this.cantidad.set(cantidad);
        this.subtotal.set(this.precio.get() * cantidad); // Actualiza subtotal
    }
    public void setPrecio(double precio) {
        this.precio.set(precio);
        this.subtotal.set(precio * this.cantidad.get()); // Actualiza subtotal
    }
    public BigDecimal getPrecioBigDecimal() {
        return BigDecimal.valueOf(precio.get()); // Convierte DoubleProperty a BigDecimal
    }
}
/*package com.lobo24.models;*/
/*
import com.lobo24.util.ProductoVenta;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.math.BigDecimal;

public class Venta {
    private String tipo;
    private BigDecimal total;
    private ObservableList<ProductoVenta> productos;
    private String cliente;
    private String fecha;
    private String hora;

    public Venta(String tipo, BigDecimal total, ObservableList<ProductoVenta> productos, String cliente, String fechaHora) {
        this.tipo = tipo;
        this.total = total;
        this.productos = productos;
        this.cliente = cliente;

        // Suponiendo que fechaHora está en formato "yyyy-MM-dd HH:mm"
        String[] partes = fechaHora.split(" ");
        this.fecha = partes[0];
        this.hora = partes.length > 1 ? partes[1] : "";
    }

    public String getOperacion() {
        return tipo;
    }

    public String getNumero() {
        return String.valueOf(System.currentTimeMillis() % 10000); // número aleatorio para ejemplo
    }

    public String getResumen() {
        return productos.stream().map(ProductoVenta::getNombre).reduce((a, b) -> a + ", " + b).orElse("-");
    }

    public String getTipo() {
        return tipo;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getOrigen() {
        return "Caja 1"; // ejemplo
    }

    public String getArticulos() {
        return String.valueOf(productos.size());
    }

    public String getCliente() {
        return cliente;
    }

    public String getObservaciones() {
        return "Sin observaciones";
    }

    public BigDecimal getTotal() {
        return total;
    }
}
*/
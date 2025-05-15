package com.lobo24.models;

import javafx.scene.image.ImageView;

public class ProductoStock {
    private int id;
    private String codigo;
    private String nombre;
    private String descripcion;
    private double precio;
    private int stockActual;
    private int pedidos;
    private int disponible;
    private String proveedor;
    private String categoria;
    private String marca;
    private String rutaImagen;
    private ImageView imagenView;

    public ProductoStock() {
    }

    public ProductoStock(int id, String codigo, String nombre, String descripcion, double precio,
                         int stockActual, int pedidos, String proveedor, String categoria, String marca, String rutaImagen) {
        this.id = id;
        this.codigo = codigo;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stockActual = stockActual;
        this.pedidos = pedidos;
        this.disponible = stockActual - pedidos; // Calculado automáticamente
        this.proveedor = proveedor;
        this.categoria = categoria;
        this.marca = marca;
        this.rutaImagen = rutaImagen;

        // La imagen se cargará posteriormente con setImagenView
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getStockActual() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
        this.disponible = stockActual - pedidos; // Actualizar disponible
    }

    public int getPedidos() {
        return pedidos;
    }

    public void setPedidos(int pedidos) {
        this.pedidos = pedidos;
        this.disponible = stockActual - pedidos; // Actualizar disponible
    }

    public int getDisponible() {
        return disponible;
    }

    public String getProveedor() {
        return proveedor;
    }

    public void setProveedor(String proveedor) {
        this.proveedor = proveedor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public ImageView getImagenView() {
        return imagenView;
    }

    public void setImagenView(ImageView imagenView) {
        this.imagenView = imagenView;
    }

    public String getNombreCompleto() {
        return nombre + "\n" + codigo + " • $" + String.format("%.2f", precio);
    }

    @Override
    public String toString() {
        return nombre;
    }
}
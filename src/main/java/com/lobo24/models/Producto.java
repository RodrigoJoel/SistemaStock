package com.lobo24.models;

import java.math.BigDecimal;

public class Producto {
    private int id;
    private String codigoBarras;
    private String codigoExtra;
    private String descripcion;
    private String categoria;
    private String marca;
    private BigDecimal precioCoste;
    private BigDecimal precioVenta;
    private int stock;
    private int stockMinimo;
    private BigDecimal incremento;
    private boolean automatico;
    private String ean;
    private String codigo;

    // Constructor vacío necesario para cargar desde ResultSet
    public Producto() {
        this.precioCoste = BigDecimal.ZERO;
        this.precioVenta = BigDecimal.ZERO;
        this.incremento = BigDecimal.ZERO;
    }

    // Constructor con campos principales
    public Producto(String codigoBarras, String descripcion, String categoria,
                    double precioCoste, double precioVenta, double incremento) {
        this();
        this.codigoBarras = codigoBarras;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.setPrecioCoste(precioCoste);
        this.setPrecioVenta(precioVenta);
        this.setIncremento(incremento);
    }

    // Constructor alternativo con BigDecimal
    public Producto(String codigoBarras, String descripcion, String categoria,
                    BigDecimal precioCoste, BigDecimal precioVenta, BigDecimal incremento) {
        this();
        this.codigoBarras = codigoBarras;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.setPrecioCoste(precioCoste);
        this.setPrecioVenta(precioVenta);
        this.setIncremento(incremento);
    }

    public static int getStockDisponible() {
        return 0;
    }

    public void calcularPrecioVentaAutomatico() {
        if (automatico && precioCoste != null && incremento != null) {
            this.precioVenta = precioCoste.add(precioCoste.multiply(incremento.divide(BigDecimal.valueOf(100))));
        }
    }


    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }

    public String getCodigoExtra() { return codigoExtra; }
    public void setCodigoExtra(String codigoExtra) { this.codigoExtra = codigoExtra; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public BigDecimal getPrecioCoste() { return precioCoste; }
    public void setPrecioCoste(BigDecimal precioCoste) {
        this.precioCoste = precioCoste != null ? precioCoste : BigDecimal.ZERO;
    }
    public void setPrecioCoste(double precioCoste) {
        this.precioCoste = BigDecimal.valueOf(precioCoste);
    }

    public BigDecimal getPrecioVenta() { return precioVenta; }
    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta != null ? precioVenta : BigDecimal.ZERO;
    }
    public void setPrecioVenta(double precioVenta) {
        this.precioVenta = BigDecimal.valueOf(precioVenta);
    }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public int getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(int stockMinimo) { this.stockMinimo = stockMinimo; }

    public BigDecimal getIncremento() { return incremento; }
    public void setIncremento(BigDecimal incremento) {
        this.incremento = incremento != null ? incremento : BigDecimal.ZERO;
    }
    public void setIncremento(double incremento) {
        this.incremento = BigDecimal.valueOf(incremento);
    }

    public boolean isAutomatico() { return automatico; }
    public void setAutomatico(boolean automatico) { this.automatico = automatico; }

    public String getEan() { return ean; }
    public void setEan(String ean) { this.ean = ean; }

    // Métodos adicionales
    public String getNombre() {
        return this.descripcion;
    }

    public String getCodigoBarrasAlternativo() {
        return this.codigoExtra;
    }

    public void setNombre(String nombre) {
        this.descripcion = nombre;
    }

    // Métodos de conveniencia para obtener valores como double
    public double getPrecioCosteAsDouble() {
        return precioCoste.doubleValue();
    }

    public double getPrecioVentaAsDouble() {
        return precioVenta.doubleValue();
    }

    public double getIncrementoAsDouble() {
        return incremento.doubleValue();
    }
}
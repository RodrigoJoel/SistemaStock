package com.lobo24.models;

import javafx.beans.property.*;

public class Producto {
    // Propiedades principales (ajustadas a BD)
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty codigoBarras = new SimpleStringProperty();
    private final StringProperty codigoExtra = new SimpleStringProperty();
    private final StringProperty descripcion = new SimpleStringProperty();
    private final StringProperty categoria = new SimpleStringProperty();
    private final StringProperty marca = new SimpleStringProperty();
    private final DoubleProperty precioVenta = new SimpleDoubleProperty();
    private final DoubleProperty precioCosto = new SimpleDoubleProperty();
    private final DoubleProperty porcentajeIncremento = new SimpleDoubleProperty();
    private final IntegerProperty stockActual = new SimpleIntegerProperty();
    private final IntegerProperty stockMinimo = new SimpleIntegerProperty();

    // Propiedades adicionales (opcionales)
    private final BooleanProperty automatico = new SimpleBooleanProperty(false);
    private final BooleanProperty controlStock = new SimpleBooleanProperty(true);
    private final StringProperty unidadMedida = new SimpleStringProperty();
    private final BooleanProperty permiteFraccionamiento = new SimpleBooleanProperty(false);
    private final StringProperty imagenPath = new SimpleStringProperty();
    private final StringProperty observaciones = new SimpleStringProperty();

    // Constructores
    public Producto() {
        // Constructor vacío para FXMLLoader
    }

    public Producto(int id, String codigoBarras, String codigoExtra, String descripcion,
                    String categoria, String marca, double precioVenta, double precioCosto,
                    double porcentajeIncremento, int stockActual, int stockMinimo) {
        this.id.set(id);
        this.codigoBarras.set(codigoBarras);
        this.codigoExtra.set(codigoExtra);
        this.descripcion.set(descripcion);
        this.categoria.set(categoria);
        this.marca.set(marca);
        this.precioVenta.set(precioVenta);
        this.precioCosto.set(precioCosto);
        this.porcentajeIncremento.set(porcentajeIncremento);
        this.stockActual.set(stockActual);
        this.stockMinimo.set(stockMinimo);
    }

    // Getters y Setters para todas las propiedades
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getCodigoBarras() { return codigoBarras.get(); }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras.set(codigoBarras); }
    public StringProperty codigoBarrasProperty() { return codigoBarras; }

    // ... (implementa getters/setters para todas las propiedades restantes)

    public String getCodigoExtra() {
        return codigoExtra.get();
    }

    public StringProperty codigoExtraProperty() {
        return codigoExtra;
    }

    public void setCodigoExtra(String codigoExtra) {
        this.codigoExtra.set(codigoExtra);
    }

    public String getDescripcion() {
        return descripcion.get();
    }

    public StringProperty descripcionProperty() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion.set(descripcion);
    }

    public String getCategoria() {
        return categoria.get();
    }

    public StringProperty categoriaProperty() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria.set(categoria);
    }

    public String getMarca() {
        return marca.get();
    }

    public StringProperty marcaProperty() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca.set(marca);
    }

    public double getPrecioVenta() {
        return precioVenta.get();
    }

    public DoubleProperty precioVentaProperty() {
        return precioVenta;
    }

    public void setPrecioVenta(double precioVenta) {
        this.precioVenta.set(precioVenta);
    }

    public double getPrecioCosto() {
        return precioCosto.get();
    }

    public DoubleProperty precioCostoProperty() {
        return precioCosto;
    }

    public void setPrecioCosto(double precioCosto) {
        this.precioCosto.set(precioCosto);
    }

    public double getPorcentajeIncremento() {
        return porcentajeIncremento.get();
    }

    public DoubleProperty porcentajeIncrementoProperty() {
        return porcentajeIncremento;
    }

    public void setPorcentajeIncremento(double porcentajeIncremento) {
        this.porcentajeIncremento.set(porcentajeIncremento);
    }

    public int getStockActual() {
        return stockActual.get();
    }

    public IntegerProperty stockActualProperty() {
        return stockActual;
    }

    public void setStockActual(int stockActual) {
        this.stockActual.set(stockActual);
    }

    public int getStockMinimo() {
        return stockMinimo.get();
    }

    public IntegerProperty stockMinimoProperty() {
        return stockMinimo;
    }

    public void setStockMinimo(int stockMinimo) {
        this.stockMinimo.set(stockMinimo);
    }

    public boolean isAutomatico() {
        return automatico.get();
    }

    public BooleanProperty automaticoProperty() {
        return automatico;
    }

    public void setAutomatico(boolean automatico) {
        this.automatico.set(automatico);
    }

    public boolean isControlStock() {
        return controlStock.get();
    }

    public BooleanProperty controlStockProperty() {
        return controlStock;
    }

    public void setControlStock(boolean controlStock) {
        this.controlStock.set(controlStock);
    }

    public String getUnidadMedida() {
        return unidadMedida.get();
    }

    public StringProperty unidadMedidaProperty() {
        return unidadMedida;
    }

    public void setUnidadMedida(String unidadMedida) {
        this.unidadMedida.set(unidadMedida);
    }

    public boolean isPermiteFraccionamiento() {
        return permiteFraccionamiento.get();
    }

    public BooleanProperty permiteFraccionamientoProperty() {
        return permiteFraccionamiento;
    }

    public void setPermiteFraccionamiento(boolean permiteFraccionamiento) {
        this.permiteFraccionamiento.set(permiteFraccionamiento);
    }

    public String getImagenPath() {
        return imagenPath.get();
    }

    public StringProperty imagenPathProperty() {
        return imagenPath;
    }

    public void setImagenPath(String imagenPath) {
        this.imagenPath.set(imagenPath);
    }

    public String getObservaciones() {
        return observaciones.get();
    }

    public StringProperty observacionesProperty() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones.set(observaciones);
    }

    // Método toString para debugging
    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id.get() +
                ", descripcion=" + descripcion.get() +
                ", codigoBarras=" + codigoBarras.get() +
                ", precioVenta=" + precioVenta.get() +
                '}';
    }
}
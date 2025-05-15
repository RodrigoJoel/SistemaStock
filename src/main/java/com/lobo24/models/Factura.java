package com.lobo24.models;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javafx.beans.property.*;

public class Factura {

    private final StringProperty status;
    private final ObjectProperty<LocalDate> fechaVencimiento;
    private final DoubleProperty valor;
    private final StringProperty referente;
    private final StringProperty proveedor;
    private final StringProperty tipo;
    private final IntegerProperty diasVencimiento;

    public Factura(String status, LocalDate fechaVencimiento, double valor,
                   String referente, String proveedor, String tipo, int diasVencimiento) {
        this.status = new SimpleStringProperty(status);
        this.fechaVencimiento = new SimpleObjectProperty<>(fechaVencimiento);
        this.valor = new SimpleDoubleProperty(valor);
        this.referente = new SimpleStringProperty(referente);
        this.proveedor = new SimpleStringProperty(proveedor);
        this.tipo = new SimpleStringProperty(tipo);
        this.diasVencimiento = new SimpleIntegerProperty(diasVencimiento);
    }

    // Getters
    public String getStatus() {
        return status.get();
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento.get();
    }

    public double getValor() {
        return valor.get();
    }

    public String getReferente() {
        return referente.get();
    }

    public String getProveedor() {
        return proveedor.get();
    }

    public String getTipo() {
        return tipo.get();
    }

    public int getDiasVencimiento() {
        return diasVencimiento.get();
    }

    // Property getters
    public StringProperty statusProperty() {
        return status;
    }

    public ObjectProperty<LocalDate> fechaVencimientoProperty() {
        return fechaVencimiento;
    }

    public DoubleProperty valorProperty() {
        return valor;
    }

    public StringProperty referenteProperty() {
        return referente;
    }

    public StringProperty proveedorProperty() {
        return proveedor;
    }

    public StringProperty tipoProperty() {
        return tipo;
    }

    public IntegerProperty diasVencimientoProperty() {
        return diasVencimiento;
    }

    // Setters
    public void setStatus(String status) {
        this.status.set(status);
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento.set(fechaVencimiento);
    }

    public void setValor(double valor) {
        this.valor.set(valor);
    }

    public void setReferente(String referente) {
        this.referente.set(referente);
    }

    public void setProveedor(String proveedor) {
        this.proveedor.set(proveedor);
    }

    public void setTipo(String tipo) {
        this.tipo.set(tipo);
    }

    public void setDiasVencimiento(int diasVencimiento) {
        this.diasVencimiento.set(diasVencimiento);
    }

    /**
     * Calcula los días restantes hasta el vencimiento o los días transcurridos desde el vencimiento
     * @return Número de días (positivo si queda tiempo, negativo si está vencido)
     */
    public int calcularDiasHastaVencimiento() {
        return (int) ChronoUnit.DAYS.between(LocalDate.now(), getFechaVencimiento());
    }

    /**
     * Determina si la factura está vencida
     * @return true si está vencida, false en caso contrario
     */
    public boolean estaVencida() {
        return calcularDiasHastaVencimiento() < 0;
    }
}
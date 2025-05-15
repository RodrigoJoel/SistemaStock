package com.lobo24.models;

public class VentaHistorico {
    private String operacion;
    private String numero;
    private String resumen;
    private String tipo;
    private String fecha;
    private String hora;
    private String origen;
    private int articulos;
    private String cliente;
    private String observaciones;

    // Constructor, getters y setters
    public VentaHistorico(String operacion, String numero, String resumen, String tipo, String fecha,
                          String hora, String origen, int articulos, String cliente, String observaciones) {
        this.operacion = operacion;
        this.numero = numero;
        this.resumen = resumen;
        this.tipo = tipo;
        this.fecha = fecha;
        this.hora = hora;
        this.origen = origen;
        this.articulos = articulos;
        this.cliente = cliente;
        this.observaciones = observaciones;
    }

    // Getters (JavaFX los necesita para bindear columnas)
    public String getOperacion() { return operacion; }
    public String getNumero() { return numero; }
    public String getResumen() { return resumen; }
    public String getTipo() { return tipo; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public String getOrigen() { return origen; }
    public int getArticulos() { return articulos; }
    public String getCliente() { return cliente; }
    public String getObservaciones() { return observaciones; }
}

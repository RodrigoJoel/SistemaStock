package com.lobo24.models;

public class Cliente {
    private String nombre;
    private String apellido;
    private String codigo;
    private String dni;
    private String telefono;
    private String email;
    private double saldo;
    private String tipoCliente;
    private String ultimaCompra;
    private String direccion;
    private String localidad;
    private String provincia;
    private String fechaNacimiento;
    private boolean recibeNotificaciones;
    private String observaciones;

    // Constructor
    public Cliente(String nombre, String codigo, String telefono, String email,
                   double saldo, String tipoCliente, String ultimaCompra, String direccion) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.telefono = telefono;
        this.email = email;
        this.saldo = saldo;
        this.tipoCliente = tipoCliente;
        this.ultimaCompra = ultimaCompra;
        this.direccion = direccion;
    }

    // Getters y Setters
    public String getNombreCompleto() {
        return nombre + (apellido != null ? " " + apellido : "");
    }

    public String getDireccionCompleta() {
        return direccion + ", " + localidad + ", " + provincia;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    public String getTipoCliente() {
        return tipoCliente;
    }

    public void setTipoCliente(String tipoCliente) {
        this.tipoCliente = tipoCliente;
    }

    public String getUltimaCompra() {
        return ultimaCompra;
    }

    public void setUltimaCompra(String ultimaCompra) {
        this.ultimaCompra = ultimaCompra;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public boolean isRecibeNotificaciones() {
        return recibeNotificaciones;
    }

    public void setRecibeNotificaciones(boolean recibeNotificaciones) {
        this.recibeNotificaciones = recibeNotificaciones;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public int getId() {
        return 0;
    }

    public void setId(int id) {
    }

}
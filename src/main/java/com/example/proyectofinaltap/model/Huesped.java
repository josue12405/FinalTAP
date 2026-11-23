package com.example.proyectofinaltap.model;

public class Huesped {
    private int idHuesped;
    private String nombre;
    private String telefono;
    private String correo;

    public Huesped() {}

    public Huesped(int idHuesped, String nombre, String telefono, String correo) {
        this.idHuesped = idHuesped;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
    }

    public Huesped(String nombre, String telefono, String correo) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
    }

    public int getIdHuesped() { return idHuesped; }
    public void setIdHuesped(int idHuesped) { this.idHuesped = idHuesped; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    @Override
    public String toString() {
        return idHuesped + " - " + nombre;
    }
}
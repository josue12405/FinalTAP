package com.example.proyectofinaltap.model;

public class Habitacion {
    private int idHabitacion;
    private String numero;
    private String tipo;
    private double precioNoche;
    private String estado;

    public Habitacion() {}

    public Habitacion(int idHabitacion, String numero, String tipo, double precioNoche, String estado) {
        this.idHabitacion = idHabitacion;
        this.numero = numero;
        this.tipo = tipo;
        this.precioNoche = precioNoche;
        this.estado = estado;
    }

    public Habitacion(String numero, String tipo, double precioNoche, String estado) {
        this.numero = numero;
        this.tipo = tipo;
        this.precioNoche = precioNoche;
        this.estado = estado;
    }

    public int getIdHabitacion() { return idHabitacion; }
    public void setIdHabitacion(int idHabitacion) { this.idHabitacion = idHabitacion; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getPrecioNoche() { return precioNoche; }
    public void setPrecioNoche(double precioNoche) { this.precioNoche = precioNoche; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Hab. " + numero + " - " + tipo + " ($" + String.format("%.2f", precioNoche) + "/noche)";
    }
}
package com.example.proyectofinaltap.model;

public class Usuario {
    private String username;
    private String nombreCompleto;
    private String rol;

    public Usuario(String username, String nombreCompleto, String rol) {
        this.username = username;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    public String getUsername() { return username; }
    public String getNombreCompleto() { return nombreCompleto; }
    public String getRol() { return rol; }
}
package com.example.proyectofinaltap.model;

public class Usuario {
    private int idUsuario;
    private String username;
    private String nombreCompleto;
    private String rol;

    public Usuario() {}

    public Usuario(String username, String nombreCompleto, String rol) {
        this.username = username;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    public Usuario(int idUsuario, String username, String nombreCompleto, String rol) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.nombreCompleto = nombreCompleto;
        this.rol = rol;
    }

    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    @Override
    public String toString() {
        return nombreCompleto + " (" + rol + ")";
    }
}
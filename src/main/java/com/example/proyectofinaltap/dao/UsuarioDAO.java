package com.example.proyectofinaltap.dao;

import com.example.proyectofinaltap.model.Usuario;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UsuarioDAO {

    public Usuario autenticar(String username, String passwordPlano) {
        String sql = "SELECT username, nombre_completo, rol FROM usuarios WHERE username = ? AND password_sha1 = ?";
        Connection con = ConexionMySQL.getInstancia();

        if (con == null) return null;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, encriptarSHA1(passwordPlano));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                            rs.getString("username"),
                            rs.getString("nombre_completo"),
                            rs.getString("rol")
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("Error al autenticar usuario: " + e.getMessage());
        }
        return null;
    }

    private String encriptarSHA1(String texto) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-1");
        byte[] hash = md.digest(texto.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }
}
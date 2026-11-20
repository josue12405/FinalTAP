package com.example.proyectofinaltap.dao;

import com.example.proyectofinaltap.model.Huesped;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HuespedDAO {

    public List<Huesped> listarTodos() {
        List<Huesped> lista = new ArrayList<>();
        String sql = "SELECT id_huesped, nombre, telefono, correo FROM huespedes ORDER BY id_huesped DESC";
        Connection con = ConexionMySQL.getInstancia();

        if (con == null) return lista;

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Huesped(
                        rs.getInt("id_huesped"),
                        rs.getString("nombre"),
                        rs.getString("telefono"),
                        rs.getString("correo")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar huéspedes: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Huesped h) {
        String sql = "INSERT INTO huespedes (nombre, telefono, correo) VALUES (?, ?, ?)";
        Connection con = ConexionMySQL.getInstancia();

        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, h.getNombre());
            ps.setString(2, h.getTelefono());
            ps.setString(3, h.getCorreo());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar huésped: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Huesped h) {
        String sql = "UPDATE huespedes SET nombre = ?, telefono = ?, correo = ? WHERE id_huesped = ?";
        Connection con = ConexionMySQL.getInstancia();

        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, h.getNombre());
            ps.setString(2, h.getTelefono());
            ps.setString(3, h.getCorreo());
            ps.setInt(4, h.getIdHuesped());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar huésped: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idHuesped) {
        String sql = "DELETE FROM huespedes WHERE id_huesped = ?";
        Connection con = ConexionMySQL.getInstancia();

        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHuesped);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar huésped: " + e.getMessage());
            return false;
        }
    }
}
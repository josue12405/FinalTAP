package com.example.proyectofinaltap.dao;

import com.example.proyectofinaltap.model.Habitacion;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HabitacionDAO {

    public List<Habitacion> listarTodas() {
        List<Habitacion> lista = new ArrayList<>();
        String sql = "SELECT id_habitacion, numero, tipo, precio_noche, estado FROM habitaciones ORDER BY numero ASC";
        Connection con = ConexionMySQL.getInstancia();

        if (con == null) return lista;

        try (Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Habitacion(
                        rs.getInt("id_habitacion"),
                        rs.getString("numero"),
                        rs.getString("tipo"),
                        rs.getDouble("precio_noche"),
                        rs.getString("estado")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar habitaciones: " + e.getMessage());
        }
        return lista;
    }

    public boolean insertar(Habitacion h) {
        String sql = "INSERT INTO habitaciones (numero, tipo, precio_noche, estado) VALUES (?, ?, ?, ?)";
        Connection con = ConexionMySQL.getInstancia();

        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, h.getNumero());
            ps.setString(2, h.getTipo());
            ps.setDouble(3, h.getPrecioNoche());
            ps.setString(4, h.getEstado());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al insertar habitación: " + e.getMessage());
            return false;
        }
    }

    public boolean actualizar(Habitacion h) {
        String sql = "UPDATE habitaciones SET numero = ?, tipo = ?, precio_noche = ?, estado = ? WHERE id_habitacion = ?";
        Connection con = ConexionMySQL.getInstancia();

        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, h.getNumero());
            ps.setString(2, h.getTipo());
            ps.setDouble(3, h.getPrecioNoche());
            ps.setString(4, h.getEstado());
            ps.setInt(5, h.getIdHabitacion());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar habitación: " + e.getMessage());
            return false;
        }
    }

    public boolean eliminar(int idHabitacion) {
        String sql = "DELETE FROM habitaciones WHERE id_habitacion = ?";
        Connection con = ConexionMySQL.getInstancia();

        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idHabitacion);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error al eliminar habitación: " + e.getMessage());
            return false;
        }
    }
}
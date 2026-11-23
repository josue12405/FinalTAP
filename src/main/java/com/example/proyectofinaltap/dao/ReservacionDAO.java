package com.example.proyectofinaltap.dao;

import com.example.proyectofinaltap.model.Reservacion;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservacionDAO {

    public List<Reservacion> listar() {
        List<Reservacion> lista = new ArrayList<>();
        String sql = "SELECT r.*, h.nombre AS nombre_huesped, hab.numero AS numero_habitacion, u.nombre_completo AS nombre_usuario " +
                "FROM reservaciones r " +
                "INNER JOIN huespedes h ON r.id_huesped = h.id_huesped " +
                "INNER JOIN habitaciones hab ON r.id_habitacion = hab.id_habitacion " +
                "INNER JOIN usuarios u ON r.id_usuario = u.id_usuario " +
                "ORDER BY r.id_reservacion DESC";

        Connection conn = ConexionMySQL.getInstancia();
        if (conn == null) return lista;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Reservacion r = new Reservacion();
                r.setIdReservacion(rs.getInt("id_reservacion"));
                r.setIdHuesped(rs.getInt("id_huesped"));
                r.setIdHabitacion(rs.getInt("id_habitacion"));
                r.setIdUsuario(rs.getInt("id_usuario"));
                r.setFechaEntrada(rs.getDate("fecha_entrada").toLocalDate());
                r.setFechaSalida(rs.getDate("fecha_salida").toLocalDate());
                r.setEstado(rs.getString("estado"));
                r.setTotal(rs.getDouble("total"));

                r.setNombreHuesped(rs.getString("nombre_huesped"));
                r.setNumeroHabitacion(rs.getString("numero_habitacion"));
                r.setNombreUsuario(rs.getString("nombre_usuario"));
                lista.add(r);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public boolean estaDisponible(int idHabitacion, LocalDate entrada, LocalDate salida) {
        String sql = "SELECT COUNT(*) FROM reservaciones " +
                "WHERE id_habitacion = ? " +
                "AND estado IN ('Reservada', 'Check-In') " +
                "AND (fecha_entrada < ? AND fecha_salida > ?)";

        Connection conn = ConexionMySQL.getInstancia();
        if (conn == null) return false;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idHabitacion);
            ps.setDate(2, Date.valueOf(salida));
            ps.setDate(3, Date.valueOf(entrada));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean registrar(Reservacion r) {
        String sql = "INSERT INTO reservaciones (id_huesped, id_habitacion, id_usuario, fecha_entrada, fecha_salida, estado, total) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        Connection conn = ConexionMySQL.getInstancia();
        if (conn == null) return false;

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getIdHuesped());
            ps.setInt(2, r.getIdHabitacion());
            ps.setInt(3, r.getIdUsuario());
            ps.setDate(4, Date.valueOf(r.getFechaEntrada()));
            ps.setDate(5, Date.valueOf(r.getFechaSalida()));
            ps.setString(6, r.getEstado() != null ? r.getEstado() : "Reservada");
            ps.setDouble(7, r.getTotal());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean cambiarEstado(int idReservacion, int idHabitacion, String nuevoEstadoReserva) {
        String sqlReserva = "UPDATE reservaciones SET estado = ? WHERE id_reservacion = ?";
        String sqlHabitacion = "UPDATE habitaciones SET estado = ? WHERE id_habitacion = ?";

        String estadoHabitacion = switch (nuevoEstadoReserva) {
            case "Check-In" -> "Ocupada";
            case "Finalizada" -> "En Limpieza";
            case "Cancelada" -> "Disponible";
            default -> null;
        };

        Connection conn = ConexionMySQL.getInstancia();
        if (conn == null) return false;

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement psRes = conn.prepareStatement(sqlReserva)) {
                psRes.setString(1, nuevoEstadoReserva);
                psRes.setInt(2, idReservacion);
                psRes.executeUpdate();
            }

            if (estadoHabitacion != null) {
                try (PreparedStatement psHab = conn.prepareStatement(sqlHabitacion)) {
                    psHab.setString(1, estadoHabitacion);
                    psHab.setInt(2, idHabitacion);
                    psHab.executeUpdate();
                }
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            try { conn.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
            return false;
        } finally {
            try { conn.setAutoCommit(true); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public double calcularIngresosTotales() {
        String sql = "SELECT COALESCE(SUM(total), 0) FROM reservaciones WHERE estado IN ('Check-In', 'Finalizada')";
        Connection conn = ConexionMySQL.getInstancia();
        if (conn == null) return 0.0;

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }
}
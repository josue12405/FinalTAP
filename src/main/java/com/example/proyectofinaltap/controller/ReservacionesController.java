package com.example.proyectofinaltap.controller;

import com.example.proyectofinaltap.dao.HabitacionDAO;
import com.example.proyectofinaltap.dao.HuespedDAO;
import com.example.proyectofinaltap.dao.ReservacionDAO;
import com.example.proyectofinaltap.model.Habitacion;
import com.example.proyectofinaltap.model.Huesped;
import com.example.proyectofinaltap.model.Reservacion;
import com.example.proyectofinaltap.model.Usuario;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ReservacionesController {

    @FXML private Label lblUsuarioActivo;
    @FXML private ComboBox<Huesped> cmbHuesped;
    @FXML private ComboBox<Habitacion> cmbHabitacion;
    @FXML private DatePicker dpFechaEntrada;
    @FXML private DatePicker dpFechaSalida;
    @FXML private Label lblTotalEstimado;

    @FXML private TableView<Reservacion> tblReservaciones;
    @FXML private TableColumn<Reservacion, Integer> colId;
    @FXML private TableColumn<Reservacion, String> colHuesped;
    @FXML private TableColumn<Reservacion, String> colHabitacion;
    @FXML private TableColumn<Reservacion, LocalDate> colEntrada;
    @FXML private TableColumn<Reservacion, LocalDate> colSalida;
    @FXML private TableColumn<Reservacion, String> colEstado;
    @FXML private TableColumn<Reservacion, Double> colTotal;
    @FXML private TableColumn<Reservacion, String> colUsuario;

    private final ReservacionDAO reservacionDAO = new ReservacionDAO();
    private final HuespedDAO huespedDAO = new HuespedDAO();
    private final HabitacionDAO habitacionDAO = new HabitacionDAO();

    private final ObservableList<Reservacion> listaReservaciones = FXCollections.observableArrayList();
    private Usuario usuarioActual;
    private double totalCalculado = 0.0;

    @FXML
    public void initialize() {
        configurarCombos();
        configurarTabla();
        configurarListenersCosto();
        cargarCatalogos();
        cargarReservaciones();
    }

    // Método para recibir el usuario logueado desde DashboardController
    public void setUsuarioActual(Usuario usuario) {
        this.usuarioActual = usuario;
        if (usuario != null) {
            lblUsuarioActivo.setText("Atiende: " + usuario.getNombreCompleto() + " (" + usuario.getRol() + ")");
        }
    }

    private void configurarCombos() {
        cmbHuesped.setConverter(new StringConverter<>() {
            @Override
            public String toString(Huesped h) {
                if (h == null) return "";
                return h.getIdHuesped() + " - " + h.getNombre() + " (" + h.getTelefono() + ")";
            }

            @Override
            public Huesped fromString(String string) {
                return null;
            }
        });

        cmbHabitacion.setConverter(new StringConverter<>() {
            @Override
            public String toString(Habitacion h) {
                if (h == null) return "";
                return "Hab. " + h.getNumero() + " - " + h.getTipo() + " ($" + String.format("%.2f", h.getPrecioNoche()) + "/noche) [" + h.getEstado() + "]";
            }

            @Override
            public Habitacion fromString(String string) {
                return null;
            }
        });
    }

    private void configurarTabla() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idReservacion"));
        colHuesped.setCellValueFactory(new PropertyValueFactory<>("nombreHuesped"));
        colHabitacion.setCellValueFactory(new PropertyValueFactory<>("numeroHabitacion"));
        colEntrada.setCellValueFactory(new PropertyValueFactory<>("fechaEntrada"));
        colSalida.setCellValueFactory(new PropertyValueFactory<>("fechaSalida"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));
        colUsuario.setCellValueFactory(new PropertyValueFactory<>("nombreUsuario"));

        tblReservaciones.setItems(listaReservaciones);
    }

    private void configurarListenersCosto() {
        cmbHabitacion.valueProperty().addListener((obs, oldVal, newVal) -> calcularTotal());
        dpFechaEntrada.valueProperty().addListener((obs, oldVal, newVal) -> calcularTotal());
        dpFechaSalida.valueProperty().addListener((obs, oldVal, newVal) -> calcularTotal());
    }

    private void calcularTotal() {
        Habitacion hab = cmbHabitacion.getValue();
        LocalDate entrada = dpFechaEntrada.getValue();
        LocalDate salida = dpFechaSalida.getValue();

        if (hab != null && entrada != null && salida != null) {
            long noches = ChronoUnit.DAYS.between(entrada, salida);
            if (noches > 0) {
                totalCalculado = noches * hab.getPrecioNoche();
                lblTotalEstimado.setText(noches + (noches == 1 ? " noche" : " noches") + " | Total: $" + String.format("%.2f", totalCalculado));
                lblTotalEstimado.setStyle("-fx-text-fill: #1e8449; -fx-font-weight: bold;");
            } else {
                totalCalculado = 0.0;
                lblTotalEstimado.setText("Fechas inválidas (salida debe ser posterior)");
                lblTotalEstimado.setStyle("-fx-text-fill: #c0392b; -fx-font-weight: bold;");
            }
        } else {
            totalCalculado = 0.0;
            lblTotalEstimado.setText("0 noches | Total: $0.00");
            lblTotalEstimado.setStyle("-fx-text-fill: #1e8449; -fx-font-weight: bold;");
        }
    }

    private void cargarCatalogos() {
        cmbHuesped.setItems(FXCollections.observableArrayList(huespedDAO.listarTodos()));
        cmbHabitacion.setItems(FXCollections.observableArrayList(habitacionDAO.listarTodas()));
    }

    private void cargarReservaciones() {
        listaReservaciones.setAll(reservacionDAO.listar());
    }

    @FXML
    private void registrarReservacion() {
        Huesped huesped = cmbHuesped.getValue();
        Habitacion habitacion = cmbHabitacion.getValue();
        LocalDate entrada = dpFechaEntrada.getValue();
        LocalDate salida = dpFechaSalida.getValue();

        if (huesped == null || habitacion == null || entrada == null || salida == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "Por favor selecciona huésped, habitación y ambas fechas.");
            return;
        }

        if (!salida.isAfter(entrada)) {
            mostrarAlerta(Alert.AlertType.WARNING, "Fechas inválidas", "La fecha de salida debe ser al menos un día después de la fecha de entrada.");
            return;
        }

        if ("En Limpieza".equalsIgnoreCase(habitacion.getEstado()) && entrada.equals(LocalDate.now())) {
            mostrarAlerta(Alert.AlertType.WARNING, "Habitación en limpieza", "La habitación " + habitacion.getNumero() + " está en limpieza y no puede ocuparse hoy.");
            return;
        }

        if (!reservacionDAO.estaDisponible(habitacion.getIdHabitacion(), entrada, salida)) {
            mostrarAlerta(Alert.AlertType.ERROR, "Habitación no disponible", "La habitación " + habitacion.getNumero() + " ya tiene una reservación activa en ese rango de fechas.");
            return;
        }

        int idUsuario = (usuarioActual != null && usuarioActual.getIdUsuario() > 0) ? usuarioActual.getIdUsuario() : 1;

        Reservacion nueva = new Reservacion(
                huesped.getIdHuesped(),
                habitacion.getIdHabitacion(),
                idUsuario,
                entrada,
                salida,
                "Reservada",
                totalCalculado
        );

        if (reservacionDAO.registrar(nueva)) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Éxito", "Reservación registrada correctamente.");
            limpiarFormulario();
            cargarReservaciones();
        } else {
            mostrarAlerta(Alert.AlertType.ERROR, "Error", "No se pudo guardar la reservación en la base de datos.");
        }
    }

    @FXML
    private void hacerCheckIn() {
        Reservacion seleccionada = tblReservaciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Selecciona una reservación de la tabla.");
            return;
        }
        if (!"Reservada".equals(seleccionada.getEstado())) {
            mostrarAlerta(Alert.AlertType.WARNING, "Estado inválido", "Solo se puede hacer Check-In a reservaciones en estado 'Reservada'.");
            return;
        }

        if (reservacionDAO.cambiarEstado(seleccionada.getIdReservacion(), seleccionada.getIdHabitacion(), "Check-In")) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Check-In realizado", "La habitación " + seleccionada.getNumeroHabitacion() + " ahora está Ocupada.");
            cargarReservaciones();
            cargarCatalogos();
        }
    }

    @FXML
    private void hacerCheckOut() {
        Reservacion seleccionada = tblReservaciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Selecciona una reservación de la tabla.");
            return;
        }
        if (!"Check-In".equals(seleccionada.getEstado())) {
            mostrarAlerta(Alert.AlertType.WARNING, "Estado inválido", "Solo se puede hacer Check-Out a estancias activas en 'Check-In'.");
            return;
        }

        if (reservacionDAO.cambiarEstado(seleccionada.getIdReservacion(), seleccionada.getIdHabitacion(), "Finalizada")) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Check-Out finalizado", "Estancia cerrada. La habitación " + seleccionada.getNumeroHabitacion() + " pasó a 'En Limpieza'.");
            cargarReservaciones();
            cargarCatalogos();
        }
    }

    @FXML
    private void cancelarReservacion() {
        Reservacion seleccionada = tblReservaciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) {
            mostrarAlerta(Alert.AlertType.WARNING, "Selección requerida", "Selecciona una reservación de la tabla.");
            return;
        }
        if ("Finalizada".equals(seleccionada.getEstado()) || "Cancelada".equals(seleccionada.getEstado())) {
            mostrarAlerta(Alert.AlertType.WARNING, "Acción no permitida", "Esta reservación ya está cerrada o cancelada.");
            return;
        }

        if (reservacionDAO.cambiarEstado(seleccionada.getIdReservacion(), seleccionada.getIdHabitacion(), "Cancelada")) {
            mostrarAlerta(Alert.AlertType.INFORMATION, "Reservación cancelada", "La reservación fue cancelada y la habitación liberada.");
            cargarReservaciones();
            cargarCatalogos();
        }
    }

    @FXML
    private void limpiarFormulario() {
        cmbHuesped.setValue(null);
        cmbHabitacion.setValue(null);
        dpFechaEntrada.setValue(null);
        dpFechaSalida.setValue(null);
        totalCalculado = 0.0;
        lblTotalEstimado.setText("0 noches | Total: $0.00");
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
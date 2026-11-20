package com.example.proyectofinaltap.controller;

import com.example.proyectofinaltap.dao.HabitacionDAO;
import com.example.proyectofinaltap.model.Habitacion;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class HabitacionesController {

    @FXML private TextField txtNumero;
    @FXML private ComboBox<String> cmbTipo;
    @FXML private TextField txtPrecio;
    @FXML private ComboBox<String> cmbEstado;
    @FXML private TableView<Habitacion> tablaHabitaciones;
    @FXML private TableColumn<Habitacion, Integer> colId;
    @FXML private TableColumn<Habitacion, String> colNumero;
    @FXML private TableColumn<Habitacion, String> colTipo;
    @FXML private TableColumn<Habitacion, Double> colPrecio;
    @FXML private TableColumn<Habitacion, String> colEstado;
    @FXML private Label lblEstado;

    private final HabitacionDAO habitacionDAO = new HabitacionDAO();
    private Habitacion habitacionSeleccionada = null;

    @FXML
    public void initialize() {
        cmbTipo.getItems().addAll("Sencilla", "Doble", "Suite");
        cmbTipo.setValue("Sencilla");

        cmbEstado.getItems().addAll("Disponible", "Ocupada", "En Limpieza");
        cmbEstado.setValue("Disponible");

        colId.setCellValueFactory(new PropertyValueFactory<>("idHabitacion"));
        colNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colPrecio.setCellValueFactory(new PropertyValueFactory<>("precioNoche"));
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

        tablaHabitaciones.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                habitacionSeleccionada = newSel;
                txtNumero.setText(newSel.getNumero());
                cmbTipo.setValue(newSel.getTipo());
                txtPrecio.setText(String.valueOf(newSel.getPrecioNoche()));
                cmbEstado.setValue(newSel.getEstado());
            }
        });

        cargarTabla();
    }

    @FXML
    private void guardarHabitacion() {
        try {
            String numero = txtNumero.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());

            if (numero.isEmpty()) {
                lblEstado.setText("Ingresa el número de habitación.");
                return;
            }

            Habitacion nueva = new Habitacion(numero, cmbTipo.getValue(), precio, cmbEstado.getValue());
            if (habitacionDAO.insertar(nueva)) {
                lblEstado.setText("Habitación " + numero + " registrada correctamente.");
                limpiarFormulario();
                cargarTabla();
            } else {
                lblEstado.setText("Error al guardar (verifica que el número no esté repetido).");
            }
        } catch (NumberFormatException e) {
            lblEstado.setText("El precio debe ser un número válido.");
        }
    }

    @FXML
    private void actualizarHabitacion() {
        if (habitacionSeleccionada == null) {
            lblEstado.setText("Selecciona una habitación de la tabla primero.");
            return;
        }
        try {
            habitacionSeleccionada.setNumero(txtNumero.getText().trim());
            habitacionSeleccionada.setTipo(cmbTipo.getValue());
            habitacionSeleccionada.setPrecioNoche(Double.parseDouble(txtPrecio.getText().trim()));
            habitacionSeleccionada.setEstado(cmbEstado.getValue());

            if (habitacionDAO.actualizar(habitacionSeleccionada)) {
                lblEstado.setText("Habitación actualizada.");
                limpiarFormulario();
                cargarTabla();
            } else {
                lblEstado.setText("No se pudo actualizar la habitación.");
            }
        } catch (NumberFormatException e) {
            lblEstado.setText("El precio debe ser un número válido.");
        }
    }

    @FXML
    private void eliminarHabitacion() {
        if (habitacionSeleccionada == null) {
            lblEstado.setText("Selecciona una habitación de la tabla para eliminar.");
            return;
        }
        if (habitacionDAO.eliminar(habitacionSeleccionada.getIdHabitacion())) {
            lblEstado.setText("Habitación eliminada.");
            limpiarFormulario();
            cargarTabla();
        } else {
            lblEstado.setText("No se puede eliminar (puede tener reservaciones activas).");
        }
    }

    @FXML
    private void limpiarFormulario() {
        habitacionSeleccionada = null;
        txtNumero.clear();
        txtPrecio.clear();
        cmbTipo.setValue("Sencilla");
        cmbEstado.setValue("Disponible");
        tablaHabitaciones.getSelectionModel().clearSelection();
    }

    private void cargarTabla() {
        tablaHabitaciones.setItems(FXCollections.observableArrayList(habitacionDAO.listarTodas()));
    }
}
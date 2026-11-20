package com.example.proyectofinaltap.controller;

import com.example.proyectofinaltap.dao.HuespedDAO;
import com.example.proyectofinaltap.model.Huesped;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class HuespedesController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;
    @FXML private TableView<Huesped> tablaHuespedes;
    @FXML private TableColumn<Huesped, Integer> colId;
    @FXML private TableColumn<Huesped, String> colNombre;
    @FXML private TableColumn<Huesped, String> colTelefono;
    @FXML private TableColumn<Huesped, String> colCorreo;
    @FXML private Label lblEstado;

    private final HuespedDAO huespedDAO = new HuespedDAO();
    private Huesped huespedSeleccionado = null;

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("idHuesped"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCorreo.setCellValueFactory(new PropertyValueFactory<>("correo"));

        tablaHuespedes.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, newSel) -> {
            if (newSel != null) {
                huespedSeleccionado = newSel;
                txtNombre.setText(newSel.getNombre());
                txtTelefono.setText(newSel.getTelefono());
                txtCorreo.setText(newSel.getCorreo());
            }
        });

        cargarTabla();
    }

    @FXML
    private void guardarHuesped() {
        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (nombre.isEmpty() || telefono.isEmpty()) {
            lblEstado.setText("Nombre y teléfono son obligatorios.");
            return;
        }

        Huesped nuevo = new Huesped(nombre, telefono, correo);
        if (huespedDAO.insertar(nuevo)) {
            lblEstado.setText("Huésped " + nombre + " registrado.");
            limpiarFormulario();
            cargarTabla();
        } else {
            lblEstado.setText("Error al registrar el huésped.");
        }
    }

    @FXML
    private void actualizarHuesped() {
        if (huespedSeleccionado == null) {
            lblEstado.setText("Selecciona un huésped de la tabla primero.");
            return;
        }

        String nombre = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        if (nombre.isEmpty() || telefono.isEmpty()) {
            lblEstado.setText("Nombre y teléfono no pueden quedar vacíos.");
            return;
        }

        huespedSeleccionado.setNombre(nombre);
        huespedSeleccionado.setTelefono(telefono);
        huespedSeleccionado.setCorreo(txtCorreo.getText().trim());

        if (huespedDAO.actualizar(huespedSeleccionado)) {
            lblEstado.setText("Datos del huésped actualizados.");
            limpiarFormulario();
            cargarTabla();
        } else {
            lblEstado.setText("No se pudo actualizar el huésped.");
        }
    }

    @FXML
    private void eliminarHuesped() {
        if (huespedSeleccionado == null) {
            lblEstado.setText("Selecciona un huésped para eliminar.");
            return;
        }

        if (huespedDAO.eliminar(huespedSeleccionado.getIdHuesped())) {
            lblEstado.setText("Huésped eliminado.");
            limpiarFormulario();
            cargarTabla();
        } else {
            lblEstado.setText("No se puede eliminar (tiene reservaciones registradas).");
        }
    }

    @FXML
    private void limpiarFormulario() {
        huespedSeleccionado = null;
        txtNombre.clear();
        txtTelefono.clear();
        txtCorreo.clear();
        tablaHuespedes.getSelectionModel().clearSelection();
    }

    private void cargarTabla() {
        tablaHuespedes.setItems(FXCollections.observableArrayList(huespedDAO.listarTodos()));
    }
}
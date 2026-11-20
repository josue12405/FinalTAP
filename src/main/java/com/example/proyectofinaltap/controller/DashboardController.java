package com.example.proyectofinaltap.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    @FXML private Label lblTituloSeccion;
    @FXML private Label lblUsuarioActivo;
    @FXML private Label lblPlaceholder;
    @FXML private StackPane contenedorCentral;

    @FXML private Button btnHabitaciones;
    @FXML private Button btnHuespedes;
    @FXML private Button btnReservaciones;

    private static final String ESTILO_ACTIVO = "-fx-background-color: #1e3a8a; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 15 0 15;";
    private static final String ESTILO_INACTIVO = "-fx-background-color: transparent; -fx-text-fill: #cbd5e1; -fx-font-weight: bold; -fx-background-radius: 8; -fx-cursor: hand; -fx-padding: 0 15 0 15;";

    @FXML
    public void initialize() {
        mostrarHabitaciones();
    }

    public void setDatosUsuario(String nombre, String rol) {
        lblUsuarioActivo.setText(nombre + " • " + rol);
    }

    @FXML
    private void mostrarHabitaciones() {
        activarBoton(btnHabitaciones);
        lblTituloSeccion.setText("Gestión de Habitaciones");
        cargarVista("/com/example/proyectofinaltap/habitaciones-view.fxml");
    }

    private void cargarVista(String rutaFxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
            contenedorCentral.getChildren().setAll((javafx.scene.Node) loader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void mostrarHuespedes() {
        activarBoton(btnHuespedes);
        lblTituloSeccion.setText("Directorio de Huéspedes");
        lblPlaceholder.setText("Aquí cargaremos el registro y consulta de Huéspedes.");
    }

    @FXML
    private void mostrarReservaciones() {
        activarBoton(btnReservaciones);
        lblTituloSeccion.setText("Control de Reservaciones");
        lblPlaceholder.setText("Aquí cargaremos el módulo de Reservaciones y cálculo de noches/total.");
    }

    @FXML
    private void cerrarSesion() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectofinaltap/login-view.fxml"));
            Scene scene = new Scene(loader.load(), 780, 520);
            Stage stage = (Stage) contenedorCentral.getScene().getWindow();
            stage.setTitle("Grand Horizon — Iniciar Sesión");
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void activarBoton(Button activo) {
        btnHabitaciones.setStyle(ESTILO_INACTIVO);
        btnHuespedes.setStyle(ESTILO_INACTIVO);
        btnReservaciones.setStyle(ESTILO_INACTIVO);
        activo.setStyle(ESTILO_ACTIVO);
    }
}
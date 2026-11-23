package com.example.proyectofinaltap.controller;

import com.example.proyectofinaltap.dao.UsuarioDAO;
import com.example.proyectofinaltap.model.Usuario;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMensaje;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String pass = txtPassword.getText().trim();

        if (usuario.isEmpty() || pass.isEmpty()) {
            lblMensaje.setText("Completa todos los campos");
            return;
        }

        Usuario userLogueado = usuarioDAO.autenticar(usuario, pass);

        if (userLogueado != null) {
            abrirDashboard(userLogueado);
        } else {
            lblMensaje.setText("Usuario o contraseña incorrectos");
        }
    }

    private void abrirDashboard(Usuario user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/proyectofinaltap/dashboard-view.fxml"));
            Scene scene = new Scene(loader.load(), 1050, 660);

            DashboardController dashController = loader.getController();
            dashController.setUsuario(user);

            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            stage.setTitle("Grand Horizon — " + user.getNombreCompleto() + " (" + user.getRol() + ")");
            stage.setScene(scene);
            stage.centerOnScreen();
        } catch (IOException e) {
            lblMensaje.setText("Error al cargar dashboard-view.fxml");
            e.printStackTrace();
        }
    }
}
package com.example.proyectofinaltap.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Label lblMensaje;

    @FXML
    private void iniciarSesion() {
        String usuario = txtUsuario.getText().trim();
        String pass = txtPassword.getText().trim();

        if (usuario.isEmpty() || pass.isEmpty()) {
            lblMensaje.setText("Completa todos los campos");
        } else {
            lblMensaje.setText("Validando usuario: " + usuario);
        }
    }
}
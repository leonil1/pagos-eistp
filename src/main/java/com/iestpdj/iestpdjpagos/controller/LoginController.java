package com.iestpdj.iestpdjpagos.controller;
import com.iestpdj.iestpdjpagos.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

public class LoginController {

    @FXML
    private Button btnLogin;
    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;

    private UsuarioService usuarioService;

    public void initialize() {
        usuarioService = new UsuarioService();
    }

    @FXML
    private void  loginButtonAction(ActionEvent event) {

        String usuario = txtUsername.getText();
        String password = txtPassword.getText();

        Stage stage = (Stage) btnLogin.getScene().getWindow();
        System.out.println("hola" + stage);
    }


}

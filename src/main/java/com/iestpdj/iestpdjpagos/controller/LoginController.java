package com.iestpdj.iestpdjpagos.controller;
import com.iestpdj.iestpdjpagos.model.Usuario;
import com.iestpdj.iestpdjpagos.service.UsuarioService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;

import java.io.IOException;

public class LoginController {

    // aqui se llama los componentes de la vista GUI
    @FXML
    private Button btnLogin;
    @FXML
    private TextField textUsuario;
    @FXML
    private PasswordField textContrasenia;

    //Injection de  del services
    private UsuarioService usuarioService;

    // nicializar la vista
    public void initialize() {
        usuarioService = new UsuarioService();
    }

    // METHOD: INICAI EL METHOS DE LOGION
    @FXML
    private void  handleLogin(ActionEvent event) {

        String username = textUsuario.getText();
        String password = textContrasenia.getText();

        if (usuarioService.iniciarSesion(username, password)){

            //obtener el informacion del usuario
            Usuario usuario = usuarioService.obtenerUsuario(username);
            try{
                //carga el dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/vistasFX/dashboard1.fxml"));
                Parent root = loader.load();

                //DashboardController dashboardController = loader.getController();
                //dashboardController.setUsuario(usuario);

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(root);

                scene.getStylesheets().add(getClass().getResource("/css/dashboard.css").toExternalForm());
                stage.  setScene(scene);
                stage.setTitle("Dashboard- " + usuario.getUsername());
                stage.show();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Credenciales inválidas. Intenta de nuevo.");
            alert.showAndWait();
            textContrasenia.clear();
        }
    }


}

package com.iestpdj.iestpdjpagos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class Main extends Application {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/vistasFX/login.fxml"));

        Scene scene = new Scene(root);
        scene.getStylesheets().add(getClass().getResource("/css/usuario.css").toExternalForm());
        stage.setTitle("Sistema de Login");
        stage.setResizable(true);
        stage.setScene(scene);

        stage.show();

        logger.info("Aplicación iniciando...");
        try {
            stage.setTitle("Sistema de Pagos");
            stage.show();
            logger.info("Ventana mostrada correctamente");
        } catch (Exception e) {
            logger.error("Error al iniciar la aplicación", e);
        }
    }

    public static void main(String[] args) {
        logger.debug("Lanzando la aplicación...");
        launch(args);
    }
}

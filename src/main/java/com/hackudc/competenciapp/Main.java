package com.hackudc.competenciapp;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        //Cargar la ventana de inicio de sesión
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("InicioSesion.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 266, 149);
        CInicioSesion controller = fxmlLoader.getController();
        controller.initCInicioSesion(stage);
        stage.setTitle("Inicio Sesión");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}
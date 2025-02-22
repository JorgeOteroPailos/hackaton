package com.hackudc.competenciapp;

import javafx.application.Application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    //Variables para la conexión
    private HttpURLConnection conexion;
    private URL consultas;
    private URL insertarDatos;
    private URL inicioSesion;

    //Variables para guardar datos
    private String clave;
    private String nombre;

    @Override
    public void start(Stage stage) throws IOException {
        //Asignar la url del servidor
        consultas = new URL("http://localhost:8000/consultar");
        insertarDatos = new URL("http://localhost:8000/insertar");
        inicioSesion = new URL("http://localhost:8000/sesion");

        //Cargar la ventana de inicio de sesión
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("InicioSesion.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 260, 125);
        CInicioSesion controller = fxmlLoader.getController();
        controller.initCInicioSesion(stage);
        stage.setTitle("Inicio Sesión");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
    
}
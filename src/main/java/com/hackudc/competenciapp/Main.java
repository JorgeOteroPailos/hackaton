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
    private HttpURLConnection conexion;
    URL consultas;
    URL insertarDatos;
    URL inicioSesion;

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
        controller.initCInicioSesion(this);
        stage.setTitle("Inicio Sesión");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


    public void verificarUsuario(String clave){
        setConexionConsulta();
        StringBuilder jsonBuilder = new StringBuilder("{\"strings\":[");
        jsonBuilder.append("\"" + clave + "\"]}");
        try (OutputStream os = conexion.getOutputStream()) {
            byte[] input = jsonBuilder.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input);
            System.out.println("ENVIADO: " + clave);
            int rc = conexion.getResponseCode();
            if(rc!=200){
                System.out.println("ERROR: rc=" + rc);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder aux = new StringBuilder(new String());
            String respuesta;
            while ((respuesta = br.readLine()) != null) {
                aux.append(respuesta.trim());
            }
            System.out.println("Respuesta del servidor: " + aux);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setConexionConsulta() {
        try {
            conexion = (HttpURLConnection) consultas.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setConexionInicioSesion() {
        try {
            conexion = (HttpURLConnection) inicioSesion.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void setConexionInsertarDatos() {
        try {
            conexion = (HttpURLConnection) insertarDatos.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
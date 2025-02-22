package com.hackudc.competenciapp;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Utils {
    private static HttpURLConnection conexion;
    static final URL CONSULTAS;
    static final URL INSERTAR_DATOS;
    static final URL INICIO_SESION;

    static {
        try {
            CONSULTAS = new URL("http://localhost:8000/consultar");
            INSERTAR_DATOS = new URL("http://localhost:8000/insertar");
            INICIO_SESION = new URL("http://localhost:8000/sesion");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    //Función para verificar el usuario
    public static String verificarUsuario(String clave){
        setConexionInicioSesion();
        StringBuilder jsonBuilder = new StringBuilder("{\"clave\": \""+clave+"\"}");
        return enviarMensaje(jsonBuilder);
    }

    public static String hacerConsulta(String consulta){
        setConexionConsulta();
        StringBuilder jsonBuilder = new StringBuilder("{\"query\": \""+consulta+"\"}");
        return enviarMensaje(jsonBuilder);
    }

    private static String enviarMensaje(StringBuilder jsonBuilder) {
        StringBuilder respuesta = new StringBuilder();
        try (OutputStream os = conexion.getOutputStream()) {
            byte[] input = jsonBuilder.toString().getBytes(StandardCharsets.UTF_8);
            os.write(input);
            int rc = conexion.getResponseCode();
            if (rc != 200) {
                System.out.println("ERROR: rc=" + rc);
            }
            System.out.println("ENVIADO: " + jsonBuilder.toString());

        } catch (
                IOException e) {
            e.printStackTrace();
        }
        try (
                BufferedReader br = new BufferedReader(new InputStreamReader(conexion.getInputStream(), StandardCharsets.UTF_8))) {
            respuesta = new StringBuilder(new String());
            String aux;
            while ((aux = br.readLine()) != null) {
                respuesta.append(aux.trim());
            }
            System.out.println("Respuesta del servidor: " + respuesta);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return respuesta.toString();
    }



    public static String insertarDatos(String datos){
        setConexionInsertarDatos();
        StringBuilder jsonBuilder = new StringBuilder("{\"query\": \""+datos+"\"}");
        return enviarMensaje(jsonBuilder);
    }

    public static void setConexionConsulta() {
        try {
            conexion = (HttpURLConnection) CONSULTAS.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setConexionInicioSesion() {
        try {
            conexion = (HttpURLConnection) INICIO_SESION.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setConexionInsertarDatos() {
        try {
            conexion = (HttpURLConnection) INSERTAR_DATOS.openConnection();
            conexion.setRequestMethod("POST");
            conexion.setRequestProperty("Content-Type", "application/json");
            conexion.setDoOutput(true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

        public static void showPopup (String title, String message, Alert.AlertType type){
            // Crear unha alerta
            Alert alert = new Alert(type);

            // Configurar o título e contido
            alert.setTitle(title);
            alert.setHeaderText(null); // Opcional, podes engadir un subtítulo
            alert.setContentText(message);

            // Mostrar o popup e esperar que o usuario o peche
            alert.showAndWait();
        }

        public static void showPopup (Exception e){
            // Crear unha alerta
            Alert alert = new Alert(Alert.AlertType.ERROR);

            // Configurar o título e contido
            alert.setTitle("Error");
            alert.setHeaderText(null); // Opcional, podes engadir un subtítulo
            alert.setContentText(e.getMessage());

            // Mostrar o popup e esperar que o usuario o peche
            alert.showAndWait();
        }
}

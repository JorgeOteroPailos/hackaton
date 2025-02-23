
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private static HttpURLConnection conexion;
    static final URL CONSULTAS;
    static final URL INSERTAR_DATOS;
    static final URL INICIO_SESION;
    static final URL GITHUB;

    private static CPrincipal controller;

    public static void setController(CPrincipal controller) {
        Utils.controller = controller;
    }

    static {
        try {
            CONSULTAS = new URL("http://localhost:8000/consultar");
            INSERTAR_DATOS = new URL("http://localhost:8000/insertar");
            INICIO_SESION = new URL("http://localhost:8000/sesion");
            GITHUB = new URL("http://localhost:8000/agregar_github");
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

    public static void hacerConsulta(String consulta){
        setConexionConsulta();
        StringBuilder jsonBuilder = new StringBuilder("{\"query\": \""+consulta+"\"}");
        String respuesta = enviarMensaje(jsonBuilder);

        Mensaje m = new Mensaje(extraerContenidoComillasDobles(respuesta),1);
        Platform.runLater(() -> {
            controller.chats[controller.estoyEnConsultas].anadirMensaje(m);
            controller.agregarMensaje(m);
            controller.esperandoMensaje=false;
        });
    }


    public static void insertarDatos(String clave, String datos){
        setConexionInsertarDatos();
        StringBuilder jsonBuilder = new StringBuilder("{\"clave\": \""+clave+"\", \"frase_competencia\": \""+datos+"\"}");
        String respuesta = enviarMensaje(jsonBuilder);
        Mensaje m = new Mensaje(extraerContenido(respuesta),1);
        Platform.runLater(() -> {
            controller.chats[controller.estoyEnConsultas].anadirMensaje(m);
            controller.agregarMensaje(m);
            controller.esperandoMensaje=false;
        });
    }

    private static String extraerContenido(String texto) {
        Pattern pattern = Pattern.compile("'(.*?)'");
        Matcher matcher = pattern.matcher(texto);

        if (matcher.find()) {
            return matcher.group(1); // Retorna el primer grupo capturado
        }
        return null; // Retorna null si no hay coincidencias
    }

    public static String extraerContenidoComillasDobles(String texto) {
        // Eliminar saltos de línea y asteriscos
        texto = texto.replace("\\n", "").replace("*", "");

        // Compilar el patrón para buscar contenido entre comillas dobles
        Pattern pattern = Pattern.compile("\"(.*?)\"");
        Matcher matcher = pattern.matcher(texto);

        // Contar las comillas dobles encontradas
        int count = 0;

        // Buscar el contenido entre comillas
        while (matcher.find()) {
            count++;
            // Retornar el segundo campo (cuando se encuentra el segundo par de comillas)
            if (count == 2) {
                return matcher.group(1);  // Retorna el contenido entre las segundas comillas dobles
            }
        }

        return null; // Retorna null si no se encuentra el segundo campo
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
            respuesta = new StringBuilder();
            String aux;
            while ((aux = br.readLine()) != null) {
                respuesta.append(aux.trim());
            }
            System.out.println("Respuesta del servidor: " + respuesta.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return respuesta.toString();
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

package com.hackudc.competenciapp;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import java.util.Objects;
import javafx.application.Platform;

public class CPrincipal {

    public static final String nombreVentanaConsultas="Consultas";
    private static final String nombreVentanaAportaciones="Aportaciones";
    public Chat[] chats;
    public boolean esperandoMensaje=false;

    private String clave;

    @FXML
    public Button botoncambiarVentana;
    @FXML
    public Label nombreVentana;
    @FXML
    public ScrollPane scrollPaneMensajes;
    @FXML
    public VBox listaMensajes;
    @FXML
    public TextField messageTextField;
    @FXML
    public Button sendButton;

    public int estoyEnConsultas=1;

    public void setClave(String clave){
        this.clave=clave;
    }

    @FXML
    public void cambiarVentana(){
        if(esperandoMensaje){
            return;
        }
        if(estoyEnConsultas==1){
            estoyEnConsultas=0;
            botoncambiarVentana.setText("Consultas");
            nombreVentana.setText("Aportaciones");
        }
        else {
            estoyEnConsultas=1;
            botoncambiarVentana.setText("Aportaciones");
            nombreVentana.setText("Consultas");
        }

        actualizarMensajes();
    }

    public void initialize(){
        chats=new Chat[2];
        chats[0]=new Chat();
        chats[1]=new Chat();
        estoyEnConsultas = 1;
        botoncambiarVentana.setText(nombreVentanaAportaciones);
        nombreVentana.setText(nombreVentanaConsultas);

        // Configurar el comportamiento del scroll manualmente
        scrollPaneMensajes.setOnScroll(event -> {
            double deltaY = event.getDeltaY() * 0.05; // Ajusta la sensibilidad
            double value = scrollPaneMensajes.getVvalue();
            scrollPaneMensajes.setVvalue(value - deltaY);
            event.consume();
        });

        messageTextField.setOnKeyPressed(event -> {
            if (Objects.requireNonNull(event.getCode()) == KeyCode.ENTER) {
                enviarMensaje(); // Llama al método para enviar el mensaje
                messageTextField.requestFocus(); // Mantener el cursor en el TextField
            }
        });

        // Configurar el botón de enviar
        sendButton.setOnAction(event -> enviarMensaje());

    }
    @FXML
    private void enviarMensaje(){
        if(esperandoMensaje){
            return;
        }
        esperandoMensaje=true;
        String prompt = messageTextField.getText();
        messageTextField.clear();
        String respuesta;

        if (!prompt.isBlank()) {
            Mensaje m=new Mensaje(prompt,0);
            chats[estoyEnConsultas].anadirMensaje(m);
            agregarMensaje(m);
            if(estoyEnConsultas==1){
                new Thread(() -> {Utils.hacerConsulta(prompt);}).start();
            }else{
                new Thread(() -> {Utils.insertarDatos(clave,prompt);}).start();
            }

        }
    }

    public void agregarMensaje(Mensaje mensaje){
        //chats[mensaje.remitente].anadirMensaje(mensaje);

            HBox contenedorMensaje = new HBox();
            VBox mensajeVBox = new VBox(5);
            mensajeVBox.setMaxWidth(300); // Limitar el ancho máximo de la burbuja del mensaje

            // Contenido del mensaje
            Label textoMensaje = new Label(mensaje.texto);
            textoMensaje.setWrapText(true);
            textoMensaje.setStyle("-fx-font-size: 16;");

            // Añadir contenido al VBox
            mensajeVBox.getChildren().addAll(textoMensaje);

            // Estilizar y alinear según el remitente
            if (mensaje.remitente==0) {
                mensajeVBox.setStyle("-fx-background-color: lightblue; -fx-padding: 10; -fx-background-radius: 10;");
                contenedorMensaje.setAlignment(Pos.CENTER_RIGHT); // Mensajes enviados a la derecha
            } else {
                mensajeVBox.setStyle("-fx-background-color: lightgray; -fx-padding: 10; -fx-background-radius: 10;");
                contenedorMensaje.setAlignment(Pos.CENTER_LEFT); // Mensajes recibidos a la izquierda
            }

            contenedorMensaje.getChildren().add(mensajeVBox);
            listaMensajes.getChildren().add(contenedorMensaje);

            // Desplazar automáticamente al final del ScrollPane
            scrollPaneMensajes.setVvalue(1.0);
    }

    private void actualizarMensajes() {
        listaMensajes.getChildren().clear();

        for (Mensaje mensaje : chats[estoyEnConsultas].getMensajes()) {
            agregarMensaje(mensaje);
        }

        // Desplaza automáticamente al final después de cargar los mensajes
        scrollPaneMensajes.setVvalue(1.0);

    }


}
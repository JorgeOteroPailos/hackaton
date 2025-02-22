package com.hackudc.competenciapp;

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

    public Button botoncambiarVentana;
    public static final String nombreVentanaConsultas="Consultas";
    private static final String nombreVentanaAportaciones="Aportaciones";
    public Label nombreVentana;
    public ScrollPane scrollPaneMensajes;
    public VBox listaMensajes;
    public TextField messageTextField;
    public Button sendButton;
    private Chat[] chats;

    private int estoyEnConsultas = 1;

    public void cambiarVentana(){
        botoncambiarVentana.setText(estoyEnConsultas==1 ? nombreVentanaAportaciones : nombreVentanaConsultas);
        nombreVentana.setText(estoyEnConsultas==1 ? nombreVentanaConsultas : nombreVentanaAportaciones);
        estoyEnConsultas= estoyEnConsultas==1 ? 0 : 1;
    }

    public void initialize(){
        estoyEnConsultas =1;
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

            chats=new Chat[2];

            chats[0]=new Chat();
            chats[1]=new Chat();
        });

        // Configurar el botón de enviar
        sendButton.setOnAction(event -> enviarMensaje());

    }

    private void enviarMensaje(){
        String prompt = messageTextField.getText();
        String respuesta = "Lo siento, parece que no puedo responderte ahora mismo, inténtalo de nuevo más tarde";
        if (!prompt.isBlank()) {
            if(estoyEnConsultas==1){
                respuesta=Utils.hacerConsulta(prompt);
            }else{
                respuesta=Utils.insertarDatos(prompt);
            }
        }
        agregarMensaje(new Mensaje(respuesta,1));
    }

    private void agregarMensaje(Mensaje mensaje){
        chats[mensaje.remitente].anadirMensaje(mensaje);
        Platform.runLater(()->{
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
            Platform.runLater(() -> scrollPaneMensajes.setVvalue(1.0));
        });
    }

    private void actualizarMensajes() {
        listaMensajes.getChildren().clear();

        for (Mensaje mensaje : chats[0].getMensajes()) {
            agregarMensaje(mensaje);
        }
        for (Mensaje mensaje : chats[1].getMensajes()) {
            agregarMensaje(mensaje);
        }
        // Desplaza automáticamente al final después de cargar los mensajes
        Platform.runLater(() -> scrollPaneMensajes.setVvalue(1.0));

    }


}
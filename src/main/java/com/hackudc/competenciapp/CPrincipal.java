package com.hackudc.competenciapp;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import java.util.Objects;

public class CPrincipal {

    public Button botoncambiarVentana;
    public static final String nombreVentanaConsultas="Consultas";
    private static final String nombreVentanaAportaciones="Aportaciones";
    public Label nombreVentana;
    public ScrollPane scrollPaneMensajes;
    public VBox listaMensajes;
    public TextField messageTextField;
    public Button sendButton;
    private Chat chatSeleccionado;

    private boolean estoyEnConsultas =true;

    public void cambiarVentana(){
        botoncambiarVentana.setText(estoyEnConsultas ? nombreVentanaAportaciones : nombreVentanaConsultas);
        nombreVentana.setText(estoyEnConsultas ? nombreVentanaConsultas : nombreVentanaAportaciones);
        estoyEnConsultas =!estoyEnConsultas;
    }

    public void initialize(){
        estoyEnConsultas =true;
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

    private void enviarMensaje() {
        //todo
    }

    //private void agregarMensaje


}
package com.hackudc.competenciapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class CInicioSesion {
    private Stage stage;
    @FXML
    private TextField clave;
    @FXML
    private Button enter;

    public void initCInicioSesion(Stage stage){
        this.stage = stage;
    }

    @FXML
    private void verificarUsuario() {
        String aux = Utils.verificarUsuario(clave.getText());
        if(!aux.equals("false")&&!aux.isEmpty()){
            abrirCPrincipal(aux);
        }else {

        }
    }
    @FXML
    public void manejadorTeclas(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            verificarUsuario();
        }
    }

    public void abrirCPrincipal(String nombre){
        try {
            //Cargar la ventana de inicio de sesión
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Principal.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 604, 433);
            CPrincipal controller = fxmlLoader.getController();
            controller.setClave(clave.getText());
            Utils.setController(controller);
            stage.setTitle(nombre);
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){

        }
    }
}

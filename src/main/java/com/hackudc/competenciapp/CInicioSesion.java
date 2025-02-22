package com.hackudc.competenciapp;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class CInicioSesion {
    private Main main;
    @FXML
    private TextField clave;
    @FXML
    private Button enter;

    public void initCInicioSesion(Main main) {
        this.main = main;
    }
    @FXML
    private void verificarUsuario() {
        main.verificarUsuario(clave.getText());
    }
    @FXML
    public void manejadorTeclas(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            verificarUsuario();
        }
    }
}

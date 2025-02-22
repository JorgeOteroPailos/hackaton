package com.hackudc.competenciapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CPrincipal {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}
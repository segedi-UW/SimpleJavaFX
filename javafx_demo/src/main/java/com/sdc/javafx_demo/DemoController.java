package com.sdc.javafx_demo;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;

public class DemoController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    public void openCanvas() {
        DemoApplication.getPrimaryStage().setScene(new Scene(DemoApplication.loadFXML("canvas.fxml"), 600, 600));
    }
}
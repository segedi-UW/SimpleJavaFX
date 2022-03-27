package com.sdc.javafx_demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class DemoApplication extends Application {

    private static Stage primary;

    @Override
    public void start(Stage stage) {
        if (primary != null) throw new IllegalStateException("Tried to re-init JavaFX");
        else primary = stage;
        Scene scene = new Scene(loadFXML("demo-view.fxml"), 320, 240);

        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static Parent loadFXML(String resource) {
        URL url = DemoApplication.class.getResource(resource);
        if (url == null) throw new NullPointerException("No resource found for: " + resource);
        FXMLLoader fxmlLoader = new FXMLLoader(url);
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        throw new IllegalStateException("Could not load FXML for resource: " + resource);
    }

    public static Stage getPrimaryStage() {
        return primary;
    }

    public static void main(String[] args) {
        launch();
    }
}
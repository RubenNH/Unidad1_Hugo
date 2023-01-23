package edu.utez.unidad2_1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Paths;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        URI uri = Paths.get("src/main/resources/vistas/alumnoFxml.fxml").toAbsolutePath().toUri();
        System.out.println(uri);
        Parent root = FXMLLoader.load(uri.toURL());
        Scene scene = new Scene(root);;
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
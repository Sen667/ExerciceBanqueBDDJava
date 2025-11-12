package com.example.cbclient;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Application bancaire utilisant FXML
 */
public class BanqueApp extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(BanqueApp.class.getResource("banque-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 700);
        
        primaryStage.setTitle("🏦 Gestion Bancaire");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

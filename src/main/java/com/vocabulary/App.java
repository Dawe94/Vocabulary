package com.vocabulary;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // FXML betöltése
        Parent root = FXMLLoader.load(getClass().getResource("vocabularyView.fxml"));
        
        // A színpad tartalmának beállítása
        Scene scene = new Scene(root);
        stage.setTitle("Vocabulary");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}

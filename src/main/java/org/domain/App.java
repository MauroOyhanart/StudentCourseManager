package org.domain;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import persistence.PersistenceHandler;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("main_page"), 640, 480);
        stage.setScene(scene);
        initialize_UI_properties(stage);
        PersistenceHandler.initializeComponents();
        stage.show();
    }

    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public void initialize_UI_properties(Stage stage){
        stage.setResizable(false);
        loadCSS(stage);
    }
    private void loadCSS(Stage stage){
        Scene scene = stage.getScene();
        scene.getStylesheets().add(getClass().getResource("main_styles.css").toExternalForm());
    }

    public static void main(String[] args) {
        launch();
    }


    public static void exitApplication(){
        onExit();
        Platform.exit();
    }

    private static void onExit(){

    }
}
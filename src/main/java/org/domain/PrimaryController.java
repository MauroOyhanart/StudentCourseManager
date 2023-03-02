package org.domain;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class PrimaryController {

    @FXML
    Label label_my_course; //automatically populated due to @FXML annotation

    private Stage stage;

    @FXML
    private void switchToMyCourse() throws IOException {
        //Get stage
        Stage stage = (Stage) label_my_course.getScene().getWindow();
        this.stage = stage;
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("mycourse.fxml"));
        try{
            Parent root = loader.load();
            //Set scene
            Scene scene = new Scene(root);
            CourseController courseController = loader.getController();
            if (courseController !=null)
                courseController.initializeController(stage);
            //Load scene to stage
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("main_styles.css").toExternalForm());
            //Put the stage in the middle of the scene <*
            Rectangle2D primScreenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX((primScreenBounds.getWidth() - stage.getWidth()) / 2);
            stage.setY((primScreenBounds.getHeight() - stage.getHeight()) / 2); // *>
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    @FXML
    private void exitApplication(){
        App.exitApplication();
    }

    public Stage getStage(){
        return stage;
    }
}

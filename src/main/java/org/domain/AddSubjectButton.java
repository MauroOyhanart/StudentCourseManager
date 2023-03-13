package org.domain;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class AddSubjectButton extends Button {
    public AddSubjectButton(String s, int term, SubjectTreeEditController subjectTreeEditController){
        super(s);
        this.setId("button_add_subject");
        this.setOnAction(event -> {
            FXMLLoader loader = loadNewWindow("addSubject_window_with_term.fxml");
            AddSubjectWithTermController controller = loader.getController();
            controller.initializeController(subjectTreeEditController, term);
        });
    }

    //Returns the fxml loader, so we could get the controller if we wanted to
    private FXMLLoader loadNewWindow(String theFxml){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(theFxml));
        try{
            Parent root = loader.load();
            //Set scene
            Scene scene = new Scene(root);
            //Load scene to stage
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("main_styles.css").toExternalForm());
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
        return loader;
    }
}

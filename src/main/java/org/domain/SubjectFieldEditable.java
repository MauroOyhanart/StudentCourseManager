package org.domain;


import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class SubjectFieldEditable extends SubjectField {

    private final SubjectTreeEditController subjectTreeEditController;

    public SubjectFieldEditable(Subject subject, SubjectTreeEditController subjectTreeEditController) {
        super(subject);
        this.setOnMouseClicked(event -> {
            FXMLLoader loader = loadNewWindow("editSubject_pacs.fxml");
            EditSubjectPACsController editSubjectPACsController = loader.getController();
            editSubjectPACsController.initializeController(subject, subjectTreeEditController);

        });
        this.subjectTreeEditController = subjectTreeEditController;
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
            subjectTreeEditController.getStage().getScene().getRoot().setDisable(true);
            stage.setOnCloseRequest(event ->{
                subjectTreeEditController.getStage().getScene().getRoot().setDisable(false);
            });

        }catch (IOException e){
            e.printStackTrace();
            System.out.println(e.getMessage());
        }
        return loader;
    }
}

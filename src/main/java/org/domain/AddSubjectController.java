package org.domain;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import persistence.PersistenceHandler;

import java.util.ArrayList;
import java.util.List;

public class AddSubjectController {
    SubjectTreeEditController subjectTreeEditController;
    Stage stage;

    @FXML
    TextField text_name;

    @FXML
    Label label_warnings;

    public void initializeController(SubjectTreeEditController subjectTreeEditController){
        this.subjectTreeEditController = subjectTreeEditController;
        this.stage = (Stage) text_name.getScene().getWindow();
        setUIwarningsVisible(false);
    }
    public void exitWindow(){
        this.stage.close();
    }

    public void createSubject(){
        String subjectName = text_name.getText();
        //Integrity checks
        if (subjectName.isBlank()){
            System.out.println("name cannot be blank");
            setUIwarningsVisible(true);
            label_warnings.setText("Name cannot be blank");
            return;
        }
        if (subjectName.length()<3){
            System.out.println("name is too short");
            setUIwarningsVisible(true);
            label_warnings.setText("Name is too short");
            return;
        }
        setUIwarningsVisible(false);
        //-1 is the value for objects that are yet to be stored in the db
        Subject subject = new Subject(this.subjectTreeEditController.getCourse().getCourseId(), -1, subjectName, this.calculateTerm(), -1);
        PersistenceHandler.getInstance().createSubject(subject);
        this.subjectTreeEditController.updateSubjects();
        this.stage.close();
    }

    protected void setUIwarningsVisible(boolean value){
        label_warnings.setVisible(value);
    }

    protected int calculateTerm(){
        return 1;
    }
}

package org.domain;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
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

    @FXML
    ComboBox<Integer> cbox_term;

    public void initializeController(SubjectTreeEditController subjectTreeEditController){
        this.subjectTreeEditController = subjectTreeEditController;
        this.stage = (Stage) text_name.getScene().getWindow();
        setUIwarningsVisible(false);
        this.populateTermBox();
    }
    public void exitWindow(){
        this.stage.close();
    }

    public void createSubject(){
        String subjectName = text_name.getText();
        Integer term = cbox_term.getValue();
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
        if (term == null){
            System.out.println("nTerms cannot be null");
            setUIwarningsVisible(true);
            label_warnings.setText("Select number of terms");
            return;
        }
        setUIwarningsVisible(false);
        //-1 is the value for objects that are yet to be stored in the db
        Subject subject = new Subject(this.subjectTreeEditController.getCourse().getCourseId(), -1, subjectName, term, -1);
        System.out.println("subject is created: " + subjectName + " " +term);
        PersistenceHandler.getInstance().createSubject(subject);
        this.subjectTreeEditController.updateSubjects();
        this.stage.close();
    }

    private void populateTermBox(){
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= subjectTreeEditController.getCourse().getnTerms(); i++){
            numbers.add(i);
        }
        cbox_term.getItems().addAll(numbers);
    }

    private void setUIwarningsVisible(boolean value){
        label_warnings.setVisible(value);
    }
}

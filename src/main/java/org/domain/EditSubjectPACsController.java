package org.domain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import persistence.PersistenceHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EditSubjectPACsController {

    private Subject subject;
    //Needs
    private List<Subject> needs = new ArrayList<>();
    private ObservableList<String> needsObs;
    //All Subjects
    private List<Subject> allSubjects;
    private List<String> allSubjectsStr = new ArrayList<>();
    //Allows
    private List<Subject> allows = new ArrayList<>();
    private ObservableList<String> allowsObs;

    @FXML
    ListView<String> lv_needs, lv_all_subjects, lv_allows;

    @FXML
    Label label_name_edit;

    private Stage stage;

    private SubjectTreeEditController subjectTreeEditController;

    //Constructor
    public EditSubjectPACsController(){

    }

    //Methods for initialization of the subject editing menu
    public void initializeController(Subject subject, SubjectTreeEditController subjectTreeEditController){
        this.subjectTreeEditController = subjectTreeEditController;
        this.subject = subject;
        this.stage = (Stage) this.label_name_edit.getScene().getWindow();
        this.stage.setTitle("Edit Subject Relationships");
        label_name_edit.setText(label_name_edit.getText() + ": " + this.subject.getSubjName());
        this.populateLists();
        this.stage.setOnCloseRequest(event -> {
            this.subjectTreeEditController.closePAC();
        });

    }

    private void populateLists(){
        this.needs = PersistenceHandler.getInstance().getSubjectNeeds(this.subject);
        this.populateNeedsList();
        this.allSubjects = PersistenceHandler.getInstance().getSubjects(this.subject.getCourseId());
        this.populateAllSubjectsList();
        this.allows = PersistenceHandler.getInstance().getSubjectAllows(this.subject);
        this.populateAllowsList();
    }

    private void populateNeedsList(){
        List<String> listAux = new ArrayList<>();
        for (Subject s: needs){
            listAux.add(s.getSubjName());
        }
        needsObs = FXCollections.observableArrayList(listAux);
        lv_needs.setItems(needsObs);
    }

    private void populateAllSubjectsList(){
        for (Subject s: allSubjects){
            allSubjectsStr.add(s.getSubjName());
        }
        Collections.sort(allSubjectsStr); //Sort by subject name
        allSubjectsStr.remove(this.subject.getSubjName());
        //We do not need to save the observable list, so we'll lose it.
        lv_all_subjects.setItems(FXCollections.observableArrayList(allSubjectsStr));
    }

    private void populateAllowsList(){
        List<String> listAux = new ArrayList<>();
        for (Subject s: allows){
            listAux.add(s.getSubjName());
        }
        allowsObs = FXCollections.observableArrayList(listAux);
        lv_allows.setItems(allowsObs);
    }

    //Methods for altering the database through the moving between lists.
    public void addSelectedToNeeds(){
        String str = lv_all_subjects.getSelectionModel().getSelectedItem();
        //Integrity checks
        if (str == null) return;
        if (this.needsObs.contains(str)) return;
        if (this.subject.getSubjName().equals(str)) return;
        if (lv_allows.getItems().contains(str)) return;

        this.needsObs.add(str);
        PersistenceHandler.getInstance().notifySubjectNeed(this.subject, str);
    }

    public void addSelectedToAllows(){
        String str = lv_all_subjects.getSelectionModel().getSelectedItem();
        //Integrity checks
        if (str == null) return;
        if (this.allowsObs.contains(str)) return;
        if (this.subject.getSubjName().equals(str)) return;
        if (lv_needs.getItems().contains(str)) return;

        this.allowsObs.add(str);
        PersistenceHandler.getInstance().notifySubjectAllow(this.subject, str);
    }

    public void removeSelectedNeed(){
        String str = lv_needs.getSelectionModel().getSelectedItem();
        if (str == null) return;

        lv_needs.getItems().remove(str);
        PersistenceHandler.getInstance().notifyNotNeeds(this.subject, str);
    }

    public void removeSelectedAllow(){
        String str = lv_allows.getSelectionModel().getSelectedItem();
        if (str == null) return;

        lv_allows.getItems().remove(str);
        PersistenceHandler.getInstance().notifyNotAllows(this.subject, str);
    }
}

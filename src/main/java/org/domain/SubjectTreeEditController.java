package org.domain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import persistence.PersistenceHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SubjectTreeEditController{

    private CourseController courseController;
    private Stage stage;

    private String courseName;
    private Course course;

    private ObservableList<Subject> subjects;

    @FXML
    Button new_subject_btn;

    @FXML
    Label course_name_label_title;

    @FXML
    ScrollPane scroll_pane_displayer;

    @FXML
    VBox vbox_displayer;

    private ObservableList<SubjectHBox> hboxDisplayers;

    public void initializeController(CourseController courseController, Stage stage, String courseName){
        this.courseController = courseController;
        this.stage = stage;
        this.courseName = courseName;
        this.course = PersistenceHandler.getInstance().getCourse(courseName);
        List<Subject> _subjects = PersistenceHandler.getInstance().getSubjects(course.getCourseId()); //Subjects come already ordered
        this.subjects = FXCollections.observableList(_subjects);
        course_name_label_title.setText(course_name_label_title.getText() + " " + this.courseName);
        this.generateSubjectTreeViewEdit();
    }

    public void openAddSubject(){
        FXMLLoader loader = loadNewWindow("addSubject_window.fxml");
        AddSubjectController addSubjectController = loader.getController();
        if (addSubjectController != null){
            addSubjectController.initializeController(this);
        }else {
            System.out.println("Something happened to controller"); //TODO logger
        }
    }



    public String getCourseName() {
        return courseName;
    }

    public Course getCourse() {
        return course;
    }

    private void generateSubjectTreeViewEdit(){ //similar method should be in CourseController
        //We have the subjects, and the vbox where they should be in.
        //We could then generate 1 hbox for every term. Populate the hboxs with the subjects, using SubjectFieldEditable as instantiatable item.
        //clear
        vbox_displayer.getChildren().clear();
        //generate 1 hbox for every term
        System.out.println("Starts");
        List<SubjectHBox> subjectHBoxes = new ArrayList<>();
        if (this.subjects.size()<=0) return;
        for (int i = 0; i < this.subjects.get(subjects.size()-1).getSubjTerm(); i++){ //for every term, from 0 to the last term
            SubjectHBox subjectHBox = new SubjectHBox();
            subjectHBoxes.add(subjectHBox);
        }
        hboxDisplayers = FXCollections.observableList(subjectHBoxes);
        //populate the hboxs with the subjects
        for (Subject s: subjects){
            int term = s.getSubjTerm();
            SubjectFieldEditable subjectFieldEditable = new SubjectFieldEditable(s, this);
            hboxDisplayers.get(term-1).getChildren().add(subjectFieldEditable);
        }

        vbox_displayer.getChildren().addAll(hboxDisplayers);

        System.out.println("Finishes");
    }

    public void updateSubjects(){
        List<Subject> subjects1 = PersistenceHandler.getInstance().getSubjects(this.getCourse().getCourseId());
        this.subjects = FXCollections.observableList(subjects1);
        generateSubjectTreeViewEdit();
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

    public Stage getStage(){
        return this.stage;
    }

    //Routing
    public void goBackToMyCourse(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("mycourse.fxml"));
        try{
            Parent root = loader.load();
            //Set scene
            Scene scene = new Scene(root);
            this.courseController = loader.getController();
            this.courseController.reInitializeController(this.course, this.stage);
            //Load scene to stage
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("main_styles.css").toExternalForm());
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}

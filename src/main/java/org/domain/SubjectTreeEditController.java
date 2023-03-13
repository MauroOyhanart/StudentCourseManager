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
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
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

    private List<SubjectFieldViewable> subjectFieldViewables = new ArrayList<>();

    private SubjectFieldEditable SubjectFieldEditable_Selected = null;

    @FXML
    AnchorPane apane_subject;

    @FXML
    Label label_name_subject;

    @FXML
    TextField textfield_name_subject;

    @FXML
    Label label_warnings, term_label;

    private boolean PAC_open = false;

    public void initializeController(CourseController courseController, Stage stage, String courseName){
        this.courseController = courseController;
        this.stage = stage;
        this.courseName = courseName;
        this.course = PersistenceHandler.getInstance().getCourse(courseName);
        List<Subject> _subjects = PersistenceHandler.getInstance().getSubjects(course.getCourseId()); //Subjects come already ordered
        this.subjects = FXCollections.observableList(_subjects);
        course_name_label_title.setText(course_name_label_title.getText() + " " + this.courseName);
        apane_subject.setVisible(false);
        setUIwarningsVisible(false);
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

    private void generateSubjectTreeViewEdit(){
        if (this.subjects.size() == 0) return;
        int nthTerm = this.subjects.get(subjects.size()-1).getSubjTerm();

        //We have the subjects, and the vbox where they should be in.
        //We could then generate 1 hbox for every term. Populate the hboxs with the subjects, using SubjectFieldEditable as instantiatable item.
        //clear
        vbox_displayer.getChildren().clear();
        subjectFieldViewables.clear();
        //generate 1 hbox for every term
        System.out.println("Starts");
        List<SubjectHBox> subjectHBoxes = new ArrayList<>();
        for (int i = 0; i < nthTerm; i++){ //for every term, from 0 to the last term
            SubjectHBox subjectHBox = new SubjectHBox();
            subjectHBoxes.add(subjectHBox);
        }
        hboxDisplayers = FXCollections.observableList(subjectHBoxes);
        //populate the hboxs with the subjects
        for (Subject s: subjects){
            int term = s.getSubjTerm();
            SubjectFieldEditable subjectFieldEditable = new SubjectFieldEditable(s, this, this.courseController);
            subjectFieldViewables.add(subjectFieldEditable);
            hboxDisplayers.get(term-1).getChildren().add(subjectFieldEditable);
        }

        vbox_displayer.getChildren().addAll(hboxDisplayers);
        generateTermBarsView();
        System.out.println("Finishes");
    }

    private void generateTermBarsView(){
        if (this.subjects.size() == 0) return;
        //divide screen in nTerms horizontal bars
        //we count on having at least one hboxDisplayer
        int i = 2;
        for (SubjectHBox subjectHBox : hboxDisplayers){
            if (i % 2 == 0){
                subjectHBox.setId("paint_one");
            }else{
                subjectHBox.setId("paint_other");
            }
            Label label = new Label("Term " + (i-1));
            label.setId("label_term");
            subjectHBox.getChildren().add(0,label);
            Button button = new AddSubjectButton("+", i-1, this);
            subjectHBox.getChildren().add(button);
            i++;
        }
    }

    public void updateSubjects(){
        List<Subject> subjects1 = PersistenceHandler.getInstance().getSubjects(this.getCourse().getCourseId());
        this.subjects = FXCollections.observableList(subjects1);
        generateSubjectTreeViewEdit();
    }

    //Methods for updating buttons for editing subject, depending on whether they are clicked or not

    public void SubjectFieldEditableSelect(SubjectFieldEditable subjectFieldEditable){
        if (SubjectFieldEditable_Selected != null) {
            SubjectFieldEditable_Selected.deselect();
        }
        this.SubjectFieldEditable_Selected = subjectFieldEditable;
        this.preparePane();
        this.addButtonsEditSubject();
    }

    private void preparePane(){
        apane_subject.setVisible(true);
        this.label_name_subject.setText(this.SubjectFieldEditable_Selected.getSubjectName());

    }
    public void SubjectFieldEditableDeselect(){
        if (SubjectFieldEditable_Selected != null) {
            SubjectFieldEditable_Selected.deselect();
            SubjectFieldEditable_Selected = null;
        }
        this.apane_subject.setVisible(false);
    }

    private void addButtonsEditSubject(){

    }

    private void removeButtonsEditSubject(){

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

    public List<SubjectFieldViewable> getSubjectFieldViewables(){
        return this.subjectFieldViewables;
    }

    //Methods for updating or deleting a subject
    public void renameSubject(){
        String subjectName = textfield_name_subject.getText();
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
        PersistenceHandler.getInstance().renameSubject(this.SubjectFieldEditable_Selected.subject, subjectName);
        this.textfield_name_subject.clear();
        this.apane_subject.setVisible(false);
        this.updateSubjects();
    }

    private void setUIwarningsVisible(boolean value){
        this.label_warnings.setVisible(value);
    }
    public void removeSubject(){
        PersistenceHandler.getInstance().removeSubject(this.SubjectFieldEditable_Selected.subject);
        this.apane_subject.setVisible(false);
        this.updateSubjects();
    }

    //Methods for moving a subject vertically or horizontally in the subject tree
    public void moveSubjectUp(){
        Subject subject = this.SubjectFieldEditable_Selected.subject;
        if (subject.getSubjTerm() == 1) return; //last
        PersistenceHandler.getInstance().changeSubjectTerm(subject, subject.getSubjTerm()-1);
        finalizeMoveSubject();
    }

    public void moveSubjectDown(){
        Subject subject = this.SubjectFieldEditable_Selected.subject;
        if (subject.getSubjTerm() == this.course.getnTerms()) return; //last
        PersistenceHandler.getInstance().changeSubjectTerm(subject, subject.getSubjTerm()+1);
        finalizeMoveSubject();
    }

    public void moveSubjectLeft(){
        Subject subject = this.SubjectFieldEditable_Selected.subject;
        PersistenceHandler.getInstance().changeSubjectOrderNumber(subject, subject.getOrderNumber()-1);
        finalizeMoveSubject();
    }

    public void moveSubjectRight(){
        Subject subject = this.SubjectFieldEditable_Selected.subject;
        PersistenceHandler.getInstance().changeSubjectOrderNumber(subject, subject.getOrderNumber()+1);
        finalizeMoveSubject();
    }

    private void finalizeMoveSubject(){
        this.SubjectFieldEditable_Selected.deselect();
        this.SubjectFieldEditable_Selected = null;
        this.apane_subject.setVisible(false);
        this.updateSubjects();
    }

    //For editing subject relationships (parent and child pac)
    public void editPACs(){
        if (PAC_open) return;
        PAC_open = true;
        FXMLLoader loader = loadNewWindow("editSubject_pacs.fxml");
        EditSubjectPACsController controller = loader.getController();
        controller.initializeController(this.SubjectFieldEditable_Selected.subject, this);
    }

    public void closePAC(){
        PAC_open = false;
    }
}

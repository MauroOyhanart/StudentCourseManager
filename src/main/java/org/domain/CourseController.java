package org.domain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;
import persistence.PersistenceHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CourseController{

    private Stage stage;
    @FXML
    Label label_notices;
    @FXML
    Label course_name_label_title;

    @FXML
    Button button_add_course;
    @FXML
    ComboBox<String> cBox_courses;
    @FXML
    Button edit_course_button;

    @FXML
    ScrollPane scroll_pane_displayer;

    @FXML
    VBox vbox_displayer;

    private final List<String> courseNames = new ArrayList<>();

    private final ObservableList<String> courseNamesObservable = FXCollections.observableList(courseNames);

    private List<SubjectFieldViewable> subjectFieldViewables = new ArrayList<>();

    private ObservableList<SubjectHBox> hBoxDisplayers;

    //Initializations
    public void initializeController(Stage stage){
        this.stage = stage;
        this.initializeUI();
        //Query db for courses
        List<Course> courses = PersistenceHandler.getInstance().getCourses();
        //ComboBox item display
        this.prepareCoursesDisplay();
        this.prepareEditButton();
        if (!courses.isEmpty()){
            //Get the names of the courses
            {
                for (Course c : courses){
                    courseNamesObservable.add(c.getName());
                }
            }
        }else{
            showMissingCourseUI(true);
            showPresentCourseUI(false);
        }
    }

    public void reInitializeController(Course course, Stage stage){
        initializeController(stage);
        this.cBox_courses.setValue(course.getName()); //Lets see if this triggers the view generation
        showEditCourseUI(true);
        course_name_label_title.setText("My Course" + ": " + cBox_courses.getSelectionModel().getSelectedItem());
        loadCourse(course);
    }


    private void prepareCoursesDisplay(){
        //Attach comboBox to observable list
        cBox_courses.setItems(courseNamesObservable);
        //Attach event to comboBox
        cBox_courses.setOnAction(new EventHandler<ActionEvent>(){ //TODO: add the action from the fxml form, and not here.
            @Override
            public void handle(ActionEvent actionEvent) {
                String selectedOption = cBox_courses.getValue();
                course_name_label_title.setText("My Course" + ": " + cBox_courses.getSelectionModel().getSelectedItem());
                Course course = PersistenceHandler.getInstance().getCourse(selectedOption);
                if (course != null) {
                    loadCourse(course);
                } else{
                    //TODO: Some sort of UI displaying that there was an error
                }
            }
        });
    }

    private void prepareEditButton(){
        this.edit_course_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                String courseName = cBox_courses.getValue();
                toEditCourse(courseName);
            }
        });
    }

    //Functionality
    public void addNewCourse(String name){
        //this.courseNames.add(name);
        this.courseNamesObservable.add(name);
        this.showPresentCourseUI(true);
        this.showMissingCourseUI(false);
    }

    private void loadCourse(Course course){
        //Enable UI for editing a course
        showEditCourseUI(true);
        //Load subScene in this scene, or pane in this scene, containing full course visualization. This is a core feature
        initializeSubjectTreeView();
    }

    private void initializeSubjectTreeView(){
        vbox_displayer.getChildren().clear();
        subjectFieldViewables.clear();
        List<Subject> subjects = PersistenceHandler.getInstance().getSubjects(this.cBox_courses.getSelectionModel().getSelectedItem());
        if (subjects.size()<=0) return;
        //We have the subjects, and the vbox where they should be in.
        //We generate 1 hbox for every term. Populate the hboxs with the subjects, using SubjectFieldEditable as instantiatable item.
        //clear displayer
        //generate 1 hbox for every term
        System.out.println("Displays course");
        List<SubjectHBox> subjectHBoxes = new ArrayList<>();
        for (int i = 0; i < subjects.get(subjects.size()-1).getSubjTerm(); i++){ //for every term, from 0 to the last term (they are ordered by term)
            SubjectHBox subjectHBox = new SubjectHBox();
            subjectHBoxes.add(subjectHBox);
        }
        hBoxDisplayers = FXCollections.observableList(subjectHBoxes);
        //populate the hboxs with the subjects
        for (Subject s: subjects){
            int term = s.getSubjTerm();
            SubjectFieldViewable subjectFieldViewable = new SubjectFieldViewable(s, this);
            subjectFieldViewables.add(subjectFieldViewable);
            subjectHBoxes.get(term-1).getChildren().add(subjectFieldViewable);
        }

        vbox_displayer.getChildren().addAll(subjectHBoxes);
        generateTermBarsView();
    }

    private void generateTermBarsView(){
        int i = 2;
        for (SubjectHBox subjectHBox : hBoxDisplayers){
            if (i % 2 == 0){
                subjectHBox.setId("paint_one");
            }else{
                subjectHBox.setId("paint_other");
            }
            Label label = new Label("Term " + (i-1));
            label.setId("label_term");
            subjectHBox.getChildren().add(0,label);
            i++;
        }
    }

    //Routing
    @FXML
    private void goBackToMainMenu() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("main_page.fxml"));
        try{
            Parent root = loader.load();
            //Set scene
            Scene scene = new Scene(root);
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

    public void toAddCourse(){
        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("addCourse_window.fxml"));
        try{
            Parent root = loader.load();
            //Set scene
            Scene scene = new Scene(root);
            AddCourseController addCourseController = loader.getController();
            if (addCourseController !=null) {
                addCourseController.initializeController(this, stage);
            }
            //Load scene to stage
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("main_styles.css").toExternalForm());
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void toEditCourse(String name){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("my_course_subjectTree_edit.fxml"));
        try{
            Parent root = loader.load();
            //Set scene
            Scene scene = new Scene(root);
            SubjectTreeEditController subjectTreeEditController= loader.getController();
            if (subjectTreeEditController !=null) {
                subjectTreeEditController.initializeController(this, stage, name);
            }
            //Load scene to stage
            stage.setScene(scene);
            scene.getStylesheets().add(getClass().getResource("main_styles.css").toExternalForm());
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //UI
    private void initializeUI(){
        this.stage.setResizable(false);
        showMissingCourseUI(false);
        showPresentCourseUI(true);
        //there's not going to be any course chosen yet
        showEditCourseUI(false);
    }

    private void showMissingCourseUI(boolean value){
        label_notices.setVisible(value);
    }

    private void showPresentCourseUI(boolean value){
        cBox_courses.setVisible(value);
    }
    private void showEditCourseUI(boolean value){
        edit_course_button.setVisible(value);
    }

    public List<SubjectFieldViewable> getSubjectFieldViewables() {
        return subjectFieldViewables;
    }


}



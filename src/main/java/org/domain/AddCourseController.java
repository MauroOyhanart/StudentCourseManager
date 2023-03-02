package org.domain;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import persistence.PersistenceHandler;

import java.util.ArrayList;
import java.util.List;

public class AddCourseController {
    //Fields
    private CourseController courseController;
    private Stage stage;

    @FXML
    TextField text_name;

    @FXML
    ComboBox<Integer> cbox_terms;

    @FXML
    Label label_warnings;


    //Constructors

    //Methods
    public void initializeController(CourseController courseController, Stage stage){
        setVisibleUIwarnings(false);
        this.setCourseController(courseController);
        this.stage = stage;
        this.stage.setAlwaysOnTop(true);
        if (cbox_terms != null){
            List<Integer> items = new ArrayList<>();
            for (int i = 1; i <= 12; i++)
                items.add(i);
            cbox_terms.getItems().addAll(items);
        }
    }

    @FXML
    public void createCourse(){
        String name = text_name.getText();
        Integer nTerms = cbox_terms.getValue();
        //input checks
        if (name.isBlank()){
            System.out.println("name cannot be blank");
            setVisibleUIwarnings(true);
            label_warnings.setText("Name cannot be blank");
            return;
        }
        if (name.length()<3){
            System.out.println("name is too short");
            setVisibleUIwarnings(true);
            label_warnings.setText("Name is too short");
            return;
        }
        if (nTerms == null){
            System.out.println("nTerms cannot be null");
            setVisibleUIwarnings(true);
            label_warnings.setText("Select number of terms");
            return;
        }
        setVisibleUIwarnings(false);
        //store the course in the database
        Course course = new Course(SCM_Utils.capitalize(name), nTerms);
        PersistenceHandler.getInstance().createCourse(course);
        //let course controller know there is a new course available
        courseController.addNewCourse(course.getName());
        exitWindow();
        System.out.println("create course via button: <"+name+", "+nTerms+" terms>");
    }

    @FXML
    public void exitWindow(){
        stage.close();
    }

    private void setVisibleUIwarnings(boolean value){
        label_warnings.setVisible(value);
    }


    //Getters and setters (also methods)
    public CourseController getCourseController() {
        return courseController;
    }

    public void setCourseController(CourseController courseController) {
        this.courseController = courseController;
    }


}

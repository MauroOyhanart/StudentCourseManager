package org.domain;


import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/** A subject field viewable that can open up a menu for editing course relationships. Can also reorder a subject in the tree **/
public class SubjectFieldEditable extends SubjectFieldViewable {
    //Fields
    private final SubjectTreeEditController subjectTreeEditController;
    List<Object> buttonObjects = new ArrayList<>();

    private boolean clicked = false;

    //Constructor
    public SubjectFieldEditable(Subject subject, SubjectTreeEditController subjectTreeEditController, CourseController courseController) {
        super(subject, courseController);
        this.subjectTreeEditController = subjectTreeEditController;
        this.setOnMouseClicked(event -> {
            if (!clicked ){
                this.subjectTreeEditController.SubjectFieldEditableDeselect();
                this.showSelected();
                this.subjectTreeEditController.SubjectFieldEditableSelect(this);
            }
            else {
                this.subjectTreeEditController.SubjectFieldEditableDeselect();
                this.deselect();
            }
        });
    }

    //Methods
    private void showSelected(){
        this.getStyleClass().add("selected_id"); /* check */
        clicked = true;
    }

    public void deselect(){
        this.getStyleClass().remove("selected_id"); /* check */
        clicked = false;
    }

    //Overrided methods for displaying.
    @Override
    protected List<SubjectFieldViewable> getListViewables(){
        return this.subjectTreeEditController.getSubjectFieldViewables();
    }



}

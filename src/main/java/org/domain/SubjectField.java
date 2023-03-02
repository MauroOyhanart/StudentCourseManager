package org.domain;


import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

/** Pane item that contains a Label with a Subject name and goes inside a SubjectHBox. **/
public class SubjectField extends Pane {

    //Fields
    protected final Subject subject;
    protected final Label label;

    protected final String asleepId = "subjectfieldpaneid";

    //Constructors
    public SubjectField(Subject subject){
        this.subject = subject;
        this.label = new Label();
        this.getChildren().add(label);
        this.initializeProperties();
    }

    //Methods
    private void initializeProperties(){
        this.label.setText(subject.getSubjName());
        this.label.setFont(new Font(12));
        this.label.setId("subjectfieldlabelid");
        this.setId(asleepId);
        // Set the layout position of the Label to center it within the Pane
        label.setLayoutX(15);
        label.setLayoutY(10);
        this.label.setWrapText(true);
    }

    //Getters
    public String getSubjectName(){
        return subject.getSubjName();
    }

    public int getSubjectTerm(){
        return subject.getSubjTerm();
    }

    public int getSubjectOrderNumber(){
        return subject.getOrderNumber();
    }
}

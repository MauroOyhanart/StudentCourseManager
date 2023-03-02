package org.domain;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;

/** Extension of a JavaFX.HBox that goes inside a Subject Tree View **/
public class SubjectHBox extends HBox {
    public SubjectHBox(){
        this.initializeProperties();
    }

    private void initializeProperties(){
        this.setAlignment(Pos.CENTER);
        Insets padding = new Insets(10, 10, 10, 10);
        this.setPadding(padding);
        this.setSpacing(10);
    }
}

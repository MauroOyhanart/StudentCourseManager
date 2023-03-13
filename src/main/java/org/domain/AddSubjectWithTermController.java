package org.domain;

public class AddSubjectWithTermController extends AddSubjectController{
    private int term;
    public AddSubjectWithTermController(){

    }

    public void initializeController(SubjectTreeEditController subjectTreeEditController, int term){
        super.initializeController(subjectTreeEditController);
        this.term = term;
    }

    @Override
    protected int calculateTerm(){
        return this.term;
    }
}

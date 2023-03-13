package org.domain;

public class Subject {
    //Fields
    private int courseId, subjId, subjTerm, orderNumber;
    private String subjName;

    //Constructors


    public Subject(int courseId, int subjId, String subjName, int subjTerm, int orderNumber) {
        this.courseId = courseId;
        this.subjId = subjId;
        this.subjName = subjName;
        this.subjTerm = subjTerm;
        this.orderNumber = orderNumber;
    }

    //Methods
    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getSubjId() {
        return subjId;
    }

    public void setSubjId(int subjId) {
        this.subjId = subjId;
    }

    public String getSubjName() {
        return subjName;
    }

    public void setSubjName(String subjName) {
        this.subjName = subjName;
    }

    public int getSubjTerm() {
        return subjTerm;
    }

    public void setSubjTerm(int subjTerm) {
        this.subjTerm = subjTerm;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(int orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void printSubject(){
        System.out.println("subject <course "+courseId+", subjId" +subjId+ ", "+ subjName + ", term "+subjTerm+", order "+ orderNumber+ ">");
    }
}

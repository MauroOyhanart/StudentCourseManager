package org.domain;

public class Course {
    //Fields
    private int courseId;
    private String courseName;
    private int nTerms;

    //Constructors
    public Course(int c_id, String c_name, int n_terms) {
        this.courseId = c_id;
        this.courseName = c_name;
        this.nTerms = n_terms;
    }

    public Course(String c_name, int n_terms) {
        this.courseId = -1;
        this.courseName = c_name;
        this.nTerms = n_terms;
    }

    //Methods
    public String getName() {
        return courseName;
    }

    public void setName(String name) {
        this.courseName = name;
    }

    public int getnTerms() {
        return nTerms;
    }

    public void setnTerms(int nTerms) {
        this.nTerms = nTerms;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
}

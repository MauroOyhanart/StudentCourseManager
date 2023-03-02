package org.domain;

import persistence.PersistenceHandler;

import java.util.List;

/** A subject field for pure display, that alerts other fields when it is hovered, to perform a colorful effect **/
public class SubjectFieldViewable extends SubjectField{

    private CourseController courseController;
    private String neededId = "needed_id";
    private String allowedId = "allowed_id";
    public SubjectFieldViewable(Subject subject, CourseController courseController){
        super(subject);
        this.courseController = courseController;
        this.setOnMouseEntered(event -> {
            this.awakeEveryNeededSubject();
            this.awakeEveryAllowedSubject();
        });
        this.setOnMouseExited(event -> {
            this.asleepEverySubject();
        });
    }

    private void awakeEveryNeededSubject(){
        List<Subject> neededSubjects = PersistenceHandler.getInstance().getSubjectNeeds(this.subject);
        for (Subject s : neededSubjects){
            SubjectFieldViewable subjectFieldViewable = this.findSubjectFieldViewableInCourseController(s);
            if (subjectFieldViewable == null) continue;
            subjectFieldViewable.awakeAsNeeded();
        }
    }

    private void awakeEveryAllowedSubject(){
        List<Subject> allowedSubjects = PersistenceHandler.getInstance().getSubjectAllows(this.subject);
        for (Subject s : allowedSubjects){
            SubjectFieldViewable subjectFieldViewable = this.findSubjectFieldViewableInCourseController(s);
            if (subjectFieldViewable == null) continue;
            subjectFieldViewable.awakeAsAllowed();
        }
    }

    private void asleepEverySubject(){
        for (SubjectFieldViewable subjectFieldViewable : courseController.getSubjectFieldViewables()){
            subjectFieldViewable.asleep();
        }
    }

    public void awakeAsNeeded(){
        this.setId(this.neededId);
        awakeEveryNeededSubject();
    }

    public void awakeAsAllowed(){
        this.setId(this.allowedId);
        awakeEveryAllowedSubject();
    }

    public void asleep(){
        this.setId(this.asleepId);
    }

    private SubjectFieldViewable findSubjectFieldViewableInCourseController(Subject subject){
        for (SubjectFieldViewable subjectFieldViewable : courseController.getSubjectFieldViewables()){
            if (subjectFieldViewable.getSubjectName().equals(subject.getSubjName())){
                return subjectFieldViewable;
            }
        }
        return null;
    }

}

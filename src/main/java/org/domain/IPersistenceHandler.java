package org.domain;

import java.util.List;

public interface IPersistenceHandler {
    public List<Course> getCourses();

    public List<Subject> getSubjects(int courseId);

    public void createCourse(Course course);
}

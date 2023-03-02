package persistence;

import org.domain.Course;
import org.domain.IPersistenceHandler;
import org.domain.Subject;

import java.sql.*;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
public class PersistenceHandler implements IPersistenceHandler {
    private static PersistenceHandler instance;
    private static Connection connection = null;

    private PersistenceHandler(){

    }

    public static PersistenceHandler getInstance(){
        if (instance == null) {
            instance = new PersistenceHandler();
        }
        return instance;
    }

    public static void initializeComponents(){
        initializeSQLDatabase();
    }

     private static void initializeSQLDatabase(){
        try {
            // create a database connection
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);  // set timeout to 30 sec.
            initializeDBItems(statement);
        }
        catch(SQLException e) {
            // if the error message is "out of memory",
            // it probably means no database file is found
            System.err.println(e.getMessage());
        }
        finally {
            try {
                if(connection != null)
                    connection.close();
            } catch(SQLException e) {
                // connection close failed.
                e.printStackTrace();
            }
        }
    }

    /** Retrieves all courses from the database **/
    public List<Course> getCourses(){
        List<Course> courses = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            //query
            ResultSet rs = statement.executeQuery("select * from course;");
            while (rs.next()){
                int id = rs.getInt("course_id");
                String name = rs.getString("course_name");
                int terms = rs.getInt("n_terms");
                courses.add(new Course(id, name, terms));
            }
            return courses;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if(connection != null)
                    connection.close();
            } catch(SQLException e) {
                // connection close failed.
                e.printStackTrace();
            }
        }
        return courses;
    }

    /** Gets all the subjects in the database for a given courseId **/
    public List<Subject> getSubjects(int courseId){
        List<Subject> subjects = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            //query
            ResultSet rs = statement.executeQuery("select * from subject where course_id="+courseId+";");
            while (rs.next()){
                int subjId = rs.getInt("subj_id");
                String subjName = rs.getString("subj_name");
                int subjTerm = rs.getInt("subj_term");
                int orderNumber = rs.getInt("orderNumber");
                subjects.add(new Subject(courseId, subjId, subjName, subjTerm, orderNumber));
            }
            orderByTermAndOrderNumber(subjects);
            return subjects;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if(connection != null)
                    connection.close();
            } catch(SQLException e) {
                // connection close failed.
                e.printStackTrace();
            }
        }
        return subjects;
    }

    /** Gets all the subjects in the database for a given courseId **/
    public List<Subject> getSubjects(String courseName){
        Course course = getCourse(courseName);
        if (course == null) return null;
        return this.getSubjects(course.getCourseId());
    }

    /** Creates and stores a course in the database. Utilizes a Course parameter with name and nTerms defined in a form. **/
    public void createCourse(Course course){
        //we assume course_id will come as -1, since the database will choose a number for storage
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            //query
            String query = "insert into course(course_name, n_terms) values ('"+course.getName()+"', "+course.getnTerms()+");";
            System.out.println(query);
            statement.executeUpdate(query);
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if(connection != null)
                    connection.close();
            } catch(SQLException e) {
                // connection close failed.
                e.printStackTrace();
            }
        }
    }

    /** Creates and stores a subject in the database. Utilizes a Subject parameter. **/
    public void createSubject(Subject subject){
        //we assume subjId and orderNumber will come as -1, since the database will choose those numbers
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            //query. The 'select coalesce' is to get the next subj_id. Cannot autoincrement because pk is compound.
            //Also, the subject is created with orderNumber = 0;
            String query = "insert into subject(course_id, subj_id, subj_name, subj_term, orderNumber)" +
                    " values ("+subject.getCourseId()+
                    ", (select coalesce(max(subj_id), 0) + 1 from subject where course_id= "+subject.getCourseId()+"), "+
                    "'"+subject.getSubjName()+"', "+subject.getSubjTerm()+", 0);";
            System.out.println(query);
            statement.executeUpdate(query);
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if(connection != null)
                    connection.close();
            } catch(SQLException e) {
                // connection close failed.
                e.printStackTrace();
            }
        }
    }

    /** Returns a Course(courseId, courseName, nTerms) given a courseName **/
    public Course getCourse(String courseName){
        Course course;
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            //query
            String query = "select * from course where course_name= '"+courseName+"' limit 1;";
            System.out.println("query: " + query);
            ResultSet rs = statement.executeQuery(query);
            int courseId = -1, nTerms = -1;
            while (rs.next()){
                courseId = rs.getInt("course_id");
                nTerms = rs.getInt("n_terms");
            }
            if (courseId == -1) return null; //If there was a problem
            course = new Course(courseId, courseName, nTerms);
            return course;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if(connection != null)
                    connection.close();
            } catch(SQLException e) {
                // connection close failed.
                e.printStackTrace();
            }
        }
        return null;
    }

    /** Finds in the database and returns the parents (what subjects it needs) of a subject.**/
    public List<Subject> getSubjectNeeds(Subject s){
        List<Subject> subjects = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            //query
            ResultSet rs = statement.executeQuery(
                    "select s.* " +
                            "from subject s " +
                            "join parent p on (p.course_par_id=s.course_id and p.subj_par_id=s.subj_id) " +
                            "where p.course_child_id=" +s.getCourseId()+" and p.subj_child_id=" + s.getSubjId() + ";"
            );
            while (rs.next()){
                int courseId = rs.getInt("course_id");
                int subjId = rs.getInt("subj_id");
                String subjName = rs.getString("subj_name");
                int subjTerm = rs.getInt("subj_term");
                int orderNumber = rs.getInt("orderNumber");
                subjects.add(new Subject(courseId, subjId, subjName, subjTerm, orderNumber));
            }
            orderByTermAndOrderNumber(subjects);
            return subjects;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if(connection != null)
                    connection.close();
            } catch(SQLException e) {
                // connection close failed.
                e.printStackTrace();
            }
        }
        return subjects;
    }

    /** Finds in the database and returns the children (what subjects it allows) of a subject.**/
    public List<Subject> getSubjectAllows(Subject s){
        List<Subject> subjects = new ArrayList<>();
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            //query
            ResultSet rs = statement.executeQuery(
                    "select s.* " +
                            "from subject s " +
                            "join parent p on (p.course_child_id=s.course_id and p.subj_child_id=s.subj_id) " +
                            "where p.course_par_id=" +s.getCourseId()+" and p.subj_par_id=" + s.getSubjId() + ";"
            );
            while (rs.next()){
                int courseId = rs.getInt("course_id");
                int subjId = rs.getInt("subj_id");
                String subjName = rs.getString("subj_name");
                int subjTerm = rs.getInt("subj_term");
                int orderNumber = rs.getInt("orderNumber");
                subjects.add(new Subject(courseId, subjId, subjName, subjTerm, orderNumber));
            }
            orderByTermAndOrderNumber(subjects);
            return subjects;
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if(connection != null)
                    connection.close();
            } catch(SQLException e) {
                // connection close failed.
                e.printStackTrace();
            }
        }
        return subjects;
    }

    public void notifySubjectNeed(Subject subject, String neededName){
        Subject subjectNeeded = this.getSubject(neededName);
        if (subjectNeeded==null) return; //Could not be found
        this.addSubjectParentChange(subject, subjectNeeded, true); //true for need
    }

    public void notifySubjectAllow(Subject subject, String allowsName){
        Subject subjectAllows = this.getSubject(allowsName);
        if (subjectAllows==null) return; //Could not be found
        this.addSubjectParentChange(subject, subjectAllows, false); //false for allow
    }

    private void addSubjectParentChange(Subject subject, Subject theOther, boolean need){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            //query
            if (need) { //subject needs the other
                statement.executeUpdate(
                        "insert into parent values (" +
                                theOther.getCourseId() + ", " + theOther.getSubjId() + ", " +
                                subject.getCourseId() + ", " + subject.getSubjId() + ");"
                );
            }else{ //subject allows the other
                statement.executeUpdate(
                        "insert into parent values (" +
                                subject.getCourseId() + ", " + subject.getSubjId() + ", " +
                                theOther.getCourseId() + ", " + theOther.getSubjId() + ");"
                );
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if(connection != null)
                    connection.close();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void notifyNotNeeds(Subject subject, String subjectNotNeeded) {
        Subject theSubject = this.getSubject(subjectNotNeeded);
        if (theSubject == null) return;
        revertSubjectParentChange(subject, theSubject, true);
    }

    public void notifyNotAllows(Subject subject, String subjectNotAllowed) {
        Subject theSubject = this.getSubject(subjectNotAllowed);
        if (theSubject == null) return;
        revertSubjectParentChange(subject, theSubject, false);
    }

    private void revertSubjectParentChange(Subject subject, Subject theOther, boolean need){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            //query
            if (need) { //subject does not need the other anymore
                statement.executeUpdate(
                        "delete from parent where (course_par_id=" +
                                theOther.getCourseId() + " and subj_par_id=" + theOther.getSubjId() + " and course_child_id= " +
                                subject.getCourseId() + " and subj_child_id= " + subject.getSubjId() + ");"
                );
            }else{ //subject allows the other
                statement.executeUpdate(
                        "delete from parent where (course_par_id=" +
                                subject.getCourseId() + " and subj_par_id= " + subject.getSubjId() + " and course_child_id= " +
                                theOther.getCourseId() + " and subj_child_id= " + theOther.getSubjId() + ");"
                );
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if(connection != null)
                    connection.close();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public Subject getSubject(String name){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            //query
            ResultSet rs = statement.executeQuery(
                    "select s.* " +
                            "from subject s " +
                            "where s.subj_name='"+name+"' limit 1;"
            );
            while (rs.next()){
                int courseId = rs.getInt("course_id");
                int subjId = rs.getInt("subj_id");
                String subjName = rs.getString("subj_name");
                int subjTerm = rs.getInt("subj_term");
                int orderNumber = rs.getInt("orderNumber");
                return new Subject(courseId, subjId, subjName, subjTerm, orderNumber);
            }
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if(connection != null)
                    connection.close();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void removeSubject(Subject subject){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(30);
            //query
            statement.executeUpdate(
                    "delete from subject where course_id="+subject.getCourseId()+" and subj_id="+subject.getSubjId()+";"
            );
            statement.executeUpdate(
                    "delete from parent where ((course_par_id="+subject.getCourseId()+" and subj_par_id="+subject.getSubjId()+")" +
                            "or (course_child_id="+subject.getCourseId()+" and subj_child_id="+subject.getSubjId()+"));"
            );
        } catch(SQLException e) {
            System.err.println(e.getMessage());
        } finally {
            try {
                if(connection != null)
                    connection.close();
            } catch(SQLException e) {
                e.printStackTrace();
            }
        }
    }

    //For initialization and utils
    private static void initializeDBItems(Statement st) throws SQLException{
        //Create tables if not exist: course, subject, parent
        st.executeUpdate("create table if not exists " +
                "course(course_id integer primary key autoincrement, course_name text not null, n_terms integer not null);");
        st.executeUpdate("create table if not exists " +
                "subject(course_id integer, subj_id integer, subj_name text not null, subj_term integer not null, " +
                "orderNumber integer not null, " +
                "primary key (course_id, subj_id), " +
                "foreign key (course_id) references course(course_id) on delete cascade on update cascade);");
        st.executeUpdate("create table if not exists " +
                "parent(course_par_id integer, subj_par_id integer, course_child_id integer, subj_child_id integer, " +
                "primary key(course_par_id, subj_par_id, course_child_id, subj_child_id), " +
                "foreign key(course_par_id, subj_par_id) references subject(course_id, subj_id) on delete cascade on update cascade, " +
                "foreign key(course_child_id, subj_child_id) references subject(course_id, subj_id) on delete cascade on update cascade );");
    }

    /** Auxiliar method to return subjects ordered by term and order number **/
    private void orderByTermAndOrderNumber(List<Subject> subjects){
        class SubjectComparator implements Comparator<Subject> {
            @Override
            public int compare(Subject s1, Subject s2){
                int result = Integer.compare(s1.getSubjTerm(), s2.getSubjTerm());
                if (result == 0){ //if they are equal
                    result = Integer.compare(s1.getOrderNumber(), s2.getOrderNumber());
                }
                return result;
            }
        }
        subjects.sort(new SubjectComparator());
    }
}

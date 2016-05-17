/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch;

import java.util.ArrayList;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author mbp
 */
public class Course {

    private final StringProperty name;
    private final BooleanProperty excepted;
    private final IntegerProperty number;
    private final IntegerProperty studentNumber;
    private String collegeName;
    private boolean scheduled = false;
    private TimeSlot session;
    private int degree;
    private final ArrayList<Integer> sections = new ArrayList();
    private final ArrayList<Student> students = new ArrayList();



    public Course(int Number, String Name, int studentNumber, boolean excepted) {
        this.number = new SimpleIntegerProperty(Number);
        this.name = new SimpleStringProperty(Name);
        this.studentNumber = new SimpleIntegerProperty(studentNumber);
        this.excepted = new SimpleBooleanProperty(excepted);
    }

    public void setSession(TimeSlot session) {
        this.session = session;
    }
    public TimeSlot getSession() {
        return session;
    }


    public int getDegree() {
        return degree;
    }

    public ArrayList<Integer> getSections() {
        return sections;
    }

    public ArrayList<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        students.add(student);
    }

    public void addSection(int section) {
        sections.add(section);
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int getNumber() {
        return number.get();
    }

    public void setNumber(int Number) {
        this.number.set(Number);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String Name) {
        this.name.set(Name);
    }

    public boolean isExcepted() {
        return excepted.get();
    }

    public void setExcepted(boolean excepted) {
        this.excepted.set(excepted);
    }

    public String getCollegeName() {
        return collegeName;
    }

    public void setCollegeName(String collegeName) {
        this.collegeName = collegeName;
    }

    public boolean isScheduled() {
        return scheduled;
    }

    public void setScheduled(boolean scheduled) {
        this.scheduled = scheduled;
    }

    public int getStudentNumber() {
        return studentNumber.get();
    }

    public void setStudentNumber(int StudentNumber) {
        this.studentNumber.set(StudentNumber);
    }

    public IntegerProperty numberProperty() {
        return number;
    }

    public IntegerProperty studentNumberProperty() {
        return studentNumber;
    }

    public StringProperty nameProperty() {
        return name;
    }

    public BooleanProperty exceptedProperty() {
        return excepted;
    }
    public IntegerProperty sessionProperty(){
       return  new SimpleIntegerProperty(session.getSlot());
    }

}

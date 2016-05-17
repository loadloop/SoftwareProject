package ch;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.ArrayList;

/**
 *
 * @author mbp
 */
public class Student {

    private final String name;
    private final College college;
    private final int number;
    private final ArrayList<Course> courses = new ArrayList<Course>();
    // public ArrayList<Integer> section = new ArrayList();
    //public ArrayList<String> hall = new ArrayList();
    //  public ArrayList<Boolean> ready = new ArrayList();

    

    public int getMax() {
        return college.getNum_of_exam();
    }

    public int getNumber() {
        return number;
    }

    public void setCourse(Course c) {
        courses.add(c);
    }

    public ArrayList<Course> getCourses() {
        return courses;
    }

    public String getName() {
        return name;
    }

    public College getCollegeName() {
        return college;
    }

    public Student(int number, String name, College college) {
        this.number = number;
        this.name = name;
        this.college = college;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Redab
 */
public class College {
    private final  IntegerProperty college_num;
    private final  IntegerProperty num_of_exam;
    private final  StringProperty college_name;

    public College(int college_num, String college_name, int num_of_exam) {
                this.college_num = new SimpleIntegerProperty(college_num);
                this.num_of_exam = new SimpleIntegerProperty(num_of_exam);
        this.college_name = new SimpleStringProperty(college_name);

    }

    public int getCollege_num() {
        return college_num.get();
    }

    public void setCollege_num(int college_num) {
        this.college_num.set(college_num);
    }

    public String getCollege_name() {
        return college_name.get();
    }

    public void setCollege_name(String college_name) {
        this.college_name.set(college_name);
    }

    public int getNum_of_exam() {
        return num_of_exam.get();
    }

    public void setNum_of_exam(int num_of_exam) {
        this.num_of_exam.set(num_of_exam);
    }
   public IntegerProperty College_numProperty() {
        return college_num;
    }
   public IntegerProperty Num_of_examProperty() {
        return num_of_exam;
    }

    public StringProperty College_nameProperty() {
        return college_name;
    }
    
}

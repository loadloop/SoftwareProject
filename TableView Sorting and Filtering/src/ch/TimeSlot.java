/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch;

import java.util.ArrayList;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author Dark_
 *
 */
public class TimeSlot {

   private final int day;
   private final int slot;
   private  int remainingCapcity;
   private ExamDate examDay;
   private final  ArrayList <Course> Scheduled =new ArrayList();
   public TimeSlot(int capacity,int day,ExamDate examDay ,int slot){
        this.day=day;
        this.slot=slot;
        remainingCapcity=capacity;
        this.examDay=examDay;
    }
    public int getDay(){
        return day;
    }
    public void addScheduled(Course c){
        Scheduled.add(c);
    }
    public ArrayList <Course> getScheduled(){
        return Scheduled;
    }
      public int getSlot(){
        return slot;
    }
    public int getRemainingCapcity(){
        return remainingCapcity;
    }
    public void setRemainingCapcity(int capcity){
        this.remainingCapcity-=capcity;
    }
    public ExamDate getExamDay(){
       return examDay;
    }
    public StringProperty getNameProperty(){
        String[] sessionName={"First Session","Second Session","Third Session"};
        return  new SimpleStringProperty(sessionName[slot]);
    }
        public String getName(){
        String[] sessionName={"First Session","Second Session","Third Session"};
        return sessionName[slot];
    }
}



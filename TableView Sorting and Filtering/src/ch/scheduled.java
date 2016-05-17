/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author mbp
 */
public class scheduled {
    Data data;
public  ArrayList<Student> student=new ArrayList<>();//this will store all student 

public  ArrayList<Course> Courses;//this will store all courses
public  ArrayList<ExamDate> examDate;//this will store all courses
public   int graph[][];

  public scheduled(){
  refresh();
  }
  private void refresh(){
  data=new Data();
  student=data.getStudents();
  examDate=data.getPureExam();
  Courses=data.getCourses();
  }
    public  void buildGraph(ArrayList<Course> courses){
        graph=new int[Courses.size()][Courses.size()];
        for(int i=0;i<Courses.size();++i){
            Course row=Courses.get(i);
            for(int j=0;j<row.getStudents().size();++j){
               
              Student  stu = row.getStudents().get(j);
            
                for(int m=0;m<stu.getCourses().size();++m){
                    Course cor=stu.getCourses().get(m);
                   
                    int col=Courses.indexOf(cor);
                    if(col==i) continue;
                    graph[i][col]++;
                }
               
            }
        }
    }
   public  boolean ConstraintCheck(Course Exam,int day){
 
        for(int i=0;i<Exam.getStudents().size();++i)
        {
            Student x=Exam.getStudents().get(i);
            int count=0;
          for(int j=0;j<x.getCourses().size();++j)
            {
              if(x.getCourses().get(j).isScheduled())
              {
              if(x.getCourses().get(j).getSession().getDay()==day)
               {
                  count=count+1;
                
                  if(count+1>x.getMax())return false;
                  
               }
              }
          }
    }
    return true;
    }
    public  void output() throws FileNotFoundException, IOException{
    Workbook wb = new HSSFWorkbook();
    Sheet sheet = wb.createSheet("Schedule");
  
  int count=0;
  for(int i=0;i<Courses.size();++i){
     
      Course temp=Courses.get(i);
      if(temp.isScheduled()==true){
       
      Row row = sheet.createRow((short)count);
       row.createCell(0).setCellValue(temp.getNumber());
      
       row.createCell(2).setCellValue(temp.getSession().getDay());
       row.createCell(3).setCellValue(temp.getSession().getSlot());
       ++count;
      }
  }
    FileOutputStream fileOut = new FileOutputStream("Final$Exam.xls");
    wb.write(fileOut);
    fileOut.close();
      }
    public  boolean checkSlot(ArrayList<Course> list,int index){
        for(int i=0;i<list.size();++i){
                int listind=Courses.indexOf(list.get(i));
                if(graph[index][listind]!=0)
                    return false;
        }
        return true;
    }
    
public  int numberOfTwoExam(){
        int result=0;
        for(int i=0;i<Courses.size();++i){
            if(Courses.get(i).isScheduled()==true){
                for(int j=i+1;j<Courses.size();++j){
                    if(i==j)continue;
                    if(Courses.get(j).isScheduled()==true){
                        if(Courses.get(j).getSession().getDay()==Courses.get(i).getSession().getDay()){
                            if(graph[i][j]!=0){
                               
                            result+=graph[i][j];
                        }
                        }
                    }
                }
            }
        }
        return result;
    }
public void start() throws IOException{
buildGraph(Courses);


Random rand=new Random();
int courseIndex,day,slot;
int  numberOfSchedules=0;
TimeSlot[][] date=new TimeSlot[examDate.size()][3];
int Best=Integer.MAX_VALUE;

 while (numberOfSchedules<5000){

  for(int i=0;i<examDate.size();++i){
           date[i][0]=new TimeSlot(2000,i,examDate.get(i),0);
           date[i][1]=new TimeSlot(2000,i,examDate.get(i),1);
           date[i][2]=new TimeSlot(2000,i,examDate.get(i),2);
       }
  int scheduled=0;
  int itreation=0;
  while(itreation<20000){
    
       courseIndex=rand.nextInt(Courses.size());
           day=rand.nextInt(examDate.size());
           slot=rand.nextInt(3);
           Course temp=Courses.get(courseIndex);
       if(temp.isScheduled()==false)
       {
        
         if(ConstraintCheck(temp,day))
         {
                 if(checkSlot(date[day][slot].getScheduled(),courseIndex))
                 {
                        if(date[day][slot].getRemainingCapcity()>temp.getStudents().size())
                        {
                           
                                 date[day][slot].setRemainingCapcity( temp.getStudents().size());
                                 temp.setSession(date[day][slot]);
                                 temp.setScheduled(true);
                                 date[day][slot].getScheduled().add(temp);
                                 ++scheduled;
                        }
                  }
         }
  
  }
       ++itreation;
  }
int twoExam=numberOfTwoExam();
 if(twoExam<Best &&scheduled>200){
     Best=twoExam;
     output();
     System.out.println("number of courses"+scheduled);
     System.out.println("student"+Best );
     System.out.println("===========");
  }
  ++numberOfSchedules;
  
 this.refresh();

  }

    }
       
 
    
   
    

public static void main(String[] args) throws IOException {
scheduled d= new scheduled();
d.start();
}
}
 


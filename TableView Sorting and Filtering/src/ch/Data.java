/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author jawad
 */
public final class Data {

    private final ObservableList<Course> courseData = FXCollections.<Course>observableArrayList((Course course) -> new Observable[]{course.exceptedProperty()});
    private final ObservableList<Course> exData = FXCollections.<Course>observableArrayList();
    private final ObservableList<ExamDate> dateData = FXCollections.<ExamDate>observableArrayList((ExamDate examDate) -> new Observable[]{examDate.check()});
    private final ObservableList<College> collegeData = FXCollections.<College>observableArrayList();
    private final ArrayList<Student> students= new ArrayList<Student>();;
    private Hashtable<Integer,Course> crsTable=new Hashtable<>();;


    public Data() {
        this.getCollegeData();
        this.getCourseData();
        this.getStudentData();
        this.getExamDateData();
    }
    public void getStudentData(){
        students.clear();
       try (Connection conn = JDBCConnection.getConnection()) {
        PreparedStatement get = conn.prepareStatement(JDBCConnection.get_students_list);
        ResultSet re1 = get.executeQuery();
        while(re1.next()){
            College college=null;
             for (int i=0;i<collegeData.size();i++){
               if(collegeData.get(i).getCollege_num()==re1.getInt("college_id"))
                   college=collegeData.get(i);
           }
            Student s = new Student(re1.getInt("student_num"), re1.getString("student_name"), college);
            students.add(s);
            PreparedStatement get2 = conn.prepareStatement(JDBCConnection.get_courses_list);
            get2.setInt(1, re1.getInt("student_num"));
             ResultSet re2 = get2.executeQuery();
            while (re2.next()){ 
                Course c=crsTable.get(re2.getInt("course_num"));
                c.addStudent(s);
                s.setCourse(c);
            }
            
        }
                } catch (SQLException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public ArrayList<Student> getStudents(){
        return this.students;
    }
    public ArrayList<Course> getCourses(){
        ArrayList<Course> course=new ArrayList(courseData);
        return course;
    }
  
    public ObservableList<Course> getE(){
        
        return exData;
    }
    public void setE(){
       
    }
   public ArrayList<ExamDate> getPureExam(){
       ArrayList<ExamDate> exam=new ArrayList();
       for (int i=0;i<dateData.size();i++){
           if (dateData.get(i).isCheck())exam.add(dateData.get(i));
       }
       return exam;
   }
    

    public void getCollegeData() {
        collegeData.clear();
        try (Connection conn = JDBCConnection.getConnection()) {
            PreparedStatement ps;
            ps = conn.prepareStatement(JDBCConnection.get_colleges);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                collegeData.add(new College(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setCollegeData() {
        try (Connection conn = JDBCConnection.getConnection()) {
            PreparedStatement ps;

            ps = conn.prepareStatement(JDBCConnection.update_college_info);
            for (int i = 0; i < collegeData.size(); i++) {
                ps.setInt(1, collegeData.get(i).getNum_of_exam());
                ps.setInt(2, collegeData.get(i).getCollege_num());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ObservableList<College> getCollege() {
        getCollegeData();
        return collegeData;
    }

    public void setCourseData() {
        try (Connection conn = JDBCConnection.getConnection()) {
            PreparedStatement ps;
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(JDBCConnection.update_courses);
            for (int i = 0; i < courseData.size(); i++) {
                ps.setInt(1, courseData.get(i).isExcepted() ? 1 : 0);
                ps.setInt(2, courseData.get(i).getNumber());
                ps.executeUpdate();
            }
            conn.commit();
        } catch (SQLException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void getCourseData() {
        courseData.clear();
        try (Connection conn = JDBCConnection.getConnection()) {
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(JDBCConnection.get_courses);

            rs = ps.executeQuery();

            while (rs.next()) {
                Course c=new Course(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getInt(4) == 1);
                courseData.add(c);
                crsTable.put(rs.getInt(1), c);
            }

        } catch (SQLException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ObservableList<Course> getCourse() {
        return courseData;

    }

    public void setExamDateData(String seasonStartDate, String seasonEndDate, String startDate, String endDate) {
        try (Connection conn = JDBCConnection.getConnection()) {
            PreparedStatement ps;
            ps = conn.prepareStatement(JDBCConnection.limit_db);
            ResultSet rs = ps.executeQuery();
            if (!rs.getBoolean(1)) {
                ps = conn.prepareStatement(JDBCConnection.Insert_Date);
            } else {
                ps = conn.prepareStatement(JDBCConnection.updat_Date);
            }
            ps.setString(1, seasonStartDate);
            ps.setString(2, seasonEndDate);
            ps.setString(3, startDate);
            ps.setString(4, endDate);
            ps.executeUpdate();
            ps = conn.prepareStatement(JDBCConnection.Delete_days);
            ps.executeUpdate();
            ps = conn.prepareStatement(JDBCConnection.Insert_Into_ExamDate);
            for (int i = 0; i < dateData.size(); i++) {

                ps.setString(1, dateData.get(i).getDay());
                ps.setString(2, dateData.get(i).getDate());

                ps.setInt(3, (dateData.get(i).isCheck()) ? 1 : 0);
                ps.setInt(4, 1);
                ps.setInt(5, 1);
                ps.setInt(6, 1);

                ps.executeUpdate();

            }
        } catch (SQLException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public ObservableList<ExamDate> getExamDate() {

        return dateData;

    }

    private void getExamDateData() {
        dateData.clear();
        try (Connection conn = JDBCConnection.getConnection()) {
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(JDBCConnection.get_exam_Date);

            rs = ps.executeQuery();

            while (rs.next()) {
                dateData.add(new ExamDate(rs.getString(1), rs.getString(2), ((rs.getInt(3)) != 0), ((rs.getInt(4)) != 0), ((rs.getInt(5)) != 0), ((rs.getInt(6)) != 0)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String[] getExamPeriod() {
        String dates[] = new String[4];
        try (Connection conn = JDBCConnection.getConnection()) {
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(JDBCConnection.get_Date);
            rs = ps.executeQuery();
            if (rs.next()) {
                dates[0] = rs.getString(1);
                dates[1] = rs.getString(2);
                dates[2] = rs.getString(3);
                dates[3] = rs.getString(4);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dates;

    }

    public void setSession(String firstStart, String firstEnd, String secondStart, String secondEnd, String thirdStart, String thirdEnd) {
        try (Connection conn = JDBCConnection.getConnection()) {
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(JDBCConnection.limit_sessios_period);
            rs = ps.executeQuery();
            if (!rs.getBoolean(1)) {
                ps = conn.prepareStatement(JDBCConnection.Insert_Session_Time);
                ps.setInt(1, 1);
                ps.setString(2, "First Session");
                ps.executeUpdate();
                ps.setInt(2, 1);
                ps.setString(2, "Second Session");
                ps.executeUpdate();
                ps.setInt(3, 1);
                ps.setString(2, "Third Session");
                ps.executeUpdate();
            }
            ps = conn.prepareStatement(JDBCConnection.update_session_Time);
            ps.setString(1, firstStart.trim());
            ps.setString(2, firstEnd.trim());
            ps.setInt(3, 1);
            ps.executeUpdate();
            ps.setString(1, secondStart.trim());
            ps.setString(2, secondEnd.trim());
            ps.setInt(3, 2);
            ps.executeUpdate();
            ps.setString(1, thirdStart.trim());
            ps.setString(2, thirdEnd.trim());
            ps.setInt(3, 3);
            ps.executeUpdate();
            ps = conn.prepareStatement(JDBCConnection.update_sessions);
            for (int i = 0; i < dateData.size(); i++) {
                ps.setInt(1, (dateData.get(i).isFirstSesstion()) ? 1 : 0);
                ps.setInt(2, (dateData.get(i).isSecondSesstion()) ? 1 : 0);
                ps.setInt(3, (dateData.get(i).isThirdSesstion()) ? 1 : 0);
                ps.setString(4, dateData.get(i).getDay());
                ps.setString(5, dateData.get(i).getDate());
                ps.executeUpdate();
            }
        } catch (SQLException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String[] getSession() {
        String session[] = new String[6];
        try (Connection conn = JDBCConnection.getConnection()) {
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(JDBCConnection.get_session_period);
            ps.setInt(1, 1);
            rs = ps.executeQuery();
            if (rs.next()) {
                session[0] = rs.getString(1);
                session[1] = rs.getString(2);
            }
            ps.setInt(1, 2);
            rs = ps.executeQuery();
            if (rs.next()) {
                session[2] = rs.getString(1);
                session[3] = rs.getString(2);
            }
            ps.setInt(1, 3);
            rs = ps.executeQuery();
            if (rs.next()) {
                session[4] = rs.getString(1);
                session[5] = rs.getString(2);
            }
        } catch (SQLException ex) {
            Logger.getLogger(Data.class.getName()).log(Level.SEVERE, null, ex);
        }
        return session;
    }
}

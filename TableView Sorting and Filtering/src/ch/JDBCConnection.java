/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author -ssc-
 */
public class JDBCConnection {

    public static String Insert_Date = "INSERT INTO history( beginning_semester, end_semester, beginning_exams, end_exams) VALUES(?,?,?,?) ";
    public static String updat_Date = "update history set beginning_semester =? , end_semester =? , beginning_exams =? , end_exams =? ";
    public static String get_Date = "select * from history ";
    public static String Insert_Session_Time = "INSERT INTO Sessions( session_num,session_name ) VALUES (?,?)";
    public static String update_session_Time = "update Sessions set session_beginning =? , session_end = ? WHERE session_num = ? ";
    public static String get_sessions_Time = "SELECT * from Sessions";
    public static String get_colleges = "select college_ID, college_name , num_of_exam from colleges ";
    public static String Insert_new_college = "INSERT INTO colleges (college_ID , college_name , num_of_exam)  VALUES(?,?,?)";
    public static String update_college_info = "UPDATE colleges SET   num_of_exam=? WHERE college_ID=? ";
    public static String Insert_Into_ExamDate = "INSERT INTO ExamDate ( Exam_Day, Exam_Date,holiday, first_session,second_session,third_session)\n"
            + "VALUES\n"
            + "(?,?,?,?,?,?)";
    public static String Delete_days = "DELETE FROM ExamDate";

    public static String update_holiday = "update ExamDate set holiday =? where  Exam_Date =? and Exam_Day =? ";
    public static String limit_db = "SELECT EXISTS(SELECT * FROM history ); ";
    public static String limit_sessios_period = "SELECT EXISTS(SELECT * FROM  Sessions); ";
    public static String limit_sessioss = "SELECT EXISTS(SELECT * FROM  Sessions); ";
    public static String get_exam_Date = "select * from ExamDate; ";
    public static String get_Sessions = "select Exam_Day , Exam_Date , first_session ,second_session , third_session from ExamDate ";
    public static String update_sessions = "UPDATE ExamDate SET first_session=?, second_session=?, third_session=? where Exam_Day=? and Exam_Date=?";
    public static String get_courses = "select course_num , course_name , num_of_student , excepted from Course ";
    public static String update_courses = "update Course set excepted = ? where course_num = ?";
    public static String insert_courses = "insert into Courses course_num , course_name , course_section , lecturer  VALUES (?,?,?,?)";
    public static String get_halls = "select * from Halls";
    public static String insert_halls = "INSERT INTO Halls ( hall_num, hall_capacity,used_hall,college_num )\n"
            + "                       VALUES\n"
            + "                       ( ?,?,?,? );";
    public static String update_halls = "UPDATE Halls SET used_hall=? where hall_num=?";
    public static String get_course_name = "select course_name from Courses where course_num = ? ";
    public static String get_session_period = "select session_beginning , session_end from Sessions where session_num=? ";
	public static String Insert_Into_temptable = "INSERT INTO temp_table(student_id,student_name,college_name,course_id,course_sec,course_name,lecturer) VALUES(?,?,?,?,?,?,?);";
	public static String Insert_Into_studentcourse = "INSERT INTO student_course(stu_num,course_num,section) VALUES(?,?,?);";
	public static String Select_collegs = "SELECT DISTINCT(college_name) FROM temp_table;";
	public static String Select_courses = "SELECT DISTINCT(temp_table.course_name), course_id,COUNT(course_name) AS total_students FROM temp_table GROUP BY course_name;";
	public static String Insert_courses = "INSERT Into course(course_num ,course_name , num_of_student,excepted) VALUES(?,?,?,0);)";
	public static String Select_luecturers = "SELECT DISTINCT(temp_table.lecturer),college_name FROM  temp_table;";
	public static String Insert_Lecturers = "INSERT Into lecturers(lecturer_num , lecturer_name , college_num) VALUES(?,?,?);";
	public static String Select_Courses_Sections = "SELECT college_name,course_id,course_sec,lecturer,count(*) as number_of_students FROM temp_table GROUP BY temp_table.course_id , temp_table.course_sec ;";
	public static String Insert_Course_Section = "INSERT INTO Courses_Sections(course_num,Section,lecturer,num_of_stu,college_num) VALUES(?,?,?,?,?);";
	public static String Insert_Students = "INSERT INTO Students (student_num, student_name, college_num) VALUES (?,?,?);";
	public static String Select_Students = "SELECT DISTINCT(temp_table.student_id),student_name,college_name FROM temp_table;";
        public static String Update_halls_colleges = "update halls SET college = ? Where college = ?";
        public static String Update_college_name = "update temp_table SET college_name = ? where college_name = ? ;";
        public static String Update_lecturer_name = "update temp_table Set Lecturer = ? WHERE Lecturer = ? ;";
        public static String Drop_all = "DELETE FROM temp_table; \r\n DELETE FROM colleges; \r\n DELETE FROM course;\r\n DELETE FROM Courses_Sections; \r\n DELETE FROM ExamDate; \r\n DELETE FROM halls; DELETE FROM Lecturers; \r\n DELETE FROM Sessions; \r\n DELETE from Student_course; \r\n DELETE FROM Students;\r\n";
        public static String Drop_temptable = "DELETE FROM temp_table;";
    public static String get_dates_order = "SELECT * FROM ExamDate WHERE holiday <> 0 ORDER BY Exam_Date;";
    public static String get_students_list = "SELECT Students.student_num,Students.student_name,colleges.college_name,colleges.college_id FROM Students,colleges WHERE colleges.college_id == Students.college_num;";
    public static String get_courses_list = "SELECT Student_course.course_num FROM Student_course WHERE stu_num=? ;" ;
    public static String get_date_count = "SELECT Count(*) FROM ExamDate WHere holiday <> 0;";
    public static String getPrevious = "SELECT * FROM ExamDate WHERE holiday <> 0 ORDER BY Exam_Date;";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:SoftwareProjectDataBase.db");
        } catch (ClassNotFoundException e) {
            System.err.println(e.getMessage());
        } catch (SQLException e) {
            System.err.println("SQL Exception");
        }
        return conn;
    }
}

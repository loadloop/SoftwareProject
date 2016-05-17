/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author Redab
 *
 */
public class ReadData {

    public ArrayList<Student> studentlist;
    public Connection con = null;
    private PreparedStatement update_halls;

    public ReadData() {
        con = JDBCConnection.getConnection();

    }

    public void close() {
        try {
            con.close();
        } catch (SQLException ex) {
            Logger.getLogger(ReadData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void read_data_only(String path) {
        try (FileInputStream file = new FileInputStream(new File(path))) {
            update_halls = con.prepareStatement(JDBCConnection.Update_halls_colleges);
            PreparedStatement prepstm = con.prepareStatement(JDBCConnection.Insert_Into_temptable);
            PreparedStatement student_insert = con.prepareStatement(JDBCConnection.Insert_Into_studentcourse);
            con.setAutoCommit(false);
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);
            String college_name, student_name, course_name, lecturer;
            int student_id, course_id, course_sec;
            for (org.apache.poi.ss.usermodel.Row row : sheet) {
                course_name = row.getCell(6).getStringCellValue();
                if (course_name.contains("نظري")) {
                    student_id = (int) row.getCell(0).getNumericCellValue();
                    student_name = row.getCell(1).getStringCellValue();
                    college_name = row.getCell(2).getStringCellValue();
                    course_id = (int) row.getCell(3).getNumericCellValue();
                    course_sec = (int) row.getCell(5).getNumericCellValue();
                    course_name = row.getCell(4).getStringCellValue();
                    lecturer = row.getCell(7).getStringCellValue();
                    prepstm.setInt(1, student_id);
                    prepstm.setString(2, student_name);
                    prepstm.setString(3, college_name);
                    prepstm.setInt(4, course_id);
                    prepstm.setInt(5, course_sec);
                    prepstm.setString(6, course_name);
                    prepstm.setString(7, lecturer);
                    student_insert.setInt(1, student_id);
                    student_insert.setInt(2, course_id);
                    student_insert.setInt(3, course_sec);
                    student_insert.addBatch();
                    prepstm.addBatch();
                }
            }
            prepstm.executeBatch();
            student_insert.executeBatch();
            PreparedStatement prep = con.prepareStatement(JDBCConnection.Insert_new_college);
            Statement stm = con.createStatement();
            ResultSet r = stm.executeQuery(JDBCConnection.Select_collegs);
            int college_id = 1;
            PreparedStatement update_college_name = con.prepareStatement(JDBCConnection.Update_college_name);
            while (r.next()) {
                prep.setInt(1, college_id * 100);
                prep.setString(2, r.getString("college_name"));
                update_college_name.setInt(1, college_id * 100);
                update_college_name.setString(2, r.getString("college_name"));
                update_college_name.addBatch();
                update_halls.setInt(1, college_id * 100);
                update_halls.setString(2, r.getString("college_name"));
                update_halls.addBatch();
                prep.addBatch();
                college_id++;
            }
            prep.executeBatch();
            update_college_name.executeBatch();
            ResultSet courses_result = stm.executeQuery(JDBCConnection.Select_courses);
            PreparedStatement add_courses = con.prepareStatement(JDBCConnection.Insert_courses);
            while (courses_result.next()) {
                add_courses.setInt(1, courses_result.getInt("course_id"));
                add_courses.setString(2, courses_result.getString("course_name"));
                add_courses.setInt(3, courses_result.getInt("total_students"));
                add_courses.addBatch();
            }
            add_courses.executeBatch();
            ResultSet lecturer_result = stm.executeQuery(JDBCConnection.Select_luecturers);
            PreparedStatement add_lecturer = con.prepareStatement(JDBCConnection.Insert_Lecturers);
            int lecturer_id = 1000;
            PreparedStatement update_lecturers = con.prepareStatement(JDBCConnection.Update_lecturer_name);
            while (lecturer_result.next()) {
                add_lecturer.setInt(1, lecturer_id);
                add_lecturer.setString(2, lecturer_result.getString("lecturer"));
                add_lecturer.setInt(3, lecturer_result.getInt("college_name"));
                add_lecturer.addBatch();
                update_lecturers.setInt(1, lecturer_id);
                update_lecturers.setString(2, lecturer_result.getString("lecturer"));
                update_lecturers.addBatch();
                lecturer_id += 1;
            }
            add_lecturer.executeBatch();
            update_lecturers.executeBatch();
            ResultSet courses_sections_set = stm.executeQuery(JDBCConnection.Select_Courses_Sections);
            PreparedStatement add_courses_sections = con.prepareStatement(JDBCConnection.Insert_Course_Section);
            while (courses_sections_set.next()) {
                add_courses_sections.setInt(1, courses_sections_set.getInt("course_id"));
                add_courses_sections.setInt(2, courses_sections_set.getInt("course_sec"));
                add_courses_sections.setInt(3, courses_sections_set.getInt("lecturer"));
                add_courses_sections.setInt(4, courses_sections_set.getInt("number_of_students"));
                add_courses_sections.setInt(5, courses_sections_set.getInt("college_name"));
                add_courses_sections.addBatch();
            }
            add_courses_sections.executeBatch();
            PreparedStatement update_students = con.prepareStatement(JDBCConnection.Insert_Students);
            ResultSet get_students = stm.executeQuery(JDBCConnection.Select_Students);
            while (get_students.next()) {
                update_students.setInt(1, get_students.getInt("student_id"));
                update_students.setString(2, get_students.getString("student_name"));
                update_students.setInt(3, get_students.getInt("college_name"));
                update_students.addBatch();
            }
            update_students.executeBatch();
            Statement drop = con.createStatement();
            drop.executeUpdate(JDBCConnection.Drop_temptable);
            con.commit();
        } catch (IOException | SQLException | org.apache.poi.openxml4j.exceptions.InvalidFormatException | EncryptedDocumentException ex) {
            Logger.getLogger(ReadData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void drop_tables() {
        try {
            Statement stm = con.createStatement();
            con.setAutoCommit(false);
            stm.executeUpdate(JDBCConnection.Drop_all);
            con.commit();
        } catch (SQLException ex) {
            Logger.getLogger(ReadData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void readHalls(String path) {
        try (FileInputStream file = new FileInputStream(new File(path))) {
            PreparedStatement prepstm = con.prepareStatement(JDBCConnection.insert_halls);
            con.setAutoCommit(false);
            //Get the workbook instance for XLS file
            Workbook workbook = WorkbookFactory.create(file);
            Sheet sheet = workbook.getSheetAt(0);
            String college_name;
            int hall_number, capacity;
            for (org.apache.poi.ss.usermodel.Row row : sheet) {
                hall_number = (int) row.getCell(0).getNumericCellValue();
                college_name = row.getCell(1).getStringCellValue();
                capacity = (int) row.getCell(2).getNumericCellValue();
                prepstm.setInt(1, hall_number);
                prepstm.setString(2, college_name);
                prepstm.setInt(3, capacity);
                prepstm.addBatch();
            }
            prepstm.executeBatch();
            update_halls.executeBatch();
            //con.prepareStatement(colleges_update_query).executeUpdate();
            con.commit();
        } catch (IOException | SQLException | org.apache.poi.openxml4j.exceptions.InvalidFormatException | EncryptedDocumentException ex) {
            Logger.getLogger(ReadData.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*public void readData(String path) throws FileNotFoundException, IOException, InvalidFormatException, ClassNotFoundException, SQLException, org.apache.poi.openxml4j.exceptions.InvalidFormatException {
        studentlist = new ArrayList<>();
        Hashtable< Integer, subjectClass> hcourse = new Hashtable();
        FileInputStream file = new FileInputStream(new File(path));
        String sql = "INSERT INTO temp_table(student_id,student_name,college_name,course_id,course_sec,course_name,lecturer) VALUES(?,?,?,?,?,?,?);";
        PreparedStatement prepstm = con.prepareStatement(sql);
        PreparedStatement student_insert = con.prepareStatement("INSERT INTO student_course(student_id,course_id,section) VALUES(?,?,?);");
        con.setAutoCommit(false);
        //Get the workbook instance for XLS file
        Workbook workbook = WorkbookFactory.create(file);

        Sheet sheet = workbook.getSheetAt(0);
        //Iterate through each rows from first sheet

        org.apache.poi.ss.usermodel.Row row;
        String collegename = "";
        String coursename = "";
        String courselec = "";
        int courseSec = 0;
        int courseid = 0;
        int stdID = 0;
        String stdName;
        int tempStdID = 0;
        row = sheet.getRow(0);
        tempStdID = (int) row.getCell(0).getNumericCellValue();
        subjectClass c;
        int rowNo = 0;
        while (rowNo < sheet.getPhysicalNumberOfRows()) {
            stdName = row.getCell(1).getStringCellValue();
            collegename = row.getCell(2).getStringCellValue();
            stdID = tempStdID;
            Student s = new Student(stdID, stdName, collegename);
            studentlist.add(s);
            while (tempStdID == stdID && rowNo <= sheet.getPhysicalNumberOfRows()) {

                if (row.getCell(6).getStringCellValue().contains("نظري")) {
                    courseid = (int) row.getCell(3).getNumericCellValue();
                    courseSec = (int) row.getCell(5).getNumericCellValue();
                    coursename = row.getCell(4).getStringCellValue();
                    courselec = row.getCell(7).getStringCellValue();
                    if (hcourse.get(courseid) == null) {
                        c = new subjectClass(courseid, coursename, collegename);
                        hcourse.put(courseid, c);
                    } else {
                        c = hcourse.get(courseid);
                    }
                    if (!c.section.contains(courseSec)) {
                        c.section.add(courseSec);
                    }
                    s.section.add(courseSec);
                    s.subjects.add(c);
                    c.student.add(s);
                }
                row = sheet.getRow(++rowNo);
                if (row == null) {
                    break;
                }
                prepstm.setInt(1, stdID);
                tempStdID = (int) row.getCell(0).getNumericCellValue();
                prepstm.setString(2, stdName);
                prepstm.setString(3, collegename);

                prepstm.setInt(4, courseid);
                prepstm.setInt(5, courseSec);
                prepstm.setString(6, coursename);
                prepstm.setString(7, courselec);
                student_insert.setInt(1, stdID);
                student_insert.setInt(2, courseid);
                student_insert.setInt(3, courseSec);
                student_insert.addBatch();
                prepstm.addBatch();
            }

        }
        prepstm.executeBatch();
        //student_insert.executeBatch();
        PreparedStatement prep = con.prepareStatement("INSERT INTO colleges(\"college_id\" , \"college_name\" , \"num_of_exam\") Values(?,?,2);");
        Statement stm = con.createStatement();
        ResultSet r = stm.executeQuery("SELECT DISTINCT(college_name) FROM temp_table;");
        int college_id = 1;
        String upd = "";
        while (r.next()) {
            prep.setInt(1, college_id * 100);
            prep.setString(2, r.getString("college_name"));
            upd += "UPDATE temp_table SET college_name = " + college_id * 100 + " WHERE college_name = \"" + r.getString("college_name") + "\"; \r\n";
            //colleges_update_query += "UPDATE halls SET college = " + college_id * 100 + " WHERE college = \"" + r.getString("college_name") + "\"; \r\n";
            prep.addBatch();
            college_id++;
        }
        prep.executeBatch();
        stm.executeUpdate(upd);
        ResultSet courses_result = stm.executeQuery("SELECT   DISTINCT(temp_table.course_name), course_id,COUNT(course_name) AS total_students FROM temp_table GROUP BY course_name;");
        PreparedStatement add_courses = con.prepareStatement("INSERT Into courses(course_id ,course_name , total_students) VALUES(?,?,?);)");
        while (courses_result.next()) {
            add_courses.setInt(1, courses_result.getInt("course_id"));
            add_courses.setString(2, courses_result.getString("course_name"));
            add_courses.setInt(3, courses_result.getInt("total_students"));
            add_courses.addBatch();
        }
        add_courses.executeBatch();
        ResultSet lecturer_result = stm.executeQuery("SELECT   DISTINCT(temp_table.lecturer),college_name FROM  temp_table;");
        PreparedStatement add_lecturer = con.prepareStatement("INSERT Into lecturers(lecturer_id , lecturer_name , college_id) VALUES(?,?,?);");
        String lecturer_query = "";
        int lecturer_id = 1000;
        while (lecturer_result.next()) {
            add_lecturer.setInt(1, lecturer_id);
            add_lecturer.setString(2, lecturer_result.getString("lecturer"));
            add_lecturer.setInt(3, lecturer_result.getInt("college_name"));
            add_lecturer.addBatch();
            lecturer_query += "UPDATE temp_table SET lecturer =\"" + lecturer_id + ""
                    + "\" WHERE lecturer = \"" + lecturer_result.getString("lecturer") + "\";";
            lecturer_id += 1;
        }
        add_lecturer.executeBatch();
        stm.executeUpdate(lecturer_query);
        ResultSet courses_sections_set = stm.executeQuery("SELECT college_name,course_id,course_sec,lecturer,count(*) as number_of_students FROM temp_table GROUP BY temp_table.course_id , temp_table.course_sec ;");
        PreparedStatement add_courses_sections = con.prepareStatement("INSERT INTO Courses_Sections(course_id,Section,lecturer_id,num_of_stu,college_id) VALUES(?,?,?,?,?);");
        while (courses_sections_set.next()) {
            add_courses_sections.setInt(1, courses_sections_set.getInt("course_id"));
            add_courses_sections.setInt(2, courses_sections_set.getInt("course_sec"));
            add_courses_sections.setInt(3, courses_sections_set.getInt("lecturer"));
            add_courses_sections.setInt(4, courses_sections_set.getInt("number_of_students"));
            add_courses_sections.setInt(5, courses_sections_set.getInt("college_name"));
            add_courses_sections.addBatch();
        }
        add_courses_sections.executeBatch();
        PreparedStatement update_students = con.prepareStatement("INSERT INTO Students (student_id, studnet_name, college_num) VALUES (?,?,?);");
        ResultSet get_students = stm.executeQuery("SELECT DISTINCT(temp_table.student_id),student_name,college_name FROM temp_table;");
        while (get_students.next()) {
            update_students.setInt(1, get_students.getInt("student_id"));
            update_students.setString(2, get_students.getString("student_name"));
            update_students.setInt(3, get_students.getInt("college_name"));
            update_students.addBatch();
        }
        update_students.executeBatch();
        Statement drop = con.createStatement();
        drop.executeUpdate("DELETE FROM temp_table");
        con.commit();
        file.close();
    }*/
}

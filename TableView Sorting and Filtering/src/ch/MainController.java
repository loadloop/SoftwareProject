/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author ipman
 */
public class MainController {

    @FXML
    AnchorPane pane;
    @FXML
    private AnchorPane an;
    @FXML
    private AnchorPane an2;
    @FXML
    private AnchorPane an3;
    @FXML
    private AnchorPane an4;
    @FXML
    private AnchorPane ac5;
    private AnchorPane examDate;
    private AnchorPane session;
    private AnchorPane course;
    private AnchorPane college;
    private AnchorPane readFiles;
    private Data data;
    private FXMLLoader loader;
    private Stage stage;
    private Stage collegeStage;
    private Stage readFilesStage;
    private CollegeController collegeController;
    private ReadFilesController readFilesController;
    @FXML
    private void an() {
        examDate.setVisible(true);
        session.setVisible(false);
        course.setVisible(false);
        an.getStyleClass().set(0, "clk");
        an2.getStyleClass().set(0, "item-selectable");
        an3.getStyleClass().set(0, "item-selectable");
        an4.getStyleClass().set(0, "item-selectable");
    }

    @FXML
    private void an2() {
        examDate.setVisible(false);
        session.setVisible(true);
        course.setVisible(false);
        an.getStyleClass().set(0, "item-selectable");
        an2.getStyleClass().set(0, "clk");
        an3.getStyleClass().set(0, "item-selectable");
        an4.getStyleClass().set(0, "item-selectable");
    }

    @FXML
    private void an3() {
        examDate.setVisible(false);
        session.setVisible(false);
        course.setVisible(true);
        an.getStyleClass().set(0, "item-selectable");
        an2.getStyleClass().set(0, "item-selectable");
        an3.getStyleClass().set(0, "clk");
        an4.getStyleClass().set(0, "item-selectable");
    }

    @FXML
    private void an4() {
        collegeController.setClicked(false);
        collegeStage.showAndWait();
       if (!collegeController.isClicked())collegeController.clear();
    }

    @FXML
    private void an5() {
        readFilesController.setClicked(false);
        readFilesStage.showAndWait();
        if (!readFilesController.isClicked())readFilesController.clear();

    }

    private void setExamDate() {
        try {
            loader = new FXMLLoader(Mains.class.getResource("ExamDate.fxml"));
            examDate = (AnchorPane) loader.load();
            ExamDateController controller
                    = loader.<ExamDateController>getController();
            controller.setData(data);
            pane.getChildren().add(examDate);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void setSession()  {
        try {
            loader = new FXMLLoader(Mains.class.getResource("Session.fxml"));
            session = (AnchorPane) loader.load();
            SessionController controller
                    = loader.<SessionController>getController();
            controller.setData(data);
            pane.getChildren().add(session);
            session.setVisible(false);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setCourse(){
        try {
            loader = new FXMLLoader(Mains.class.getResource("Course.fxml"));
            course = (AnchorPane) loader.load();
            CourseController controller
                    = loader.<CourseController>getController();
            controller.setData(data);
            pane.getChildren().add(course);
            course.setVisible(false);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setCollege() {
        try {
            loader = new FXMLLoader(Mains.class.getResource("College.fxml"));
            college = (AnchorPane) loader.load();
            collegeStage = new Stage();
            collegeStage.setTitle("College");
            collegeStage.initModality(Modality.APPLICATION_MODAL);
            collegeStage.initOwner(stage);
            Scene scene = new Scene(college);
            collegeStage.setScene(scene);
            collegeController
                    = loader.<CollegeController>getController();
            collegeController.setData(data);
           
            collegeController.setStage(collegeStage);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void setReadFiles()  {
        try {
            loader = new FXMLLoader(Mains.class.getResource("ReadFiles.fxml"));
            readFiles = (AnchorPane) loader.load();
            readFilesStage = new Stage();
            readFilesStage.setTitle("ReadFiles");
            readFilesStage.initModality(Modality.APPLICATION_MODAL);
            readFilesStage.initOwner(stage);
            Scene scene = new Scene(readFiles);
            readFilesStage.setScene(scene);
            readFilesController
                    = loader.<ReadFilesController>getController();
            // controller.setData(data);
            readFilesController.setStage(readFilesStage);
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        data = new Data();
        setExamDate();
        setSession();
        setCourse();
        setCollege();
        setReadFiles();
    }

}

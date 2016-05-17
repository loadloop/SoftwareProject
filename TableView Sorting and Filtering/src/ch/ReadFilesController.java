package ch;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author انا
 */
public class ReadFilesController implements Initializable {

    @FXML
    private TextField hall_path;
    @FXML
    private TextField student_path;
    private Data data;
    private Stage stage;
    private Boolean clicked;

    public Boolean isClicked() {
        return clicked;
    }

    public void setClicked(Boolean clicked) {
        this.clicked = clicked;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void clear() {
        hall_path.setText("");
        student_path.setText("");
    }

    @FXML
    private void Browse_halls() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Halls File");
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            hall_path.setText(selectedFile.getAbsolutePath());
        }

    }

    @FXML
    private void Browse_students() {
        FileChooser fileChooser2 = new FileChooser();
        fileChooser2.setTitle("Open Halls File");
        File selectedFile2 = fileChooser2.showOpenDialog(stage);
        if (selectedFile2 != null) {
            student_path.setText(selectedFile2.getAbsolutePath());
        }

    }

    @FXML

    private void Read_files() {
        clicked = true;
        boolean ok=true;
        if (hall_path.getText().equals("")) {
            hall_path.getStyleClass().remove("hey");
            hall_path.getStyleClass().add("hey");
            ok=false;
        } else {
            hall_path.getStyleClass().remove("hey");
        }
        if (student_path.getText().equals("")) {
            ok=false;
            student_path.getStyleClass().remove("hey");
            student_path.getStyleClass().add("hey");
        } else {
            student_path.getStyleClass().remove("hey");
        }
        if(!ok)return;
        ReadData r = new ReadData();
        r.drop_tables();
        r.read_data_only(student_path.getText());
        r.readHalls(hall_path.getText());
        data.getCollegeData();
        data.getCourseData();
        stage.close();
    }

    @FXML

    private void close() {
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

}

package ch;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 *
 * @author Marco jawad
 */
public class ExceptionController {

    @FXML
    private TextField courseNo;
    @FXML
    private DatePicker examDate;
    @FXML
    private ComboBox<String> session;
    @FXML
    private TableView<Course> course;
    @FXML
    private TableColumn<Course, Number> courseNoColumn;
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    @FXML
    private TableColumn<Course, Number> courseSNoColumn;
    @FXML
    private TableColumn<Course, String> courseExamDate;
    @FXML
    private TableColumn<Course, String> courseExamDay;
    @FXML
    private TableColumn<Course, String> courseSessionColmun;
    @FXML
    private TableColumn<Course, String> removeColumn;
    private ObservableList<Course> courseData;
    private ObservableList<Course> eData;
    private Course c;
    private Data data;

    /**
     * Just add some sample data in the constructor.
     *
     */

    public void setData(Data data) {
        this.data = data;
        eData = data.getE();
        courseData=data.getCourse();
        set();

    }

    @FXML
    private void saveData() {
        data.setE();
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *
     * Initializes the table columns and sets up sorting and filtering.
     */
    public void initialize() {
        courseNoColumn.setCellValueFactory(cellData -> cellData.getValue().numberProperty());
        courseSNoColumn.setCellValueFactory(cellData -> cellData.getValue().studentNumberProperty());
        courseNameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        courseExamDay.setCellValueFactory(cellData -> cellData.getValue().getSession().getExamDay().dayProperty());
        courseExamDate.setCellValueFactory(cellData -> cellData.getValue().getSession().getExamDay().dateProperty());
        courseSessionColmun.setCellValueFactory(cellData -> cellData.getValue().getSession().getNameProperty());
        removeColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
    }

    private void set() {
        
        courseNo.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (oldValue.equals(newValue)) {
                return;
            }
            int value = -1;
            if ("".equals(newValue)) {
                value = 0;
            } else if (newValue.matches("\\d*")) {
                value = Integer.parseInt(newValue);
            } else {
                courseNo.setText(oldValue);
            }
            if (value != -1) {
                for (int i = 0; i < courseData.size(); i++) {
                    if (courseData.get(i).getNumber()==value) {
                        c=courseData.get(i);
                    }
                }
            }
        });
        course.setItems(eData);


    }
}

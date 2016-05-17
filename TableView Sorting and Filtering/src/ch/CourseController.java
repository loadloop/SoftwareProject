package ch;

import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;

/**
 *
 * @author Marco jawad
 */
public class CourseController {

    @FXML
    private TextField search;
    @FXML
    private TextField exception;
    @FXML
    private TableView<Course> course;
    @FXML
    private TableColumn<Course, Number> courseNoColumn;
    @FXML
    private TableColumn<Course, String> courseNameColumn;
    @FXML
    private TableColumn<Course, Number> courseSNoColumn;
    @FXML
    private TableColumn<Course, Boolean> exceptionColumn;
    private ObservableList<Course> courseData;
    private Data data;

    /**
     * Just add some sample data in the constructor.
     *
     */

    public void setData(Data data) {
        this.data = data;
        courseData = data.getCourse();
        set();

    }

    @FXML
    private void saveData() {
        data.setCourseData();

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
        exceptionColumn.setCellValueFactory(cellData -> cellData.getValue().exceptedProperty());
        exceptionColumn.setCellFactory(CheckBoxTableCell.forTableColumn(exceptionColumn));
        exceptionColumn.setEditable(true);
        // 1. Wrap the ObservableList in a FilteredList (initially display all data).

    }

    private void set() {
        FilteredList<Course> filteredData = new FilteredList<>(courseData, p -> true);
        // 2. Set the filter Predicate whenever the filter changes.
        search.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            filteredData.setPredicate((Course c) -> {
                // If filter text is empty, display all persons.
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                course.refresh();
                // Compare first name and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();

                if (c.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches first name.
                } else if (String.valueOf(c.getNumber()).toLowerCase().contains(lowerCaseFilter)) {
                    return true; // Filter matches last name.
                }
                return false; // Does not match.
            });
        });
        exception.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (oldValue.equals(newValue)) {
                return;
            }
            int value = -1;
            if ("".equals(newValue)) {
                value = 0;
            } else if (newValue.matches("\\d*")) {
                value = Integer.parseInt(newValue);
            } else {
                exception.setText(oldValue);
            }
            if (value != -1) {
                for (int i = 0; i < courseData.size(); i++) {
                    if (courseData.get(i).getStudentNumber() > value) {
                        courseData.get(i).setExcepted(false);
                    } else {
                        courseData.get(i).setExcepted(true);
                    }
                }
            }
        });
        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<Course> sortedData = new SortedList<>(filteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedData.comparatorProperty().bind(course.comparatorProperty());
        course.setRowFactory((TableView<Course> param) -> new TableRow<Course>() {
            @Override
            public void updateSelected(boolean selected) {
                super.updateSelected(selected);
                if (this.getItem() != null && this.getItem().isExcepted()) {
                    getStyleClass().remove("false");
                    getStyleClass().add("false");
                } else {
                    getStyleClass().remove("false");

                }
            }

        });
        course.setEditable(true);
        // 5. Add sorted (and filtered) data to the table.
        course.setItems(sortedData);

        courseData.addListener((Change<? extends Course> change) -> {
            if (courseData.isEmpty()) {
                return;
            }
            change.next();
            course.getSelectionModel().select(courseData.get(change.getFrom()));
        });
    }
}

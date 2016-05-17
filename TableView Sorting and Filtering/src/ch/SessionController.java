package ch;

import java.time.LocalTime;
import java.util.Collections;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;

/**
 * View-Controller for the person table.
 *
 * @author Marco Jakob
 */
public class SessionController {

    @FXML
    private ComboBox<LocalTime> firstStart;
    @FXML
    private ComboBox<LocalTime> firstEnd;
    @FXML
    private ComboBox<LocalTime> secondStart;
    @FXML
    private ComboBox<LocalTime> secondEnd;
    @FXML
    private ComboBox<LocalTime> thirdStart;
    @FXML
    private ComboBox<LocalTime> thirdEnd;
    @FXML
    private TableView<ExamDate> exam;
    @FXML
    private TableColumn<ExamDate, String> dayColumn;
    @FXML
    private TableColumn<ExamDate, String> dateColumn;
    @FXML
    private TableColumn<ExamDate, Boolean> firstSessionColumn;
    @FXML
    private TableColumn<ExamDate, Boolean> secondSessionColumn;
    @FXML
    private TableColumn<ExamDate, Boolean> thirdSessionColumn;

    private ObservableList<ExamDate> dateData = FXCollections.<ExamDate>observableArrayList((ExamDate examDate) -> new Observable[]{examDate.check()});
    private Data data;

    /**
     * Just add some sample data in the constructor.
     *
     */
    public void setData(Data data)  {
        this.data = data;
        this.dateData = data.getExamDate();
        String[] sessions = data.getSession();
        String[] dates = data.getSession();
        firstStart.setValue(LocalTime.parse(dates[0]));
        firstEnd.setValue(LocalTime.parse(dates[1]));
        secondStart.setValue(LocalTime.parse(dates[2]));
        secondEnd.setValue(LocalTime.parse(dates[3]));
        thirdStart.setValue(LocalTime.parse(dates[4]));
        thirdEnd.setValue(LocalTime.parse(dates[5]));
        set();
    }

    /*
    private void insertDate() {
        if (startDate.getValue() == null || endDate.getValue() == null) {
            return;
        }
        dateData.clear();
        LocalDate Day = startDate.getValue();
        LocalDate lastDay = endDate.getValue();
        while (Day.isBefore(lastDay.plusDays(1))) {
            dateData.add(new ExamDate(Day.getDayOfWeek().name(), Day.toString(), Day.getDayOfWeek().getValue() != 5, true, true, true));
            Day = Day.plusDays(1);
        }
    }
     
    private void shadow(Node x) {
        x.getStyleClass().remove("hey");
    shadow.addListener(it -> {
        if (shadow.get()==300){
        x.getStyleClass().remove("hey");
        shadow.set(0);
    }
    }
    );
    KeyValue keyValue = new KeyValue(shadow, 300);
    
    KeyFrame keyFrame = new KeyFrame(Duration.millis(300), keyValue);
    Timeline timeline = new Timeline(keyFrame);
    timeline.play();
}
    private DoubleProperty shadow = new SimpleDoubleProperty(this, "menuPaneLocation");
     */
    private boolean check(ComboBox... comboBox) {
        boolean ok = true;
        for (ComboBox comboBox1 : comboBox) {
            if (comboBox1.getValue() == null) {
                comboBox1.getStyleClass().remove("hey");
                comboBox1.getStyleClass().add("hey");
                ok = false;
            } else {
                comboBox1.getStyleClass().remove("hey");
            }
        }
        return ok;

    }

    public void saveData()  {
        //if (firstStart.getValue() == null || firstEnd.getValue() == null || secondStart.getValue() == null || secondEnd.getValue() == null || thirdStart.getValue() == null || thirdEnd.getValue() == null || dateData.isEmpty()) {
        //         return;
        //   }

        if (dateData.isEmpty()) {
            exam.getStyleClass().remove("hey");
            exam.getStyleClass().add("hey");
        } else {
            exam.getStyleClass().remove("hey");
        }
        if (!check(firstStart, firstEnd, secondStart, secondEnd, thirdStart, thirdEnd) || dateData.isEmpty()) {
            return;
        }
        data.setSession(firstStart.getValue().toString(), firstEnd.getValue().toString(), secondStart.getValue().toString(), secondEnd.getValue().toString(), thirdStart.getValue().toString(), thirdEnd.getValue().toString());
    }

    private void set() {
        SortedList<ExamDate> sortedData;
        FilteredList<ExamDate> filteredData;

        filteredData = new FilteredList<>(dateData, p -> true);
        filteredData.setPredicate(date -> {
            return date.isCheck();
        });
        sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(exam.comparatorProperty());

        exam.setItems(sortedData);

        firstStart.getItems().addAll(LocalTime.of(8, 0, 0), LocalTime.of(8, 30, 0), LocalTime.of(9, 0, 0), LocalTime.of(9, 30, 0), LocalTime.of(10, 0, 0));
        data(firstEnd.getItems(), 1, firstStart.getValue());
        data(secondStart.getItems(), 0, firstEnd.getValue());
        data(secondEnd.getItems(), 1, secondStart.getValue());
        data(thirdStart.getItems(), 0, secondEnd.getValue());
        data(thirdEnd.getItems(), 1, thirdStart.getValue());
        firstStart.valueProperty().addListener((ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) -> {
            data(firstEnd.getItems(), 1, firstStart.getValue());

        });
        firstEnd.valueProperty().addListener((ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) -> {

            data(secondStart.getItems(), 0, firstEnd.getValue());

            if (secondStart.getSelectionModel().isEmpty()) {
                data(secondEnd.getItems(), 0, secondStart.getItems().get(0));
            }

        });
        secondStart.valueProperty().addListener((ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) -> {
            data(secondEnd.getItems(), 1, secondStart.getValue());
        });
        secondEnd.valueProperty().addListener((ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) -> {
            data(thirdStart.getItems(), 0, secondEnd.getValue());
        });
        thirdStart.valueProperty().addListener((ObservableValue<? extends LocalTime> observable, LocalTime oldValue, LocalTime newValue) -> {
            data(thirdEnd.getItems(), 1, thirdStart.getValue());

        });

    }

    private void data(ObservableList<LocalTime> list, int x, LocalTime newValue) {
        if (newValue == null) {
            return;
        }
        for (int i = 1; i <= 4; i++) {
            if (!list.contains(newValue.plusMinutes((i + x) * 30))) {
                list.add(newValue.plusMinutes((i + x) * 30));
            }
        }
        list.removeIf(p -> p.isBefore(newValue.plusMinutes(29 + (30 * x))) || p.isAfter(newValue.plusMinutes(121 + (30 * x))));
        Collections.sort(list);
    }

    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *
     */
    public void initialize() {
        dayColumn.setCellValueFactory(cellData -> cellData.getValue().dayProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        firstSessionColumn.setCellValueFactory(cellData -> cellData.getValue().firstSesstion());
        firstSessionColumn.setCellFactory((TableColumn<ExamDate, Boolean> param) -> new CheckBoxTableCell<ExamDate, Boolean>() {

            @Override
            public void updateItem(Boolean item, boolean empty) {

                super.updateItem(item, empty);
                if (!empty && !item && !this.isSelected()) {

                    this.getStyleClass().remove("false");
                    this.getStyleClass().add("false");
                } else {
                    this.getStyleClass().remove("false");
                }

            }
        });
        firstSessionColumn.setEditable(true);
        secondSessionColumn.setCellValueFactory(cellData -> cellData.getValue().secondSesstion());
        secondSessionColumn.setCellFactory((TableColumn<ExamDate, Boolean> param) -> new CheckBoxTableCell<ExamDate, Boolean>() {

            @Override
            public void updateItem(Boolean item, boolean empty) {

                super.updateItem(item, empty);
                if (!empty && !item && !this.isSelected()) {

                    this.getStyleClass().remove("false");
                    this.getStyleClass().add("false");
                } else {
                    this.getStyleClass().remove("false");
                }

            }
        });
        secondSessionColumn.setEditable(true);
        thirdSessionColumn.setCellValueFactory(cellData -> cellData.getValue().thirdSesstion());
        thirdSessionColumn.setCellFactory((TableColumn<ExamDate, Boolean> param) -> new CheckBoxTableCell<ExamDate, Boolean>() {

            @Override
            public void updateItem(Boolean item, boolean empty) {

                super.updateItem(item, empty);
                if (!empty && !item && !this.isSelected()) {

                    this.getStyleClass().remove("false");
                    this.getStyleClass().add("false");
                } else {
                    this.getStyleClass().remove("false");
                }

            }
        });
        thirdSessionColumn.setEditable(true);

        exam.setEditable(true);

    }
}

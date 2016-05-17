package ch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener.Change;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.util.Callback;

/**
 * View-Controller for the person table.
 *
 * @author Marco Jakob
 */
public class ExamDateController {

    @FXML
    private TableView<ExamDate> exam;
    @FXML
    private TableColumn<ExamDate, String> dayColumn;
    @FXML
    private TableColumn<ExamDate, String> dateColumn;
    @FXML
    private TableColumn<ExamDate, Boolean> vacationColumn;
    @FXML
    private DatePicker startDate;
    @FXML
    private DatePicker endDate;
    @FXML
    private DatePicker seasonStartDate;
    @FXML
    private DatePicker seasonEndDate;
    private ObservableList<ExamDate> dateData = FXCollections.<ExamDate>observableArrayList((ExamDate examDate) -> new Observable[]{examDate.check()});
    private Data data;
    private SortedList<ExamDate> sortedData;

    /**
     * Just add some sample data in the constructor.
     *
     */
    public ExamDateController() {

    }

    public void setData(Data data)  {
        this.data = data;
        this.dateData = data.getExamDate();
        sortedData = new SortedList<>(dateData);
        sortedData.comparatorProperty().bind(exam.comparatorProperty());
        exam.setItems(sortedData);
        String[] dates = data.getExamPeriod();
        seasonStartDate.setValue(LocalDate.parse(dates[0]));
        seasonEndDate.setValue(LocalDate.parse(dates[1]));
        startDate.setValue(LocalDate.parse(dates[2]));
        endDate.setValue(LocalDate.parse(dates[3]));
    }

    @FXML
    private void insertDate() {

        if (!check(startDate, endDate)) {
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

    private boolean check(DatePicker... datepicker) {
        boolean ok = true;
        for (DatePicker datepicker1 : datepicker) {
            if (datepicker1.getValue() == null) {
                datepicker1.getStyleClass().remove("hey");
                datepicker1.getStyleClass().add("hey");
                ok = false;
            } else {
                datepicker1.getStyleClass().remove("hey");
            }
        }
        return ok;

    }

    @FXML

    private void saveDate() {

        if (dateData.isEmpty()) {
            exam.getStyleClass().remove("hey");
            exam.getStyleClass().add("hey");
        } else {
            exam.getStyleClass().remove("hey");
        }
        if (!check(startDate, endDate, seasonStartDate, seasonEndDate) || dateData.isEmpty()) {
            return;
        }
        data.setExamDateData(seasonStartDate.getValue().toString(), seasonEndDate.getValue().toString(), startDate.getValue().toString(), endDate.getValue().toString());
    }

    /*
    public void setData(ObservableList<ExamDate> dateData) throws SQLException {
        this.dateData = dateData;
        
        try (Connection conn = JDBCConnection.getConnection()) {
            PreparedStatement ps;
            ResultSet rs;
            ps = conn.prepareStatement(JDBCConnection.get_exam_Date);

            rs = ps.executeQuery();

            while (rs.next()) {
                dateData.add(new ExamDate(rs.getString(1), rs.getString(2), ((rs.getInt(3)) != 0), ((rs.getInt(4)) != 0), ((rs.getInt(5)) != 0), ((rs.getInt(6)) != 0)));
            }
            ps = conn.prepareStatement(JDBCConnection.get_Date);
            rs = ps.executeQuery();
            while (rs.next()) {
                seasonStartDate.setValue(LocalDate.parse(rs.getString(1)));
                seasonEndDate.setValue(LocalDate.parse(rs.getString(2)));
                startDate.setValue(LocalDate.parse(rs.getString(3)));
                endDate.setValue(LocalDate.parse(rs.getString(4)));
            }
        }

    }
     */
    /**
     * Initializes the controller class. This method is automatically called
     * after the fxml file has been loaded.
     *
     * Initializes the table columns and sets up sorting and filtering.
     */
    @FXML

    public void initialize(){
        dayColumn.setCellValueFactory(cellData -> cellData.getValue().dayProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        vacationColumn.setCellValueFactory(cellData -> cellData.getValue().check());
        vacationColumn.setCellFactory(CheckBoxTableCell.forTableColumn(vacationColumn));
        vacationColumn.setEditable(true);
        exam.setRowFactory((TableView<ExamDate> param) -> new TableRow<ExamDate>() {
            @Override
            public void updateSelected(boolean selected) {
                super.updateSelected(selected);
                if (this.getItem() != null && !this.getItem().isCheck()) {
                    getStyleClass().remove("false");
                    getStyleClass().add("false");
                } else {
                    getStyleClass().remove("false");

                }
            }

        });
        exam.setEditable(true);
        dateData.addListener((Change<? extends ExamDate> change) -> {
            if (dateData.isEmpty()) {
                return;
            }
            change.next();
            exam.getSelectionModel().select(dateData.get(change.getFrom()));
            exam.requestFocus();
        });

        final Callback<DatePicker, DateCell> start = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (seasonEndDate.getValue() != null && seasonStartDate.getValue() != null && (item.isAfter(seasonStartDate.getValue()) && item.isBefore(seasonEndDate.getValue()))) {
                    setDisable(false);
                } else {
                    setDisable(true);
                }
                if (startDate.getValue() != null) {
                    if (endDate.getValue() != null && startDate.getValue().isAfter(endDate.getValue())) {
                        endDate.setValue(null);
                    }
                }
            }
        };
        startDate.setDayCellFactory(start);
        final Callback<DatePicker, DateCell> end = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (startDate.getValue() != null && item.isAfter(startDate.getValue()) && seasonStartDate.getValue() != null && seasonEndDate.getValue() != null && (item.isAfter(seasonStartDate.getValue()) && item.isBefore(seasonEndDate.getValue().plusDays(5)))) {
                    setDisable(false);
                } else {
                    setDisable(true);
                }

            }
        };
        endDate.setDayCellFactory(end);
        final Callback<DatePicker, DateCell> endS = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (seasonStartDate.getValue() != null && item.isAfter(seasonStartDate.getValue()) && item.isBefore(seasonStartDate.getValue().plusMonths(9))) {
                    setDisable(false);

                } else {
                    setDisable(true);
                }
                if (seasonEndDate.getValue() != null) {
                    if (startDate.getValue() != null && startDate.getValue().isAfter(seasonEndDate.getValue())) {
                        startDate.setValue(null);
                        endDate.setValue(null);
                    }
                }

            }
        };
        seasonEndDate.setDayCellFactory(endS);
        final Callback<DatePicker, DateCell> startS = (final DatePicker datePicker) -> new DateCell() {
            @Override
            public void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (seasonStartDate.getValue() != null) {
                    if (seasonEndDate.getValue() != null && seasonEndDate.getValue().isBefore(seasonStartDate.getValue())) {
                        seasonEndDate.setValue(null);
                    }
                    if (startDate.getValue() != null && startDate.getValue().isBefore(seasonStartDate.getValue())) {
                        startDate.setValue(null);
                        endDate.setValue(null);
                    }
                }
            }
        };
        seasonStartDate.setDayCellFactory(startS);
    }
}

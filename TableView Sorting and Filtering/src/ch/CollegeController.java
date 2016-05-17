/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch;

import java.util.Collections;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;

/**
 *
 * @author jawad
 */
public class CollegeController {

    private Stage stage;
    @FXML
    private TableView<College> college;
    @FXML
    private TableColumn<College, String> collegeName;
    @FXML
    private TableColumn<College, Number> noOfExamAday;
    @FXML
    private TableColumn<College, Number> collegeNumberColmun;
private Boolean clicked;
    private ObservableList<College> collegeData = FXCollections.<College>observableArrayList();
    private Data data;

    public void setData(Data data) {
        this.data = data;
        collegeData = data.getCollege();
        college.setItems(collegeData);
    }
    public Boolean isClicked(){
        return clicked;
    }
    public void setClicked(Boolean clicked){
        this.clicked=clicked;
    }
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void ok() {
        clicked=true;
        data.setCollegeData();
        stage.close();
    }

    @FXML

    private void cancel() {
        stage.close();
    }
    public void clear(){
        collegeData=data.getCollege();
        college.refresh();

    }

    public void initialize() {
        collegeName.setCellValueFactory(cellData -> cellData.getValue().College_nameProperty());
        noOfExamAday.setCellValueFactory(cellData -> cellData.getValue().Num_of_examProperty());
        collegeNumberColmun.setCellValueFactory(cellData -> cellData.getValue().College_numProperty());

        noOfExamAday.setCellFactory(c -> new TableCell<College, Number>() {
            private final ToggleButton button = new ToggleButton("امتحانين");

            {
                button.getStyleClass().add("toggle");
                button.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                    if (isNowSelected) {
                        ((College) this.getTableRow().getItem()).setNum_of_exam(1);
                        button.setText(" امتحان ");
                    } else {
                        ((College) this.getTableRow().getItem()).setNum_of_exam(2);
                        button.setText("امتحانين");
                    }
                });

            }

            @Override
            public void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    if (item.intValue() == 1) {
                        button.setSelected(true);
                    } else {
                        button.setSelected(false);
                    }
                    setGraphic(button);
                }
            }
        });
    }

}

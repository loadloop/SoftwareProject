package ch;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import javafx.application.Application;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Main class to start the application.
 * 
 * @author Marco Jakob
 */
public class Mains extends Application {

    @Override
    public void start(Stage primaryStage) throws SQLException {
        primaryStage.setTitle("Sorting and Filtering");

        try {
            FXMLLoader loader = new FXMLLoader(Mains.class.getResource("dialog.fxml"));
            DialogPane page = (DialogPane) loader.load();
            Scene scene = new Scene(page);

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

  /*  public static void main(String[] args) {
        launch(args);
    } */
    private void setData(ExamDateController controller) throws SQLException{
    Connection conn = JDBCConnection.getConnection();
PreparedStatement ps;
ResultSet rs ;
            ps = conn.prepareStatement(JDBCConnection.get_exam_Date);
            rs = ps.executeQuery();
            while (rs.next()) {
          //     controller.masterData.add(new ExamDate(rs.getString(1), rs.getString(2), ((rs.getInt(3)) != 0)));
            }

                      conn.close();

}
}
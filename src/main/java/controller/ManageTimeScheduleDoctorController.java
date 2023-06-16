package controller;



import com.jfoenix.controls.JFXButton;
import dao.custom.TimeDAO;
import dao.custom.impl.TimeDAOImpl;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.TimeDTO;
import org.controlsfx.control.Notifications;
import tdm.TimeTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class ManageTimeScheduleDoctorController implements Initializable {
    public TextField search;
    public TableView<TimeTM> timeTable;
    public TableColumn d_name;
    public TableColumn Day;
    public TableColumn time_to;
    public TableColumn time_from;
    public TimeDAO timeDAO=new TimeDAOImpl();
    public TableColumn action;

    public void searchOnAction(KeyEvent keyEvent) {
        setSearchFilter();
    }
    public void setBillTableItems() {
        d_name.setCellValueFactory(new PropertyValueFactory<>("d_name"));
        Day.setCellValueFactory(new PropertyValueFactory<>("day"));
        time_to.setCellValueFactory(new PropertyValueFactory<>("toTime"));
        time_from.setCellValueFactory(new PropertyValueFactory<>("fromTime"));
        action.setCellValueFactory(new PropertyValueFactory<>("button"));
        timeTable.getItems().clear();
        try {
            ArrayList<TimeDTO> all = timeDAO.getAll();
            for (TimeDTO temp:all) {
                Image img = new Image("/view/assests/images/icons8-delete-100.png");
                ImageView imageView = new ImageView(img);
                imageView.setFitHeight(30);
                imageView.setPreserveRatio(true);
                JFXButton button = new JFXButton();
                button.setGraphic(imageView);
                setRemoveBtnOnAction(button, temp);
                timeTable.getItems().add(new TimeTM(temp.getD_name(),temp.getDay(),temp.getFromTime(),temp.getToTime(),button));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
        }
    }

    private void setRemoveBtnOnAction(JFXButton button, TimeDTO temp) {
        button.setOnAction((actionEvent -> {
            ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
            Optional<ButtonType> buttonType = new Alert(Alert.AlertType.INFORMATION, "Are you sure to remove?", yes, no).showAndWait();
            if (buttonType.orElse(no) == yes) {
                try {
                    boolean delete = timeDAO.delete(temp.getNo());
                    if (delete) {

                        // Deleted Successful

                        setBillTableItems();
                        Notifications.create()
                                .graphic(new ImageView(new Image("/view/assests/images/icons8-ok-48.png")))
                                .text("Deleted Successful  ")
                                .title("success")
                                .hideAfter(Duration.seconds(5))
                                .position(Pos.TOP_RIGHT)
                                .show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
                }


            }

        }));
    }

    void setSearchFilter() {
        FilteredList<TimeTM> filteredData = new FilteredList<>(timeTable.getItems(), b -> true);

        search.setOnKeyPressed(keyEvent -> {
            search.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate(TimeTM -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (TimeTM.getD_name().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (TimeTM.getDay().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }));

            SortedList<TimeTM> sortedList = new SortedList<>(filteredData);
            sortedList.comparatorProperty().bind(timeTable.comparatorProperty());
            timeTable.setItems(sortedList);
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setBillTableItems();
    }

    public void addTimeOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addTimeAndDay.fxml"));
        Parent parent = fxmlLoader.load();
        stage.setScene(new Scene(parent));
        AddTimeAndDayController controller = fxmlLoader.getController();
        controller.manageTimeScheduleDoctorController = this;
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(search.getScene().getWindow());
        stage.centerOnScreen();
        stage.show();
    }
}

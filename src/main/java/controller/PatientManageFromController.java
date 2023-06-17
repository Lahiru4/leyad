package controller;

import com.jfoenix.controls.JFXButton;
import dao.CrudDAO;
import dao.custom.impl.PatientDAOImpl;
import db.DbConnection;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import dto.PatientDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import tdm.PatientTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PatientManageFromController implements Initializable {
    public TableView<PatientTM> patientTable;
    public TableColumn id;
    public TableColumn name;
    public TableColumn age;
    public TableColumn gen;
    public TableColumn address;
    public TableColumn phoneNumber;
    public AnchorPane patient;
    public JFXButton print;
    CrudDAO<PatientDTO> patientDTOCrudDAO=new PatientDAOImpl();
    public TextField search;
    public void searchOnAction(ActionEvent actionEvent) {
        FilteredList<PatientTM> filteredData = new FilteredList<>(patientTable.getItems(), b -> true);

        search.setOnKeyPressed(keyEvent -> {
            search.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate(PatientTM -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (PatientTM.getName().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (PatientTM.getId().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }));

            SortedList<PatientTM> sortedList = new SortedList<>(filteredData);
            sortedList.comparatorProperty().bind(patientTable.comparatorProperty());
            patientTable.setItems(sortedList);
        });
    }

    public void logOutOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) search.getScene().getWindow();
        stage.close();
        Stage stage1 = new Stage();
        stage1.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/main-form.fxml"))));
        stage1.setResizable(false);
        stage1.centerOnScreen();
        stage1.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setTableData();
    }

    public void setTableData() {
        id.setCellValueFactory(new PropertyValueFactory<>("id"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        age.setCellValueFactory(new PropertyValueFactory<>("age"));
        gen.setCellValueFactory(new PropertyValueFactory<>("gen"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phone"));
        patientTable.getItems().clear();

        try {
            ArrayList<PatientDTO> all = patientDTOCrudDAO.getAll();
            for (PatientDTO temp:all) {
                patientTable.getItems().add(new PatientTM(temp.getId(),temp.getName()+temp.getMr_ms(),temp.getAge(),temp.getPhone(),temp.getGen(),temp.getMr_ms(),temp.getName()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onMouseOnAction(MouseEvent mouseEvent) throws IOException {
        int selectedIndex = patientTable.getSelectionModel().getSelectedIndex();
        PatientTM patientTM = patientTable.getItems().get(selectedIndex);

        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addPatient.fxml"));
        Parent parent = fxmlLoader.load();
        stage.setScene(new Scene(parent));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(patient.getScene().getWindow());
        AddPatientController addDoctorController = fxmlLoader.getController();
        addDoctorController.save.setText("Updated");
        addDoctorController.setOldData(patientTM);
        //write fxml loge controller
        addDoctorController.patientManageFromController=this;
        stage.centerOnScreen();
        stage.show();
    }

    public void addPatientOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addPatient.fxml"));
        Parent parent = fxmlLoader.load();
        stage.setScene(new Scene(parent));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(patient.getScene().getWindow());
        AddPatientController addDoctorController = fxmlLoader.getController();

        //write fxml loge controller
        addDoctorController.patientManageFromController=this;
        addDoctorController.setId();
        stage.centerOnScreen();
        stage.show();
    }

    public void printOnActinon(ActionEvent actionEvent) {
        Thread thread=new Thread() {
            @SneakyThrows
            public void run() {
                String sql = "select * from patient";
                JasperDesign jasdi = JRXmlLoader.load(this.getClass().getResourceAsStream("/report/patientReport.jrxml"));
                JRDesignQuery newQuery = new JRDesignQuery();
                newQuery.setText(sql);
                jasdi.setQuery(newQuery);
                JasperReport js = JasperCompileManager.compileReport(jasdi);
                JasperPrint jp = JasperFillManager.fillReport(js, null, DbConnection.getInstance().getConnection());
                JasperViewer.viewReport(jp,false);
            }
        };
        thread.start();
    }
}

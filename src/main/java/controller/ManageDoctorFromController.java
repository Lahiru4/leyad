package controller;

import com.jfoenix.controls.JFXButton;
import dao.CrudDAO;

import dao.custom.impl.DoctorDAOImpl;
import db.DbConnection;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import dto.DoctorDTO;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import tdm.DoctorTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ManageDoctorFromController implements Initializable {
    public JFXButton addDoctor;
    public TableView<DoctorTM> doctorShowTable;
    public TableColumn id;
    public TableColumn name;
    public TableColumn gen;
    public TableColumn free;
    public TableColumn gmail;
    public TableColumn phoneNumber;
    public TableColumn btn;
    public TextField search;
    public TableColumn hosFee_t;

    private CrudDAO<DoctorDTO> dtoCrudDAO = new DoctorDAOImpl();

    public void addDoctorOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addDoctor.fxml"));
        Parent parent = fxmlLoader.load();
        stage.setScene(new Scene(parent));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(search.getScene().getWindow());
        AddDoctorController addDoctorController = fxmlLoader.getController();
        //write fxml loge controller
        addDoctorController.manageDoctorFromController=this;
        addDoctorController.setNewId();
        stage.centerOnScreen();
        stage.show();
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
        id.setCellValueFactory(new PropertyValueFactory<>("idNumber"));
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        gen.setCellValueFactory(new PropertyValueFactory<>("gen"));
        free.setCellValueFactory(new PropertyValueFactory<>("free"));
        gmail.setCellValueFactory(new PropertyValueFactory<>("gmail"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        hosFee_t.setCellValueFactory(new PropertyValueFactory<>("hos_fee"));

        doctorShowTable.getItems().clear();
        try {
            ArrayList<DoctorDTO> all = dtoCrudDAO.getAll();
            for (DoctorDTO temp : all) {
                doctorShowTable.getItems().add(new DoctorTM(temp.getIdNumber(),temp.getMr_ms(),temp.getName(),temp.getName(),temp.getGen(),temp.getFree(),temp.getHos_free(),temp.getPhoneNumber()
                        ,temp.getGmail()));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void searchOnAction(KeyEvent keyEvent1) {
        FilteredList<DoctorTM> filteredData = new FilteredList<>(doctorShowTable.getItems(), b -> true);
        search.setOnKeyPressed(keyEvent -> {
            search.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate(DoctorTM -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (DoctorTM.getName().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (DoctorTM.getIdNumber().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else {
                        return false;
                    }
                });
            }));
            SortedList<DoctorTM> sortedList = new SortedList<>(filteredData);
            sortedList.comparatorProperty().bind(doctorShowTable.comparatorProperty());
            doctorShowTable.setItems(sortedList);
        });
    }
    public void onMouseClickOnAction(MouseEvent mouseEvent) throws IOException {
        int selectedIndex = doctorShowTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex!=-1) {
            DoctorTM doctor = doctorShowTable.getItems().get(selectedIndex);
            DoctorDTO doctorDTO = new DoctorDTO(doctor.getIdNumber(), doctor.getMr_ms(), doctor.getAdd_name(), doctor.getGen(), doctor.getFree(),doctor.getHos_fee(), doctor.getPhoneNumber(), doctor.getGmail());
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/addDoctor.fxml"));
            Parent parent = fxmlLoader.load();
            stage.setScene(new Scene(parent));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(addDoctor.getScene().getWindow());
            AddDoctorController addDoctorController = fxmlLoader.getController();
            //write fxml loge controller
            addDoctorController.manageDoctorFromController=this;
            addDoctorController.save.setText("Update");
            addDoctorController.setOldData(doctorDTO);
            stage.centerOnScreen();
            stage.show();
        }else new Alert(Alert.AlertType.ERROR,"Dont Have Data").show();
    }
    public void timeScheduleOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/manage-timeSchedule-doctor.fxml"));
        Parent parent = fxmlLoader.load();
        stage.setScene(new Scene(parent));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(search.getScene().getWindow());
        stage.centerOnScreen();
        stage.show();

    }
    public void printOnAction(ActionEvent actionEvent) {
        Thread thread=new Thread() {
            @SneakyThrows
            public void run() {
                String sql = "select * from doctor";
                JasperDesign jasdi = JRXmlLoader.load(this.getClass().getResourceAsStream("/report/doctor.jrxml"));
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
    public void hosFreeAndTestFreeAddOnAction(MouseEvent mouseEvent) throws IOException {
        Stage stage = new Stage();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/HosFreeAndTeztFree.fxml"));
        Parent parent = fxmlLoader.load();
        stage.setScene(new Scene(parent));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(search.getScene().getWindow());
        stage.centerOnScreen();
        stage.show();
    }
}

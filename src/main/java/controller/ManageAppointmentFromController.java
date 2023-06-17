package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import dao.custom.AppointnmentDAO;
import dao.custom.DoctorDAO;
import dao.custom.PatientDAO;
import dao.custom.impl.AppointnmentBAOImpl;
import dao.custom.impl.DoctorDAOImpl;
import dao.custom.impl.PatientDAOImpl;
import db.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import dto.AppintnmentDTO;
import dto.DoctorDTO;
import dto.PatientDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import org.controlsfx.control.Notifications;
import tdm.PatientTM;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ManageAppointmentFromController implements Initializable {
    public ComboBox<String> gen;
    public ComboBox<String> mr_ms;
    public ComboBox<String> doctorComBox;
    public Label doctorFree;
    public Label doctorName;
    public Label doctor;
    public TextField hospitalFree;
    public ComboBox<String> selectTestComBox;

    public JFXButton save;
    public TextField total;
    public TextField testFreeTexFeld;
    public Label testFreeLbl;
    public DoctorDAO doctorDAO = new DoctorDAOImpl();
    public PatientDAO patientDAO = new PatientDAOImpl();
    public AppointnmentDAO appointnmentBAO = new AppointnmentBAOImpl();
    public ArrayList<DoctorDTO> all;
    public TextField cashPaid;
    public JFXRadioButton medicalTest;
    public JFXRadioButton chane;
    public TableView<PatientTM> patientTable;
    public TableColumn patient_name;
    public TableColumn patient_id;
    public TextField idNumber;
    public TextField name;
    public TextField age;
    public Label invoiceNumber;
    public TextField phoneNumber;
    public TextField address;
    public TextField number;
    public Label appointnmentTime;
    public Label appointnmentDate;
    public TextField blance;

    public Label scanFreeLbl;
    public TextField scanFree;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setGenComBoXData();
        lodePapatientTable();
        setInvoiceNumber();
        setPatientId();


        appointnmentDate.setText(LocalDate.now() + "");
    }

    private void setInvoiceNumber() {
        String newAppId = generateNewOrderId();
        invoiceNumber.setText(newAppId);
    }

    private void lodePapatientTable() {
        patient_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        patient_name.setCellValueFactory(new PropertyValueFactory<>("name"));
        try {
            ArrayList<PatientDTO> all1 = patientDAO.getAll();
            for (PatientDTO temp : all1) {
                patientTable.getItems().add(new PatientTM(temp.getId(), temp.getName(), temp.getAge(), temp.getPhone(), temp.getGen()
                        , temp.getMr_ms(), temp.getMr_ms() + temp.getName()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setGenComBoXData() {
        ObservableList<String> dataTemp = FXCollections.observableArrayList();
        dataTemp.add("Male");
        dataTemp.add("Female");
        gen.setItems(dataTemp);

        ObservableList<String> dataTemp2 = FXCollections.observableArrayList();
        dataTemp2.add("Mr.");
        dataTemp2.add("Mrs.");
        dataTemp2.add("Miss.");
        dataTemp2.add("Rev.");
        mr_ms.setItems(dataTemp2);

        ObservableList<String> doctors = getDoctorNames();

        setDoctorCombox();

        selectTestComBox.getItems().add("Electrocardiogram (ECG)");
        selectTestComBox.getItems().add("x- Ray");
        selectTestComBox.getItems().add("Nebulize");
        selectTestComBox.getItems().add("Dressing ");
    }

    private void setDoctorCombox() {
        try {
            all = doctorDAO.getAll();
            for (DoctorDTO temp : all) {
                String name = temp.getName();
                doctorComBox.getItems().add(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private ObservableList<String> getDoctorNames() {
        return null;
    }

    public void saveOnAction(ActionEvent actionEvent) {
        boolean validate = validate();
        boolean medicalTestOPT = medicalTest.isSelected();
        boolean chaneOPT = chane.isSelected();
        if (validate) {
            if (medicalTestOPT) {
                if (validateTest()) {
                    PatientDTO patientDTO = getPatient();
                    AppintnmentDTO appointnmentDTO = getTestData();
                    try {
                        boolean save1 = appointnmentBAO.save(appointnmentDTO, patientDTO);
                        if (save1) {
                            Notifications.create()
                                    .graphic(new ImageView(new Image("/view/assests/images/icons8-ok-48.png")))
                                    .text("Appointnment success ")
                                    .title("success")
                                    .hideAfter(Duration.seconds(5))
                                    .position(Pos.TOP_RIGHT)
                                    .show();
                            printBill(appointnmentDTO.getId(), "Test", this.getClass().getResourceAsStream("/report/theerakaMailBill2023.06.10findtobebill.jrxml"));
                            Clear_Tex_Field();
                            lodePapatientTable();


                        } else {
                            Notifications.create()
                                    .graphic(new ImageView(new Image("/view/assests/images/icons8-cancel-96.png")))
                                    .text("Appointnment failed ")
                                    .title("Fall")
                                    .hideAfter(Duration.seconds(5))
                                    .position(Pos.TOP_RIGHT)
                                    .show();
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                    }
                }
            } else if (chaneOPT) {
                if (validateChanel()) {
                    PatientDTO patientDTO = getPatient();
                    AppintnmentDTO appointnmentDTO = getChanelData();
                    try {
                        boolean save1 = appointnmentBAO.save(appointnmentDTO, patientDTO);
                        if (save1) {
                            Notifications.create()
                                    .graphic(new ImageView(new Image("/view/assests/images/icons8-ok-48.png")))
                                    .text("Appointnment success ")
                                    .title("success")
                                    .hideAfter(Duration.seconds(5))
                                    .position(Pos.TOP_RIGHT)
                                    .show();
                            if (Double.parseDouble(scanFree.getText()) > 0) {

                                printBill(appointnmentDTO.getId(), "Doctor", this.getClass().getResourceAsStream("/report/therakaScaneBillbill.jrxml"));
                                Clear_Tex_Field();
                                lodePapatientTable();
                            } else {
                                printBill(appointnmentDTO.getId(), "Doctor", this.getClass().getResourceAsStream("/report/theerakaMailBill2023.06.10findtobebill.jrxml"));
                                Clear_Tex_Field();
                                lodePapatientTable();

                            }


                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                        new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                    }

                }


            }
        }
    }

    private void setPatientId() {
        idNumber.setText(generateNewOrderId2());
    }

    public String generateNewOrderId2() {
        String lastId = null;
        try {
            lastId = patientDAO.getNewID();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        String id;
        if (lastId == null) {
            id = "00-001";
        } else {
            String[] split = lastId.split("[00][-]");
            int i = Integer.parseInt(split[1]);
            i++;
            id = String.format("00-%03d", i);
        }
        return id;
    }


    private void printBill(String orderId, String testorDoctro, InputStream resourceAsStream) {

        try {
            Connection connection = connection = DbConnection.getInstance().getConnection();
            String query = "select ap.scanFee,ap.date,ap.id,ap.number,p.phoneNumber,ap.patientIdNumber,ap.doutor_name,ap.doctor_free,ap.patient_Name,ap.hospital_free,ap.tot_amount,ap.chash_paod ,p.age,p.gen from appointnment ap INNER JOIN patient p ON p.patientIdNumber=ap.patientIdNumber WHERE ap.id=?;";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, orderId);
            ResultSet resultSet = preparedStatement.executeQuery();
            JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(resultSet);
            JasperDesign jasperDesign = JRXmlLoader.load(resourceAsStream);
            JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);


            Map<String, Object> parameters = new HashMap<>();

            // crate balance
            double v = Double.parseDouble(cashPaid.getText()) - Double.parseDouble(total.getText());

            parameters.put("balance", v);
            parameters.put("doctorortest", testorDoctro);

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, resultSetDataSource);

            net.sf.jasperreports.view.JasperViewer.viewReport(jasperPrint, false);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (JRException e) {
            e.printStackTrace();
        }

    }

    private void Clear_Tex_Field() {
        patientTable.getItems().clear();
        setInvoiceNumber();
        idNumber.clear();
        mr_ms.getSelectionModel().isEmpty();
        name.clear();
        age.clear();
        number.clear();
        phoneNumber.clear();
        address.clear();
        hospitalFree.clear();
        total.clear();
        cashPaid.clear();
        testFreeTexFeld.clear();
        selectTestComBox.getSelectionModel().isEmpty();
        blance.clear();
        scanFree.clear();
        setPatientId();

    }

    public PatientDTO getPatient() {
        return new PatientDTO(idNumber.getText(), mr_ms.getSelectionModel().getSelectedItem(), name.getText(), Integer.parseInt(age.getText()), phoneNumber.getText(),
                gen.getSelectionModel().getSelectedItem());
    }

    private AppintnmentDTO getChanelData() {
        return new AppintnmentDTO(generateNewOrderId(), number.getText(), idNumber.getText(), doctorComBox.getSelectionModel().getSelectedItem()
                , Double.parseDouble(doctorFree.getText()), name.getText(), Double.parseDouble(hospitalFree.getText()), Double.parseDouble(total.getText()),
                Double.parseDouble(cashPaid.getText() + 00), Double.parseDouble(scanFree.getText() + 00));
    }

    public String generateNewOrderId() {
        String lastId = null;
        try {
            lastId = appointnmentBAO.getNewID();
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        String id;
        if (lastId == null) {
            id = "000-0001";
        } else {
            String[] split = lastId.split("[000][-]");
            int i = Integer.parseInt(split[1]);
            i++;
            id = String.format("000-%04d", i);
        }
        return id;
    }

    private AppintnmentDTO getTestData() {
        return new AppintnmentDTO(generateNewOrderId(), number.getText(), idNumber.getText(), selectTestComBox.getSelectionModel().getSelectedItem()
                , Double.parseDouble(testFreeTexFeld.getText()), name.getText(), Double.parseDouble(hospitalFree.getText()), Double.parseDouble(total.getText()),
                Double.parseDouble(cashPaid.getText()));
    }

    public void logOutOnAction(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) gen.getScene().getWindow();
        stage.close();
        Stage stage1 = new Stage();
        stage1.setScene(new Scene(FXMLLoader.load(getClass().getResource("/view/main-form.fxml"))));
        stage1.setResizable(false);
        stage1.centerOnScreen();
        stage1.show();

    }

    public void doctorSelectOnActio(ActionEvent actionEvent) {
        int selectedItem = doctorComBox.getSelectionModel().getSelectedIndex();
        DoctorDTO doctorDTO = all.get(selectedItem);
        doctorFree.setText(doctorDTO.getFree() + "");
        scanFree.requestFocus();


        if (Double.parseDouble(doctorDTO.getHos_free() + "") > 0) {
            hospitalFree.setText(doctorDTO.getHos_free() + "");
        } else hospitalFree.setText("00");
    }

    public void hospitelFreeOnActio(ActionEvent actionEvent) {
        if (!hospitalFree.getText().matches("^[0-9]+[.]?[0-9]*$")) {
            new Alert(Alert.AlertType.ERROR, "Check Hospital Free").show();
            hospitalFree.requestFocus();
        } else {
            if (chane.isSelected()) {
                System.out.println(doctorComBox.getValue());
                if (doctorComBox.getValue() != null) {
                    double d_free = Double.parseDouble(doctorFree.getText()) + 00;
                    double h_free = Double.parseDouble(hospitalFree.getText()) + 00;


                    double s_free = 00;

                    if (Double.parseDouble(scanFree.getText()) > 0) {
                        s_free = Double.parseDouble(scanFree.getText());
                    }


                    total.setText(d_free + h_free + s_free + "");
                } else {
                    new Alert(Alert.AlertType.ERROR, "plz select Doctor").show();
                    doctorComBox.requestFocus();
                }

            } else if (medicalTest.isSelected()) {
                if (testFreeTexFeld.getText().equals("")) {
                    new Alert(Alert.AlertType.ERROR, "fill test fee").show();
                    return;
                }
                double t_free = 0;
                if (Double.parseDouble(testFreeTexFeld.getText()) > 0) {
                    t_free = Double.parseDouble(testFreeTexFeld.getText());
                }
                double h_free = Double.parseDouble(hospitalFree.getText());
                total.setText(t_free + h_free + "");
            }
            cashPaid.requestFocus();
        }
    }

    public void medicalTestOnAction(ActionEvent actionEvent) {

        // setVisible true
        testFreeTexFeld.setVisible(true);
        testFreeLbl.setVisible(true);
        selectTestComBox.setVisible(true);
        // setVisible false
        scanFreeLbl.setVisible(false);
        scanFree.setVisible(false);
        doctor.setVisible(false);
        doctorFree.setVisible(false);
        doctorName.setVisible(false);
        doctorComBox.setVisible(false);


    }

    public void chanelTestOnAction(ActionEvent actionEvent) {
        // setVisible true
        scanFreeLbl.setVisible(true);
        scanFree.setVisible(true);
        doctor.setVisible(true);
        doctorFree.setVisible(true);
        doctorName.setVisible(true);
        doctorComBox.setVisible(true);

        // setVisible false
        testFreeLbl.setVisible(false);
        testFreeTexFeld.setVisible(false);
        selectTestComBox.setVisible(false);
    }

    public void enterOnAction(ActionEvent actionEvent) {
        if (selectTestComBox.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "Plz Select Test Nme").show();
            selectTestComBox.requestFocus();
            return;
        }
        if (!testFreeTexFeld.getText().matches("^[0-9]+[.]?[0-9]*$")) {
            new Alert(Alert.AlertType.ERROR, "Plz Check Test Free").show();
            testFreeTexFeld.requestFocus();
            return;
        }
        hospitalFree.requestFocus();
    }

    public void cashPaidOnAction(ActionEvent actionEvent) {
        if (!cashPaid.getText().matches("^[0-9]+[.]?[0-9]*$") || Double.parseDouble(total.getText()) > Double.parseDouble(cashPaid.getText())) {
            new Alert(Alert.AlertType.ERROR, "Plz Check Cash Pail Amount").show();
            cashPaid.requestFocus();
            return;
        }
        blance.setText(Double.parseDouble(cashPaid.getText()) - Double.parseDouble(total.getText()) + "");
        save.requestFocus();
    }

    public void idNumberOnKeyAction(KeyEvent keyEvent) {
        setSearchFilter(idNumber);
    }

    public void nameOnKeyAction(KeyEvent keyEvent) {
        setSearchFilter(name);
    }

    void setSearchFilter(TextField search) {
        FilteredList<PatientTM> filteredData = new FilteredList<>(patientTable.getItems(), b -> true);

        search.setOnKeyPressed(keyEvent -> {
            search.textProperty().addListener(((observableValue, oldValue, newValue) -> {
                filteredData.setPredicate(PatientTM -> {
                    if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                        return true;
                    }
                    String searchKeyWord = newValue.toLowerCase();
                    if (PatientTM.getId().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true;
                    } else if (PatientTM.getName().toLowerCase().indexOf(searchKeyWord) > -1) {
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

    public void setPationDataOnMouseClickAction(MouseEvent mouseEvent) {
        int selectedIndex = patientTable.getSelectionModel().getSelectedIndex();
        PatientTM patientTM = patientTable.getItems().get(selectedIndex);
        name.setText(patientTM.getName());
        mr_ms.getSelectionModel().select(patientTM.getMr_ms());
        idNumber.setText(patientTM.getId());
        age.setText(patientTM.getAge() + "");
        gen.getSelectionModel().select(patientTM.getGen());
        phoneNumber.setText(patientTM.getPhone());
    }

    private boolean validate() {
        if (idNumber.getText().matches("")) {
            new Alert(Alert.AlertType.ERROR, "Invalid ID Number").show();
            idNumber.requestFocus();
            return false;
        } else if (name.getText().matches("")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Name ").show();
            name.requestFocus();
            return false;
        } else if (mr_ms.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, " Select  Mr./Mrs....").show();
            mr_ms.requestFocus();
            return false;
        } else if (!age.getText().matches("^(?:[1-9]|[1-9][0-9])$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Age ").show();
            age.requestFocus();
            return false;
        } else if (gen.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "Select gender ").show();
            gen.requestFocus();
            return false;
        } else if (!phoneNumber.getText().matches("^0\\d{9}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid  Phone Number").show();
            phoneNumber.requestFocus();
            return false;
        } else return true;
    }

    private boolean validateChanel() {
        if (doctorComBox.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "Select Doctor").show();
            doctorComBox.requestFocus();
            return false;
        } else if (!hospitalFree.getText().matches("^[0-9]+[.]?[0-9]*$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Hospital Free").show();
            hospitalFree.requestFocus();
            return false;
        } else if (!cashPaid.getText().matches("^[0-9]+[.]?[0-9]*$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid CashPaid Free").show();
            cashPaid.requestFocus();
            return false;
        }
        return true;
    }

    private boolean validateTest() {
        if (selectTestComBox.getValue() == null) {
            new Alert(Alert.AlertType.ERROR, "Select Test").show();
            return false;
        } else if (!testFreeTexFeld.getText().matches("^[0-9]+[.]?[0-9]*$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Test Free").show();
            testFreeTexFeld.requestFocus();
            return false;
        } else return true;
    }

    public void scaneFreeOnAction(ActionEvent actionEvent) {
        hospitalFree.requestFocus();
    }

    public void hospitelFreeOnActio2(KeyEvent keyEvent) {

        if (chane.isSelected()) {
            System.out.println(doctorComBox.getValue());
            if (doctorComBox.getValue() != null) {
                double d_free = Double.parseDouble(doctorFree.getText()) + 00;
                double h_free = Double.parseDouble(hospitalFree.getText()) + 00;


                double s_free = 00;

                if (Double.parseDouble(scanFree.getText()) > 0) {
                    s_free = Double.parseDouble(scanFree.getText());
                }


                total.setText(d_free + h_free + s_free + "");
            } else {
                new Alert(Alert.AlertType.ERROR, "plz select Doctor").show();
                doctorComBox.requestFocus();
            }

        } else if (medicalTest.isSelected()) {
            if (testFreeTexFeld.getText().equals("")) {
                new Alert(Alert.AlertType.ERROR, "fill test fee").show();
                return;
            }
            double t_free = 0;
            if (Double.parseDouble(testFreeTexFeld.getText()) > 0) {
                t_free = Double.parseDouble(testFreeTexFeld.getText());
            }
            double h_free = Double.parseDouble(hospitalFree.getText());
            total.setText(t_free + h_free + "");
        }


    }
}

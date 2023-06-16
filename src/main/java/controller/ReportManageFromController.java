package controller;

import dao.custom.AppointnmentDAO;
import dao.custom.DoctorDAO;
import dao.custom.impl.AppointnmentBAOImpl;
import dao.custom.impl.DoctorDAOImpl;
import db.DbConnection;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.SneakyThrows;
import model.AppintnmentDTO;
import model.DoctorDTO;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.design.JRDesignQuery;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;
import util.SendMailWithAttachment;

import java.io.File;
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

public class ReportManageFromController implements Initializable {

    public TextField search;
    public TableColumn patient_Name;

    public TableColumn doctorName;

    public AppointnmentDAO appointnmentDAO = new AppointnmentBAOImpl();
    public TableView<AppintnmentDTO> table;
    public TableColumn invoice;
    public TableColumn free;
    public TableColumn osFree;
    public TableColumn tot;
    public TableColumn date;
    public DatePicker dateP_M_select;
    public TableColumn scan;
    public DatePicker dateMail;
    public ComboBox<String> selectDoctorName;

    public void printOnActinon(ActionEvent actionEvent) {
        LocalDate value = dateP_M_select.getValue();
        if (value == null) {
            new Alert(Alert.AlertType.ERROR, "select Date").show();
            return;
        } else {
            System.out.println(value);
            Thread thread = new Thread() {
                @SneakyThrows
                public void run() {
                    String sql = "select * from appointnment where date='" + value + "';";
                    JasperDesign jasdi = JRXmlLoader.load(this.getClass().getResourceAsStream("/report/thirakaSumary.jrxml"));
                    JRDesignQuery newQuery = new JRDesignQuery();
                    newQuery.setText(sql);
                    jasdi.setQuery(newQuery);
                    JasperReport js = JasperCompileManager.compileReport(jasdi);
                    JasperPrint jp = JasperFillManager.fillReport(js, null, DbConnection.getInstance().getConnection());
                    JasperViewer.viewReport(jp, false);
                }
            };
            thread.start();
        }

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
        DoctorDAO doctorDAO = new DoctorDAOImpl();
        try {
            ArrayList<DoctorDTO> all = doctorDAO.getAll();
            for (DoctorDTO temp :
                    all) {
                selectDoctorName.getItems().add(temp.getName());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setTableData() {
        invoice.setCellValueFactory(new PropertyValueFactory<>("id"));
        patient_Name.setCellValueFactory(new PropertyValueFactory<>("patient_Name"));
        doctorName.setCellValueFactory(new PropertyValueFactory<>("doutor_nameOrTestName"));
        free.setCellValueFactory(new PropertyValueFactory<>("doctor_freeOrTestFree"));
        osFree.setCellValueFactory(new PropertyValueFactory<>("hospital_free"));
        tot.setCellValueFactory(new PropertyValueFactory<>("tot_amount"));
        date.setCellValueFactory(new PropertyValueFactory<>("date"));
        scan.setCellValueFactory(new PropertyValueFactory<>("scanFee"));
        table.getItems().clear();
        ArrayList<AppintnmentDTO> all = getPiltarData();
        for (AppintnmentDTO appintnmentDTO : all) {
            table.getItems().add(appintnmentDTO);
        }

    }

    public ArrayList<AppintnmentDTO> getPiltarData() {
        ArrayList<String> names = new ArrayList<>();
        ArrayList<AppintnmentDTO> allAll = new ArrayList<>();

        DoctorDAO doctorDAO = new DoctorDAOImpl();
        try {
            ArrayList<DoctorDTO> all = doctorDAO.getAll();
            for (DoctorDTO temp : all) {
                names.add(temp.getName());
            }
            names.add("Electrocardiogram (ECG)");
            names.add("x- Ray");
            names.add("Nebulize");
            names.add("Dressing ");

            for (String name : names) {
                ArrayList<AppintnmentDTO> all2 = appointnmentDAO.getAll2(name);
                for (AppintnmentDTO data : all2) {
                    allAll.add(data);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return allAll;
    }

    public void mailOnAction(ActionEvent actionEvent) {
        LocalDate value = dateP_M_select.getValue();
        if (value == null) {
            new Alert(Alert.AlertType.ERROR, "select Date").show();
            return;
        } else {
            Thread thread = new Thread() {
                @SneakyThrows
                public void run() {
                    try {
                        String sql = "select * from appointnment where date='" + value + "';";
                        JasperDesign jasdi = JRXmlLoader.load(this.getClass().getResourceAsStream("/report/thirakaSumary.jrxml"));
                        JRDesignQuery newQuery = new JRDesignQuery();
                        newQuery.setText(sql);

                        jasdi.setQuery(newQuery);
                        JasperReport js = JasperCompileManager.compileReport(jasdi);
                        JasperPrint jp = JasperFillManager.fillReport(js, null, DbConnection.getInstance().getConnection());

                        Platform.runLater(() -> {
                            FileChooser fileChooser = new FileChooser();
                            fileChooser.setTitle("Save Excel File");
                            fileChooser.getExtensionFilters().addAll(
                                    new FileChooser.ExtensionFilter("Pdf", "*.pdf")
                            );

                            File file1 = fileChooser.showSaveDialog(search.getScene().getWindow());
                            if (file1 != null) {
                                String absolutePath1 = file1.getAbsolutePath();
                                try {
                                    JasperExportManager.exportReportToPdfFile(jp, absolutePath1);
                                } catch (JRException e) {
                                    e.printStackTrace();
                                }
                                File file = new File(absolutePath1);
                                new SendMailWithAttachment(file, "" + value + "\nDaily Summary  ");
                            }
                        });
                    } catch (JRException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            };
            thread.start();
        }
    }

    public void dateP_M_selectOnAction(ActionEvent actionEvent) {
        search.setText(dateP_M_select.getValue() + "");
        FilteredList<AppintnmentDTO> filteredData = new FilteredList<>(table.getItems(), b -> true);

        search.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate(AppintnmentDTO -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String searchKeyWord = newValue.toLowerCase();

                if (AppintnmentDTO.getPatient_Name().toLowerCase().indexOf(searchKeyWord) > -1) {
                    return true;
                } else if (AppintnmentDTO.getDoutor_nameOrTestName().toLowerCase().indexOf(searchKeyWord) > -1) {
                    return true;
                } else if (AppintnmentDTO.getPatientIdNumber().toLowerCase().indexOf(searchKeyWord) > -1) {
                    return true;
                } else if (AppintnmentDTO.getId().toLowerCase().indexOf(searchKeyWord) > -1) {
                    return true;
                } else if (AppintnmentDTO.getDate().toString().toLowerCase().indexOf(searchKeyWord) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        }));

        SortedList<AppintnmentDTO> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }

    public void mouseClickOnAction(MouseEvent mouseEvent) {
        AppintnmentDTO selectedItem = table.getSelectionModel().getSelectedItem();
        String id = selectedItem.getId();
        cashPaid = selectedItem.getChash_paod() + "";
        total = selectedItem.getTot_amount() + "";
        String scanFee = selectedItem.getScanFee()+"";
        String doutorNameOrTestName = selectedItem.getDoutor_nameOrTestName();

        System.out.println(scanFee);
        switch (doutorNameOrTestName){
            case "Electrocardiogram (ECG)":printBill(selectedItem.getId(), "Test",this.getClass().getResourceAsStream("/report/theerakaMailBill2023.06.10findtobebill.jrxml"));break;
            case "x- Ray":printBill(selectedItem.getId(), "Test",this.getClass().getResourceAsStream("/report/theerakaMailBill2023.06.10findtobebill.jrxml"));break;
            case "Nebulize":printBill(selectedItem.getId(), "Test",this.getClass().getResourceAsStream("/report/theerakaMailBill2023.06.10findtobebill.jrxml"));break;
            case "Dressing ":printBill(selectedItem.getId(), "Test",this.getClass().getResourceAsStream("/report/theerakaMailBill2023.06.10findtobebill.jrxml"));break;

            default:if (scanFee.equals("0.0")) {
                printBill(selectedItem.getId(), "Doctor",this.getClass().getResourceAsStream("/report/theerakaMailBill2023.06.10findtobebill.jrxml"));
            }else {
                printBill(selectedItem.getId(), "Doctor",this.getClass().getResourceAsStream("/report/therakaScaneBillbill.jrxml"));
            }
        }
    }
    private String cashPaid;
    private String total;

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
            double v = Double.parseDouble(cashPaid) - Double.parseDouble(total);

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

    public void searchOnAction(KeyEvent keyEvent) {
        FilteredList<AppintnmentDTO> filteredData = new FilteredList<>(table.getItems(), b -> true);
        search.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            filteredData.setPredicate(AppintnmentDTO -> {
                if (newValue.isEmpty() || newValue.isBlank() || newValue == null) {
                    return true;
                }
                String searchKeyWord = newValue.toLowerCase();

                if (AppintnmentDTO.getPatient_Name().toLowerCase().indexOf(searchKeyWord) > -1) {
                    return true;
                } else if (AppintnmentDTO.getDoutor_nameOrTestName().toLowerCase().indexOf(searchKeyWord) > -1) {
                    return true;
                } else if (AppintnmentDTO.getPatientIdNumber().toLowerCase().indexOf(searchKeyWord) > -1) {
                    return true;
                } else if (AppintnmentDTO.getId().toLowerCase().indexOf(searchKeyWord) > -1) {
                    return true;
                } else if (AppintnmentDTO.getDate().toString().toLowerCase().indexOf(searchKeyWord) > -1) {
                    return true;
                } else {
                    return false;
                }
            });
        }));

        SortedList<AppintnmentDTO> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }

    public void setectPrintMailOnAction(ActionEvent actionEvent) {
        if (dateMail.getValue()==null) {
            new Alert(Alert.AlertType.ERROR,"Select Date").show();
            return;
        }
        if (selectDoctorName.getSelectionModel().getSelectedItem()==null) {
            new Alert(Alert.AlertType.ERROR,"Select Doctor").show();
            return;
        }
        Thread thread = new Thread() {
            @SneakyThrows
            public void run() {
                try {
                    System.out.println(dateMail.getValue());
                    System.out.println(selectDoctorName.getSelectionModel().getSelectedItem());

                    Connection connection = DbConnection.getInstance().getConnection();
                    String query = "select *  from appointnment where date=? AND doutor_name=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, dateMail.getValue() + "");
                    preparedStatement.setString(2, selectDoctorName.getSelectionModel().getSelectedItem());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(resultSet);
                    JasperDesign jasperDesign = JRXmlLoader.load(this.getClass().getResourceAsStream("/report/Select  Summery Print.jrxml"));
                    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);
                    Map<String, Object> parameters = new HashMap<>();
                    double v = getTot();
                    parameters.put("tot", v);
                    JasperPrint jp = JasperFillManager.fillReport(jasperReport, parameters, resultSetDataSource);
                    Platform.runLater(() -> {
                        FileChooser fileChooser = new FileChooser();
                        fileChooser.setTitle("Save Excel File");
                        fileChooser.getExtensionFilters().addAll(
                                new FileChooser.ExtensionFilter("Pdf", "*.pdf")
                        );
                        File file1 = fileChooser.showSaveDialog(dateMail.getScene().getWindow());
                        if (file1 != null) {
                            String absolutePath1 = file1.getAbsolutePath();
                            try {
                                JasperExportManager.exportReportToPdfFile(jp, absolutePath1);
                            } catch (JRException e) {
                                e.printStackTrace();
                            }
                            File file = new File(absolutePath1);
                            String gmailDoctor = getGmailDoctor();
                            if (gmailDoctor.equals("00")) {
                                return;
                            }
                            new SendMailWithAttachment(file, LocalDate.now() + "  Report ",gmailDoctor);
                        }
                    });
                } catch (JRException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }

    public String getGmailDoctor() {
        String newGmail="00";
        DoctorDAO doctorDAO=new DoctorDAOImpl();
        try {
            newGmail = doctorDAO.yetNameGetGmail(selectDoctorName.getSelectionModel().getSelectedItem());
            System.out.println(newGmail);
            if (newGmail.equals("00")) {
                new Alert(Alert.AlertType.ERROR,"Not A Gmail").show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return newGmail;
    }

    public void setectPrintOnAction(ActionEvent actionEvent) {
        if (dateMail.getValue()==null) {
            new Alert(Alert.AlertType.ERROR,"Select Date").show();
            return;
        }
        if (selectDoctorName.getSelectionModel().getSelectedItem()==null) {
            new Alert(Alert.AlertType.ERROR,"Select Doctor").show();
            return;
        }
        Thread thread = new Thread() {
            @SneakyThrows
            public void run() {
                try {
                    System.out.println(dateMail.getValue());
                    System.out.println(selectDoctorName.getSelectionModel().getSelectedItem());

                    Connection connection = DbConnection.getInstance().getConnection();
                    String query = "select *  from appointnment where date=? AND doutor_name=?";
                    PreparedStatement preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, dateMail.getValue() + "");
                    preparedStatement.setString(2, selectDoctorName.getSelectionModel().getSelectedItem());
                    ResultSet resultSet = preparedStatement.executeQuery();
                    JRResultSetDataSource resultSetDataSource = new JRResultSetDataSource(resultSet);
                    JasperDesign jasperDesign = JRXmlLoader.load(this.getClass().getResourceAsStream("/report/Select  Summery Print.jrxml"));
                    JasperReport jasperReport = JasperCompileManager.compileReport(jasperDesign);


                    Map<String, Object> parameters = new HashMap<>();
                    double v = getTot();
                    parameters.put("tot", v);

                    JasperPrint jp = JasperFillManager.fillReport(jasperReport, parameters, resultSetDataSource);
                    net.sf.jasperreports.view.JasperViewer.viewReport(jp, false);

                } catch (JRException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        };
        thread.start();
    }

    private double getTot() {
        double tot = 00;
        try {
            ArrayList<AppintnmentDTO> all3 = appointnmentDAO.getAll3(selectDoctorName.getSelectionModel().getSelectedItem() + "", dateMail.getValue() + "");
            for (AppintnmentDTO temp :
                    all3) {
                tot += (temp.getDoctor_freeOrTestFree() + temp.getScanFee() + 00);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return tot;
    }
}

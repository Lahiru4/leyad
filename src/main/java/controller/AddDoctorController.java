package controller;

import com.jfoenix.controls.JFXButton;
import dao.custom.DoctorDAO;
import dao.custom.impl.DoctorDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import dto.DoctorDTO;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddDoctorController implements Initializable {

    public TextField doctorId;
    public TextField name;

    public TextField phoneNumber;
    public TextField free;
    public TextField gmail;
    public ComboBox<String> gen;
    public ComboBox<String> mr_ms;
    public ManageDoctorFromController manageDoctorFromController;
    public JFXButton save;


    public DoctorDAO doctorDAO=new DoctorDAOImpl();
    public TextField hosFee;


    public void saveOnAction(ActionEvent actionEvent) {
        if (validate()) {
            DoctorDTO doctorDTO = collectData();
            if (save.getText().equals("Save")) {


                try {
                    boolean add = doctorDAO.add(doctorDTO);
                    if (add) {
                        new Alert(Alert.AlertType.INFORMATION,"success").show();
                        manageDoctorFromController.setTableData();
                        save.getScene().getWindow().hide();
                    }else {
                        new Alert(Alert.AlertType.ERROR,"failed").show();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
                }
            } else {
                try {
                    boolean update = doctorDAO.update(doctorDTO);
                    if (update) {
                        new Alert(Alert.AlertType.INFORMATION,"success").show();
                        manageDoctorFromController.setTableData();
                        save.getScene().getWindow().hide();
                    }else {
                        new Alert(Alert.AlertType.ERROR,"failed").show();
                    }

                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
                }

            }
        }
    }

    private boolean validate() {
        if (mr_ms.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "Select  Gender").show();
            gen.requestFocus();
            return false;
        }
        if (name.getText().matches("")) {
            new Alert(Alert.AlertType.ERROR, "Fill The" +
                    " Name").show();
            name.requestFocus();
            return false;
        } else if (gen.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "Select  Gender").show();
            gen.requestFocus();
            return false;
        } else if (!free.getText().matches("^[0-9]+[.]?[0-9]*$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid Doctor Free ").show();
            free.requestFocus();
            return false;
        } else if (!phoneNumber.getText().matches("^(?:0|94|\\+94)?(?:(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|41|45|47|51|52|54|55|57|63|65|66|67|81|912)(0|2|3|4|5|7|9)|7(0|1|2|5|6|7|8)\\d)\\d{6}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid  Phone Number ").show();
            phoneNumber.requestFocus();
            return false;
        } else return true;
    }

    public DoctorDTO collectData() {
        return new DoctorDTO(doctorId.getText(), mr_ms.getSelectionModel().getSelectedItem() + "", name.getText(), gen.getSelectionModel().getSelectedItem() + "", Double.parseDouble(free.getText()),Double.parseDouble(hosFee.getText()), phoneNumber.getText(), gmail.getText());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setComBoxData();
    }

    public void setOldData(DoctorDTO dto) {
        hosFee.setText(dto.getHos_free()+"");
        doctorId.setText(dto.getIdNumber());
        mr_ms.getSelectionModel().select(dto.getMr_ms());
        name.setText(dto.getName());
        gen.getSelectionModel().select(dto.getGen());
        free.setText(dto.getFree() + "");
        phoneNumber.setText(dto.getPhoneNumber());
        gmail.setText(dto.getGmail());
    }


    private void setComBoxData() {
        mr_ms.getItems().add("Doctor: ");


        gen.getItems().add("Male");
        gen.getItems().add("Female");


    }
    public String generateNewOrderId() {
        String lastId = null;
        try {
            lastId = doctorDAO.getNewID();
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


    public void setNewId() {
        doctorId.setText(generateNewOrderId());
    }
}

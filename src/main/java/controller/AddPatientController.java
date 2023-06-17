package controller;

import com.jfoenix.controls.JFXButton;
import dao.custom.PatientDAO;
import dao.custom.impl.PatientDAOImpl;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import dto.PatientDTO;
import org.controlsfx.control.Notifications;
import tdm.PatientTM;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class AddPatientController implements Initializable {

    public ComboBox<String> mr_ms;
    public ComboBox<String> gen;
    public TextField patient;
    public TextField name;
    public TextField phoneNumber;
    public TextField age;
    public TextField address;
    public PatientManageFromController patientManageFromController;
    public JFXButton save;
    PatientDAO patientDTOCrudDAO = new PatientDAOImpl();

    public void saveOnAction(ActionEvent actionEvent) {
        if (save.getText().equals("Save")) {
            boolean validate = validate();
            if (validate) {
                PatientDTO patientDTO = collectData();
                try {
                    boolean add = patientDTOCrudDAO.add(patientDTO);
                    if (add) {
                        patientManageFromController.setTableData();
                        Notifications.create()
                                .graphic(new ImageView(new Image("/view/assests/images/icons8-ok-48.png")))
                                .text(" success ")
                                .title("success")
                                .hideAfter(Duration.seconds(5))
                                .position(Pos.TOP_RIGHT)
                                .show();
                        mr_ms.getScene().getWindow().hide();

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                    new Alert(Alert.AlertType.ERROR,e.getMessage()).show();
                }
            }


        }else {
            boolean validate = validate();
            if (validate) {
                PatientDTO patientDTO = collectData();
                try {
                    boolean update = patientDTOCrudDAO.update(patientDTO);
                    if (update) {
                        patientManageFromController.setTableData();
                        Notifications.create()
                                .graphic(new ImageView(new Image("/view/assests/images/icons8-ok-48.png")))
                                .text(" success ")
                                .title("success")
                                .hideAfter(Duration.seconds(5))
                                .position(Pos.TOP_RIGHT)
                                .show();
                        mr_ms.getScene().getWindow().hide();
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

    private PatientDTO collectData() {
        return new PatientDTO(patient.getText(), mr_ms.getSelectionModel().getSelectedItem(), name.getText(), Integer.parseInt(age.getText()), phoneNumber.getText(), gen.getSelectionModel().getSelectedItem());
    }

    private boolean validate() {
        if (patient.getText().matches("")) {
            new Alert(Alert.AlertType.ERROR, "Invalid ID").show();
            patient.requestFocus();
            return false;
        } else if (mr_ms.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "All Select Fill").show();
            gen.requestFocus();
            return false;
        }
        if (!name.getText().matches("^([ \\u00c0-\\u01ffa-zA-Z'\\-]{5,})+$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid name").show();
            name.requestFocus();
            return false;
        } else if (gen.getSelectionModel().getSelectedItem() == null) {
            new Alert(Alert.AlertType.ERROR, "Select  Gender").show();
            gen.requestFocus();
            return false;
        } else if (!phoneNumber.getText().matches("^(?:0|94|\\+94)?(?:(11|21|23|24|25|26|27|31|32|33|34|35|36|37|38|41|45|47|51|52|54|55|57|63|65|66|67|81|912)(0|2|3|4|5|7|9)|7(0|1|2|5|6|7|8)\\d)\\d{6}$")) {
            new Alert(Alert.AlertType.ERROR, "Invalid  Phone Number ").show();
            phoneNumber.requestFocus();
            return false;
        } else return true;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mr_ms.getItems().add("Mr.");
        mr_ms.getItems().add("Mrs.");
        mr_ms.getItems().add("Miss.");
        mr_ms.getItems().add("Rev.");

        gen.getItems().add("Male");
        gen.getItems().add("Female");
    }

    public void setId() {
        patient.setText(generateNewOrderId());
    }
    public String generateNewOrderId() {
        String lastId = null;
        try {
            lastId = patientDTOCrudDAO.getNewID();
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

    public void setOldData(PatientTM patientTM) {
        mr_ms.getSelectionModel().select(patientTM.getMr_ms());
        gen.getSelectionModel().select(patientTM.getGen());
        patient.setText(patientTM.getId());
        name.setText(patientTM.getAddName());
        phoneNumber.setText(patientTM.getPhone());
        age.setText(patientTM.getAge() + "");

    }
}

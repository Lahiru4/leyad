package controller;

import bo.custom.DoctorBO;
import bo.custom.impl.DoctorBOImpl;
import com.jfoenix.controls.JFXRadioButton;
import dao.custom.DoctorDAO;
import dao.custom.impl.DoctorDAOImpl;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import dto.DoctorDTO;
import dto.TimeDTO;
import tdm.DoctorTM;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddTimeAndDayController implements Initializable {
    public JFXRadioButton monDay;
    public JFXRadioButton tuesday;
    public JFXRadioButton wednesday;
    public JFXRadioButton thursday;
    public JFXRadioButton friday;
    public JFXRadioButton saturday;
    public JFXRadioButton sunday;
    public TextField m_f;
    public TextField m_to;
    public TextField tu_f;
    public TextField tu_to;
    public TextField we_f;
    public TextField we_to;
    public TextField thu_f;
    public TextField thu_to;
    public TextField fr_f;
    public TextField fr_to;
    public TextField sat_f;
    public TextField sat_to;
    public TextField sun_f;
    public TextField sun_to;
    public ComboBox<String> m_com;
    public ComboBox<String> tu_com;
    public ComboBox<String> we_com;
    public ComboBox<String> tur_com;
    public ComboBox<String> fri_com;
    public ComboBox<String> satu_com;
    public ComboBox<String> sun_com;
    public TableView<DoctorTM> doctor;

    public TableColumn name;
    public DoctorDAO doctorDAO = new DoctorDAOImpl();
    public DoctorBO doctorBO = new DoctorBOImpl();
    public Label nameDoctor;
    public ManageTimeScheduleDoctorController manageTimeScheduleDoctorController;
    public TextField search;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDoctorNameTable();
        setCom_am_pm_data();
    }

    private void setCom_am_pm_data() {
        m_com.getItems().add(".am");
        m_com.getItems().add(".pm");

        tu_com.getItems().add(".am");
        tu_com.getItems().add(".pm");

        we_com.getItems().add(".am");
        we_com.getItems().add(".pm");

        tur_com.getItems().add(".am");
        tur_com.getItems().add(".pm");

        fri_com.getItems().add(".am");
        fri_com.getItems().add(".pm");

        satu_com.getItems().add(".am");
        satu_com.getItems().add(".pm");

        sun_com.getItems().add(".am");
        sun_com.getItems().add(".pm");
    }

    private void setDoctorNameTable() {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));

        try {
            ArrayList<DoctorDTO> all = doctorDAO.getAll();
            for (DoctorDTO temp : all) {
                doctor.getItems().add(new DoctorTM(temp.getIdNumber(), temp.getMr_ms(), temp.getMr_ms() + temp.getName(), temp.getName(), temp.getGen(), temp.getFree(),temp.getHos_free(), temp.getPhoneNumber(), temp.getGmail()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    void setSearchFilter() {
        FilteredList<DoctorTM> filteredData = new FilteredList<>(doctor.getItems(), b -> true);

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
            sortedList.comparatorProperty().bind(doctor.comparatorProperty());
            doctor.setItems(sortedList);
        });
    }

    private ArrayList<TimeDTO> collectTimeData(String doctorId, String doctorName) {
        ArrayList<TimeDTO> timeDTOS = new ArrayList<>();
        if (monDay.isSelected()) {
            if (m_com.getSelectionModel().getSelectedItem()==null) {
                new Alert(Alert.AlertType.ERROR,"select (am) or (pm)").show();
                return null;
            }
            timeDTOS.add(new TimeDTO(genTimeNo(), doctorId, doctorName, "Monday", m_f.getText() + m_com.getSelectionModel().getSelectedItem(), m_to.getText() + m_com.getSelectionModel().getSelectedItem()));
        }
        if (tuesday.isSelected()) {
            if (tu_com.getSelectionModel().getSelectedItem()==null) {
                new Alert(Alert.AlertType.ERROR,"select (am) or (pm)").show();
                return null;
            }
            timeDTOS.add(new TimeDTO(genTimeNo(), doctorId, doctorName, "Tuesday", tu_f.getText() + tu_com.getSelectionModel().getSelectedItem(), tu_to.getText() + tu_com.getSelectionModel().getSelectedItem()));
        }
        if (wednesday.isSelected()) {
            if (we_com.getSelectionModel().getSelectedItem()==null) {
                new Alert(Alert.AlertType.ERROR,"select (am) or (pm)").show();
                return null;
            }
            timeDTOS.add(new TimeDTO(genTimeNo(), doctorId, doctorName, "Wednesday", we_f.getText() + we_com.getSelectionModel().getSelectedItem(), we_to.getText() + we_com.getSelectionModel().getSelectedItem()));
        }
        if (thursday.isSelected()) {
            if (tur_com.getSelectionModel().getSelectedItem()==null) {
                new Alert(Alert.AlertType.ERROR,"select (am) or (pm)").show();
                return null;
            }
            timeDTOS.add(new TimeDTO(genTimeNo(), doctorId, doctorName, "Thursday", thu_f.getText() + tur_com.getSelectionModel().getSelectedItem(), thu_to.getText() + tur_com.getSelectionModel().getSelectedItem()));
        }
        if (friday.isSelected()) {
            if (fri_com.getSelectionModel().getSelectedItem()==null) {
                new Alert(Alert.AlertType.ERROR,"select (am) or (pm)").show();
                return null;
            }
            timeDTOS.add(new TimeDTO(genTimeNo(), doctorId, doctorName, "Friday", fr_f.getText() + fri_com.getSelectionModel().getSelectedItem(), fr_to.getText() + fri_com.getSelectionModel().getSelectedItem()));
        }
        if (saturday.isSelected()) {
            if (satu_com.getSelectionModel().getSelectedItem()==null) {
                new Alert(Alert.AlertType.ERROR,"select (am) or (pm)").show();
                return null;
            }
            timeDTOS.add(new TimeDTO(genTimeNo(), doctorId, doctorName, "Saturday", sat_f.getText() + satu_com.getSelectionModel().getSelectedItem(), sat_to.getText() + satu_com.getSelectionModel().getSelectedItem()));
        }
        if (sunday.isSelected()) {
            if (sun_com.getSelectionModel().getSelectedItem()==null) {
                new Alert(Alert.AlertType.ERROR,"select (am) or (pm)").show();
                return null;
            }
            timeDTOS.add(new TimeDTO(genTimeNo(), doctorId, doctorName, "Sunday", sun_f.getText() + sun_com.getSelectionModel().getSelectedItem(), sun_to.getText() + sun_com.getSelectionModel().getSelectedItem()));
        }
        return timeDTOS;
    }

    public int count;

    private String genTimeNo() {
        String id = "000-0001";
        try {
            String newID = doctorBO.getNewID();

            if (newID == null) {
                id = "000-0001";
                count++;
            } else {
                String[] split = newID.split("[000][-]");
                int i = Integer.parseInt(split[1]);
                i++;
                i += count;
                id = String.format("000-%04d", i);
                count++;

            }
        } catch (SQLException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
        }
        return id;
    }

    public void saveOnAction(ActionEvent actionEvent) {
        if (!nameDoctor.getText().equals("Doctor Name")) {
            ArrayList<TimeDTO> timeDTOS = collectTimeData(selectDoctorId, selectDoctorName);
            try {
                boolean b = doctorBO.addDoctorAndTime(timeDTOS);
                if (!b) {
                    new Alert(Alert.AlertType.ERROR, "Fall").show();
                } else {
                    new Alert(Alert.AlertType.ERROR, "success").show();
                    manageTimeScheduleDoctorController.setBillTableItems();
                    monDay.getScene().getWindow().hide();

                }
            } catch (SQLException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
                new Alert(Alert.AlertType.ERROR, e.getMessage()).show();
            }

        }else new Alert(Alert.AlertType.ERROR,"Select Doctor ").show();
    }

    public void tableMouseClickOnAction(MouseEvent mouseEvent) {
        DoctorTM selectedItem = doctor.getSelectionModel().getSelectedItem();
        nameDoctor.setText(selectedItem.getAdd_name());
        selectDoctorId = selectedItem.getIdNumber();
        selectDoctorName = selectedItem.getName();
    }


    public String selectDoctorId;
    public String selectDoctorName;

    public void searcoOnAction(KeyEvent keyEvent) {
        setSearchFilter();
    }
}

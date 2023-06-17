package dao.custom;

import dao.CrudDAO;
import dto.AppintnmentDTO;
import dto.PatientDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface AppointnmentDAO extends CrudDAO<AppintnmentDTO> {
    public boolean save(AppintnmentDTO dto, PatientDTO patientDTO) throws SQLException, ClassNotFoundException;
    public String getNewID() throws SQLException, ClassNotFoundException;
    public ArrayList<AppintnmentDTO> getAll2(String name) throws SQLException, ClassNotFoundException;
    public ArrayList<AppintnmentDTO> getAll3(String name,String dt) throws SQLException, ClassNotFoundException;

}

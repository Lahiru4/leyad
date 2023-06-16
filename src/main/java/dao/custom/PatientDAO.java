package dao.custom;

import dao.CrudDAO;
import model.PatientDTO;

import java.sql.SQLException;

public interface PatientDAO extends CrudDAO<PatientDTO> {
    public String getNewID() throws SQLException, ClassNotFoundException;
}

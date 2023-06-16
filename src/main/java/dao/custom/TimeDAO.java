package dao.custom;

import dao.CrudDAO;
import model.TimeDTO;

import java.sql.SQLException;

public interface TimeDAO extends CrudDAO<TimeDTO> {
    public boolean delete(String id) throws SQLException, ClassNotFoundException;

}

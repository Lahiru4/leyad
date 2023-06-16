package dao.custom;

import model.AdminDTO;

import java.sql.SQLException;

public interface AdminDAO {
    public AdminDTO getAdmin() throws SQLException, ClassNotFoundException;
}

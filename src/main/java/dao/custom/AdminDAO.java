package dao.custom;

import dao.CrudDAO;
import dao.SQLUtil;
import dto.AdminDTO;
import entity.Admin;

import java.sql.SQLException;

public interface AdminDAO extends CrudDAO<Admin> {
    public Admin getAdmin() throws SQLException, ClassNotFoundException;
}

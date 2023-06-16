package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.AdminDAO;
import model.AdminDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAOImpl implements AdminDAO {
    @Override
    public AdminDTO getAdmin() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT * FROM admin");

        if (rst.next()) {
            return new AdminDTO(rst.getString(1), rst.getString(4), rst.getString(3), rst.getString(2));
        }
        return null;
    }
}

package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.AdminDAO;
import entity.Admin;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class AdminDAOImpl implements AdminDAO {
    @Override
    public Admin getAdmin() throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT * FROM admin");
        if (rst.next()) {
            return new Admin(rst.getString(1), rst.getString(4), rst.getString(3), rst.getString(2));
        }
        return null;
    }
    @Override
    public ArrayList<Admin> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<Admin> allAdmins = new ArrayList<>();
        ResultSet rst = SQLUtil.execute("SELECT * FROM admin");
        while (rst.next()) {
            Admin customer = new Admin(rst.getString(1),rst.getString(4),rst.getString(3),rst.getString(2));
            allAdmins.add(customer);
        }
        return allAdmins;
    }
    @Override
    public boolean add(Admin dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO admin (name,password, phone_number,gmail) VALUES (?,?,?,?)",dto.getName(),dto.getPassword(),dto.getNumber(),dto.getGmail());
    }
    @Override
    public boolean update(Admin dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE admin SET  password=?,phone_number=?,gmail=? WHERE name=?", dto.getPassword(),dto.getNumber(),dto.getGmail(),dto.getName());
    }
    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT name FROM admin WHERE name=?", id);
        return rst.next();
    }
}
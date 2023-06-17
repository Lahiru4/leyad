package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.TimeDAO;
import dto.TimeDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class TimeDAOImpl implements TimeDAO {
    @Override
    public ArrayList<TimeDTO> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<TimeDTO> timeDTO = new ArrayList<>();
        ResultSet rst = SQLUtil.execute("SELECT * FROM timeperiod");
        while (rst.next()) {
            TimeDTO all = new TimeDTO(rst.getString(1),rst.getString(2),rst.getString(3),rst.getString(4),rst.getString(5),rst.getString(6));
            timeDTO.add(all);
        }
        return timeDTO;
    }

    @Override
    public boolean add(TimeDTO dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean update(TimeDTO dto) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return false;
    }

    @Override
    public boolean delete(String id) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("delete from timeperiod  where no = ?", id);
    }
}

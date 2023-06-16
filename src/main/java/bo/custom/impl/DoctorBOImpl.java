package bo.custom.impl;

import bo.custom.DoctorBO;
import dao.SQLUtil;
import dao.custom.DoctorDAO;
import dao.custom.impl.DoctorDAOImpl;
import db.DbConnection;
import model.DoctorDTO;
import model.TimeDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DoctorBOImpl implements DoctorBO {
    public DoctorDAO doctorDAO=new DoctorDAOImpl();
    @Override
    public boolean addDoctorAndTime( ArrayList<TimeDTO> timeDTO) throws SQLException, ClassNotFoundException {
        for (TimeDTO detail : timeDTO) {
            boolean b = doctorDAO.addTime(detail);
            if (!b) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean updateDoctorAndTime(DoctorDTO doctorDTO, ArrayList<TimeDTO> timeDTO) throws SQLException, ClassNotFoundException {
        Connection connection = DbConnection.getInstance().getConnection();
        boolean exist = doctorDAO.exist(doctorDTO.getIdNumber());
        if (!exist) {
            return false;
        }
        connection.setAutoCommit(false);
        boolean update = doctorDAO.update(doctorDTO);
        if (!update) {
            return false;
        }
        for (TimeDTO detail : timeDTO) {
            boolean b = doctorDAO.updateTime(detail);
            if (!b) {
                connection.rollback();
                connection.setAutoCommit(true);
                return false;
            }
        }
        return true;
    }

    @Override
    public String getNewID() throws SQLException, ClassNotFoundException {
        ResultSet ret = SQLUtil.execute("SELECT no FROM timeperiod ORDER BY no DESC LIMIT 1;");
        if (ret.next()) {
            return ret.getString("no");
        }
        return "000-1";
    }
}

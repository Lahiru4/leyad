package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.DoctorDAO;
import model.DoctorDTO;
import model.TimeDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DoctorDAOImpl implements DoctorDAO {
    @Override
    public ArrayList<DoctorDTO> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<DoctorDTO> allDoctors = new ArrayList<>();
        ResultSet rst = SQLUtil.execute("SELECT * FROM doctor");
        while (rst.next()) {
            DoctorDTO doctorDTO = new DoctorDTO(rst.getString(1),rst.getString(2),rst.getString(3),rst.getString(4),rst.getDouble(5),rst.getDouble(6)
                    ,rst.getString(7),rst.getString(8));
            allDoctors.add(doctorDTO);
        }
        return allDoctors;
    }
    @Override
    public boolean add(DoctorDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO doctor (doctorId,mr_ms, d_name,gen,free,phoneNumber,gmail,hosFree ) VALUES (?,?,?,?,?,?,?,?)",dto.getIdNumber(),dto.getMr_ms(),dto.getName(),dto.getGen(),dto.getFree(),dto.getPhoneNumber(),dto.getGmail(),dto.getHos_free());
    }
    @Override
    public boolean update(DoctorDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE doctor SET  mr_ms=?,d_name=? ,gen=?,free=?,phoneNumber=?,gmail=?,hosFree=?" +
                "WHERE doctorId=?",dto.getMr_ms(),dto.getName(),dto.getGen(),dto.getFree(),dto.getPhoneNumber(),dto.getGmail(),dto.getHos_free(),dto.getIdNumber());
    }
    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT doctorId FROM doctor WHERE doctorId=?", id);
        return rst.next();
    }

    @Override
    public String yetNameGetID(String name) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT doctorId FROM doctor WHERE d_name=?", name);
        String id="00";
        if (rst.next()){
            System.out.println(rst.getString(1));
            id=rst.getString(1);
        }
        return id;
    }

    @Override
    public ArrayList<TimeDTO> getTime(TimeDTO dto) throws SQLException, ClassNotFoundException {
        ArrayList<TimeDTO> allTimes = new ArrayList<>();
        ResultSet rst = SQLUtil.execute("SELECT * FROM timeperiod");
        while (rst.next()) {
            allTimes.add(new TimeDTO(dto.getNo(),dto.getDoctorId(),dto.getD_name(),dto.getDay(),dto.getFromTime(),dto.getToTime()));
        }
        return allTimes;
    }
    @Override
    public boolean addTime(TimeDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO timeperiod (no, doctorId,d_name,day,fromTime,toTime ) VALUES (?,?,?,?,?,?)",
                dto.getNo(),dto.getDoctorId(),dto.getD_name(),dto.getDay(),dto.getFromTime(),dto.getToTime());
    }

    @Override
    public boolean updateTime(TimeDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE timeperiod SET  doctorId=?,d_name=? ,day=?,fromTime=?,toTime=?" +
                "gmail=? WHERE no=?",dto.getDoctorId(),dto.getD_name(),dto.getDay(),dto.getToTime(),dto.getFromTime());
    }

    @Override
    public String getNewID() throws SQLException, ClassNotFoundException {
        ResultSet ret = SQLUtil.execute("SELECT doctorId FROM doctor ORDER BY doctorId DESC LIMIT 1;");
        if (ret.next()) {
            return ret.getString("doctorId");
        }
        return "000-1";
    }
    public String yetNameGetGmail(String gmail) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT gmail FROM doctor WHERE d_name=?", gmail);
        String id="00";
        if (rst.next()){
            System.out.println(rst.getString(1));
            id=rst.getString(1);
        }
        return id;
    }



}

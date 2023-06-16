package dao.custom.impl;

import dao.SQLUtil;
import dao.custom.PatientDAO;
import model.DoctorDTO;
import model.PatientDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class PatientDAOImpl implements PatientDAO {
    @Override
    public ArrayList<PatientDTO> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<PatientDTO> allDoctors = new ArrayList<>();
        ResultSet rst = SQLUtil.execute("SELECT * FROM patient");
        while (rst.next()) {
            PatientDTO patientDTO = new PatientDTO(rst.getString(1),rst.getString(2),rst.getString(3),rst.getInt(4),rst.getString(5),rst.getString(6));
            allDoctors.add(patientDTO);
        }
        return allDoctors;
    }

    @Override
    public boolean add(PatientDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO patient (patientIdNumber,mr_ms, patientName,age,phoneNumber,gen ) VALUES (?,?,?,?,?,?)",
                dto.getId(),dto.getMr_ms(),dto.getName(),dto.getAge(),dto.getPhone(),dto.getGen());
    }

    @Override
    public boolean update(PatientDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("UPDATE patient SET mr_ms=?, patientName=?,age=? ,phoneNumber=?,gen=? WHERE patientIdNumber=?",
                dto.getMr_ms(),dto.getName(),dto.getAge(),dto.getPhone(),dto.getGen(),dto.getId());
    }

    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        ResultSet rst = SQLUtil.execute("SELECT patientIdNumber FROM patient WHERE patientIdNumber=?", id);
        return rst.next();
    }

    @Override
    public String getNewID() throws SQLException, ClassNotFoundException {
        ResultSet ret = SQLUtil.execute("SELECT patientIdNumber FROM patient ORDER BY patientIdNumber DESC LIMIT 1;");
        if (ret.next()) {
            return ret.getString("patientIdNumber");
        }
        return "000-1";
    }
}

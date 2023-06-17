package dao.custom.impl;

import dao.CrudDAO;
import dao.SQLUtil;
import dao.custom.AppointnmentDAO;
import db.DbConnection;
import dto.AppintnmentDTO;
import dto.PatientDTO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

public class AppointnmentBAOImpl implements AppointnmentDAO {
    CrudDAO<PatientDTO> patientDTOCrudDAO=new PatientDAOImpl();
    @Override
    public ArrayList<AppintnmentDTO> getAll() throws SQLException, ClassNotFoundException {
        ArrayList<AppintnmentDTO> appointnment = new ArrayList<>();
        ResultSet rst = SQLUtil.execute("SELECT * FROM appointnment");
        while (rst.next()) {
            AppintnmentDTO appintnmentDTO = new AppintnmentDTO(rst.getString(1),rst.getString(2),rst.getString(3)
                    ,rst.getString(4),rst.getDouble(5),rst.getString(6),rst.getDouble(7),rst.getDouble(8),rst.getDouble(9),rst.getDate(10),rst.getDouble(11));
            appointnment.add(appintnmentDTO);
        }
        return appointnment;
    }
    @Override
    public ArrayList<AppintnmentDTO> getAll2(String name) throws SQLException, ClassNotFoundException {
        ArrayList<AppintnmentDTO> all=new ArrayList<>();
        ResultSet rst = SQLUtil.execute("SELECT * FROM appointnment where doutor_name=?;",name);
        while (rst.next()) {
            all.add(new AppintnmentDTO(rst.getString(1),rst.getString(2),rst.getString(3)
                    ,rst.getString(4),rst.getDouble(5),rst.getString(6),rst.getDouble(7),rst.getDouble(8),rst.getDouble(9),rst.getDate(10),rst.getDouble(11)) );
        }
        return all;
    }

    @Override
    public ArrayList<AppintnmentDTO> getAll3(String name,String dt) throws SQLException, ClassNotFoundException {
        ArrayList<AppintnmentDTO> all=new ArrayList<>();
        ResultSet rst = SQLUtil.execute("select * from appointnment where date=? AND doutor_name=?;",dt,name);
        while (rst.next()) {
            all.add(new AppintnmentDTO(rst.getString(1),rst.getString(2),rst.getString(3)
                    ,rst.getString(4),rst.getDouble(5),rst.getString(6),rst.getDouble(7),rst.getDouble(8),rst.getDouble(9),rst.getDate(10),rst.getDouble(11)) );
        }
        return all;
    }
    @Override
    public boolean add(AppintnmentDTO dto) throws SQLException, ClassNotFoundException {
        return SQLUtil.execute("INSERT INTO appointnment (id,number,patientIdNumber,doutor_name,doctor_free,patient_Name,hospital_free,tot_amount,chash_paod,date,scanFee) VALUES (?,?,?,?,?,?,?,?,?,?,?)",
                dto.getId(),dto.getNumber(),dto.getPatientIdNumber(),dto.getDoutor_nameOrTestName(),dto.getDoctor_freeOrTestFree(),
                dto.getPatient_Name(),dto.getHospital_free(),dto.getTot_amount(),dto.getChash_paod(), LocalDate.now(),dto.getScanFee());
    }
    @Override
    public boolean update(AppintnmentDTO dto) throws SQLException, ClassNotFoundException {
        return false;
    }
    @Override
    public boolean exist(String id) throws SQLException, ClassNotFoundException {
        return false;
    }
    @Override
    public boolean save(AppintnmentDTO dto, PatientDTO patientDTO) throws SQLException, ClassNotFoundException {
        Connection connection = null;
        connection=DbConnection.getInstance().getConnection();
        boolean exist = patientDTOCrudDAO.exist(patientDTO.getId());
        if (!exist) {
            connection.setAutoCommit(false);
            //save patient
            boolean add = patientDTOCrudDAO.add(patientDTO);
            if (!add) {
                return false;
            }
        }
        //save Appoint
        boolean add1 = add(dto);
        if (!add1) {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }
        if (!exist) {
            connection.commit();
            connection.setAutoCommit(true);
        }

        return true;
    }
    @Override
    public String getNewID() throws SQLException, ClassNotFoundException {
        ResultSet ret = SQLUtil.execute("SELECT id FROM appointnment ORDER BY id DESC LIMIT 1;");
        if (ret.next()) {
            return ret.getString("id");
        }
        return "000-1";
    }
}

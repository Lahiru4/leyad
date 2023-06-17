package dao.custom;

import dao.CrudDAO;
import dto.DoctorDTO;
import dto.TimeDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DoctorDAO extends CrudDAO<DoctorDTO> {
    public String yetNameGetID(String name) throws SQLException, ClassNotFoundException;
    public ArrayList<TimeDTO> getTime(TimeDTO dto) throws SQLException, ClassNotFoundException;
    public boolean addTime(TimeDTO dto) throws SQLException, ClassNotFoundException;
    public boolean updateTime(TimeDTO dto) throws SQLException, ClassNotFoundException;
    public String getNewID() throws SQLException, ClassNotFoundException;
    public String yetNameGetGmail(String gmail) throws SQLException, ClassNotFoundException;



}

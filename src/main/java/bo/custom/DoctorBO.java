package bo.custom;

import dto.DoctorDTO;
import dto.TimeDTO;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DoctorBO {
    public boolean addDoctorAndTime( ArrayList<TimeDTO> timeDTO) throws SQLException, ClassNotFoundException;
    public boolean updateDoctorAndTime(DoctorDTO doctorDTO,ArrayList<TimeDTO> timeDTO) throws SQLException, ClassNotFoundException;
    public String getNewID() throws SQLException, ClassNotFoundException;
}

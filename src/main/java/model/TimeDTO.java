package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimeDTO {
    private String no;
    private String doctorId;
    private String d_name;
    private String day;
    private String fromTime;
    private String toTime;

}

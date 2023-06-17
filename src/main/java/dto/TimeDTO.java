package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

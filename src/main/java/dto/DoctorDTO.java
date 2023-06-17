package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorDTO {
    private String idNumber;
    private String mr_ms;
    private String name;
    private String gen;
    private double free;
    private double hos_free;
    private String phoneNumber;
    private String gmail;
}

package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientDTO {
    private String  id;
    private String  mr_ms;
    private String  name;
    private int age;
    private String  phone;
    private String  gen;


}

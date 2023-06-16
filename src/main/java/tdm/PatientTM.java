package tdm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PatientTM {
    private String  id;
    private String  name;
    private int age;
    private String  phone;
    private String  gen;
    private String  mr_ms;
    private String  addName;

}

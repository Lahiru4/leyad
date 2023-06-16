package model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class AdminDTO {
    private String name;
    private String gmail;
    private String number;
    private String password;
}

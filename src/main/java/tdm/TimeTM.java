package tdm;

import com.jfoenix.controls.JFXButton;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Time;
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TimeTM {
    private String d_name;
    private String day;
    private String fromTime;
    private String toTime;

    private JFXButton button;
}

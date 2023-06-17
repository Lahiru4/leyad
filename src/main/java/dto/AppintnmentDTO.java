package dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@NoArgsConstructor
@Data
public class AppintnmentDTO {
    private String id;
    private String number;
    private String patientIdNumber;
    private String doutor_nameOrTestName;
    private double doctor_freeOrTestFree;
    private String patient_Name;
    private double hospital_free;
    private double tot_amount;
    private double chash_paod;
    private Date date;
    private double scanFee;

    public AppintnmentDTO(String id, String number, String patientIdNumber, String doutor_nameOrTestName, double doctor_freeOrTestFree, String patient_Name, double hospital_free, double tot_amount, double chash_paod, double scanFee) {
        this.id = id;
        this.number = number;
        this.patientIdNumber = patientIdNumber;
        this.doutor_nameOrTestName = doutor_nameOrTestName;
        this.doctor_freeOrTestFree = doctor_freeOrTestFree;
        this.patient_Name = patient_Name;
        this.hospital_free = hospital_free;
        this.tot_amount = tot_amount;
        this.chash_paod = chash_paod;
        this.scanFee = scanFee;
    }

    public AppintnmentDTO(String id, String number, String patientIdNumber, String doutor_nameOrTestName, double doctor_freeOrTestFree, String patient_Name, double hospital_free, double tot_amount, double chash_paod, Date date,double scanFee) {
        this.id = id;
        this.number = number;
        this.patientIdNumber = patientIdNumber;
        this.doutor_nameOrTestName = doutor_nameOrTestName;
        this.doctor_freeOrTestFree = doctor_freeOrTestFree;
        this.patient_Name = patient_Name;
        this.hospital_free = hospital_free;
        this.tot_amount = tot_amount;
        this.chash_paod = chash_paod;
        this.date = date;
        this.scanFee=scanFee;
    }

    public AppintnmentDTO(String id, String number, String patientIdNumber, String doutor_nameOrTestName, double doctor_freeOrTestFree, String patient_Name, double hospital_free, double tot_amount, double chash_paod) {
        this.id = id;
        this.number = number;
        this.patientIdNumber = patientIdNumber;
        this.doutor_nameOrTestName = doutor_nameOrTestName;
        this.doctor_freeOrTestFree = doctor_freeOrTestFree;
        this.patient_Name = patient_Name;
        this.hospital_free = hospital_free;
        this.tot_amount = tot_amount;
        this.chash_paod = chash_paod;
    }
}

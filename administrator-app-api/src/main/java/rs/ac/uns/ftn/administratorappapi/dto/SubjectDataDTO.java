package rs.ac.uns.ftn.administratorappapi.dto;

import org.bouncycastle.asn1.x500.X500Name;

import java.util.Date;

public class SubjectDataDTO {

    private String x500name;
    private String serialNumber;
    private Date startDate;
    private Date endDate;

    public SubjectDataDTO(){

    }

    public SubjectDataDTO(String x500name, String serialNumber, Date startDate, Date endDate) {
        this.x500name = x500name;
        this.serialNumber = serialNumber;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public String getX500name() {
        return x500name;
    }

    public void setX500name(String x500name) {
        this.x500name = x500name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "SubjectDataDTO{" +
                "x500name='" + x500name + '\'' +
                ", serialNumber='" + serialNumber + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}

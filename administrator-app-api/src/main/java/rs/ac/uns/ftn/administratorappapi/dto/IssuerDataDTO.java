package rs.ac.uns.ftn.administratorappapi.dto;

public class IssuerDataDTO {

    private String serialNumber;
    private String x500name;

    public IssuerDataDTO(){

    }

    public IssuerDataDTO(String serialNumber, String x500Name) {
        this.serialNumber = serialNumber;
        this.x500name = x500Name;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getX500Name() {
        return x500name;
    }

    public void setX500Name(String x500Name) {
        this.x500name = x500Name;
    }

    @Override
    public String toString() {
        return "IssuerDataDTO{" +
                "serialNumber='" + serialNumber + '\'' +
                ", x500Name='" + x500name + '\'' +
                '}';
    }
}

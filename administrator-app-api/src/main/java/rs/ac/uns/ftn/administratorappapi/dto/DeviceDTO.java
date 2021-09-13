package rs.ac.uns.ftn.administratorappapi.dto;

public class DeviceDTO {
    private String name;
    private String trustedOrganisationId;

    public DeviceDTO(){
        super();
    }

    public DeviceDTO(String name, String trustedOrganisationId) {
        this.name = name;
        this.trustedOrganisationId = trustedOrganisationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTrustedOrganisationId() {
        return trustedOrganisationId;
    }

    public void setTrustedOrganisationId(String trustedOrganisationId) {
        this.trustedOrganisationId = trustedOrganisationId;
    }
}

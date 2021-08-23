package rs.ac.uns.ftn.administratorappapi.dto;

import rs.ac.uns.ftn.administratorappapi.model.CertificateType;

import java.util.Date;

public class CertificateGenerateRequestDTO {
    private String issuerSerialNumber;
    private String commonName;
    private String organizationUnit;
    private String organization;
    private String country;
    private CertificateType certificateType;

    public Date getExpiringAt() {
        return expiringAt;
    }

    public void setExpiringAt(Date expiringAt) {
        this.expiringAt = expiringAt;
    }

    private Date expiringAt;

    public CertificateGenerateRequestDTO() {}

    public CertificateGenerateRequestDTO(String issuerSerialNumber, String commonName, String organizationUnit, String organization, String country, CertificateType certificateType, Date expiringAt) {
        this.issuerSerialNumber = issuerSerialNumber;
        this.commonName = commonName;
        this.organizationUnit = organizationUnit;
        this.organization = organization;
        this.country = country;
        this.certificateType = certificateType;
        this.expiringAt = expiringAt;
    }

    public SubjectDTO getSubjectDTO() {
        return new SubjectDTO(
                this.commonName,
                this.organizationUnit,
                this.organization,
                this.country
        );
    }

    public String getIssuerSerialNumber() {
        return issuerSerialNumber;
    }

    public void setIssuerSerialNumber(String issuerSerialNumber) {
        this.issuerSerialNumber = issuerSerialNumber;
    }

    public String getCommonName() {
        return commonName;
    }

    public void setCommonName(String commonName) {
        this.commonName = commonName;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public CertificateType getCertificateType() {
        return certificateType;
    }

    public void setCertificateType(CertificateType certificateType) {
        this.certificateType = certificateType;
    }
}

package rs.ac.uns.ftn.administratorappapi.dto;

import rs.ac.uns.ftn.administratorappapi.model.CertificateType;

import java.util.Date;

public class CertificateGenerateRequestDTO {
    private String issuerSerialNumber;
    private String commonName;
    private String organizationUnit;
    private String organization;
    private String country;
    private String city;
    private CertificateType certificateType;
    private String secretWord1;
    private String secretWord2;
    private String secretWord3;

    private String secretWord1confirmed;
    private String secretWord2confirmed;
    private String secretWord3confirmed;

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

    public CertificateGenerateRequestDTO(String issuerSerialNumber,
                                         String commonName,
                                         String organizationUnit,
                                         String organization,
                                         String country,
                                         String city,
                                         CertificateType certificateType,
                                         String secretWord1,
                                         String secretWord2,
                                         String secretWord3
    ) {
        this.issuerSerialNumber = issuerSerialNumber;
        this.commonName = commonName;
        this.organizationUnit = organizationUnit;
        this.organization = organization;
        this.country = country;
        this.city = city;
        this.certificateType = certificateType;
        this.secretWord1 = secretWord1;
        this.secretWord2 = secretWord2;
        this.secretWord3 = secretWord3;
    }

    public CertificateGenerateRequestDTO(String issuerSerialNumber,
                                         String commonName,
                                         String organizationUnit,
                                         String organization,
                                         String country,
                                         String city,
                                         CertificateType certificateType,
                                         String secretWord1,
                                         String secretWord2,
                                         String secretWord3,
                                         String secretWord1confirmed,
                                         String secretWord2confirmed,
                                         String secretWord3confirmed
    ) {
        this.issuerSerialNumber = issuerSerialNumber;
        this.commonName = commonName;
        this.organizationUnit = organizationUnit;
        this.organization = organization;
        this.country = country;
        this.city = city;
        this.certificateType = certificateType;
        this.secretWord1 = secretWord1;
        this.secretWord2 = secretWord2;
        this.secretWord3 = secretWord3;
        this.secretWord1confirmed = secretWord1confirmed;
        this.secretWord2confirmed = secretWord2confirmed;
        this.secretWord3confirmed = secretWord3confirmed;
    }


    public CertificateGenerateRequestDTO(String issuerSerialNumber, String commonName, String organizationUnit, String organization, String country, String city) {
        this.issuerSerialNumber = issuerSerialNumber;
        this.commonName = commonName;
        this.organizationUnit = organizationUnit;
        this.organization = organization;
        this.country = country;
        this.city = city;
    }

    public SubjectDTO getSubjectDTO() {
        return new SubjectDTO(
                this.commonName,
                this.organizationUnit,
                this.organization,
                this.country,
                this.city
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getSecretWord1() {
        return secretWord1;
    }

    public void setSecretWord1(String secretWord1) {
        this.secretWord1 = secretWord1;
    }

    public String getSecretWord2() {
        return secretWord2;
    }

    public void setSecretWord2(String secretWord2) {
        this.secretWord2 = secretWord2;
    }

    public String getSecretWord3() {
        return secretWord3;
    }

    public void setSecretWord3(String secretWord3) {
        this.secretWord3 = secretWord3;
    }


    public String getSecretWord1confirmed() {
        return secretWord1confirmed;
    }

    public void setSecretWord1confirmed(String secretWord1confirmed) {
        this.secretWord1confirmed = secretWord1confirmed;
    }

    public String getSecretWord2confirmed() {
        return secretWord2confirmed;
    }

    public void setSecretWord2confirmed(String secretWord2confirmed) {
        this.secretWord2confirmed = secretWord2confirmed;
    }

    public String getSecretWord3confirmed() {
        return secretWord3confirmed;
    }

    public void setSecretWord3confirmed(String secretWord3confirmed) {
        this.secretWord3confirmed = secretWord3confirmed;
    }
}

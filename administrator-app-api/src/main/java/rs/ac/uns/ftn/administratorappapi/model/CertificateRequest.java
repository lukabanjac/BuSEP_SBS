package rs.ac.uns.ftn.administratorappapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "certificateRequest")
public class CertificateRequest {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "issuer_serial_number")
    private String issuerSerialNumber;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "organization")
    private String organization;

    @Column(name = "organization_unit")
    private String organizationUnit;

    @Column(name = "status")
    private CertificateRequestStatus status;

    @Column(name = "rejectedReason")
    private String rejectedReason;

    @OneToOne
    @JsonManagedReference
    private User user;

    public CertificateRequest(){
        super();
    }


    public CertificateRequest(Long id, String issuerSerialNumber, String country, String city, String organization, String organizationUnit, CertificateRequestStatus status, String rejectedReason) {
        this.id = id;
        this.issuerSerialNumber = issuerSerialNumber;
        this.country = country;
        this.city = city;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.status = status;
        this.rejectedReason = rejectedReason;
    }

    public CertificateRequest(Long id, String issuerSerialNumber, String country, String city, String organization, String organizationUnit) {
        this.id = id;
        this.issuerSerialNumber = issuerSerialNumber;
        this.country = country;
        this.city = city;
        this.organization = organization;
        this.organizationUnit = organizationUnit;
        this.status = CertificateRequestStatus.PENDING;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssuerSerialNumber() {
        return issuerSerialNumber;
    }

    public void setIssuerSerialNumber(String issuerSerialNumber) {
        this.issuerSerialNumber = issuerSerialNumber;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organisation) {
        this.organization = organisation;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organisationUnit) {
        this.organizationUnit = organisationUnit;
    }

    public CertificateRequestStatus getStatus() {
        return status;
    }

    public void setStatus(CertificateRequestStatus status) {
        this.status = status;
    }

    public String getRejectedReason() {
        return rejectedReason;
    }

    public void setRejectedReason(String recetedReason) {
        this.rejectedReason = recetedReason;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

}



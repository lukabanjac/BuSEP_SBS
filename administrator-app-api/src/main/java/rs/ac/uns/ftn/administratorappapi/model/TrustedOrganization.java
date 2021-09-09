package rs.ac.uns.ftn.administratorappapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "trusted_organization")
public class TrustedOrganization {
    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "country")
    private String country;

    @Column(name = "city")
    private String city;

    @Column(name = "organization")
    private String organization;

    @Column(name = "organization_unit")
    private String organizationUnit;

    @Column(name = "secret_word_1")
    private String secretWord1;

    @Column(name = "secret_word_2")
    private String secretWord2;

    @Column(name = "secret_word_3")
    private String secretWord3;

    @OneToOne
    @JsonManagedReference
    private Admin admin;

    public TrustedOrganization() {
        super();
    }

    public TrustedOrganization(String country, String city, String organisation, String organizationUnit, String secretWord1, String secretWord2, String secretWord3) {
        this.country = country;
        this.city = city;
        this.organization = organisation;
        this.organizationUnit = organizationUnit;
        this.secretWord1 = secretWord1;
        this.secretWord2 = secretWord2;
        this.secretWord3 = secretWord3;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(String organizationUnit) {
        this.organizationUnit = organizationUnit;
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

    public Admin getAdmin() {
        return admin;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }
}


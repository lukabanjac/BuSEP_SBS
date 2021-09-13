package rs.ac.uns.ftn.administratorappapi.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;

@Entity
@Table(name = "device")
public class Device {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonManagedReference
    private TrustedOrganization trusted_organization;

    @OneToOne(targetEntity = Certificate.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "certificate_id")
    private Certificate certificate;


    public Device(){
        super();
    }

    public Device(String name, TrustedOrganization trusted_organization, Certificate certificate) {
        this.name = name;
        this.trusted_organization = trusted_organization;
        this.certificate = certificate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    public TrustedOrganization getTrusted_organization() {
        return trusted_organization;
    }

    public void setTrusted_organization(TrustedOrganization trusted_organization) {
        this.trusted_organization = trusted_organization;
    }
}

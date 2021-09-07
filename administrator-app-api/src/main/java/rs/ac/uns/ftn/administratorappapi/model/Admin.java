package rs.ac.uns.ftn.administratorappapi.model;


import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import java.sql.Timestamp;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends User {

    @OneToOne
    @JoinColumn(name = "certificate_id", referencedColumnName = "id")
    private Certificate certificate;

    @OneToOne
    @JsonBackReference
    private TrustedOrganisation trustedOrganisation;



    public Admin(){
        super();
    }

    public Admin(String username, String firstName, String lastName, String email, String password, Timestamp lastPasswordResetDate) {
        super(username, firstName, lastName, email, password, lastPasswordResetDate);
    }

    public Admin(String username, String firstName, String lastName, String email, String password, Timestamp lastPasswordResetDate, Certificate certificate) {
        super(username, firstName, lastName, email, password, lastPasswordResetDate);
        this.certificate = certificate;
    }

    public TrustedOrganisation getTrustedOrganisation() {
        return trustedOrganisation;
    }

    public void setTrustedOrganisation(TrustedOrganisation trustedOrganisation) {
        this.trustedOrganisation = trustedOrganisation;
    }
}

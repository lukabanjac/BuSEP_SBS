package rs.ac.uns.ftn.administratorappapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import java.sql.Timestamp;

@Entity
@DiscriminatorValue("DOCTOR")
public class Doctor extends User {

    @ManyToOne(fetch = FetchType.EAGER)
    @JsonManagedReference
    private TrustedOrganization trusted_organization;

    public Doctor(){
        super();
    }

    public Doctor(String username, String firstName, String lastName, String email, String password, Timestamp lastPasswordResetDate) {
        super(username, firstName, lastName, email, password, lastPasswordResetDate);
    }

    public TrustedOrganization getTrusted_organization() {
        return trusted_organization;
    }

    public void setTrusted_organization(TrustedOrganization trusted_organization) {
        this.trusted_organization = trusted_organization;
    }
}

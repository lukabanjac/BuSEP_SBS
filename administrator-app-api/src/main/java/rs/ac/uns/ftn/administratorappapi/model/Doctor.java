package rs.ac.uns.ftn.administratorappapi.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@DiscriminatorValue("DOCTOR")
public class Doctor extends User {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trusted_organization_id")
    private TrustedOrganization trustedOrganization;

    public Doctor(){
        super();
    }

    public Doctor(String username, String firstName, String lastName, String email, String password, Timestamp lastPasswordResetDate) {
        super(username, firstName, lastName, email, password, lastPasswordResetDate);
    }

    public TrustedOrganization getTrustedOrganization() {
        return trustedOrganization;
    }

    public void setTrustedOrganization(TrustedOrganization trustedOrganization) {
        this.trustedOrganization = trustedOrganization;
    }
}

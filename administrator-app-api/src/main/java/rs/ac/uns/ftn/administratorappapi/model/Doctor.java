package rs.ac.uns.ftn.administratorappapi.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.sql.Timestamp;

@Entity
@DiscriminatorValue("DOCTOR")
public class Doctor extends User {

    public Doctor(){
        super();
    }

    public Doctor(String username, String firstName, String lastName, String email, String password, Timestamp lastPasswordResetDate) {
        super(username, firstName, lastName, email, password, lastPasswordResetDate);
    }
}

package rs.ac.uns.ftn.administratorappapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

//@SpringBootApplication(exclude={SecurityAutoConfiguration.class}) // TODO: privremeno disableovan spring security, zavrsiti kasnije
@SpringBootApplication
public class AdministratorAppApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdministratorAppApiApplication.class, args);
	}

}

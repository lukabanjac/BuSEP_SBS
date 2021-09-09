package rs.ac.uns.ftn.administratorappapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.administratorappapi.model.TrustedOrganization;

public interface TrustedOrganizationRepository extends JpaRepository<TrustedOrganization, Long> {
    TrustedOrganization findByCountryAndCityAndOrganizationAndOrganizationUnit(String country, String city, String organization, String organizationUnit);
}

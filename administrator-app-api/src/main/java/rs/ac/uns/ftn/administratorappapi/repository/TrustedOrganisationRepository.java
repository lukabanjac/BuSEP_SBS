package rs.ac.uns.ftn.administratorappapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.administratorappapi.model.TrustedOrganization;

public interface TrustedOrganisationRepository extends JpaRepository<TrustedOrganization, Long> {
    TrustedOrganization findByCountryAndCityAndOrganisationAndOrganisationUnit(String country, String city, String organisation, String organisationUnit);
}

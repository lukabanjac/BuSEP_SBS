package rs.ac.uns.ftn.administratorappapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.administratorappapi.model.TrustedOrganisation;

public interface TrustedOrganisationRepository extends JpaRepository<TrustedOrganisation, Long> {
    TrustedOrganisation findByCountryAndCityAndOrganisationAndOrganisationUnit(String country, String city, String organisation, String organisationUnit);
}

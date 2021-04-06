package rs.ac.uns.ftn.administratorappapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.administratorappapi.model.Authority;


@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}

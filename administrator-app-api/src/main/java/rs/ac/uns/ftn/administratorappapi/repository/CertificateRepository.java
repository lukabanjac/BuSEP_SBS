package rs.ac.uns.ftn.administratorappapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.administratorappapi.model.Certificate;

import java.util.List;
import java.util.Optional;


public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findAll();

    Optional<Certificate> findBySerialNumber(String id);

    List<Certificate> findAllByCaSerialNumberEquals(String parentId);

}

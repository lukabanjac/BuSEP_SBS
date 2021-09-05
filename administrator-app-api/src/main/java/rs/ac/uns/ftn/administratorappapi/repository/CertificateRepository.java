package rs.ac.uns.ftn.administratorappapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.administratorappapi.model.Certificate;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;


public interface CertificateRepository extends JpaRepository<Certificate, Long> {

    List<Certificate> findAll();

    Optional<Certificate> findBySerialNumber(BigInteger id);

    List<Certificate> findAllByCaSerialNumberEquals(BigInteger parentId);

}

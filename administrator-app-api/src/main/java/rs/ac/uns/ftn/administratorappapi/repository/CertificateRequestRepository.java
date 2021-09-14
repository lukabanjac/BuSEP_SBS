package rs.ac.uns.ftn.administratorappapi.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import rs.ac.uns.ftn.administratorappapi.model.CertificateRequest;

import java.util.List;


public interface CertificateRequestRepository extends JpaRepository<CertificateRequest, Long> {


    CertificateRequest getByUserId(Long id);
    List<CertificateRequest> findByIssuerSerialNumber(String id);
}

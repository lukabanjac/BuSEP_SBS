package rs.ac.uns.ftn.administratorappapi.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.administratorappapi.dto.CertificateGenerateDTO;
import rs.ac.uns.ftn.administratorappapi.model.Certificate;
import rs.ac.uns.ftn.administratorappapi.model.IssuerData;
import rs.ac.uns.ftn.administratorappapi.model.SubjectData;

import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.List;

@Service
public interface CertificateService {
    X509Certificate generateCertificate(SubjectData subject, IssuerData issuer);
    void generate(CertificateGenerateDTO certificateGenerateDTO);
    KeyPair generateKeyPair();
    SubjectData generateSubjectData(PublicKey subjectKey, CertificateGenerateDTO certificateGenerateDTO);
    HttpStatus issueTo(String username);
    List<Certificate> getAll();
}

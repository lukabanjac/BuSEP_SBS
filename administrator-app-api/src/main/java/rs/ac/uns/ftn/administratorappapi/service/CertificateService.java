package rs.ac.uns.ftn.administratorappapi.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.administratorappapi.dto.CertificateGenerateRequestDTO;
import rs.ac.uns.ftn.administratorappapi.dto.MessageDTO;
import rs.ac.uns.ftn.administratorappapi.dto.SubjectDTO;
import rs.ac.uns.ftn.administratorappapi.model.Certificate;
import rs.ac.uns.ftn.administratorappapi.model.CertificateType;
import rs.ac.uns.ftn.administratorappapi.model.IssuerData;
import rs.ac.uns.ftn.administratorappapi.model.SubjectData;

import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.List;

@Service
public interface CertificateService {
    //X509Certificate generateCertificate(SubjectData subject, IssuerData issuer);
    //KeyPair generateKeyPair();
    //SubjectData generateSubjectData(PublicKey subjectKey, CertificateGenerateDTO certificateGenerateDTO);
    //HttpStatus issueTo(String username);
    List<Certificate> getAll();
    Certificate revokeCertificate(BigInteger serialNumber, String revokeReason);
    Certificate findBySerialNumber(BigInteger serialNumber);
    Certificate createCertificate(CertificateGenerateRequestDTO request, String issuerSerialNumber, CertificateType type);
    MessageDTO generateRequest(CertificateGenerateRequestDTO requestDTO);
}

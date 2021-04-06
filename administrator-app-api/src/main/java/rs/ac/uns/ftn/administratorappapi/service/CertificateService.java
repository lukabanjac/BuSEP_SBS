package rs.ac.uns.ftn.administratorappapi.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.administratorappapi.dto.CertificateGenerateDTO;
import rs.ac.uns.ftn.administratorappapi.model.Issuer;
import rs.ac.uns.ftn.administratorappapi.model.Subject;
import rs.ac.uns.ftn.administratorappapi.model.SubjectData;

import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.X509Certificate;

@Service
public interface CertificateService {
    X509Certificate generateCertificate(Subject subject, Issuer issuer);

    void generate(CertificateGenerateDTO certificateGenerateDTO);

    KeyPair generateKeyPair();


    SubjectData generateSubjectData(PublicKey subjectKey, CertificateGenerateDTO certificateGenerateDTO);


}

package rs.ac.uns.ftn.administratorappapi.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.administratorappapi.model.Issuer;
import rs.ac.uns.ftn.administratorappapi.model.Subject;

import java.security.cert.X509Certificate;

@Service
public interface CertificateService {
    X509Certificate generateCertificate(Subject subject, Issuer issuer);
}

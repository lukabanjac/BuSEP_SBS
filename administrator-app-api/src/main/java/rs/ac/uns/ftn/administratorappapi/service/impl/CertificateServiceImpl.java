package rs.ac.uns.ftn.administratorappapi.service.impl;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.administratorappapi.model.Issuer;
import rs.ac.uns.ftn.administratorappapi.model.Subject;
import rs.ac.uns.ftn.administratorappapi.service.CertificateService;

import java.math.BigInteger;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Service
public class CertificateServiceImpl implements CertificateService {

    public CertificateServiceImpl() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public X509Certificate generateCertificate(Subject subject, Issuer issuer) {
        try {
            JcaContentSignerBuilder builder = new JcaContentSignerBuilder("SHA256WithRSAEncryption");

            builder = builder.setProvider("BC");

            ContentSigner contentSigner = builder.build(issuer.getPrivateKey());

            X509v3CertificateBuilder certificateGenerator = new JcaX509v3CertificateBuilder(
                    issuer.getX500name(),
                    new BigInteger(subject.getSerialNumber()),
                    subject.getStartDate(),
                    subject.getEndDate(),
                    subject.getX500name(),
                    subject.getPublicKey());

            X509CertificateHolder certificateHolder = certificateGenerator.build(contentSigner);

            JcaX509CertificateConverter certificateConverter = new JcaX509CertificateConverter();
            certificateConverter = certificateConverter.setProvider("BC");

            return certificateConverter.getCertificate(certificateHolder);
        } catch (IllegalArgumentException |
                IllegalStateException |
                OperatorCreationException |
                CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }
}

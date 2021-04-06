package rs.ac.uns.ftn.administratorappapi.service.impl;

import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.administratorappapi.certificates.CertificateGenerator;
import rs.ac.uns.ftn.administratorappapi.dto.CertificateGenerateDTO;
import rs.ac.uns.ftn.administratorappapi.model.*;
import rs.ac.uns.ftn.administratorappapi.model.Certificate;
import rs.ac.uns.ftn.administratorappapi.repository.CertificateRepository;
import rs.ac.uns.ftn.administratorappapi.repository.UserRepository;
import rs.ac.uns.ftn.administratorappapi.service.CertificateService;
import rs.ac.uns.ftn.administratorappapi.storage.CertificateStorage;
import rs.ac.uns.ftn.administratorappapi.util.DataGenerator;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private CertificateStorage certificateStorage;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataGenerator dataGenerator;

    public CertificateServiceImpl() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public X509Certificate generateCertificate(SubjectData subject, IssuerData issuer) {
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



    public void generate(CertificateGenerateDTO certificateGenerateDTO) {

        KeyPair keyPair = generateKeyPair();

        IssuerData issuerData;
        SubjectData subjectData;
        X509Certificate x509Certificate;

        subjectData = generateSubjectData(keyPair.getPublic(), certificateGenerateDTO);


        if(certificateGenerateDTO.getCertificateType() == CertificateType.ROOT) {
            issuerData = new IssuerData(keyPair.getPrivate(), subjectData.getX500name(), keyPair.getPublic(), new BigInteger(subjectData.getSerialNumber()));
        }else{
            issuerData = certificateStorage.getIssuerDataBySerialNumber(certificateGenerateDTO.getIssuerDataDTO().getSerialNumber());
        }



        x509Certificate = CertificateGenerator.generateCertificate(subjectData, issuerData);
        certificateStorage.storeCertificateChan(new X509Certificate[]{x509Certificate}, keyPair.getPrivate());

        certificateRepository.save(new Certificate(
                subjectData.getSerialNumber(),
                certificateGenerateDTO.getCertificateType(),
                "",
                "",
                "",
                false,
                null,
                "",
                certificateGenerateDTO.getSubjectDataDTO().getStartDate(),
                certificateGenerateDTO.getSubjectDataDTO().getEndDate()

        ));

    }


    public KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG", "SUN");
            keyGen.initialize(2048, random);
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        return null;
    }




    public SubjectData generateSubjectData(PublicKey subjectKey, CertificateGenerateDTO certificateGenerateDTO) {
        try {


            X500NameBuilder builder = new X500NameBuilder(BCStyle.INSTANCE);
            builder.addRDN(BCStyle.CN, certificateGenerateDTO.getSubjectDataDTO().getX500name());
            if (certificateGenerateDTO.getCertificateType() != CertificateType.DEVICE){
                builder.addRDN(BCStyle.UID, System.currentTimeMillis()+"");
            }


            long now = System.currentTimeMillis();


            return new SubjectData(subjectKey, builder.build(), String.valueOf(now),
                    certificateGenerateDTO.getSubjectDataDTO().getStartDate() , certificateGenerateDTO.getSubjectDataDTO().getEndDate());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public HttpStatus issueTo(String username) {
        User user = userRepository.findByUsername(username);
        KeyPair keyPairIssuer = dataGenerator.generateKeyPair();


        SubjectData subject = dataGenerator.generateSubject(user);
        IssuerData issuer = dataGenerator.generateIssuer(keyPairIssuer.getPrivate());

        generateCert(subject, issuer);

        return HttpStatus.OK;
    }

    @Override
    public List<Certificate> getAll() {
        return certificateRepository.findAll();
    }

    public void generateCert(SubjectData subjectData, IssuerData issuerData) {
        KeyPair keyPair = generateKeyPair();

        X509Certificate x509Certificate;
        x509Certificate = CertificateGenerator.generateCertificate(subjectData, issuerData);

        certificateStorage.storeCertificateChan(new X509Certificate[]{x509Certificate}, keyPair.getPrivate());
        certificateRepository.save(new Certificate(
                subjectData.getSerialNumber(),
                CertificateType.INTERMEDIATE,
                "",
                "",
                "",
                false,
                null,
                "",
                subjectData.getStartDate(),
                subjectData.getEndDate()
        ));
    }
}

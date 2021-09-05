package rs.ac.uns.ftn.administratorappapi.service.impl;

import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.*;
import org.bouncycastle.cert.CertIOException;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.administratorappapi.dto.CertificateGenerateRequestDTO;
import rs.ac.uns.ftn.administratorappapi.dto.MessageDTO;
import rs.ac.uns.ftn.administratorappapi.dto.SubjectDTO;
import rs.ac.uns.ftn.administratorappapi.exception.EntityNotFoundException;
import rs.ac.uns.ftn.administratorappapi.exception.PkiMalfunctionException;
import rs.ac.uns.ftn.administratorappapi.model.*;
import rs.ac.uns.ftn.administratorappapi.model.Certificate;
import rs.ac.uns.ftn.administratorappapi.repository.CertificateRepository;
import rs.ac.uns.ftn.administratorappapi.repository.CertificateRequestRepository;
import rs.ac.uns.ftn.administratorappapi.repository.TrustedOrganisationRepository;
import rs.ac.uns.ftn.administratorappapi.repository.UserRepository;
import rs.ac.uns.ftn.administratorappapi.service.CertificateService;
import rs.ac.uns.ftn.administratorappapi.storage.CertificateStorage;
import rs.ac.uns.ftn.administratorappapi.util.DataGenerator;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private CertificateStorage certificateStorage;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private CertificateRequestRepository  certificateRequestRepository;

    @Autowired
    private TrustedOrganisationRepository trustedOrganisationRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DataGenerator dataGenerator;

    @Value("${pki.key-store-password}")
    private char[] keyStorePassword;

    @Value("${pki.trust-store-password}")
    private char[] trustStorePassword;

    @Value("${pki.certificate.provider}")
    private String provider;

    @Value("${pki.algorithm.signature}")
    private String signatureAlgorithm;

    @Value("${pki.algorithm.key}")
    private String keyAlgorithm;

    @Value("${pki.seed.algorithm}")
    private String seedAlgorithm;

    @Value("${pki.seed.provider}")
    private String seedProvider;

    @Value("${pki.ocsp.responder-server-url}")
    private String OCSPResponderServerURL;

    @Value("${pki.ca.keysize}")
    private int caKeySize;

    @Value("${pki.user.keysize}")
    private int userKeySize;

    public CertificateServiceImpl() {
        Security.addProvider(new BouncyCastleProvider());
    }

    public X509Certificate generateCertificate(SubjectData subject, IssuerData issuer, boolean isCA) {
        try {
            ContentSigner contentSigner = new JcaContentSignerBuilder(this.signatureAlgorithm)
                    .setProvider(this.provider)
                    .build(issuer.getPrivateKey());

            X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(
                    issuer.getX500name(),
                    subject.getSerialNumber(),
                    subject.getStartDate(),
                    subject.getEndDate(),
                    subject.getX500name(),
                    subject.getPublicKey());

            BasicConstraints basicConstraints = new BasicConstraints(isCA);
            certificateBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.19"), true, basicConstraints);

            JcaX509ExtensionUtils extensionUtils = new JcaX509ExtensionUtils();

            AuthorityKeyIdentifier authorityKeyIdentifier = extensionUtils
                    .createAuthorityKeyIdentifier(issuer.getPublicKey());
            certificateBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.35"), false, authorityKeyIdentifier);

            SubjectKeyIdentifier subjectKeyIdentifier = extensionUtils
                    .createSubjectKeyIdentifier(subject.getPublicKey());
            certificateBuilder.addExtension(new ASN1ObjectIdentifier("2.5.29.14"), false, subjectKeyIdentifier);


            /* Add OCSP response server data */
            GeneralName ocspName =
                    new GeneralName(
                            GeneralName.uniformResourceIdentifier,
                            this.OCSPResponderServerURL + issuer.getSerialNumber().toString());
            AuthorityInformationAccess authorityInformationAccess =
                    new AuthorityInformationAccess(X509ObjectIdentifiers.ocspAccessMethod, ocspName);

            return new JcaX509CertificateConverter()
                    .setProvider(this.provider)
                    .getCertificate(certificateBuilder.build(contentSigner));

        } catch (IllegalArgumentException | IllegalStateException | OperatorCreationException | CertIOException | NoSuchAlgorithmException | CertificateException e) {
            e.printStackTrace();
        }
        return null;
    }


    public MessageDTO generateRequest(CertificateGenerateRequestDTO requestDTO){

        if(requestDTO.getIssuerSerialNumber().trim().equalsIgnoreCase("") ||
                requestDTO.getCity().trim().equalsIgnoreCase("") ||
                requestDTO.getCountry().trim().equalsIgnoreCase("") ||
                requestDTO.getOrganization().trim().equalsIgnoreCase("") ||
                requestDTO.getOrganizationUnit().trim().equalsIgnoreCase("") ||
                requestDTO.getSecretWord1().trim().equalsIgnoreCase("") ||
                requestDTO.getSecretWord2().trim().equalsIgnoreCase("") ||
                requestDTO.getSecretWord3().trim().equalsIgnoreCase("")
        ){
            return new MessageDTO(false, "All fields must be filled!");
        }


        Certificate issuerCertificate = certificateRepository
                .findBySerialNumber(BigInteger.valueOf(Long.parseLong(requestDTO.getIssuerSerialNumber()))).get();

        if(issuerCertificate == null){
            return new MessageDTO(false, "Invalid issuer serial number!");
        }

        if(issuerCertificate.getType() == CertificateType.LEAF){
            return new MessageDTO(false, "Invalid issuer certificate type!");
        }



        TrustedOrganisation to = trustedOrganisationRepository
                .findByCountryAndCityAndOrganisationAndOrganisationUnit
                        (
                                requestDTO.getCountry(),
                                requestDTO.getCity(),
                                requestDTO.getOrganization(),
                                requestDTO.getOrganizationUnit()
                        );


        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if(!to.getSecretWord1().equals(encoder.encode(requestDTO.getSecretWord1())) ||
        !to.getSecretWord2().equals(encoder.encode(requestDTO.getSecretWord2())) ||
        !to.getSecretWord3().equals(encoder.encode(requestDTO.getSecretWord3()))){
            return new MessageDTO(false, "Invalid secret words!");
        }


        CertificateRequest cr = new CertificateRequest();
        cr.setIssuerSerialNumber(requestDTO.getIssuerSerialNumber());
        cr.setCountry(requestDTO.getCountry());
        cr.setCity(requestDTO.getCity());
        cr.setOrganisation(requestDTO.getOrganization());
        cr.setOrganisationUnit(requestDTO.getOrganizationUnit());
        cr.setStatus(CertificateRequestStatus.PENDING);

        certificateRequestRepository.save(cr);

        return new MessageDTO(true, "Request was successfully sent :)");
    }

    private KeyPair generateKeyPair(boolean isCA) {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance(this.keyAlgorithm);
            SecureRandom random = SecureRandom.getInstance(this.seedAlgorithm, this.seedProvider);
            if (isCA) {
                keyGen.initialize(this.caKeySize, random);
            } else {
                keyGen.initialize(this.userKeySize, random);
            }
            return keyGen.generateKeyPair();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            e.printStackTrace();
            throw new PkiMalfunctionException("Error while generating new key pair.");
        }
    }


    private SubjectData generateSubjectData(PublicKey publicKey, X500Name subjectDN, boolean isCA) {
        long now = System.currentTimeMillis();
        Date startDate = new Date(now);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.YEAR, 1);
        Date endDate = calendar.getTime();
        return new SubjectData(publicKey, subjectDN, new BigInteger(Long.toString(now)), startDate, endDate);
    }

    @Override
    public List<Certificate> getAll() {
        return certificateRepository.findAll();
    }

    @Override
    public Certificate revokeCertificate(BigInteger serialNumber, String revokeReason) {

        Optional<Certificate> opt = this.certificateRepository.findBySerialNumber(serialNumber);
        opt.orElseThrow(() -> new EntityNotFoundException(Certificate.class, "serialNumber", serialNumber.toString()));

        List<Certificate> childCertificates = this.certificateRepository.findAllByCaSerialNumberEquals(serialNumber);
        Date now = new Date();

        // revoke top level certificate
        Certificate cert = opt.get();
        if(cert.getType() != CertificateType.ROOT) {
            cert.setRevoked(true);
            cert.setRevokeReason(revokeReason);
            cert.setRevokedAt(now);

            this.certificateRepository.save(cert);

            // revoke all child certificates also
            for (Certificate child : childCertificates) {
                child.setRevokedAt(now);
                child.setRevoked(true);
                child.setRevokeReason("Parent revoked because: " + revokeReason);
            }

            this.certificateRepository.saveAll(childCertificates);
        }

        return cert;
    }

    @Override
    public Certificate findBySerialNumber(BigInteger serialNumber) {
        System.out.println("serialNumber = " + serialNumber);
        Optional<Certificate> opt = this.certificateRepository.findBySerialNumber(serialNumber);
        return opt.orElseThrow(() -> new EntityNotFoundException(Certificate.class, "serialNumber", serialNumber.toString()));
    }

    @Override
    public Certificate createCertificate(
            CertificateGenerateRequestDTO request,
            String issuerSerialNumber,
            CertificateType type) {
        SubjectDTO subjectDTO = request.getSubjectDTO();
        X500Name subjectDN = this.subjectDTOToX500Name(subjectDTO);
        KeyPair keyPair;
        SubjectData subject;
        IssuerData issuer;

        X509Certificate certificate;

        if (type == CertificateType.ROOT) {
            keyPair = generateKeyPair(true);
            subject = generateSubjectData(keyPair.getPublic(), subjectDN, true);
            issuer = new IssuerData(keyPair.getPrivate(), subjectDN, subject.getPublicKey(), subject.getSerialNumber());
            certificate = generateCertificate(subject, issuer, true);
        }
        else if (type == CertificateType.INTERMEDIATE) {
            keyPair = generateKeyPair(true);
            subject = generateSubjectData(keyPair.getPublic(), subjectDN, true);
            System.out.println(subject.getSerialNumber());
            issuer = this.certificateStorage.getIssuerDataBySerialNumber(issuerSerialNumber);
            System.out.println(issuer.getSerialNumber());
            certificate = generateCertificate(subject, issuer, true);
        }
        else {
            keyPair = generateKeyPair(false);
            issuer = this.certificateStorage.getIssuerDataBySerialNumber(issuerSerialNumber);
            subject = generateSubjectData(keyPair.getPublic(), subjectDN, false);
            certificate = generateCertificate(subject, issuer, false);
        }

        this.certificateStorage.storeCertificateChain(new X509Certificate[]{certificate}, keyPair.getPrivate());

        /* Create distribution here */
        String[] filePathsOfDistributionFiles =
                this.certificateStorage
                        .storeCertificateDistributionFiles(
                            certificate.getSerialNumber().toString(),
                            type);


        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Certificate c = new Certificate(
                subject.getSerialNumber(),
                issuer.getSerialNumber(),
                type,
                type != CertificateType.LEAF,
                filePathsOfDistributionFiles[0],
                filePathsOfDistributionFiles[1],
                filePathsOfDistributionFiles[2],
                false,
                null,
                null,
                subject.getStartDate(),
                subject.getEndDate()
        );

        System.out.println(c.toString());
        certificateRepository.save(c);
        return c;
    }

    private X500Name subjectDTOToX500Name(SubjectDTO subjectDTO) {
        X500NameBuilder nameBuilder = new X500NameBuilder();

        if (!subjectDTO.getCommonName().isEmpty()) {
            nameBuilder.addRDN(BCStyle.CN, subjectDTO.getCommonName());
        }
        if (!subjectDTO.getOrganizationUnit().isEmpty()) {
            nameBuilder.addRDN(BCStyle.OU, subjectDTO.getOrganizationUnit());
        }
        if (!subjectDTO.getOrganization().isEmpty()) {
            nameBuilder.addRDN(BCStyle.O, subjectDTO.getOrganization());
        }
        if (!subjectDTO.getCountry().isEmpty()) {
            nameBuilder.addRDN(BCStyle.C, subjectDTO.getCountry());
        }
        if (!subjectDTO.getCity().isEmpty()) {
            nameBuilder.addRDN(BCStyle.L, subjectDTO.getCity());
        }
        return nameBuilder.build();
    }

}

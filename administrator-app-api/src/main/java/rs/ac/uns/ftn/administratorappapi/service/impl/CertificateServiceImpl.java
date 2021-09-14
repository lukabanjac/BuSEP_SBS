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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.administratorappapi.dto.CertificateDTO;
import rs.ac.uns.ftn.administratorappapi.dto.CertificateGenerateRequestDTO;
import rs.ac.uns.ftn.administratorappapi.dto.MessageDTO;
import rs.ac.uns.ftn.administratorappapi.dto.SubjectDTO;
import rs.ac.uns.ftn.administratorappapi.exception.EntityNotFoundException;
import rs.ac.uns.ftn.administratorappapi.exception.PkiMalfunctionException;
import rs.ac.uns.ftn.administratorappapi.model.*;
import rs.ac.uns.ftn.administratorappapi.model.Certificate;
import rs.ac.uns.ftn.administratorappapi.repository.CertificateRepository;
import rs.ac.uns.ftn.administratorappapi.repository.CertificateRequestRepository;
import rs.ac.uns.ftn.administratorappapi.repository.TrustedOrganizationRepository;
import rs.ac.uns.ftn.administratorappapi.repository.UserRepository;
import rs.ac.uns.ftn.administratorappapi.service.CertificateService;
import rs.ac.uns.ftn.administratorappapi.storage.CertificateStorage;
import rs.ac.uns.ftn.administratorappapi.util.DataGenerator;

import java.math.BigInteger;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CertificateServiceImpl implements CertificateService {

    @Autowired
    private CertificateStorage certificateStorage;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private CertificateRequestRepository  certificateRequestRepository;

    @Autowired
    private TrustedOrganizationRepository trustedOrganizationRepository;

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
                requestDTO.getUserId().toString().trim().equalsIgnoreCase("") ||
                requestDTO.getSecretWord1().trim().equalsIgnoreCase("") ||
                requestDTO.getSecretWord2().trim().equalsIgnoreCase("") ||
                requestDTO.getSecretWord3().trim().equalsIgnoreCase("")
        ){
            return new MessageDTO(false, "All fields must be filled!");
        }


        Optional<Certificate> issuerCertificate = certificateRepository
                .findBySerialNumber(requestDTO.getIssuerSerialNumber());

        if(!issuerCertificate.isPresent()){
            return new MessageDTO(false, "Invalid issuer serial number!");
        }

        if(issuerCertificate.get().getType() == CertificateType.LEAF){
            return new MessageDTO(false, "Invalid issuer certificate type!");
        }



        TrustedOrganization to = trustedOrganizationRepository
                .findByCountryAndCityAndOrganizationAndOrganizationUnit
                        (
                                requestDTO.getCountry(),
                                requestDTO.getCity(),
                                requestDTO.getOrganization(),
                                requestDTO.getOrganizationUnit()
                        );


        System.out.println("Trusted org DTO ->" + requestDTO.toString());
        System.out.println("Trusted org ->" + to);

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        System.out.println("\n\n");

        System.out.println(encoder.encode(requestDTO.getSecretWord1()));
        System.out.println(to.getSecretWord1());
        System.out.println("\n\n");

        System.out.println(requestDTO.getSecretWord1());
        System.out.println(requestDTO.getSecretWord2());
        System.out.println(requestDTO.getSecretWord3());

        System.out.println(encoder.matches(requestDTO.getSecretWord1(), to.getSecretWord1()));
        System.out.println(encoder.matches(requestDTO.getSecretWord2(), to.getSecretWord2()));
        System.out.println(encoder.matches(requestDTO.getSecretWord3(), to.getSecretWord3()));

        if(!encoder.matches(requestDTO.getSecretWord1(), to.getSecretWord1()) ||
        !encoder.matches(requestDTO.getSecretWord2(), to.getSecretWord2()) ||
        !encoder.matches(requestDTO.getSecretWord3(), to.getSecretWord3())){
            return new MessageDTO(false, "Invalid secret words!");

        }

        Optional<User> user = this.userRepository.findById(requestDTO.getUserId());
        if(!user.isPresent()){
            return new MessageDTO(false, "User id not found");
        }


        String organisation = "";
        String organisationUnit = "";
        if(user.get() instanceof  Admin){
            Admin admin = (Admin) user.get();
            organisation = admin.getTrustedOrganization().getOrganization();
            organisationUnit = admin.getTrustedOrganization().getOrganizationUnit();
        }else  if(user.get() instanceof Doctor){
            Doctor doctor = (Doctor) user.get();
            System.out.println(doctor.toString());

            TrustedOrganization trustedOrganization = trustedOrganizationRepository.getByOrganization(requestDTO.getOrganization());

            organisation = "Vlada RS";
            organisationUnit = "Ministarstvo Zdravlja";

            //TODO: prebaci da se u registraciji postavi na neki nacin kojoj organizaciji pripada

            //organisation = doctor.getTrusted_organization().getOrganization();
            //organisationUnit = doctor.getTrusted_organization().getOrganizationUnit();
        }




        CertificateRequest cr = new CertificateRequest();
        cr.setIssuerSerialNumber(requestDTO.getIssuerSerialNumber());
        cr.setCountry(requestDTO.getCountry());
        cr.setCity(requestDTO.getCity());

        cr.setOrganization(organisation);
        cr.setOrganizationUnit(organisationUnit);

//        cr.setOrganization(requestDTO.getOrganization());
//        cr.setOrganizationUnit(requestDTO.getOrganizationUnit());

        cr.setStatus(CertificateRequestStatus.PENDING);
        cr.setUser(user.get());

        System.out.println(cr);
        certificateRequestRepository.save(cr);

        return new MessageDTO(true, "Request was successfully sent :)");
    }

    @Override
    public List<Certificate> getRevoked(Boolean active) {
        return certificateRepository.findByRevoked(active);
    }

    @Override
    public List<CertificateDTO> getAllByIssuerNo(String id) {
        return this.certificateRepository.findAllByCaSerialNumberEquals(id).stream().map(c -> new CertificateDTO(c)).collect(Collectors.toList());
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


    private SubjectData generateSubjectData(PublicKey publicKey, X500Name subjectDN, boolean isCA, String dateFrom, String dateTo) throws ParseException {
        long now = System.currentTimeMillis();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        formatter.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        Date startDate = null;
        Date endDate = null;
        try{
            startDate = formatter.parse(dateFrom);
            endDate = formatter.parse(dateTo);
        }catch(ParseException e){
            throw  new ParseException("Invalid date format!",1);
        }
        return new SubjectData(publicKey, subjectDN, new BigInteger(Long.toString(now)), startDate, endDate);
    }

    @Override
    public List<Certificate> getAll() {
        return certificateRepository.findAll();
    }






    @Override
    public Certificate revokeCertificate(String serialNumber, String revokeReason) {

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
    public Certificate findBySerialNumber(String serialNumber) {
        System.out.println("serialNumber = " + serialNumber);
        Optional<Certificate> opt = this.certificateRepository.findBySerialNumber(serialNumber);
        return opt.orElseThrow(() -> new EntityNotFoundException(Certificate.class, "serialNumber", serialNumber.toString()));
    }

    @Override
    public CertificateRequest rejectRequest(Long id, String reason) {
        CertificateRequest cr = certificateRequestRepository.findById(id).get();
        cr.setRejectedReason(reason);
        cr.setStatus(CertificateRequestStatus.REJECTED);
        certificateRequestRepository.save(cr);
        return cr;
    }

    @Override
    public List<CertificateRequest> listCertificateRequestsByIssuerId(String id){
        List<CertificateRequest> list = this.certificateRequestRepository.findByIssuerSerialNumber(id);
        return list;
    }

    @Override
    public Certificate createCertificate(
            CertificateGenerateRequestDTO request,
            String issuerSerialNumber,
            CertificateType type) throws ParseException{
        SubjectDTO subjectDTO = request.getSubjectDTO();
        X500Name subjectDN = this.subjectDTOToX500Name(subjectDTO);
        KeyPair keyPair;
        SubjectData subject;
        IssuerData issuer;

        X509Certificate certificate;

        if (type == CertificateType.ROOT) {
            keyPair = generateKeyPair(true);
            subject = generateSubjectData(keyPair.getPublic(), subjectDN, true,request.getDateFrom(),request.getDateTo());
            issuer = new IssuerData(keyPair.getPrivate(), subjectDN, subject.getPublicKey(), subject.getSerialNumber());
            certificate = generateCertificate(subject, issuer, true);
        }
        else if (type == CertificateType.INTERMEDIATE) {
            keyPair = generateKeyPair(true);
            subject = generateSubjectData(keyPair.getPublic(), subjectDN, true,request.getDateFrom(),request.getDateTo());
            System.out.println(subject.getSerialNumber());
            issuer = this.certificateStorage.getIssuerDataBySerialNumber(issuerSerialNumber);
            System.out.println(issuer.getSerialNumber());
            certificate = generateCertificate(subject, issuer, true);
        }
        else {
            keyPair = generateKeyPair(false);
            issuer = this.certificateStorage.getIssuerDataBySerialNumber(issuerSerialNumber);
            subject = generateSubjectData(keyPair.getPublic(), subjectDN, false, request.getDateFrom(),request.getDateTo());
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
                subject.getSerialNumber().toString(),
                issuer.getSerialNumber().toString(),
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
        Optional<User> user = this.userRepository.findById(request.getUserId());
        c.setUser(user.get());
        System.out.println(c.getUser());
        certificateRepository.save(c);

        if(c.getType() != CertificateType.ROOT){
            CertificateRequest cr = certificateRequestRepository.findById(request.getCertificateRequestId()).get();
            cr.setStatus(CertificateRequestStatus.ACCEPTED);
            certificateRequestRepository.save(cr);
        }


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

package rs.ac.uns.ftn.administratorappapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import rs.ac.uns.ftn.administratorappapi.dto.CertificateGenerateDTO;
import rs.ac.uns.ftn.administratorappapi.model.Issuer;
import rs.ac.uns.ftn.administratorappapi.model.Subject;
import rs.ac.uns.ftn.administratorappapi.service.CertificateService;
import rs.ac.uns.ftn.administratorappapi.service.impl.CertificateServiceImpl;
import rs.ac.uns.ftn.administratorappapi.util.DataGenerator;

import java.security.KeyPair;
import java.security.cert.X509Certificate;

@RestController
public class CertificateController {

    @Autowired
    CertificateService certificateService;

    @GetMapping("createCertificate")
    public ResponseEntity<String> createCerttificate() {
        DataGenerator dataGen = new DataGenerator();

        Subject subject = dataGen.generateSubject();

        KeyPair keyPairIssuer = dataGen.generateKeyPair();
        Issuer issuer = dataGen.generateIssuer(keyPairIssuer.getPrivate());

        X509Certificate certificate = certificateService.generateCertificate(subject, issuer);

        String resData = "Radi";
        resData = resData.concat("\n===== Podaci o izdavacu sertifikata =====");
        resData = resData.concat(certificate.getIssuerX500Principal().getName());
        resData = resData.concat("\n===== Podaci o vlasniku sertifikata =====");
        resData = resData.concat(certificate.getSubjectX500Principal().getName());
        resData = resData.concat("\n===== Sertifikat =====");
        resData = resData.concat("-------------------------------------------------------");
        resData = resData.concat(certificate.toString());
        resData = resData.concat("-------------------------------------------------------");

        return new ResponseEntity<String>(resData, HttpStatus.OK);
    }


    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestBody CertificateGenerateDTO certificateGenerateDTO){
        certificateService.generate(certificateGenerateDTO);
        return new ResponseEntity<>("Certificate successfully generated!", HttpStatus.OK);
    }
}

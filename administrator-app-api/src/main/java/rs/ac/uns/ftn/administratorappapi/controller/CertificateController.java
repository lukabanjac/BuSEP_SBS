package rs.ac.uns.ftn.administratorappapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.administratorappapi.dto.CertificateGenerateDTO;
import rs.ac.uns.ftn.administratorappapi.model.IssuerData;
import rs.ac.uns.ftn.administratorappapi.model.SubjectData;
import rs.ac.uns.ftn.administratorappapi.service.CertificateService;
import rs.ac.uns.ftn.administratorappapi.util.DataGenerator;

import javax.websocket.server.PathParam;
import java.security.KeyPair;
import java.security.cert.X509Certificate;

@RestController
@RequestMapping("api/certificates")
public class CertificateController {

    @Autowired
    CertificateService certificateService;

    @Autowired
    DataGenerator dataGenerator;

    @GetMapping("create")
    public ResponseEntity<String> createCerttificate() {

        SubjectData subject = dataGenerator.generateSubjectExample();

        KeyPair keyPairIssuer = dataGenerator.generateKeyPair();
        IssuerData issuer = dataGenerator.generateIssuer(keyPairIssuer.getPrivate());

        X509Certificate certificate = certificateService.generateCertificate(subject, issuer);

        String resData = "Radi";
        resData = resData.concat("\n===== Podaci o izdavacu sertifikata =====\n");
        resData = resData.concat(certificate.getIssuerX500Principal().getName());
        resData = resData.concat("\n===== Podaci o vlasniku sertifikata =====\n");
        resData = resData.concat(certificate.getSubjectX500Principal().getName());
        resData = resData.concat("\n===== Sertifikat =====\n");
        resData = resData.concat("\n-------------------------------------------------------\n");
        resData = resData.concat(certificate.toString());
        resData = resData.concat("\n-------------------------------------------------------\n");

        return new ResponseEntity<String>(resData, HttpStatus.OK);
    }


    @PostMapping("generate")
    public ResponseEntity<String> generate(@RequestBody CertificateGenerateDTO certificateGenerateDTO){
        System.out.println(certificateGenerateDTO);
        certificateService.generate(certificateGenerateDTO);
        return new ResponseEntity<>("Certificate successfully generated!", HttpStatus.OK);
    }


    @PostMapping("issueTo/{username}")
    public ResponseEntity<String> issueTo(@PathVariable String username) {
        return new ResponseEntity<String>(certificateService.issueTo(username));
    }
}

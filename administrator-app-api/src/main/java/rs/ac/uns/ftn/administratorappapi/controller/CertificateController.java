package rs.ac.uns.ftn.administratorappapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.administratorappapi.dto.CertificateDTO;
import rs.ac.uns.ftn.administratorappapi.dto.CertificateGenerateDTO;
import rs.ac.uns.ftn.administratorappapi.dto.CertificateGenerateRequestDTO;
import rs.ac.uns.ftn.administratorappapi.exception.ParentCertificateExpireException;
import rs.ac.uns.ftn.administratorappapi.model.Certificate;
import rs.ac.uns.ftn.administratorappapi.model.CertificateType;
import rs.ac.uns.ftn.administratorappapi.model.IssuerData;
import rs.ac.uns.ftn.administratorappapi.model.SubjectData;
import rs.ac.uns.ftn.administratorappapi.service.CertificateService;
import rs.ac.uns.ftn.administratorappapi.util.DataGenerator;

import javax.websocket.server.PathParam;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.cert.X509Certificate;
import java.util.List;

@RestController
@RequestMapping("api/certificates")
public class CertificateController {

    @Autowired
    CertificateService certificateService;

    @Autowired
    DataGenerator dataGenerator;

    @RequestMapping(value = "generate",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CertificateDTO> generate(@RequestBody CertificateGenerateRequestDTO request) {
        /*if (!(this.authService.hasPermission(PermissionTableSeed.ISSUE_ROOT_CERTIFICATE) && request.getCertificateType() == CertificateType.ROOT ||
                this.authService.hasPermission(PermissionTableSeed.ISSUE_INTERMEDIATE_CERTIFICATE) && request.getCertificateType() == CertificateType.INTERMEDIATE ||
                this.authService.hasPermission(PermissionTableSeed.ISSUE_USER_CERTIFICATE) && request.getCertificateType() == CertificateType.USER)) {
            throw new AccessDeniedException("User has no permission to issue " + request.getCertificateType().name() + " certificates.");
        }*/

        Certificate parentCertificate;
        String issuerSerialNumber = "";

        if(request.getCertificateType() != CertificateType.ROOT) {
            parentCertificate = this.certificateService.findBySerialNumber(new BigInteger(request.getIssuerSerialNumber()));
            issuerSerialNumber = parentCertificate.getSerialNumber().toString();
        }

//        if (parentCertificate.getExpiringAt().before(request.getExpiringAt())) {
//            throw new ParentCertificateExpireException();
//        }

        Certificate c = certificateService.createCertificate(
                request.getSubjectDTO(),
                issuerSerialNumber,
                request.getCertificateType()
        );

        return new ResponseEntity<>(new CertificateDTO(c), HttpStatus.OK);
    }
//    @PostMapping("generate")
//    public ResponseEntity<String> generate(@RequestBody CertificateGenerateDTO certificateGenerateDTO){
//        System.out.println(certificateGenerateDTO);
//        certificateService.generate(certificateGenerateDTO);
//        return new ResponseEntity<>("Certificate successfully generated!", HttpStatus.OK);
//    }


    /*
    @PostMapping("issueTo/{username}")
    public ResponseEntity<String> issueTo(@PathVariable String username) {
        return new ResponseEntity<String>(certificateService.issueTo(username));
    }
+
     */

    @GetMapping("getAll")
    public ResponseEntity<List<Certificate>> getAll() {
        return new ResponseEntity<>(certificateService.getAll(), HttpStatus.OK);
    }

    @PostMapping("revoke")
    public ResponseEntity<Certificate> revoke(@RequestBody CertificateDTO certificate) {
        return new ResponseEntity<>(certificateService.revokeCertificate(new BigInteger(certificate.getSerialNumber()), certificate.getRevokeReason()), HttpStatus.OK);
    }

    @GetMapping("getOne/{serialNumber}")
    public ResponseEntity<Certificate> getOne(@PathVariable String serialNumber) {
        return new ResponseEntity<>(certificateService.findBySerialNumber(new BigInteger(serialNumber)), HttpStatus.OK);
    }

}

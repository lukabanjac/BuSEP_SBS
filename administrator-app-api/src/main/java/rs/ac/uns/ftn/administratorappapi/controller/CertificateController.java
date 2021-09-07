package rs.ac.uns.ftn.administratorappapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.administratorappapi.dto.CertificateDTO;
import rs.ac.uns.ftn.administratorappapi.dto.CertificateGenerateRequestDTO;
import rs.ac.uns.ftn.administratorappapi.dto.MessageDTO;
import rs.ac.uns.ftn.administratorappapi.model.Certificate;
import rs.ac.uns.ftn.administratorappapi.model.CertificateRequest;
import rs.ac.uns.ftn.administratorappapi.model.CertificateType;
import rs.ac.uns.ftn.administratorappapi.service.CertificateService;
import rs.ac.uns.ftn.administratorappapi.util.DataGenerator;

import java.math.BigInteger;
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
    public ResponseEntity<CertificateDTO> generate(@RequestBody CertificateGenerateRequestDTO request) throws Exception {
        Certificate parentCertificate;
        String issuerSerialNumber = "";

        if(request.getCertificateType() != CertificateType.ROOT) {
            parentCertificate = this.certificateService.findBySerialNumber(request.getIssuerSerialNumber());
            issuerSerialNumber = parentCertificate.getSerialNumber().toString();
        }

//        if (parentCertificate.getExpiringAt().before(request.getExpiringAt())) {
//            throw new ParentCertificateExpireException();
//        }

        Certificate c = certificateService.createCertificate(
                request,
                issuerSerialNumber,
                request.getCertificateType()
        );

        return new ResponseEntity<>(new CertificateDTO(c), HttpStatus.OK);
    }

    @RequestMapping(value = "getCertificatesByIssuerId/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCertificatesByIssuerId(@PathVariable Long id) {

        List<CertificateRequest> certificateRequests = this.certificateService.listCertificateRequestsByIssuerId(id);
        if(certificateRequests == null){
            return new ResponseEntity<>(certificateRequests, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(certificateRequests, HttpStatus.OK);

    }


    @RequestMapping(value = "generateRequest",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MessageDTO> generateRequest(@RequestBody CertificateGenerateRequestDTO request) {
        MessageDTO messageDTO = certificateService.generateRequest(request);
        return new ResponseEntity<MessageDTO>( messageDTO, HttpStatus.OK);
    }

    @GetMapping("getAll")
    public ResponseEntity<List<Certificate>> getAll() {
        return new ResponseEntity<>(certificateService.getAll(), HttpStatus.OK);
    }

    @PostMapping("revoke")
    public ResponseEntity<Certificate> revoke(@RequestBody CertificateDTO certificate) {
        return new ResponseEntity<>(certificateService.revokeCertificate(certificate.getSerialNumber(), certificate.getRevokeReason()), HttpStatus.OK);
    }

    @GetMapping("getOne/{serialNumber}")
    public ResponseEntity<Certificate> getOne(@PathVariable String serialNumber) {
        return new ResponseEntity<>(certificateService.findBySerialNumber(serialNumber), HttpStatus.OK);
    }

}

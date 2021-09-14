package rs.ac.uns.ftn.administratorappapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.administratorappapi.dto.CertificateDTO;
import rs.ac.uns.ftn.administratorappapi.dto.CertificateGenerateRequestDTO;
import rs.ac.uns.ftn.administratorappapi.dto.DeviceDTO;
import rs.ac.uns.ftn.administratorappapi.dto.MessageDTO;
import rs.ac.uns.ftn.administratorappapi.model.*;
import rs.ac.uns.ftn.administratorappapi.repository.TrustedOrganizationRepository;
import rs.ac.uns.ftn.administratorappapi.service.CertificateService;
import rs.ac.uns.ftn.administratorappapi.service.DeviceService;
import rs.ac.uns.ftn.administratorappapi.util.DataGenerator;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("api/certificates")
public class CertificateController {

    @Autowired
    CertificateService certificateService;

    @Autowired
    DataGenerator dataGenerator;

    @Autowired
    DeviceService deviceService;

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

    @RequestMapping(value = "generateForLeaf",
            method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> generateForLeaf(@RequestBody HashMap<String,Object> body) throws Exception {
        DeviceDTO deviceDTO = (DeviceDTO) body.get("deviceDTO");
        Device device = deviceService.addDevice(deviceDTO);
        ResponseEntity<CertificateDTO> re = this.generate((CertificateGenerateRequestDTO) body.get("certificateRequestDTO"));
        HashMap<String, Object> responseJson = new HashMap<>();
        responseJson.put("device", device);
        responseJson.put("certificateDTO", re.getBody());
        return new ResponseEntity<>(responseJson, HttpStatus.OK);
    }



    @RequestMapping(value = "getCertificatesByIssuerId/{id}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getCertificatesByIssuerId(@PathVariable("id") String id) {
        System.out.println(id);
        List<CertificateRequest> certificateRequests = this.certificateService.listCertificateRequestsByIssuerId(id);
        System.out.println(certificateRequests.size());
        if(certificateRequests == null){
            return new ResponseEntity<>(certificateRequests, HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(certificateRequests, HttpStatus.OK);
    }

    @RequestMapping(value = "getCertificatesByUserId/{user_id}")
    public ResponseEntity<?> getCertificateByUserId(@PathVariable("user_id") Long user_id) {
        System.out.println("certReq get by user id -> "+user_id);
        CertificateRequest certificateRequest = certificateService.getCertReqByUserId(user_id);
        if (certificateRequest != null) {
            return new ResponseEntity<>(certificateRequest, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(certificateRequest, HttpStatus.NO_CONTENT);
        }
    }



    @RequestMapping(value = "rejectRequest/{id}/{reason}",
            method = RequestMethod.PUT,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> rejectRequest(@PathVariable("id") String id,@PathVariable("reason") String reason) {
        System.out.println(id);
        System.out.println(reason);
        CertificateRequest cr = certificateService.rejectRequest(Long.parseLong(id),reason);
        return new ResponseEntity<>(cr, HttpStatus.OK);
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

    @GetMapping("getAllByCaSerialNo/{id}")
    public ResponseEntity<List<CertificateDTO>> getAllByCaSerialNo(@PathVariable("id") String id){
        return new ResponseEntity<List<CertificateDTO>>(certificateService.getAllByIssuerNo(id), HttpStatus.OK);
    }

    @PostMapping("revoke")
    public ResponseEntity<Certificate> revoke(@RequestBody CertificateDTO certificate) {
        return new ResponseEntity<>(certificateService.revokeCertificate(certificate.getSerialNumber(), certificate.getRevokeReason()), HttpStatus.OK);
    }

    @GetMapping("getOne/{serialNumber}")
    public ResponseEntity<Certificate> getOne(@PathVariable String serialNumber) {
        return new ResponseEntity<>(certificateService.findBySerialNumber(serialNumber), HttpStatus.OK);
    }

    @GetMapping("getActive")
    public ResponseEntity<List<Certificate>> getActive(){
        return new ResponseEntity<>(certificateService.getRevoked(false), HttpStatus.OK);
    }

    @GetMapping("getRevoked")
    public ResponseEntity<List<Certificate>> getRevoked(){
        return new ResponseEntity<>(certificateService.getRevoked(true), HttpStatus.OK);
    }


}

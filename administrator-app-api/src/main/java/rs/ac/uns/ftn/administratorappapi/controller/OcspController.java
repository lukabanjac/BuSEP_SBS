package rs.ac.uns.ftn.administratorappapi.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("verify")
@Slf4j
public class OcspController {

    @GetMapping
    public ResponseEntity<String> gene(){
        System.out.println("usao u konzolu");
        return new ResponseEntity<String>("asfasfasfasfsafas", HttpStatus.OK);
    }


}

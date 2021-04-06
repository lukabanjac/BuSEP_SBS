package rs.ac.uns.ftn.administratorappapi.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.ws.Response;
import java.net.http.HttpResponse;

@RestController()
public class UserController {

    @GetMapping("test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<String>("test", HttpStatus.OK);
    }
}

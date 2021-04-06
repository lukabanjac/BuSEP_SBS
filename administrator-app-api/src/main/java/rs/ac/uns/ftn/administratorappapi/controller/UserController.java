package rs.ac.uns.ftn.administratorappapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.administratorappapi.dto.UserDTO;
import rs.ac.uns.ftn.administratorappapi.model.User;
import rs.ac.uns.ftn.administratorappapi.service.UserService;

import javax.xml.ws.Response;
import java.net.http.HttpResponse;
import java.util.List;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("test")
    public ResponseEntity<String> test() {
        return new ResponseEntity<String>("test", HttpStatus.OK);
    }

    @PostMapping("register")
    public ResponseEntity<User> register(@RequestBody UserDTO userDto) {
        return new ResponseEntity<>(userService.register(userDto), HttpStatus.OK);
    }

    @GetMapping("all")
    public ResponseEntity<List<User>> getAll() {
        return new ResponseEntity<>(userService.getAll(), HttpStatus.OK);
    }
}

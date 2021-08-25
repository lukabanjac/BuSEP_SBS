package rs.ac.uns.ftn.administratorappapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import rs.ac.uns.ftn.administratorappapi.dto.UserDTO;
import rs.ac.uns.ftn.administratorappapi.model.Authority;
import rs.ac.uns.ftn.administratorappapi.model.User;
import rs.ac.uns.ftn.administratorappapi.model.UserTokenState;
import rs.ac.uns.ftn.administratorappapi.security.TokenHelper;
import rs.ac.uns.ftn.administratorappapi.security.auth.JwtAuthenticationRequest;
import rs.ac.uns.ftn.administratorappapi.service.UserService;
import rs.ac.uns.ftn.administratorappapi.service.impl.CustomUserDetailsService;

import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Response;
import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/user")
public class UserController {

    @Autowired
    TokenHelper tokenHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

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


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(
            @RequestBody JwtAuthenticationRequest authenticationRequest,
            HttpServletResponse response
    ) throws AuthenticationException, IOException {

        System.out.println("login");
        System.out.println(authenticationRequest.getUsername());

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        String encoded =  passwordEncoder.encode(authenticationRequest.getPassword());
        System.out.println(encoded);

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );


        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Kreiraj token
        User user = (User)authentication.getPrincipal();

        System.out.println("email: "  + user.getEmail() + "\npassword: " + user.getPassword());
        String jws = tokenHelper.generateToken(user);
        int expiresIn = tokenHelper.getExpiredIn();

        ArrayList<Authority> authorities = user.getAuthorities().stream().map(authority->(Authority) authority).collect(Collectors.toCollection(ArrayList::new));

        // Vrati token kao odgovor na uspesno autentifikaciju
        return ResponseEntity.ok(new UserTokenState(jws, expiresIn, authorities.get(0).getAuthority()));
    }
}

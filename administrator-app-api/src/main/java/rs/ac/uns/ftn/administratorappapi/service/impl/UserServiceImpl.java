package rs.ac.uns.ftn.administratorappapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.administratorappapi.dto.UserDTO;
import rs.ac.uns.ftn.administratorappapi.exception.DataDTONotValidException;
import rs.ac.uns.ftn.administratorappapi.model.*;
import rs.ac.uns.ftn.administratorappapi.repository.AuthorityRepository;
import rs.ac.uns.ftn.administratorappapi.repository.TrustedOrganizationRepository;
import rs.ac.uns.ftn.administratorappapi.repository.UserRepository;
import rs.ac.uns.ftn.administratorappapi.service.UserService;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    TrustedOrganizationRepository trustedOrganizationRepository;

    @Override
    public User register(UserDTO userDto){
        User user = null;
        Authority authority;
        TrustedOrganization trustedOrganization = trustedOrganizationRepository.findById(Long.parseLong(userDto.getOrganisationId())).get();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        try{
            if(userDto.getType().toString().equals("ADMIN")){
                user = new Admin();
                authority = authorityRepository.findByName("ROLE_ADMIN");
                ((Admin) user).setTrustedOrganization(trustedOrganization);
                System.out.println(((Admin) user).getTrustedOrganization());
            }else if(userDto.getType().toString().equals("DOCTOR")){
                user = new Doctor();
                authority = authorityRepository.findByName("ROLE_DOCTOR");
                ((Doctor) user).setTrustedOrganization(trustedOrganization);
                System.out.println(((Doctor) user).getTrustedOrganization().getId());


            }else{
                throw new DataDTONotValidException("User type not valid!");
            }



            user.setUsername(userDto.getUsername());
            user.setPassword(encoder.encode(userDto.getPassword()));
            user.setEmail(userDto.getEmail());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setLastPasswordResetDate(new Timestamp(System.currentTimeMillis()));
            user.setAuthorities(Collections.singletonList(authority));

            System.out.println("authoriy test ---> " + user.getAuthorities());


        }catch (DataDTONotValidException e){
            System.out.println("Data not valid exception!");
        }

        user.setEnabled(true);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
}

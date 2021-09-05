package rs.ac.uns.ftn.administratorappapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.administratorappapi.dto.UserDTO;
import rs.ac.uns.ftn.administratorappapi.exception.DataDTONotValidException;
import rs.ac.uns.ftn.administratorappapi.model.Admin;
import rs.ac.uns.ftn.administratorappapi.model.Doctor;
import rs.ac.uns.ftn.administratorappapi.model.User;
import rs.ac.uns.ftn.administratorappapi.repository.UserRepository;
import rs.ac.uns.ftn.administratorappapi.service.UserService;
import java.sql.Timestamp;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User register(UserDTO userDto){
        User user = null;

        try{
            if(userDto.getType().toString().equals("ADMIN")){
                user = new Admin();
                user.setPassword(userDto.getPassword());
            }else if(userDto.getType().toString().equals("DOCTOR")){
                user = new Doctor();
                user.setPassword(userDto.getPassword());
            }else{
                throw new DataDTONotValidException("User type not valid!");
            }

            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setLastPasswordResetDate(new Timestamp(System.currentTimeMillis()));


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

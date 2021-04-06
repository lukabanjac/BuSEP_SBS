package rs.ac.uns.ftn.administratorappapi.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.administratorappapi.dto.UserDTO;
import rs.ac.uns.ftn.administratorappapi.model.Authority;
import rs.ac.uns.ftn.administratorappapi.model.User;
import rs.ac.uns.ftn.administratorappapi.repository.UserRepository;
import rs.ac.uns.ftn.administratorappapi.service.UserService;

import java.sql.Timestamp;
import java.sql.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User register(UserDTO userDto) {
        System.out.println(userDto);

        User user = new User(
                userDto.getUsername(),
                userDto.getFirstName(),
                userDto.getLastName(),
                userDto.getEmail(),
                userDto.getPassword(),
                new Timestamp(new java.util.Date().getTime())
        );

        System.out.println(user);
        user.setEnabled(false);
        return userRepository.save(user);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }
}

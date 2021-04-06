package rs.ac.uns.ftn.administratorappapi.service;

import org.springframework.stereotype.Service;
import rs.ac.uns.ftn.administratorappapi.dto.UserDTO;
import rs.ac.uns.ftn.administratorappapi.model.User;

import java.util.List;

@Service
public interface
UserService {
    User register(UserDTO user);
    List<User> getAll();
}

package com.nodo.retotecnico.serviceImpl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.nodo.retotecnico.dto.RegisterRequest;
import com.nodo.retotecnico.model.User;
import com.nodo.retotecnico.repository.UsersRepository;
import com.nodo.retotecnico.service.UsersService;

@Service
public class UsersServiceImpl implements UsersService{

    @Autowired
    private UsersRepository UserRepository;

    @Autowired
    private com.nodo.retotecnico.repository.UserRepository specificUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return UserRepository.findAll();
    }

    @Override
    public User getUsersById(Integer id){
        return UserRepository.findById(id).orElse(null);
    }
    
    @Override
    public Integer createUser(User user) {
        // Fallback for creating user directly if needed
        return UserRepository.save(user).getId();
    }

    public Integer registerUser(RegisterRequest request) {
        if (specificUserRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists.");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setName(request.getFirstName() + " " + request.getLastName());
        newUser.setFirstName(request.getFirstName());
        newUser.setLastName(request.getLastName());
        newUser.setCountry(request.getCountry());
        newUser.setRole("ROLE_USER");
        newUser.setRegistrationDate(new Date());
        newUser.setEmail(request.getEmail() != null ? request.getEmail() : request.getUsername() + "@example.com");

        return UserRepository.save(newUser).getId();
    }

}



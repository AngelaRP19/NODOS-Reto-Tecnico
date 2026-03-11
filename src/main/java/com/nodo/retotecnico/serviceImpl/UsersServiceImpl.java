package com.nodo.retotecnico.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nodo.retotecnico.model.User;
import com.nodo.retotecnico.repository.UsersRepository;
import com.nodo.retotecnico.service.UsersService;

@Service
public class UsersServiceImpl implements UsersService{

    @Autowired
    private UsersRepository UserRepository;

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
        if(UserRepository.findById(user.getId()).isPresent()){
            throw new IllegalArgumentException("User with id " + user.getId() + " already exists.");
        }
        return UserRepository.save(user).getId();
    }

}



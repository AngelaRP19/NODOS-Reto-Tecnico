package com.nodo.retotecnico.serviceImpl;

import org.springframework.stereotype.Service;

@Service
public class UsersService {
    
}

@Service
public class UserServiveImpl implements IUserService{

    @Override
    public list<User> getAllUser() {
        return UserRepository.findAll();
    }

    @Override
    public User getUserById(Integer id){
        return UserRepository.findById(id).orElse(null);
    }

}



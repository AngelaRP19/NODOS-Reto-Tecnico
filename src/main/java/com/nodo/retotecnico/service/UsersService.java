package com.nodo.retotecnico.service;

public interface  IUsersService {
    
}

public interface UsersService{
    List<Users> getAllUsers();
    Users getUsersById(Long id);
}
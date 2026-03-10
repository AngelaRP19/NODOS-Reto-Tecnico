package com.nodo.retotecnico.service;

public interface  IUsersService {

    List<Users> getAllUsers();
    Users getUsersById(Integer id);
}
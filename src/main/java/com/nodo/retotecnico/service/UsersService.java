package com.nodo.retotecnico.service;

import java.util.List;

import com.nodo.retotecnico.model.User;

public interface  UsersService {

    List<User> getAllUsers();
    User getUsersById(Integer id);
}
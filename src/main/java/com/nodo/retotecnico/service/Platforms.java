package com.nodo.retotecnico.service;

import java.util.List;

import com.nodo.retotecnico.model.User;

public interface  Platforms {

    List<User> getAllPlatforms();
    User getUsersById(Integer id);
    Integer createPlatform(User name);
}
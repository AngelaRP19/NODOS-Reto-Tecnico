package com.nodo.retotecnico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nodo.retotecnico.model.User;
import com.nodo.retotecnico.service.UsersService;

@RestController
@RequestMapping("/nodos/Users")
public class UsersController {

    @Autowired
    private UsersService UsersService;

    @GetMapping
    public List<User> getAllUsers() {
        return UsersService.getAllUsers();
    }

    @GetMapping ("/{id}")
    public User getUsersById(@PathVariable Integer id) {
        return UsersService.getUsersById(id);
    }
    @PostMapping("/create")
    public Integer createUser(@RequestBody User user) {
        return UsersService.createUser(user);
    }

    @GetMapping
    public String welcome() {return "Welcome Spring Security";}
}

package com.nodo.retotecnico.controller;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class UsersController {
    
}

@RequestMapping("/nodos/Users")
public class UsersController {

    @Autowired
    private UsersService UsersService;

    @GetMapping
    public list<Users> getAllUsers() {
        return UsersService.getAllUsers();
    }

    @GetMapping ("/{id}")
    public Users getUsersById(@PathVariable long id) {
        return UsersService.getUsersById(id);
    }
}
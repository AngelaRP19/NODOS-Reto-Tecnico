package com.nodo.retotecnico.Users;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<com.nodo.retotecnico.Users.User, Integer>{
    Optional<com.nodo.retotecnico.Users.User> findByUsername(String username);
}

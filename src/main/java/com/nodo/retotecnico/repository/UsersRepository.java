package com.nodo.retotecnico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nodo.retotecnico.model.User;

import java.util.Optional;

@Repository
public interface
UsersRepository extends JpaRepository<User, Integer>{
    
}
public interface UserRepository extends JpaRepository<com.nodo.retotecnico.User.User, Integer>{
    Optional<com.nodo.retotecnico.User.User> findByUsername(String username);
}
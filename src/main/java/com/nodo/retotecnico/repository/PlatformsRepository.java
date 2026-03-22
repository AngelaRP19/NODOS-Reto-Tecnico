package com.nodo.retotecnico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nodo.retotecnico.model.Platforms;

import java.util.Optional;

@Repository
public interface PlatformsRepository extends JpaRepository<Platforms, Integer> {

    // Buscar una plataforma por su nombre
    Optional<Platforms> findByName(String name);

}
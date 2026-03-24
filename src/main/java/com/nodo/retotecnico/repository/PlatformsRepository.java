package com.nodo.retotecnico.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nodo.retotecnico.model.Platform;

@Repository
public interface PlatformsRepository extends JpaRepository<Platform, Integer> {
    Optional<Platform> findByName(String name);

}
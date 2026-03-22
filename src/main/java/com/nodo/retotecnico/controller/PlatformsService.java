package com.nodo.retotecnico.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nodo.retotecnico.model.Platforms;
import com.nodo.retotecnico.repository.PlatformsRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PlatformsService {

    @Autowired
    private PlatformsRepository platformsRepository;

    // 1️ Listar todas las plataformas
    public List<Platforms> getAllPlatforms() {
        return platformsRepository.findAll();
    }

    // 2️ Buscar plataforma por ID
    public Optional<Platforms> getPlatformById(Integer id) {
        return platformsRepository.findById(id);
    }

    // 3️ Buscar plataforma por nombre
    public Optional<Platforms> getPlatformByName(String name) {
        return platformsRepository.findByName(name);
    }

    // 4️ Crear nueva plataforma
    @SuppressWarnings("null")
    public Platforms createPlatform(Platforms platform) {
        return platformsRepository.save(platform);
    }
}
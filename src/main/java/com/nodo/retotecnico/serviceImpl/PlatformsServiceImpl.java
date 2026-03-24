package com.nodo.retotecnico.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nodo.retotecnico.model.Platform;
import com.nodo.retotecnico.repository.PlatformsRepository;
import com.nodo.retotecnico.service.PlatformsService;

@Service
public class PlatformsServiceImpl implements PlatformsService {

    @Autowired
    private PlatformsRepository platformsRepository;

    @Override
    public List<Platform> getAllPlatform() {
        return platformsRepository.findAll();
    }

    @Override
    public Platform getPlatformById(Integer id) {
        return platformsRepository.findById(id).orElse(null);
    }

    @Override
    public Integer createPlatform(Platform platform) {
        Platform saved = platformsRepository.save(platform);
        return saved.getId();
    }

    @Override
    public Platform updatePlatform(Integer id, Platform platforms) {
        Platform existingPlatform = platformsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Platform not found"));
        existingPlatform.setName(platforms.getName());
        existingPlatform.setUrl(platforms.getUrl());
        return platformsRepository.save(existingPlatform);

    }

    @Override
    public void deletePlatform(Integer id) {
        if (!platformsRepository.existsById(id)) {
            throw new RuntimeException("Platform not found");
        }
        platformsRepository.deleteById(id);
    }
    
}
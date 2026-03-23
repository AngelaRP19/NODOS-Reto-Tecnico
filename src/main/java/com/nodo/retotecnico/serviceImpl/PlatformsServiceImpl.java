package com.nodo.retotecnico.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nodo.retotecnico.model.Platforms;
import com.nodo.retotecnico.repository.PlatformsRepository;
import com.nodo.retotecnico.service.PlatformsService;

@Service
public class PlatformsServiceImpl implements PlatformsService {

    @Autowired
    private PlatformsRepository platformsRepository;

    @Override
    public List<Platforms> getAllPlatform() {
        return platformsRepository.findAll();
    }

    @Override
    public Platforms getPlatformById(Integer id) {
        return platformsRepository.findById(id).orElse(null);
    }

    @Override
    public Integer createPlatform(Platforms platform) {
        Platforms saved = platformsRepository.save(platform);
        return saved.getId();
    }
}
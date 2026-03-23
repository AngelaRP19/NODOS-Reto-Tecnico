package com.nodo.retotecnico.service;

import java.util.List;

import com.nodo.retotecnico.model.Platform;

public interface PlatformsService {

    List<Platform> getAllPlatform();
    Platform getPlatformById(Integer id);
    Integer createPlatform(Platform platform);
    Platform updatePlatform(Integer id, Platform platforms);
    void deletePlatform(Integer id);
}
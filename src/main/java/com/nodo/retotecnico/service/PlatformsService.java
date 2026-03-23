package com.nodo.retotecnico.service;

import java.util.List;
import com.nodo.retotecnico.model.Platforms;

public interface PlatformsService {

    List<Platforms> getAllPlatform();
    Platforms getPlatformById(Integer id);
    Integer createPlatform(Platforms platform);
}
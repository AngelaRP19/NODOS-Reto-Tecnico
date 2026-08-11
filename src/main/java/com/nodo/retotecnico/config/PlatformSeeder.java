package com.nodo.retotecnico.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nodo.retotecnico.model.Platform;
import com.nodo.retotecnico.repository.PlatformsRepository;

/**
 * Siembra las plataformas referenciadas por el campo "platforms" de
 * ExpansionPackSeeder ("PC / Mac / Consolas" en los 6 packs sembrados), para
 * que POST /nodos/cart/add tenga un platformId válido contra el que matchear.
 */
@Component
public class PlatformSeeder implements CommandLineRunner {

    @Autowired
    private PlatformsRepository platformsRepository;

    @Override
    public void run(String... args) {
        if (platformsRepository.count() > 0) {
            return;
        }
        platformsRepository.saveAll(List.of(
            platform("PC", "https://store.steampowered.com"),
            platform("Mac", "https://www.apple.com/mac/"),
            platform("Consolas", "https://www.playstation.com/es-co/")
        ));
    }

    private Platform platform(String name, String url) {
        Platform platform = new Platform();
        platform.setName(name);
        platform.setUrl(url);
        return platform;
    }
}

package com.nodo.retotecnico.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nodo.retotecnico.model.Platforms;
import com.nodo.retotecnico.service.PlatformsService;

@RestController
@RequestMapping("/nodos/platform")
public class PlatformController {

    @Autowired
    private PlatformsService platformsService;

    @GetMapping("/")
    public String hello() {
        return "Juega gratis";
    }

    @GetMapping
    public List<Platforms> getAllPlatforms() {
        return platformsService.getAllPlatform();
    }

    @GetMapping ("/{id}")
    public Platforms getPlatformById(@PathVariable Integer id) {
        return platformsService.getPlatformById(id);
    }
    @PostMapping("/create")
    public Integer createPlatform(@RequestBody Platforms platforms) {
        return platformsService.createPlatform(platforms);
    }
}

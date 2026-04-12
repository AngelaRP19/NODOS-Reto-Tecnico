package com.nodo.retotecnico.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nodo.retotecnico.dto.PlatformDTO;
import com.nodo.retotecnico.model.Platform;
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
    public List<PlatformDTO> getAllPlatforms() {
        return platformsService.getAllPlatform().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @GetMapping ("/{id}")
    public PlatformDTO getPlatformById(@PathVariable Integer id) {
        return convertToDTO(platformsService.getPlatformById(id));
    }
    
    @PostMapping("/add")
    public Integer createPlatform(@RequestBody Platform platforms) {
        return platformsService.createPlatform(platforms);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Platform> updatePlatform(@PathVariable Integer id, @RequestBody Platform platforms){
        return ResponseEntity.ok(platformsService.updatePlatform(id, platforms));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePlatform(@PathVariable Integer id){
        platformsService.deletePlatform(id);
        return ResponseEntity.ok("Platform deleted successfully");
    }

    private PlatformDTO convertToDTO(Platform platform) {
        return new PlatformDTO(platform.getId(), platform.getName());
    }
}

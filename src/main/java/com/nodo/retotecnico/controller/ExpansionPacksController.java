package com.nodo.retotecnico.controller;

import java.util.List;
import java.util.Locale;
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

import com.nodo.retotecnico.dto.ExpansionPackResponseDTO;
import com.nodo.retotecnico.model.ExpansionPack;
import com.nodo.retotecnico.service.ExpansionPacksService;
import com.nodo.retotecnico.service.LocalizedContentService;

@RestController
@RequestMapping("/nodos/expansionpacks")
public class ExpansionPacksController {

    @Autowired
    private ExpansionPacksService expansionPacksService;

    @Autowired
    private LocalizedContentService localizedContentService;

    @GetMapping
    public List<ExpansionPackResponseDTO> getAllExpansionPacks(Locale locale) {
        return expansionPacksService.getAllExpansionPacks(locale.getLanguage()).stream()
                .map(pack -> localizedContentService.toResponseDto(pack, locale))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ExpansionPackResponseDTO getExpansionPacksById(@PathVariable Integer id, Locale locale) {
        ExpansionPack pack = expansionPacksService.getExpansionPacksById(id);
        return localizedContentService.toResponseDto(pack, locale);
    }

    @PostMapping("/create")
    public Integer createExpansionPack(@RequestBody ExpansionPack expansionPack) {
        return expansionPacksService.createExpansionPack(expansionPack);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpansionPack> updateExpansionPack(@PathVariable Integer id,
            @RequestBody ExpansionPack expansionPack) {
        return ResponseEntity.ok(expansionPacksService.updateExpansionPack(id, expansionPack));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpansionPack(@PathVariable Integer id, Locale locale) {
        expansionPacksService.deleteExpansionPack(id);
        return ResponseEntity.ok(localizedContentService.getMessage("expansionpack.delete.success", locale,
                "Expansion pack deleted successfully"));
    }
}

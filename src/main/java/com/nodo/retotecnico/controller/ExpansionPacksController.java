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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nodo.retotecnico.dto.ExpansionPackResponseDTO;
import com.nodo.retotecnico.dto.PlatformSelectionDTO;
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

    private Locale resolveLocale(String langParam, String languageParam, Locale locale) {
        String code = langParam != null && !langParam.isBlank() ? langParam
                : (languageParam != null && !languageParam.isBlank() ? languageParam
                : (locale != null ? locale.getLanguage() : "es"));
        return Locale.forLanguageTag(code);
    }

    @GetMapping
    public List<ExpansionPackResponseDTO> getAllExpansionPacks(
            @RequestParam(name = "lang", required = false) String lang,
            @RequestParam(name = "language", required = false) String language,
            Locale locale) {
        Locale targetLocale = resolveLocale(lang, language, locale);
        return expansionPacksService.getAllExpansionPacks(targetLocale.getLanguage()).stream()
                .map(pack -> localizedContentService.toResponseDto(pack, targetLocale))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ExpansionPackResponseDTO getExpansionPacksById(
            @PathVariable Integer id,
            @RequestParam(name = "lang", required = false) String lang,
            @RequestParam(name = "language", required = false) String language,
            Locale locale) {
        Locale targetLocale = resolveLocale(lang, language, locale);
        ExpansionPack pack = expansionPacksService.getExpansionPacksById(id);
        return localizedContentService.toResponseDto(pack, targetLocale);
    }

    /*Devuelve las plataformas disponibles para una expansión.*/
    @GetMapping("/{id}/platforms")
    public List<PlatformSelectionDTO> getPlatformsByExpansion(
            @PathVariable Integer id,
            @RequestParam(name = "lang", required = false) String lang,
            @RequestParam(name = "language", required = false) String language,
            Locale locale) {
        Locale targetLocale = resolveLocale(lang, language, locale);
        return expansionPacksService.getPlatformsByExpansion(id, targetLocale);
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
    public ResponseEntity<String> deleteExpansionPack(
            @PathVariable Integer id,
            @RequestParam(name = "lang", required = false) String lang,
            @RequestParam(name = "language", required = false) String language,
            Locale locale) {
        Locale targetLocale = resolveLocale(lang, language, locale);
        expansionPacksService.deleteExpansionPack(id);
        return ResponseEntity.ok(localizedContentService.getMessage("expansionpack.delete.success", targetLocale,
                "Expansion pack deleted successfully"));
    }
}

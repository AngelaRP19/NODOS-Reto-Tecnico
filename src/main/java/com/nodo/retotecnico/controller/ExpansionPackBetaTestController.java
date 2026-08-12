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

import com.nodo.retotecnico.dto.ExpansionPackBetaTestResponseDTO;
import com.nodo.retotecnico.dto.ExpansionPackResponseDTO;
import com.nodo.retotecnico.model.ExpansionPackBetaTest;
import com.nodo.retotecnico.service.ExpansionPackBetaTestService;
import com.nodo.retotecnico.service.LocalizedContentService;

@RestController
@RequestMapping("/nodos/expansionpackbetatests")
public class ExpansionPackBetaTestController {

    @Autowired
    private ExpansionPackBetaTestService expansionPackBetaTestService;

    @Autowired
    private LocalizedContentService localizedContentService;

    @GetMapping
    public List<ExpansionPackBetaTestResponseDTO> getAllExpansionPackBetaTests(Locale locale) {
        return expansionPackBetaTestService.getAllExpansionPackBetaTests().stream()
                .map(betaTest -> toResponseDto(betaTest, locale))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ExpansionPackBetaTestResponseDTO getExpansionPackBetaTestById(@PathVariable Integer id, Locale locale) {
        ExpansionPackBetaTest betaTest = expansionPackBetaTestService.getExpansionPackBetaTestById(id);
        return toResponseDto(betaTest, locale);
    }

    @PostMapping("/create")
    public Integer createExpansionPackBetaTest(@RequestBody ExpansionPackBetaTest expansionPackBetaTest) {
        return expansionPackBetaTestService.createExpansionPackBetaTest(expansionPackBetaTest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ExpansionPackBetaTest> updateExpansionPackBetaTest(@PathVariable Integer id,
            @RequestBody ExpansionPackBetaTest expansionPackBetaTest) {
        return ResponseEntity.ok(expansionPackBetaTestService.updateExpansionPackBetaTest(id, expansionPackBetaTest));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteExpansionPackBetaTest(@PathVariable Integer id, Locale locale) {
        expansionPackBetaTestService.deleteExpansionPackBetaTest(id);
        return ResponseEntity.ok(localizedContentService.getMessage("expansionpackbetatest.delete.success", locale,
                "Expansion pack beta test deleted successfully"));
    }

    @GetMapping("/user/{userId}")
    public List<ExpansionPackBetaTestResponseDTO> getBetaTestsByUser(@PathVariable Integer userId, Locale locale) {
        return expansionPackBetaTestService.getBetaTestsByUser(userId).stream()
                .map(betaTest -> toResponseDto(betaTest, locale))
                .collect(Collectors.toList());
    }

    private ExpansionPackBetaTestResponseDTO toResponseDto(ExpansionPackBetaTest betaTest, Locale locale) {
        if (betaTest == null) {
            return null;
        }
        ExpansionPackResponseDTO expansionPackDto = localizedContentService.toResponseDto(betaTest.getExpansionPack(),
                locale);
        return new ExpansionPackBetaTestResponseDTO(
                betaTest.getId(),
                betaTest.getUser().getId(),
                expansionPackDto,
                betaTest.getStatus().name(),
                betaTest.getStartDate(),
                betaTest.getEndDate(),
                betaTest.getFeedback());
    }
}

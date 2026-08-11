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

import com.nodo.retotecnico.dto.ChallengeResponseDTO;
import com.nodo.retotecnico.model.Challenge;
import com.nodo.retotecnico.service.ChallengeService;
import com.nodo.retotecnico.service.LocalizedContentService;

@RestController
@RequestMapping("/nodos/challenges")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private LocalizedContentService localizedContentService;

    @GetMapping
    public List<ChallengeResponseDTO> getAllChallenges(Locale locale) {
        return challengeService.getAllChallenges(locale.getLanguage()).stream()
                .map(challenge -> localizedContentService.toResponseDto(challenge, locale))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ChallengeResponseDTO getChallengeById(@PathVariable Integer id, Locale locale) {
        Challenge challenge = challengeService.getChallengeById(id);
        return localizedContentService.toResponseDto(challenge, locale);
    }

    @PostMapping("/create")
    public Integer createChallenge(@RequestBody Challenge challenge) {
        return challengeService.createChallenge(challenge);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Challenge> updateChallenge(@PathVariable Integer id, @RequestBody Challenge challenge) {
        return ResponseEntity.ok(challengeService.updateChallenge(id, challenge));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteChallenge(@PathVariable Integer id, Locale locale) {
        challengeService.deleteChallenge(id);
        return ResponseEntity.ok(localizedContentService.getMessage("challenge.delete.success", locale,
                "Challenge deleted successfully"));
    }
}

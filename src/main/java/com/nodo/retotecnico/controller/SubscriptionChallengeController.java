package com.nodo.retotecnico.controller;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nodo.retotecnico.dto.ChallengeResponseDTO;
import com.nodo.retotecnico.dto.SubscriptionChallengeResponseDTO;
import com.nodo.retotecnico.model.ChallengeStatus;
import com.nodo.retotecnico.model.SubscriptionChallenge;
import com.nodo.retotecnico.service.LocalizedContentService;
import com.nodo.retotecnico.service.SubscriptionChallengeService;

@RestController
@RequestMapping("/nodos/subscriptionchallenges")
public class SubscriptionChallengeController {

    @Autowired
    private SubscriptionChallengeService subscriptionChallengeService;

    @Autowired
    private LocalizedContentService localizedContentService;

    // Solo ADMIN puede marcar un reto como FINALIZADO o FALLIDO
    private void checkStatusTransitionAllowed(ChallengeStatus status) {
        if (status != ChallengeStatus.FINALIZADO && status != ChallengeStatus.FALLIDO) {
            return;
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = auth != null && auth.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new AccessDeniedException("Solo un administrador puede marcar un reto como finalizado o fallido.");
        }
    }

    @GetMapping
    public List<SubscriptionChallengeResponseDTO> getAllSubscriptionChallenges(Locale locale) {
        return subscriptionChallengeService.getAllSubscriptionChallenges().stream()
                .map(subscription -> toResponseDto(subscription, locale))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public SubscriptionChallengeResponseDTO getSubscriptionChallengeById(@PathVariable Integer id, Locale locale) {
        SubscriptionChallenge subscription = subscriptionChallengeService.getSubscriptionChallengeById(id);
        return toResponseDto(subscription, locale);
    }

    @PostMapping("/create")
    public Integer createSubscriptionChallenge(@RequestBody SubscriptionChallenge subscriptionChallenge) {
        return subscriptionChallengeService.createSubscriptionChallenge(subscriptionChallenge);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubscriptionChallenge> updateSubscriptionChallenge(@PathVariable Integer id,
            @RequestBody SubscriptionChallenge subscriptionChallenge) {
        checkStatusTransitionAllowed(subscriptionChallenge.getStatus());
        return ResponseEntity.ok(subscriptionChallengeService.updateSubscriptionChallenge(id, subscriptionChallenge));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSubscriptionChallenge(@PathVariable Integer id, Locale locale) {
        subscriptionChallengeService.deleteSubscriptionChallenge(id);
        return ResponseEntity.ok(localizedContentService.getMessage("subscriptionchallenge.delete.success", locale,
                "Subscription challenge deleted successfully"));
    }

    @GetMapping("/user/{userId}")
    public List<SubscriptionChallengeResponseDTO> getChallengesByUser(@PathVariable Integer userId, Locale locale) {
        return subscriptionChallengeService.getChallengesByUser(userId).stream()
                .map(subscription -> toResponseDto(subscription, locale))
                .collect(Collectors.toList());
    }

    private SubscriptionChallengeResponseDTO toResponseDto(SubscriptionChallenge subscription, Locale locale) {
        if (subscription == null) {
            return null;
        }
        ChallengeResponseDTO challengeDto = localizedContentService.toResponseDto(subscription.getChallenge(), locale);
        return new SubscriptionChallengeResponseDTO(
                subscription.getId(),
                subscription.getUser().getId(),
                challengeDto,
                subscription.getStatus().name());
    }
}

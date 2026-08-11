package com.nodo.retotecnico.service;

import java.util.Locale;

import com.nodo.retotecnico.dto.ChallengeResponseDTO;
import com.nodo.retotecnico.dto.ExpansionPackResponseDTO;
import com.nodo.retotecnico.model.Challenge;
import com.nodo.retotecnico.model.ExpansionPack;

public interface LocalizedContentService {
    ExpansionPackResponseDTO toResponseDto(ExpansionPack expansionPack, Locale locale);

    ChallengeResponseDTO toResponseDto(Challenge challenge, Locale locale);

    String getMessage(String key, Locale locale, String defaultMessage);
}

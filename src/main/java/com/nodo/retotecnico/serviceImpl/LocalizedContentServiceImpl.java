package com.nodo.retotecnico.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.nodo.retotecnico.dto.ChallengeResponseDTO;
import com.nodo.retotecnico.dto.ExpansionPackResponseDTO;
import com.nodo.retotecnico.model.Challenge;
import com.nodo.retotecnico.model.ExpansionPack;
import com.nodo.retotecnico.service.LocalizedContentService;

@Service
public class LocalizedContentServiceImpl implements LocalizedContentService {

    @Autowired
    private MessageSource messageSource;

    private static final Map<String, String> EXPANSION_PACK_KEY_MAP = new HashMap<>();
    private static final Map<String, String> CHALLENGE_KEY_MAP = new HashMap<>();

    static {
        EXPANSION_PACK_KEY_MAP.put("The Sims™ 4: Naturaleza Encantada", "expansionpack.enchanted");
        EXPANSION_PACK_KEY_MAP.put("The Sims™ 4: Dinastías y Linajes", "expansionpack.legacy");
        EXPANSION_PACK_KEY_MAP.put("The Sims™ 4: Rancho de Caballos", "expansionpack.horse_ranch");
        EXPANSION_PACK_KEY_MAP.put("The Sims™ 4: Vida en el Pueblo", "expansionpack.cottage_living");
        EXPANSION_PACK_KEY_MAP.put("The Sims™ 4: Perros y Gatos", "expansionpack.cats_dogs");
        EXPANSION_PACK_KEY_MAP.put("The Sims™ 4: ¡A Trabajar!", "expansionpack.get_to_work");

        CHALLENGE_KEY_MAP.put("Reto: Jardín Encantado", "challenge.enchanted_garden");
        CHALLENGE_KEY_MAP.put("Reto: Dinastía Real", "challenge.royal_dynasty");
        CHALLENGE_KEY_MAP.put("Reto: Rancho Perfecto", "challenge.perfect_ranch");
        CHALLENGE_KEY_MAP.put("Reto: Vida de Pueblo", "challenge.cottage_life");
        CHALLENGE_KEY_MAP.put("Reto: Refugio de Mascotas", "challenge.pet_shelter");
        CHALLENGE_KEY_MAP.put("Reto: Emprendedor Simmer", "challenge.simmer_entrepreneur");
    }

    @Override
    public ExpansionPackResponseDTO toResponseDto(ExpansionPack expansionPack, Locale locale) {
        if (expansionPack == null) {
            return null;
        }

        String contentKey = EXPANSION_PACK_KEY_MAP.getOrDefault(expansionPack.getName(), null);
        String name = translate(contentKey, locale, "name", expansionPack.getName());
        String description = translate(contentKey, locale, "description", expansionPack.getDescription());
        String category = translate(contentKey, locale, "category", expansionPack.getCategory());
        String platforms = translate(contentKey, locale, "platforms", expansionPack.getPlatforms());
        String publicationDate = translate(contentKey, locale, "publicationDate", expansionPack.getPublicationDate());

        List<String> characteristics = new ArrayList<>();
        for (int i = 0; i < expansionPack.getCharacteristics().size(); i++) {
            String defaultCharacteristic = expansionPack.getCharacteristics().get(i);
            characteristics.add(translate(contentKey, locale, "characteristic." + (i + 1), defaultCharacteristic));
        }

        return new ExpansionPackResponseDTO(
                expansionPack.getId(),
                name,
                description,
                platforms,
                expansionPack.getPrice(),
                category,
                publicationDate,
                locale.getLanguage(),
                expansionPack.getURLImage(),
                characteristics);
    }

    @Override
    public ChallengeResponseDTO toResponseDto(Challenge challenge, Locale locale) {
        if (challenge == null) {
            return null;
        }

        String contentKey = CHALLENGE_KEY_MAP.getOrDefault(challenge.getName(), null);
        String name = translate(contentKey, locale, "name", challenge.getName());
        String description = translate(contentKey, locale, "description", challenge.getDescription());

        return new ChallengeResponseDTO(
                challenge.getId(),
                name,
                challenge.getStart(),
                challenge.getEnd(),
                description,
                challenge.getImageURL(),
                locale.getLanguage());
    }

    @Override
    public String getMessage(String key, Locale locale, String defaultMessage) {
        return messageSource.getMessage(key, null, defaultMessage, locale);
    }

    private String translate(String contentBaseKey, Locale locale, String fieldSuffix, String defaultValue) {
        if (contentBaseKey == null) {
            return defaultValue;
        }
        String key = contentBaseKey + "." + fieldSuffix;
        return messageSource.getMessage(key, null, defaultValue, locale);
    }
}

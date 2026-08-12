package com.nodo.retotecnico.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nodo.retotecnico.model.ExpansionPack;
import com.nodo.retotecnico.repository.ExpansionPacksRepository;
import com.nodo.retotecnico.service.ExpansionPacksService;

import java.util.ArrayList;
import java.util.Arrays;

import com.nodo.retotecnico.dto.PlatformSelectionDTO;

@Service
public class ExpansionPacksServiceImpl implements ExpansionPacksService {

    @Autowired
    private ExpansionPacksRepository expansionPacksRepository;

    @Override
    public List<ExpansionPack> getAllExpansionPacks(String language) {
        return expansionPacksRepository.findByLanguage(language);
    }

    @Override
    public ExpansionPack getExpansionPacksById(Integer id) {
        return expansionPacksRepository.findById(id).orElse(null);
    }

    @Override
    public Integer createExpansionPack(ExpansionPack expansionPack) {
        return expansionPacksRepository.save(expansionPack).getId();
    }

    @Override
    public ExpansionPack updateExpansionPack(Integer id, ExpansionPack expansionPack) {
        ExpansionPack existingExpansionPack = expansionPacksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ExpansionPack not found"));
        existingExpansionPack.setName(expansionPack.getName());
        existingExpansionPack.setDescription(expansionPack.getDescription());
        existingExpansionPack.setPlatforms(expansionPack.getPlatforms());
        existingExpansionPack.setPrice(expansionPack.getPrice());
        existingExpansionPack.setCategory(expansionPack.getCategory());
        existingExpansionPack.setPublicationDate(expansionPack.getPublicationDate());
        existingExpansionPack.setLanguage(expansionPack.getLanguage());
        existingExpansionPack.setURLImage(expansionPack.getURLImage());
        existingExpansionPack.setCharacteristics(expansionPack.getCharacteristics());
        existingExpansionPack.setScreenshots(expansionPack.getScreenshots());
        existingExpansionPack.setMinimumRequirements(expansionPack.getMinimumRequirements());
        existingExpansionPack.setRecommendedRequirements(expansionPack.getRecommendedRequirements());
        return expansionPacksRepository.save(existingExpansionPack);
    }

    @Override
    public void deleteExpansionPack(Integer id) {
        ExpansionPack existingExpansionPack = expansionPacksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ExpansionPack not found"));
        expansionPacksRepository.deleteById(id);
    }

    @Autowired
    private org.springframework.context.MessageSource messageSource;

    /* Construye la lista de plataformas disponibles para una expansión.*/
    @Override
    public List<PlatformSelectionDTO> getPlatformsByExpansion(Integer expansionId) {
        return getPlatformsByExpansion(expansionId, java.util.Locale.getDefault());
    }

    @Override
    public List<PlatformSelectionDTO> getPlatformsByExpansion(Integer expansionId, java.util.Locale locale) {
        ExpansionPack expansion = expansionPacksRepository.findById(expansionId)
                .orElseThrow(() -> new RuntimeException("Expansion Pack not found"));

        List<PlatformSelectionDTO> result = new ArrayList<>();

        if (expansion.getPlatforms() == null || expansion.getPlatforms().isBlank()) {
            return result;
        }

        List<String> platforms = Arrays.stream(expansion.getPlatforms().split("/"))
                .map(String::trim)
                .toList();

        for (String platform : platforms) {
            String label;
            switch (platform.toLowerCase()) {
                case "steam":
                    label = messageSource.getMessage("platform.label.steam", null, "Continuar a la tienda de Steam", locale);
                    break;
                case "pc":
                case "windows":
                    label = messageSource.getMessage("platform.label.windows", null, "Comprar para Windows", locale);
                    break;
                case "mac":
                    label = messageSource.getMessage("platform.label.mac", null, "Comprar para Mac", locale);
                    break;
                case "movil":
                case "móvil":
                case "mobile":
                    label = messageSource.getMessage("platform.label.mobile", null, "Comprar para Móvil", locale);
                    break;
                default:
                    label = messageSource.getMessage("platform.label.default", new Object[]{platform}, "Comprar para " + platform, locale);
            }
            result.add(new PlatformSelectionDTO(platform, label));
        }

        return result;
    }
}

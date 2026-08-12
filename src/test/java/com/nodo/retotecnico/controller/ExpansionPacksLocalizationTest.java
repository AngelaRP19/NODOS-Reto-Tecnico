package com.nodo.retotecnico.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.nodo.retotecnico.model.ExpansionPack;
import com.nodo.retotecnico.repository.ExpansionPacksRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class ExpansionPacksLocalizationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ExpansionPacksRepository expansionPacksRepository;

    private Integer packId;

    @BeforeEach
    void setUp() {
        ExpansionPack pack = new ExpansionPack();
        pack.setName("The Sims™ 4: Naturaleza Encantada");
        pack.setDescription("Conéctate con la magia del bosque...");
        pack.setPlatforms("Steam / Windows / Mac");
        pack.setPrice(79900.0);
        pack.setCategory("Pack de expansión");
        pack.setPublicationDate("Lanzamiento reciente");
        pack.setLanguage("es");
        pack.setCharacteristics(List.of("Nuevo mundo explorable: Arboleda de las Hadas."));
        pack.setMinimumRequirements(List.of("SO: Windows 10 · 64 bits"));
        pack.setRecommendedRequirements(List.of("SO: Windows 10/11 · 64 bits"));
        
        pack = expansionPacksRepository.save(pack);
        packId = pack.getId();
    }

    @Test
    void getExpansionPackByIdTranslatesAllFieldsIncludingRequirementsToEnglish() throws Exception {
        mockMvc.perform(get("/nodos/expansionpacks/" + packId + "?lang=en"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("The Sims™ 4: Enchanted Nature"))
                .andExpect(jsonPath("$.category").value("Expansion Pack"))
                .andExpect(jsonPath("$.language").value("en"))
                .andExpect(jsonPath("$.minimumRequirements[0]").value("OS: Windows 10 · 64-bit"))
                .andExpect(jsonPath("$.recommendedRequirements[0]").value("OS: Windows 10/11 · 64-bit"));
    }

    @Test
    void getExpansionPackByIdTranslatesAllFieldsToFrenchUsingLanguageQueryParam() throws Exception {
        mockMvc.perform(get("/nodos/expansionpacks/" + packId + "?language=fr"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("The Sims™ 4: Nature Enchantée"))
                .andExpect(jsonPath("$.category").value("Pack d'extension"))
                .andExpect(jsonPath("$.language").value("fr"))
                .andExpect(jsonPath("$.minimumRequirements[0]").value("SE : Windows 10 · 64 bits"))
                .andExpect(jsonPath("$.recommendedRequirements[0]").value("SE : Windows 10/11 · 64 bits"));
    }

    @Test
    void getPlatformsByExpansionTranslatesActionLabels() throws Exception {
        mockMvc.perform(get("/nodos/expansionpacks/" + packId + "/platforms?lang=en"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].label").value("Continue to Steam Store"))
                .andExpect(jsonPath("$[1].label").value("Buy for Windows"))
                .andExpect(jsonPath("$[2].label").value("Buy for Mac"));

        mockMvc.perform(get("/nodos/expansionpacks/" + packId + "/platforms?lang=es"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].label").value("Continuar a la tienda de Steam"))
                .andExpect(jsonPath("$[1].label").value("Comprar para Windows"))
                .andExpect(jsonPath("$[2].label").value("Comprar para Mac"));
    }
}

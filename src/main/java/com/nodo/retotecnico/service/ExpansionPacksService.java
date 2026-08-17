package com.nodo.retotecnico.service;

import java.util.List;

import com.nodo.retotecnico.model.ExpansionPack;

import com.nodo.retotecnico.dto.PlatformSelectionDTO;

public interface ExpansionPacksService {

    List<ExpansionPack> getAllExpansionPacks(String language);

    ExpansionPack getExpansionPacksById(Integer id);

    Integer createExpansionPack(ExpansionPack expansionPack);

    ExpansionPack updateExpansionPack(Integer id, ExpansionPack expansionPack);

    void deleteExpansionPack(Integer id);
    /*Devuelve las plataformas disponibles para una expansión.*/
List<PlatformSelectionDTO> getPlatformsByExpansion(Integer expansionId);

    /*Notifica por correo a todos los usuarios beta tester sobre una nueva expansión, sin persistirla.*/
    Integer notifyBetaTesters(ExpansionPack expansionPack);
}
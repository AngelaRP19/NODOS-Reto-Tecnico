package com.nodo.retotecnico.service;

import java.util.List;

import com.nodo.retotecnico.model.ExpansionPack;

public interface ExpansionPacksService {

    List<ExpansionPack> getAllExpansionPacks(String language);

    ExpansionPack getExpansionPacksById(Integer id);

    Integer createExpansionPack(ExpansionPack expansionPack);

    ExpansionPack updateExpansionPack(Integer id, ExpansionPack expansionPack);

    void deleteExpansionPack(Integer id);
}
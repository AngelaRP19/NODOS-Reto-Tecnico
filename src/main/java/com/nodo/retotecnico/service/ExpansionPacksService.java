package com.nodo.retotecnico.service;

import java.util.List;

import com.nodo.retotecnico.model.ExpansionPack;

public interface ExpansionPacksService {

    List<ExpansionPack> getAllExpansionPacks();
    ExpansionPack getExpansionPacksById(Integer id);
}
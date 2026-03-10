package com.nodo.retotecnico.service;

public interface IExpansionPacksService {

    List<ExpansionPacks> getAllExpansionPacks();
    ExpansionPacks getExpansionPacksById(Integer id);
}
package com.nodo.retotecnico.service;

public interface IExpansionPacksService {
    
}

public interface ExpansionPacksService{
    List<ExpansionPacks> getAllExpansionPacks();
    ExpansionPacks getExpansionPacksById(Long id);
}
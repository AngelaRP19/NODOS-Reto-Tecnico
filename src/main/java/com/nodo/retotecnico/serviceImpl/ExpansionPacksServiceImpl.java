package com.nodo.retotecnico.serviceImpl;

import org.springframework.stereotype.Service;

@Service
public class ExpansionPacksService {
    
}

@Service
public class ExpansionPackServiveImpl implements IExpansionPackService{


    @Override
    public list<ExpansionPack> getAllExpansionPack() {

        return ExpansionPackRepository.findAll();
    }

    @Override
    public ExpansionPack getExpansionPackById(Integer id){

        return ExpansionPackRepository.findById(id).orElse(null);
    }
}


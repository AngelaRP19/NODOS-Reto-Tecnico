package com.nodo.retotecnico.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nodo.retotecnico.model.ExpansionPack;
import com.nodo.retotecnico.repository.ExpansionPacksRepository;
import com.nodo.retotecnico.service.ExpansionPacksService;


@Service
public class ExpansionPacksServiceImpl implements ExpansionPacksService{

    @Autowired
    private ExpansionPacksRepository expansionPacksRepository;

    @Override
    public List<ExpansionPack> getAllExpansionPacks() {
        return expansionPacksRepository.findAll();
    }

    @Override
    public ExpansionPack getExpansionPacksById(Integer id) {
        return expansionPacksRepository.findById(id).orElse(null);
    }
    @Override
    public Integer createExpansionPack(ExpansionPack expansionPack) {
        if(expansionPacksRepository.findById(expansionPack.getId()).isPresent()){
            throw new IllegalArgumentException("Expansion Pack with id " + expansionPack.getId() + " already exists.");
        }
        return expansionPacksRepository.save(expansionPack).getId();
    }
}


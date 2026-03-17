package com.nodo.retotecnico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nodo.retotecnico.model.ExpansionPack;

@Repository
public interface  ExpansionPacksRepository extends JpaRepository<ExpansionPack, Integer>{
    
}


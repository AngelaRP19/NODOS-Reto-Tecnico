package com.nodo.retotecnico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nodo.retotecnico.model.Buy;

@Repository
public interface  BuysRepository extends JpaRepository<Buy, Integer>{
    
}

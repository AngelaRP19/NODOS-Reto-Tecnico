package com.nodo.retotecnico.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nodo.retotecnico.model.Buy;

@Repository
public interface  BuysRepository extends JpaRepository<Buy, Integer>{
    
    /**
     * Encuentra todas las compras de un usuario específico
     * @param userId ID del usuario
     * @return Lista de compras del usuario
     */
    List<Buy> findByCartUserId(Integer userId);
}

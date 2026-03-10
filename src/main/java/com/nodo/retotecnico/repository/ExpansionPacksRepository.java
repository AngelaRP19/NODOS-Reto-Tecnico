package com.nodo.retotecnico.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface  ExpansionPacksRepository {
    
}

public interface ExpansionPacksRepository extends JpaRepository<ExpansionPacks, Long>{

}
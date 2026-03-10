package com.nodo.retotecnico.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface  BuysRepository {
    
}

  public interface BuyRepository extends JpaRepository<Buy, Long>{

  }
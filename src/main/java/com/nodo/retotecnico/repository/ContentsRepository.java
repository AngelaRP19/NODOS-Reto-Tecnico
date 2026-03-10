package com.nodo.retotecnico.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface  ContentsRepository {
    
}

public interface ContentsRepository extends JpaRepository<Contents, long>{

}
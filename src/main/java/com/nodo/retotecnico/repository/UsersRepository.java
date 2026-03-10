package com.nodo.retotecnico.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository {
    
}

public interface UsersRepository extends JpaRepository<Users, Long>{

}
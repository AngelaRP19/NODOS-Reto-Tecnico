package com.nodo.retotecnico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nodo.retotecnico.model.ExpansionPackBetaTest;

@Repository
public interface ExpansionPackBetaTestRepository extends JpaRepository<ExpansionPackBetaTest, Integer> {
    List<ExpansionPackBetaTest> findByUserId(Integer userId);
}

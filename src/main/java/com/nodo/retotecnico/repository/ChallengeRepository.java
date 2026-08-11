package com.nodo.retotecnico.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nodo.retotecnico.model.Challenge;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenge, Integer> {
    List<Challenge> findByLanguage(String language);

    long countByLanguage(String language);
}

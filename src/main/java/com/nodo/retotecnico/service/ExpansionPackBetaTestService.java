package com.nodo.retotecnico.service;

import java.util.List;

import com.nodo.retotecnico.model.ExpansionPackBetaTest;

public interface ExpansionPackBetaTestService {

    List<ExpansionPackBetaTest> getAllExpansionPackBetaTests();
    ExpansionPackBetaTest getExpansionPackBetaTestById(Integer id);
    Integer createExpansionPackBetaTest(ExpansionPackBetaTest expansionPackBetaTest);
    ExpansionPackBetaTest updateExpansionPackBetaTest(Integer id, ExpansionPackBetaTest expansionPackBetaTest);
    void deleteExpansionPackBetaTest(Integer id);
    List<ExpansionPackBetaTest> getBetaTestsByUser(Integer userId);
}

package com.nodo.retotecnico.serviceImpl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.nodo.retotecnico.model.BetaTestStatus;
import com.nodo.retotecnico.model.ExpansionPackBetaTest;
import com.nodo.retotecnico.model.User;
import com.nodo.retotecnico.repository.ExpansionPackBetaTestRepository;
import com.nodo.retotecnico.repository.UserRepository;
import com.nodo.retotecnico.service.ExpansionPackBetaTestService;

@Service
public class ExpansionPackBetaTestServiceImpl implements ExpansionPackBetaTestService {

    @Autowired
    private ExpansionPackBetaTestRepository expansionPackBetaTestRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<ExpansionPackBetaTest> getAllExpansionPackBetaTests() {
        return expansionPackBetaTestRepository.findAll();
    }

    @Override
    public ExpansionPackBetaTest getExpansionPackBetaTestById(Integer id) {
        return expansionPackBetaTestRepository.findById(id).orElse(null);
    }

    @Override
    public Integer createExpansionPackBetaTest(ExpansionPackBetaTest expansionPackBetaTest) {
        User user = userRepository.findById(expansionPackBetaTest.getUser().getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!Boolean.TRUE.equals(user.getBetaTester())) {
            throw new AccessDeniedException("El usuario no está inscripto como beta tester.");
        }

        expansionPackBetaTest.setUser(user);
        expansionPackBetaTest.setStatus(BetaTestStatus.EN_PRUEBA);
        expansionPackBetaTest.setStartDate(LocalDate.now());

        return expansionPackBetaTestRepository.save(expansionPackBetaTest).getId();
    }

    @Override
    public ExpansionPackBetaTest updateExpansionPackBetaTest(Integer id, ExpansionPackBetaTest expansionPackBetaTest) {
        ExpansionPackBetaTest existing = expansionPackBetaTestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("ExpansionPackBetaTest not found"));

        existing.setStatus(expansionPackBetaTest.getStatus());
        existing.setFeedback(expansionPackBetaTest.getFeedback());

        if ((existing.getStatus() == BetaTestStatus.FINALIZADO || existing.getStatus() == BetaTestStatus.CANCELADO)
                && existing.getEndDate() == null) {
            existing.setEndDate(LocalDate.now());
        }

        return expansionPackBetaTestRepository.save(existing);
    }

    @Override
    public void deleteExpansionPackBetaTest(Integer id) {
        expansionPackBetaTestRepository.deleteById(id);
    }

    @Override
    public List<ExpansionPackBetaTest> getBetaTestsByUser(Integer userId) {
        return expansionPackBetaTestRepository.findByUserId(userId);
    }
}

package com.nodo.retotecnico.config;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nodo.retotecnico.model.BetaTestStatus;
import com.nodo.retotecnico.model.ExpansionPack;
import com.nodo.retotecnico.model.ExpansionPackBetaTest;
import com.nodo.retotecnico.model.User;
import com.nodo.retotecnico.repository.ExpansionPackBetaTestRepository;
import com.nodo.retotecnico.repository.ExpansionPacksRepository;
import com.nodo.retotecnico.repository.UserRepository;

/**
 * A diferencia de los demás seeders, este no inventa datos de referencia
 * fijos: crea historial de beta testing para los usuarios que YA existen en
 * la base con betaTester=true, para poder ver el tab "Beta testing" del
 * perfil con datos reales en desarrollo. Si no hay ningún usuario con
 * betaTester=true todavía, no siembra nada (el tab ya maneja el estado
 * vacío correctamente).
 */
@Component
public class ExpansionPackBetaTestSeeder implements CommandLineRunner {

    @Autowired
    private ExpansionPackBetaTestRepository expansionPackBetaTestRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpansionPacksRepository expansionPacksRepository;

    private static final BetaTestStatus[] STATUSES = {
            BetaTestStatus.EN_PRUEBA, BetaTestStatus.FINALIZADO, BetaTestStatus.CANCELADO
    };

    private static final String[] FINALIZADO_FEEDBACKS = {
            "Muy buena experiencia, encontré algunos bugs menores pero en general funciona bien.",
            "Excelente, sin problemas. Recomendado para el lanzamiento oficial.",
    };

    private static final String CANCELADO_FEEDBACK =
            "No tuve tiempo suficiente para probarlo a fondo, lo cancelé por ahora.";

    @Override
    public void run(String... args) {
        if (expansionPackBetaTestRepository.count() > 0) {
            return;
        }

        List<User> betaTesters = userRepository.findAll().stream()
                .filter(user -> Boolean.TRUE.equals(user.getBetaTester()))
                .toList();
        if (betaTesters.isEmpty()) {
            return;
        }

        List<ExpansionPack> packs = expansionPacksRepository.findByLanguage("es");
        if (packs.isEmpty()) {
            return;
        }

        List<ExpansionPackBetaTest> seeds = new ArrayList<>();
        for (int i = 0; i < betaTesters.size(); i++) {
            User user = betaTesters.get(i);
            ExpansionPack pack = packs.get(i % packs.size());
            BetaTestStatus status = STATUSES[i % STATUSES.length];

            ExpansionPackBetaTest betaTest = new ExpansionPackBetaTest();
            betaTest.setUser(user);
            betaTest.setExpansionPack(pack);
            betaTest.setStatus(status);
            betaTest.setStartDate(LocalDate.now().minusDays(10 + (i % 20)));

            if (status == BetaTestStatus.FINALIZADO) {
                betaTest.setEndDate(LocalDate.now().minusDays(i % 5));
                betaTest.setFeedback(FINALIZADO_FEEDBACKS[i % FINALIZADO_FEEDBACKS.length]);
            } else if (status == BetaTestStatus.CANCELADO) {
                betaTest.setEndDate(LocalDate.now().minusDays(i % 5));
                betaTest.setFeedback(CANCELADO_FEEDBACK);
            }

            seeds.add(betaTest);
        }

        expansionPackBetaTestRepository.saveAll(seeds);
    }
}

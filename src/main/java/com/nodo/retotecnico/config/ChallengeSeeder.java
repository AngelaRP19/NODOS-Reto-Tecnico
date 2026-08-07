package com.nodo.retotecnico.config;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nodo.retotecnico.model.Challenge;
import com.nodo.retotecnico.repository.ChallengeRepository;

@Component
public class ChallengeSeeder implements CommandLineRunner {

    private final ChallengeRepository challengeRepository;

    // Inyección recomendada por constructor
    public ChallengeSeeder(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }

    @Override
    public void run(String... args) {
        if (challengeRepository.count() > 0) {
            return;
        }

        challengeRepository.saveAll(List.of(
            challenge(
                "Reto: Jardín Encantado",
                LocalDate.of(2026, 7, 15),
                LocalDate.of(2026, 8, 15),
                "Construye el jardín más mágico posible: combina flora exótica, criaturas místicas y espacios de descanso al aire libre para tus Sims.",
                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053159/image1.jpg.adapt.crop191x100.628p_uvqtid.jpg"
            ),
            challenge(
                "Reto: Dinastía Real",
                LocalDate.of(2026, 8, 1),
                LocalDate.of(2026, 8, 31),
                "Funda un linaje real y llévalo a través de tres generaciones sin perder el trono: cuida el protocolo, las alianzas y el patrimonio familiar.",
                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053279/dinasriareal_aobuos.jpg"
            ),
            challenge(
                "Reto: Rancho Perfecto",
                LocalDate.of(2026, 7, 20),
                LocalDate.of(2026, 8, 10),
                "Levanta el rancho de caballos mejor cuidado de Chestnut Ridge: entrena, cría y compite con tus caballos hasta ganar tu primera copa.",
                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053279/dinasriareal_aobuos.jpg" // TODO: Actualizar URL duplicada
            ),
            challenge(
                "Reto: Vida de Pueblo",
                LocalDate.of(2026, 9, 1),
                LocalDate.of(2026, 9, 30),
                "Vive un mes entero de la tierra: cultiva, cría animales y participa en todas las ferias del pueblo sin comprar nada del supermercado.",
                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053451/the-sims-4-cottage-living-3_dkjfs2.webp"
            ),
            challenge(
                "Reto: Refugio de Mascotas",
                LocalDate.of(2026, 8, 15),
                LocalDate.of(2026, 9, 15),
                "Abre un refugio y llega a cuidar al mismo tiempo a la mayor cantidad de perros y gatos posible, todos felices y con buena salud.",
                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053451/the-sims-4-cottage-living-3_dkjfs2.webp" // TODO: Actualizar URL duplicada
            ),
            challenge(
                "Reto: Emprendedor Simmer",
                LocalDate.of(2026, 7, 1),
                LocalDate.of(2026, 7, 31),
                "Arranca un negocio desde cero y conviértelo en un imperio rentable en menos de un mes de juego.",
                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053574/simmer_n9flar.jpg"
            )
        ));
    }

    private Challenge challenge(String name, LocalDate start, LocalDate end, String description, String imageURL) {
        Challenge challenge = new Challenge();
        challenge.setName(name);
        challenge.setStart(start); // Asegúrate de que en la entidad 'Challenge' la propiedad sea 'start' y no 'startDate'
        challenge.setEnd(end);     // Asegúrate de que en la entidad 'Challenge' la propiedad sea 'end' y no 'endDate'
        challenge.setDescription(description);
        challenge.setImageURL(imageURL); // Asegúrate de que se llame 'setImageURL' y no 'setImageUrl'
        return challenge;
    }
}
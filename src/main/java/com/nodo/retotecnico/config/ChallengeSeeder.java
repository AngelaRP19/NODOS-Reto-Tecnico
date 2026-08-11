package com.nodo.retotecnico.config;

import java.time.LocalDate;
import java.util.ArrayList;
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
                boolean enSeeded = challengeRepository.countByLanguage("en") > 0;
                boolean esSeeded = challengeRepository.countByLanguage("es") > 0;
                boolean frSeeded = challengeRepository.countByLanguage("fr") > 0;
                if (enSeeded && esSeeded && frSeeded) {
                        return;
                }

                List<Challenge> challenges = new ArrayList<>();
                if (!enSeeded) {
                        challenges.addAll(createEnglishChallenges());
                }
                if (!esSeeded) {
                        challenges.addAll(createSpanishChallenges());
                }
                if (!frSeeded) {
                        challenges.addAll(createFrenchChallenges());
                }

                if (!challenges.isEmpty()) {
                        challengeRepository.saveAll(challenges);
                }
        }

        private List<Challenge> createEnglishChallenges() {
                return List.of(
                                challenge(
                                                "Challenge: Enchanted Garden",
                                                LocalDate.of(2026, 7, 15),
                                                LocalDate.of(2026, 8, 15),
                                                "Build the most magical garden possible: combine exotic flora, mystical creatures, and outdoor relaxation spaces for your Sims.",
                                                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053159/image1.jpg.adapt.crop191x100.628p_uvqtid.jpg",
                                                "en"),
                                challenge(
                                                "Challenge: Royal Dynasty",
                                                LocalDate.of(2026, 8, 1),
                                                LocalDate.of(2026, 8, 31),
                                                "Found a royal lineage and carry it through three generations without losing the throne: manage protocol, alliances, and family heritage.",
                                                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053279/dinasriareal_aobuos.jpg",
                                                "en"),
                                challenge(
                                                "Challenge: Perfect Ranch",
                                                LocalDate.of(2026, 7, 20),
                                                LocalDate.of(2026, 8, 10),
                                                "Create the best horse ranch in Chestnut Ridge: train, breed, and compete with your horses until you win your first cup.",
                                                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053279/dinasriareal_aobuos.jpg", // TODO: Actualizar URL duplicada
                                                "en"),
                                challenge(
                                                "Challenge: Cottage Life",
                                                LocalDate.of(2026, 9, 1),
                                                LocalDate.of(2026, 9, 30),
                                                "Live a full month in the countryside: farm, raise animals, and participate in every village fair without buying anything from the supermarket.",
                                                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053451/the-sims-4-cottage-living-3_dkjfs2.webp",
                                                "en"),
                                challenge(
                                                "Challenge: Pet Shelter",
                                                LocalDate.of(2026, 8, 15),
                                                LocalDate.of(2026, 9, 15),
                                                "Open a shelter and care for as many dogs and cats as possible, keeping them happy and healthy.",
                                                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053451/the-sims-4-cottage-living-3_dkjfs2.webp", // TODO: Actualizar URL duplicada
                                                "en"),
                                challenge(
                                                "Challenge: Simmer Entrepreneur",
                                                LocalDate.of(2026, 7, 1),
                                                LocalDate.of(2026, 7, 31),
                                                "Start a business from scratch and turn it into a profitable empire in less than a month of gameplay.",
                                                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053574/simmer_n9flar.jpg",
                                                "en"));
        }

        private List<Challenge> createSpanishChallenges() {
                return List.of(
                                challenge(
                                                "Reto: Jardín Encantado",
                                                LocalDate.of(2026, 7, 15),
                                                LocalDate.of(2026, 8, 15),
                                                "Construye el jardín más mágico posible: combina flora exótica, criaturas místicas y espacios de descanso al aire libre para tus Sims.",
                                                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053159/image1.jpg.adapt.crop191x100.628p_uvqtid.jpg",
                                                "es"),
                                challenge(
                                                "Reto: Dinastía Real",
                                                LocalDate.of(2026, 8, 1),
                                                LocalDate.of(2026, 8, 31),
                                                "Funda un linaje real y llévalo a través de tres generaciones sin perder el trono: gestiona protocolo, alianzas y patrimonio familiar.",
                                                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053279/dinasriareal_aobuos.jpg",
                                                "es"),
                                challenge(
                                                "Reto: Rancho Perfecto",
                                                LocalDate.of(2026, 7, 20),
                                                LocalDate.of(2026, 8, 10),
                                                "Crea el mejor rancho de caballos en Chestnut Ridge: entrena, cría y compite con tus caballos hasta ganar tu primera copa.",
                                                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053279/dinasriareal_aobuos.jpg", // TODO: Actualizar URL duplicada
                                                "es"),
                                challenge(
                                                "Reto: Vida de Pueblo",
                                                LocalDate.of(2026, 9, 1),
                                                LocalDate.of(2026, 9, 30),
                                                "Vive un mes entero en el campo: cultiva, cría animales y participa en todas las ferias del pueblo sin comprar nada en el supermercado.",
                                                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053451/the-sims-4-cottage-living-3_dkjfs2.webp",
                                                "es"),
                                challenge(
                                                "Reto: Refugio de Mascotas",
                                                LocalDate.of(2026, 8, 15),
                                                LocalDate.of(2026, 9, 15),
                                                "Abre un refugio y cuida a la mayor cantidad posible de perros y gatos, manteniéndolos felices y saludables.",
                                                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053451/the-sims-4-cottage-living-3_dkjfs2.webp", // TODO: Actualizar URL duplicada
                                                "es"),
                                challenge(
                                                "Reto: Emprendedor Simmer",
                                                LocalDate.of(2026, 7, 1),
                                                LocalDate.of(2026, 7, 31),
                                                "Inicia un negocio desde cero y conviértelo en un imperio rentable en menos de un mes de juego.",
                                                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053574/simmer_n9flar.jpg",
                                                "es"));
        }

        private List<Challenge> createFrenchChallenges() {
                return List.of(
                                challenge(
                                                "Défi : Jardin Enchanté",
                                                LocalDate.of(2026, 7, 15),
                                                LocalDate.of(2026, 8, 15),
                                                "Construisez le jardin le plus magique possible : combinez flore exotique, créatures mystiques et espaces de détente en plein air.",
                                                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053159/image1.jpg.adapt.crop191x100.628p_uvqtid.jpg",
                                                "fr"),
                                challenge(
                                                "Défi : Dynastie Royale",
                                                LocalDate.of(2026, 8, 1),
                                                LocalDate.of(2026, 8, 31),
                                                "Fondez un linage royal et maintenez-le trois générations sans perdre le trône : gérez le protocole, les alliances et le patrimoine familial.",
                                                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053279/dinasriareal_aobuos.jpg",
                                                "fr"),
                                challenge(
                                                "Défi : Ranch Parfait",
                                                LocalDate.of(2026, 7, 20),
                                                LocalDate.of(2026, 8, 10),
                                                "Créez le meilleur ranch de chevaux à Chestnut Ridge : entraînez, élevez et affrontez vos chevaux jusqu'à votre première coupe.",
                                                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053279/dinasriareal_aobuos.jpg", // TODO: Actualizar URL duplicada
                                                "fr"),
                                challenge(
                                                "Défi : Vie au Village",
                                                LocalDate.of(2026, 9, 1),
                                                LocalDate.of(2026, 9, 30),
                                                "Vivez un mois complet à la campagne : cultivez, élevez des animaux et participez à chaque foire sans rien acheter au supermarché.",
                                                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053451/the-sims-4-cottage-living-3_dkjfs2.webp",
                                                "fr"),
                                challenge(
                                                "Défi : Refuge pour Animaux",
                                                LocalDate.of(2026, 8, 15),
                                                LocalDate.of(2026, 9, 15),
                                                "Ouvrez un refuge et prenez soin du plus grand nombre possible de chiens et de chats, en les gardant heureux et en bonne santé.",
                                                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053451/the-sims-4-cottage-living-3_dkjfs2.webp", // TODO: Actualizar URL duplicada
                                                "fr"),
                                challenge(
                                                "Défi : Entrepreneur Simmer",
                                                LocalDate.of(2026, 7, 1),
                                                LocalDate.of(2026, 7, 31),
                                                "Démarrez une entreprise à partir de zéro et transformez-la en un empire rentable en moins d'un mois de jeu.",
                                                "https://res.cloudinary.com/w1jl4sa5/image/upload/v1786053574/simmer_n9flar.jpg",
                                                "fr"));
        }

        private Challenge challenge(String name, LocalDate start, LocalDate end, String description, String imageURL,
                        String language) {
                Challenge challenge = new Challenge();
                challenge.setName(name);
                challenge.setStart(start);
                challenge.setEnd(end);
                challenge.setDescription(description);
                challenge.setImageURL(imageURL);
                challenge.setLanguage(language);
                return challenge;
        }
}

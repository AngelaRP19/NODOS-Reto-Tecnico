package com.nodo.retotecnico.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.nodo.retotecnico.model.ExpansionPack;
import com.nodo.retotecnico.repository.ExpansionPacksRepository;

@Component
public class ExpansionPackSeeder implements CommandLineRunner {

    @Autowired
    private ExpansionPacksRepository expansionPacksRepository;

    private static final List<String> MINIMUM_REQUIREMENTS = List.of(
        "SO: Windows 10 · 64 bits",
        "Procesador: Intel Core i3",
        "Memoria: 4 GB RAM",
        "Almacenamiento: 8 GB disponibles"
    );

    private static final List<String> RECOMMENDED_REQUIREMENTS = List.of(
        "SO: Windows 10/11 · 64 bits",
        "Procesador: Intel Core i5",
        "Memoria: 8 GB RAM",
        "Almacenamiento: 8 GB disponibles"
    );

    @Override
    public void run(String... args) {
        boolean enSeeded = expansionPacksRepository.countByLanguage("en") > 0;
        boolean esSeeded = expansionPacksRepository.countByLanguage("es") > 0;
        boolean frSeeded = expansionPacksRepository.countByLanguage("fr") > 0;
        if (enSeeded && esSeeded && frSeeded) {
            return;
        }

        List<ExpansionPack> packs = new ArrayList<>();
        if (!enSeeded) {
            packs.addAll(createEnglishExpansionPacks());
        }
        if (!esSeeded) {
            packs.addAll(createSpanishExpansionPacks());
        }
        if (!frSeeded) {
            packs.addAll(createFrenchExpansionPacks());
        }

        if (!packs.isEmpty()) {
            expansionPacksRepository.saveAll(packs);
        }
    }

    private List<ExpansionPack> createEnglishExpansionPacks() {
        return List.of(
                pack(
                        "The Sims™ 4: Enchanted Nature",
                        "Expansion Pack",
                        79900.0,
                        "PC / Mac / Consoles",
                        "Connect with the magic of the forest and wildlife! Discover mystical creatures, learn herbalism, build sustainable shelters, and master the art of mystical botany in stunning natural environments.",
                        List.of(
                                "New explorable world: Fairy Grove.",
                                "Special skill: Herbalism and Natural Magic.",
                                "Interactive magical creatures and forest sprites.",
                                "Rustic wood furniture and free-spirited clothing."),
                        "https://res.cloudinary.com/w1jl4sa5/image/upload/v1784572920/TS4_Pack-Art_Enchanted-by-Nature_ES_iv5fev.avif",
                        "en"),
                pack(
                        "The Sims™ 4: Royal Dynasty",
                        "Expansion Pack",
                        79900.0,
                        "PC / Mac / Consoles",
                        "Create prosperous kingdoms, define your royal family legacy, and master protocol or palace intrigue. Build majestic castles, host lavish banquets, and guide your lineage through generations.",
                        List.of(
                                "Exclusive world: Royal City Region.",
                                "Court mechanics, noble titles, and advanced family trees.",
                                "Medieval build objects and opulent gala attire.",
                                "New aspirations: Just Monarch or Machiavellian."),
                        "https://res.cloudinary.com/w1jl4sa5/image/upload/v1784574803/TS4_Pack-Art_Royalty-and-Legacy_ES_zggtwv.avif",
                        "en"),
                pack(
                        "The Sims™ 4: Horse Ranch",
                        "Expansion Pack",
                        89900.0,
                        "PC / Mac / Consoles",
                        "Join the Chestnut Ridge community and create your own equestrian lifestyle! Care for, train, and bond with majestic horses, customize your ranch, and live the countryside experience.",
                        List.of(
                                "Explorable world: Chestnut Ridge.",
                                "Horse care, training, and breeding.",
                                "Nectar crafting and line dancing.",
                                "New animals: Mini goats and mini sheep."),
                        "https://res.cloudinary.com/w1jl4sa5/image/upload/v1784574889/TS4_Pack-Art_HorseRanch_ES_r2ddxj.avif",
                        "en"),
                pack(
                        "The Sims™ 4: Cottage Living",
                        "Expansion Pack",
                        69900.0,
                        "PC / Mac / Consoles",
                        "Enjoy rural life by creating a farm, caring for animals, and exploring a charming village in Henford-on-Bagley. Grow fresh produce, raise animals, and participate in traditional fairs.",
                        List.of(
                                "Experience rural life in Henford-on-Bagley.",
                                "Grow, raise animals, attend fairs, and enjoy tradition.",
                                "Take your Sims out of the city and into the countryside. Build a farm!",
                                "Complete daily activities and enjoy traditional rural fairs."),
                        "https://res.cloudinary.com/w1jl4sa5/image/upload/v1784574983/ES_Sims4-cottage-living-1x1-Loc_rp5yll.avif",
                        "en"),
                pack(
                        "The Sims™ 4: Cats & Dogs",
                        "Expansion Pack",
                        69900.0,
                        "PC / Mac / Consoles",
                        "Grow your family with adorable pets and enjoy unforgettable adventures alongside them in the beautiful coastal world of Brindleton Bay.",
                        List.of(
                                "Open your own veterinary clinic and care for pets.",
                                "Care, train, and play with dogs and cats while strengthening bonds.",
                                "Customize unique pets and accompany your Sims in a loving adventure.",
                                "Adopt dogs and cats, care for them, and build a loving home."),
                        "https://res.cloudinary.com/w1jl4sa5/image/upload/v1784575067/ES_Sims4-cats-and-dogs-1x1-Loc_rw592n.avif",
                        "en"),
                pack(
                        "The Sims™ 4: Get to Work",
                        "Expansion Pack",
                        74900.0,
                        "PC / Mac / Consoles",
                        "Take your Sims to work and discover professions full of challenge, fun, and the opportunity to build your own business.",
                        List.of(
                                "Control each workday and build a successful career full of opportunities.",
                                "Explore new professions and decide your Sims' future in every shift.",
                                "Become a doctor, detective, or scientist and experience thrilling work adventures.",
                                "Open your own business and grow an empire with your entrepreneurial skills."),
                        "https://res.cloudinary.com/w1jl4sa5/image/upload/v1784575123/ES_Sims4-get-to-work-1x1-Loc_saiuva.avif",
                        "en"));
    }

    private List<ExpansionPack> createSpanishExpansionPacks() {
        return List.of(
                pack(
                        "The Sims™ 4: Naturaleza Encantada",
                        "Pack de expansión",
                        79900.0,
                        "PC / Mac / Consolas",
                        "Conéctate con la magia del bosque y la vida silvestre. Descubre criaturas místicas, aprende herbalismo, construye refugios sostenibles y domina la botánica mística en entornos naturales impresionantes.",
                        List.of(
                                "Nuevo mundo explorable: Arboleda de las Hadas.",
                                "Habilidad especial: Herbalismo y Magia Natural.",
                                "Criaturas mágicas y duendes de bosque interactivos.",
                                "Muebles de madera rústica y vestuario de espíritu libre."),
                        "https://res.cloudinary.com/w1jl4sa5/image/upload/v1784572920/TS4_Pack-Art_Enchanted-by-Nature_ES_iv5fev.avif",
                        "es"),
                pack(
                        "The Sims™ 4: Dinastías y Linajes",
                        "Pack de expansión",
                        79900.0,
                        "PC / Mac / Consolas",
                        "Crea reinos prósperos, define la herencia de tu familia real y domina el protocolo o la intriga palaciega. Construye castillos majestuosos, organiza banquetes suntuosos y guía tu linaje a través de generaciones.",
                        List.of(
                                "Mundo exclusivo: Región de Ciudad Real.",
                                "Mecánicas de corte, títulos nobiliarios y árboles genealógicos avanzados.",
                                "Muebles medievales y atuendo de gala opulento.",
                                "Nuevas aspiraciones: Monarca Justo o Maquiavélico."),
                        "https://res.cloudinary.com/w1jl4sa5/image/upload/v1784574803/TS4_Pack-Art_Royalty-and-Legacy_ES_zggtwv.avif",
                        "es"),
                pack(
                        "The Sims™ 4: Rancho de Caballos",
                        "Pack de expansión",
                        89900.0,
                        "PC / Mac / Consolas",
                        "Únete a la comunidad de Chestnut Ridge y crea tu propio estilo de vida ecuestre. Cuida, entrena y forma vínculo con caballos majestuosos, personaliza tu rancho y vive la experiencia del campo.",
                        List.of(
                                "Mundo explorable: Chestnut Ridge.",
                                "Cuidado, entrenamiento y cría de caballos.",
                                "Fabricación de néctar y baile en línea.",
                                "Nuevos animales: Mini cabras y mini ovejas."),
                        "https://res.cloudinary.com/w1jl4sa5/image/upload/v1784574889/TS4_Pack-Art_HorseRanch_ES_r2ddxj.avif",
                        "es"),
                pack(
                        "The Sims™ 4: Vida en el Pueblo",
                        "Pack de expansión",
                        69900.0,
                        "PC / Mac / Consolas",
                        "Disfruta de la vida rural creando una granja, cuidando animales y explorando un encantador pueblo en Henford-on-Bagley. Cultiva productos frescos, cría animales y participa en ferias tradicionales.",
                        List.of(
                                "Vive la vida rural en Henford-on-Bagley.",
                                "Cultiva, cría animales, asiste a ferias y disfruta de la tradición.",
                                "Lleva a tus Sims fuera de la ciudad y al campo. ¡Construye una granja!",
                                "Realiza actividades diarias y disfruta de las ferias rurales tradicionales."),
                        "https://res.cloudinary.com/w1jl4sa5/image/upload/v1784574983/ES_Sims4-cottage-living-1x1-Loc_rp5yll.avif",
                        "es"),
                pack(
                        "The Sims™ 4: Perros y Gatos",
                        "Pack de expansión",
                        69900.0,
                        "PC / Mac / Consolas",
                        "Haz crecer tu familia con adorables mascotas y vive aventuras inolvidables junto a ellas en el hermoso mundo costero de Brindleton Bay.",
                        List.of(
                                "Abre tu propia clínica veterinaria y cuida mascotas.",
                                "Cuida, entrena y juega con perros y gatos mientras fortaleces el vínculo.",
                                "Personaliza mascotas únicas y acompaña a tus Sims en una aventura cariñosa.",
                                "Adopta perros y gatos, cuídalos con cariño y crea un hogar lleno de amor."),
                        "https://res.cloudinary.com/w1jl4sa5/image/upload/v1784575067/ES_Sims4-cats-and-dogs-1x1-Loc_rw592n.avif",
                        "es"),
                pack(
                        "The Sims™ 4: ¡A Trabajar!",
                        "Pack de expansión",
                        74900.0,
                        "PC / Mac / Consolas",
                        "Lleva a tus Sims al trabajo y descubre profesiones llenas de desafíos, diversión y la oportunidad de crear tu propio negocio.",
                        List.of(
                                "Controla cada jornada laboral y construye una carrera exitosa.",
                                "Explora nuevas profesiones y decide el futuro de tus Sims en cada turno.",
                                "Conviértete en médico, detective o científico y vive aventuras profesionales.",
                                "Abre tu propio negocio y haz crecer un imperio con tus habilidades."),
                        "https://res.cloudinary.com/w1jl4sa5/image/upload/v1784575123/ES_Sims4-get-to-work-1x1-Loc_saiuva.avif",
                        "es"));
    }

    private List<ExpansionPack> createFrenchExpansionPacks() {
        return List.of(
                pack(
                        "The Sims™ 4: Nature Enchantée",
                        "Pack d'extension",
                        79900.0,
                        "PC / Mac / Consoles",
                        "Connectez-vous à la magie de la forêt et de la faune ! Découvrez des créatures mystiques, apprenez l'herboristerie, construisez des abris durables et maîtrisez la botanique mystique.",
                        List.of(
                                "Nouveau monde explorables : Bosquet des Fées.",
                                "Compétence spéciale : Herboristerie et magie naturelle.",
                                "Créatures magiques et lutins de la forêt interactifs.",
                                "Meubles en bois rustique et tenues esprit libre."),
                        "https://res.cloudinary.com/w1jl4sa5/image/upload/v1784572920/TS4_Pack-Art_Enchanted-by-Nature_ES_iv5fev.avif",
                        "fr"),
                pack(
                        "The Sims™ 4: Dynasties et Héritage",
                        "Pack d'extension",
                        79900.0,
                        "PC / Mac / Consoles",
                        "Créez des royaumes prospères, définissez l'héritage de votre famille royale et maîtrisez le protocole ou l'intrigue de palais. Construisez des châteaux majestueux, organisez des banquets somptueux et guidez votre lignée.",
                        List.of(
                                "Monde exclusif : Région Royale.",
                                "Mécaniques de cour, titres nobles et arbres généalogiques avancés.",
                                "Objets de construction médiévaux et tenues de gala opulentes.",
                                "Nouvelles aspirations : Monarque Juste ou Machiavélique."),
                        "https://res.cloudinary.com/w1jl4sa5/image/upload/v1784574803/TS4_Pack-Art_Royalty-and-Legacy_ES_zggtwv.avif",
                        "fr"),
                pack(
                        "The Sims™ 4: Ranch Équestre",
                        "Pack d'extension",
                        89900.0,
                        "PC / Mac / Consoles",
                        "Rejoignez la communauté de Chestnut Ridge et créez votre style de vie équestre ! Soignez, entraînez et liez-vous à des chevaux majestueux.",
                        List.of(
                                "Monde explorables : Chestnut Ridge.",
                                "Soins, entraînement et élevage de chevaux.",
                                "Fabrication de nectar et danse en ligne.",
                                "Nouveaux animaux : mini chèvres et mini moutons."),
                        "https://res.cloudinary.com/w1jl4sa5/image/upload/v1784574889/TS4_Pack-Art_HorseRanch_ES_r2ddxj.avif",
                        "fr"),
                pack(
                        "The Sims™ 4: Vie au Village",
                        "Pack d'extension",
                        69900.0,
                        "PC / Mac / Consoles",
                        "Profitez de la vie rurale en créant une ferme, en soignant des animaux et en explorant un charmant village. Cultivez des produits frais, élevez des animaux et participez aux foires traditionnelles.",
                        List.of(
                                "Vivez la vie rurale à Henford-on-Bagley.",
                                "Cultivez, élevez des animaux, participez aux fêtes et profitez de la tradition.",
                                "Éloignez vos Sims de la ville et amenez-les à la campagne. Construisez une ferme !",
                                "Accomplissez les activités quotidiennes et profitez des foires rurales."),
                        "https://res.cloudinary.com/w1jl4sa5/image/upload/v1784574983/ES_Sims4-cottage-living-1x1-Loc_rp5yll.avif",
                        "fr"),
                pack(
                        "The Sims™ 4: Chiens et Chats",
                        "Pack d'extension",
                        69900.0,
                        "PC / Mac / Consoles",
                        "Faites grandir votre famille avec des animaux adorables et vivez des aventures inoubliables avec eux.",
                        List.of(
                                "Ouvrez votre propre clinique vétérinaire.",
                                "Soin, entraînement et jeu avec chiens et chats.",
                                "Personnalisez des animaux uniques et accompagnez vos Sims.",
                                "Adoptez des chiens et chats et créez un foyer affectueux."),
                        "https://res.cloudinary.com/w1jl4sa5/image/upload/v1784575067/ES_Sims4-cats-and-dogs-1x1-Loc_rw592n.avif",
                        "fr"),
                pack(
                        "The Sims™ 4: Au Travail !",
                        "Pack d'extension",
                        74900.0,
                        "PC / Mac / Consoles",
                        "Emmenez vos Sims au travail et découvrez des professions pleines de défis, de plaisir et la possibilité de créer votre propre entreprise.",
                        List.of(
                                "Contrôlez chaque journée de travail et construisez une carrière réussie.",
                                "Explorez de nouvelles professions et décidez de l'avenir de vos Sims.",
                                "Devenez médecin, détective ou scientifique.",
                                "Ouvrez votre propre entreprise et développez un empire."),
                        "https://res.cloudinary.com/w1jl4sa5/image/upload/v1784575123/ES_Sims4-get-to-work-1x1-Loc_saiuva.avif",
                        "fr"));
    }

    private ExpansionPack pack(String name, String category, double price, String platforms,
            String description, List<String> characteristics, String urlImage, String language) {
        ExpansionPack expansionPack = new ExpansionPack();
        expansionPack.setName(name);
        expansionPack.setDescription(description);
        expansionPack.setPlatforms(platforms);
        expansionPack.setPrice(price);
        expansionPack.setCategory(category);
        expansionPack.setPublicationDate(publicationDateFor(language));
        expansionPack.setLanguage(language);
        expansionPack.setURLImage(urlImage);
        expansionPack.setCharacteristics(characteristics);
        expansionPack.setScreenshots(List.of(urlImage));
        expansionPack.setMinimumRequirements(MINIMUM_REQUIREMENTS);
        expansionPack.setRecommendedRequirements(RECOMMENDED_REQUIREMENTS);
        return expansionPack;
    }

    private String publicationDateFor(String language) {
        return switch (language) {
            case "es" -> "Lanzamiento reciente";
            case "fr" -> "Sortie récente";
            default -> "Recent Release";
        };
    }
}

package fr.diginamic;


import fr.diginamic.dao.*;
import fr.diginamic.entities.*;
import fr.diginamic.parser.CsvParser;
import fr.diginamic.service.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        // Création de la factory sur la persistence-unit
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("movies-bdd");
        EntityManager em = entityManagerFactory.createEntityManager(); // se connecte à la bdd

        // Boite à outils : paires DAO/Service
        CountryDao countryDao = new CountryDao(em, Country.class); // parle à la bdd
        CountryService countryService = new CountryService(countryDao); // vérifie les doublons

        GenreDao genreDao = new GenreDao(em, Genre.class);
        GenreService genreService = new GenreService(genreDao);

        LanguageDao languageDao = new LanguageDao(em, Language.class);
        LanguageService languageService = new LanguageService(languageDao);

        BirthPlaceDao birthPlaceDao = new BirthPlaceDao(em, BirthPlace.class);
        BirthPlaceService birthPlaceService = new BirthPlaceService(birthPlaceDao);

        DirectorDao directorDao = new DirectorDao(em, Director.class);
        DirectorService directorService = new DirectorService(directorDao);

        MovieDao movieDao = new MovieDao(em, Movie.class);
        MovieService movieService = new MovieService(movieDao);

        ActorDao actorDao = new ActorDao(em, Actor.class);
        ActorService actorService = new ActorService(actorDao);

        RoleDao roleDao = new RoleDao(em, Role.class);
        RoleService roleService = new RoleService(roleDao);

        CsvParser csvParser = new CsvParser(countryService, genreService, languageService, directorService, movieService, actorService, birthPlaceService, roleService );


    }
}
package fr.diginamic.service;

import fr.diginamic.dao.MovieDao;
import fr.diginamic.entities.Movie;

import java.util.List;

/**
 * Service pour l'entité Movie.
 * Pas besoin de vérifier les doublons : l'id du film vient déjà du fichier source.
 */
public class MovieService {
    private MovieDao movieDao;

    /**
     * Constructeur pour créer un objet MovieService
     * @param movieDao le DAO utilisé pour accéder aux données
     */
    public MovieService(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    /**
     * Enregistre un film en base
     * @param movie le film à enregistrer
     */
    public void create(Movie movie) {
        movieDao.save(movie);
    }

    /**
     * Recherche un film par son id
     * @param id l'identifiant du film
     * @return le film trouvé, ou null
     */
    public Movie findById(String id) {
        return movieDao.findById(id);
    }

    /**
     * Recherche la filmographie d'un acteur.
     * @param actorName le nom de l'acteur recherché (recherche partielle)
     * @return la liste des films dans lesquels il a joué
     */
    public List<Movie> findByActor(String actorName) {
        return movieDao.findByActor(actorName);
    }

    /**
     * Recherche les films sortis entre deux années, bornes incluses.
     * @param startYear l'année de début
     * @param endYear l'année de fin
     * @return la liste des films de la période
     */
    public List<Movie> findByYearRange(int startYear, int endYear) {
        return movieDao.findByYearRange(startYear, endYear);
    }

    /**
     * Recherche les films dans lesquels deux acteurs ont joué ensemble.
     * @param firstActorName le nom du premier acteur (recherche partielle)
     * @param secondActorName le nom du second acteur (recherche partielle)
     * @return la liste des films communs aux deux acteurs
     */
    public List<Movie> findCommonMovies(String firstActorName, String secondActorName) {
        return movieDao.findCommonMovies(firstActorName, secondActorName);
    }

    /**
     * Recherche les films sortis entre deux années avec un acteur donné au casting.
     * @param startYear l'année de début
     * @param endYear l'année de fin
     * @param actorName le nom de l'acteur recherché (recherche partielle)
     * @return la liste des films correspondants
     */
    public List<Movie> findByYearRangeAndActor(int startYear, int endYear, String actorName) {
        return movieDao.findByYearRangeAndActor(startYear, endYear, actorName);
    }

}
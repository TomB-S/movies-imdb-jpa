package fr.diginamic.service;

import fr.diginamic.dao.MovieDao;
import fr.diginamic.entities.Movie;

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
}
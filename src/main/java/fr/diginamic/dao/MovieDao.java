package fr.diginamic.dao;

import fr.diginamic.entities.Movie;
import jakarta.persistence.EntityManager;

/**
 * Class MovieDao qui donne accès aux données de l'entité Movie.
 */
public class MovieDao extends GenericDao<Movie, String> {

    /**
     * Constructeur pour créer un objet MovieDao
     * @param em la connexion active à la base de données
     * @param entityClass la class Movie gérée par le DAO
     */
    public MovieDao(EntityManager em, Class<Movie> entityClass) {
        super(em, entityClass);
    }
}
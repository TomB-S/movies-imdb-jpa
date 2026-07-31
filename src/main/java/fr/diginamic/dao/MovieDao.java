package fr.diginamic.dao;

import fr.diginamic.entities.Movie;
import jakarta.persistence.EntityManager;

import java.util.List;

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

    /**
     * Recherche la filmographie d'un acteur, triée par année de sortie.
     * @param actorName le nom de l'acteur recherché (recherche partielle)
     * @return la liste des films dans lesquels il a joué
     */
    public List<Movie> findByActor(String actorName) {
        String queryString = "select distinct m from Movie m " +
                "join m.roles r " +
                "where lower(r.actor.identity) like lower(:actorName) " +
                "order by m.year";

        return getEm().createQuery(queryString, Movie.class)
                .setParameter("actorName", "%" + actorName + "%")
                .getResultList();
    }

    /**
     * Recherche les films sortis entre deux années, bornes incluses.
     * @param startYear l'année de début
     * @param endYear l'année de fin
     * @return la liste des films de la période, triée par année
     */
    public List<Movie> findByYearRange(int startYear, int endYear) {
        String queryString = "select m from Movie m " +
                "where m.year between :startYear and :endYear " +
                "order by m.year";

        return getEm().createQuery(queryString, Movie.class)
                .setParameter("startYear", startYear)
                .setParameter("endYear", endYear)
                .getResultList();
    }

    /**
     * Recherche les films dans lesquels deux acteurs ont joué ensemble.
     * @param firstActorName le nom du premier acteur (recherche partielle)
     * @param secondActorName le nom du second acteur (recherche partielle)
     * @return la liste des films communs aux deux acteurs
     */
    public List<Movie> findCommonMovies(String firstActorName, String secondActorName) {
        String queryString = "select distinct m from Movie m " +
                "join m.roles r1 " +
                "join m.roles r2 " +
                "where lower(r1.actor.identity) like lower(:firstActorName) " +
                "and lower(r2.actor.identity) like lower(:secondActorName) " +
                "order by m.year";

        return getEm().createQuery(queryString, Movie.class)
                .setParameter("firstActorName", "%" + firstActorName + "%")
                .setParameter("secondActorName", "%" + secondActorName + "%")
                .getResultList();
    }

    /**
     * Recherche les films sortis entre deux années avec un acteur donné au casting.
     * @param startYear l'année de début
     * @param endYear l'année de fin
     * @param actorName le nom de l'acteur recherché (recherche partielle)
     * @return la liste des films correspondants, triée par année
     */
    public List<Movie> findByYearRangeAndActor(int startYear, int endYear, String actorName) {
        String queryString = "select distinct m from Movie m " +
                "join m.roles r " +
                "where m.year between :startYear and :endYear " +
                "and lower(r.actor.identity) like lower(:actorName) " +
                "order by m.year";

        return getEm().createQuery(queryString, Movie.class)
                .setParameter("startYear", startYear)
                .setParameter("endYear", endYear)
                .setParameter("actorName", "%" + actorName + "%")
                .getResultList();
    }
}
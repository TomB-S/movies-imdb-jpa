package fr.diginamic.dao;

import fr.diginamic.entities.Actor;
import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Class ActorDao qui donne accès aux données de l'entité Actor.
 * Hérite des méthodes de GenericDao.
 */
public class ActorDao extends GenericDao<Actor, String> {

    /**
     * Constructeur pour créer un objet ActorDao
     * @param em la connexion active à la base de données
     * @param entityClass la class Actor gérée par le DAO
     */
    public ActorDao(EntityManager em, Class<Actor> entityClass) {
        super(em, entityClass);
    }

    /**
     * Recherche les acteurs ayant joué dans deux films donnés.
     * @param firstMovieName le titre du premier film (recherche partielle)
     * @param secondMovieName le titre du second film (recherche partielle)
     * @return la liste des acteurs communs aux deux films
     */
    public List<Actor> findCommonActors(String firstMovieName, String secondMovieName) {
        String queryString = "select distinct r1.actor from Role r1, Role r2 " +
                "where r1.actor = r2.actor " +
                "and lower(r1.movie.name) like lower(:firstMovieName) " +
                "and lower(r2.movie.name) like lower(:secondMovieName) " +
                "order by r1.actor.identity";

        return getEm().createQuery(queryString, Actor.class)
                .setParameter("firstMovieName", "%" + firstMovieName + "%")
                .setParameter("secondMovieName", "%" + secondMovieName + "%")
                .getResultList();
    }
}
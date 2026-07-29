package fr.diginamic.dao;

import fr.diginamic.entities.Actor;
import jakarta.persistence.EntityManager;

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
}
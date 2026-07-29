package fr.diginamic.dao;

import fr.diginamic.entities.Director;
import jakarta.persistence.EntityManager;

/**
 * Class DirectorDao qui donne accès aux données de l'entité Director.
 * Hérite des méthodes de GenericDao.
 */
public class DirectorDao extends GenericDao<Director, String> {

    /**
     * Constructeur pour créer un objet DirectorDao
     * @param em la connexion active à la base de données
     * @param entityClass la class Director gérée par le DAO
     */
    public DirectorDao(EntityManager em, Class<Director> entityClass) {
        super(em, entityClass);
    }
}
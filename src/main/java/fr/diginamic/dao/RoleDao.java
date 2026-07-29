package fr.diginamic.dao;

import fr.diginamic.entities.Role;
import jakarta.persistence.EntityManager;

/**
 * Class RoleDao qui donne accès aux données de l'entité Role.
 * Hérite des méthodes de GenericDao.
 */
public class RoleDao extends GenericDao<Role, Long> {

    /**
     * Constructeur pour créer un objet RoleDao
     * @param em la connexion active à la base de données
     * @param entityClass la class Role gérée par le DAO
     */
    public RoleDao(EntityManager em, Class<Role> entityClass) {
        super(em, entityClass);
    }
}
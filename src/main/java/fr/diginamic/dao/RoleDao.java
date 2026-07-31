package fr.diginamic.dao;

import fr.diginamic.entities.Role;
import jakarta.persistence.EntityManager;

import java.util.List;

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

    /**
     * Recherche le casting d'un film : tous les roles avec leur acteur.
     * @param movieName le titre du film recherché (recherche partielle)
     * @return la liste des rôles du film
     */
    public List<Role> findByMovieName(String movieName) {
        String queryString = "select r from Role r " +
                "where lower(r.movie.name) like lower(:movieName)";

        return getEm().createQuery(queryString, Role.class)
                .setParameter("movieName", "%" + movieName + "%")
                .getResultList();
    }
}

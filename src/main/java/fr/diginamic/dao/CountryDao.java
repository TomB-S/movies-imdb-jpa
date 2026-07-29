package fr.diginamic.dao;

import fr.diginamic.entities.Country;
import jakarta.persistence.EntityManager;

/**
 * Class CountryDao qui donne accès aux données de l'entité Country.
 * Hérite des méthodes de GenericDao et ajoute findByName pour éviter les doublons.
 */
public class CountryDao extends GenericDao<Country, Long> {
    /**
     * Constructeur pour créer un objet CountryDao
     * @param em la connexion active à la base de données
     * @param entityClass la class Country gérée par le DAO
     */
    public CountryDao(EntityManager em, Class<Country> entityClass) {
        super(em, entityClass);
    }

    /**
     * Recherche un pays donné dans la base de données
     * @param name le nom du pays recherché
     * @return le pays trouvé
     */
    public Country findByName(String name) {
        String jpql = "SELECT c FROM Country c WHERE c.name = :name";
        return getEm().createQuery(jpql, Country.class)
                .setParameter("name", name)
                .getSingleResult();
    }

}


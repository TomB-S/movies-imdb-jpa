package fr.diginamic.dao;

import fr.diginamic.entities.BirthPlace;
import fr.diginamic.entities.Country;
import jakarta.persistence.EntityManager;

/**
 * Class BirthPlaceDao qui donne accès aux données de l'entité BirthPlace.
 * Hérite des méthodes de GenericDao et ajoute findByCityAndStateAndCountry
 */
public class BirthPlaceDao extends GenericDao<BirthPlace, Long> {

    /**
     * Constructeur pour créer un objet BirthPlaceDao
     * @param em la connexion active à la base de données
     * @param entityClass la class BirthPlace gérée par le DAO
     */
    public BirthPlaceDao(EntityManager em, Class<BirthPlace> entityClass) {
        super(em, entityClass);
    }

    /**
     * Recherche un lieu de naissance donné dans la base de données, combinaison ville/état/pays
     * @param city la ville recherchée
     * @param state le state recherchée
     * @param country le pays recherché
     * @return le lieu de naissance trouvé
     */
    public BirthPlace findByCityAndStateAndCountry(String city, String state, Country country) {
        String jpql = "SELECT b from BirthPlace b WHERE b.city = :city AND b.state =:state AND b.country = :country";
        return getEm().createQuery(jpql, BirthPlace.class)
                .setParameter("city", city)
                .setParameter("state", state)
                .setParameter("country", country)
                .getSingleResult();
    }
}
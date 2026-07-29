package fr.diginamic.dao;

import fr.diginamic.entities.Language;
import jakarta.persistence.EntityManager;

/**
 * Class LanguageDao qui donne accès aux données de l'entité Language.
 * Hérite des méthodes de GenericDao et ajoute findByName pour éviter les doublons.
 */
public class LanguageDao extends GenericDao<Language, Long> {

    /**
     * Constructeur pour créer un objet LanguageDao
     * @param em la connexion active à la base de données
     * @param entityClass la class Language gérée par le DAO
     */
    public LanguageDao(EntityManager em, Class<Language> entityClass) {
        super(em, entityClass);
    }

    /**
     * Recherche une langue donnée dans la base de données
     * @param name le nom de la langue recherchée
     * @return la langue trouvée
     */
    public Language findByName(String name) {
        String jpql = "SELECT l FROM Language l WHERE l.name = :name";
        return getEm().createQuery(jpql, Language.class)
                .setParameter("name", name)
                .getSingleResult();
    }
}
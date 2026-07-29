package fr.diginamic.dao;

import fr.diginamic.entities.Language;
import jakarta.persistence.EntityManager;

public class LanguageDaodd extends GenericDao<Language, Long> {

    // Étape 1 : le constructeur reçoit l'EntityManager et la classe de l'entité,
    // et les transmet au constructeur de GenericDao via super(...)
    public LanguageDaodd(EntityManager em, Class<Language> entityClass) {
        super(em, entityClass);
    }

    // Étape 2 : méthode spécifique à Language, absente de GenericDao,
    // utile pour vérifier l'unicité avant de créer une nouvelle langue
    public Language findByName(String name) {

        // Étape 2A : écrire la requête JPQL avec un paramètre nommé (:name)
        String jpql = "SELECT l FROM Language l WHERE l.name = :name";

        // Étape 2B : créer la requête (getEm() car "em" est privé dans GenericDao,
        // on passe par le getter hérité pour y accéder)
        // Étape 2C : injecter la vraie valeur dans le paramètre nommé ":name"
        // Étape 2D : récupérer un seul résultat (pas une liste)
        return getEm().createQuery(jpql, Language.class)
                .setParameter("name", name)
                .getSingleResult();
    }
}
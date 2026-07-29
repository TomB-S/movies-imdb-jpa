package fr.diginamic.dao;

import fr.diginamic.entities.Genre;
import jakarta.persistence.EntityManager;

/**
 * Class GenreDao qui donne accès aux données de l'entité Genre.
 * Hérite des méthodes de GenericDao et ajoute findByName pour éviter les doublons.
 */
public class GenreDao extends GenericDao<Genre, Long> {

    /**
     * Constructeur pour créer un objet GenreDao
     * @param em la connexion active à la base de données
     * @param entityClass la class Genre gérée par le DAO
     */
    public GenreDao(EntityManager em, Class<Genre> entityClass) {
        super(em, entityClass);
    }

    /**
     * Recherche un genre donné dans la base de données
     * @param name le nom du genre recherché
     * @return le genre trouvé
     */
    public Genre findByName(String name) {
        String jpql = "select g from Genre g where g.name=:name";
        return getEm().createQuery(jpql, Genre.class)
                .setParameter("name", name)
                .getSingleResult();
    }
}
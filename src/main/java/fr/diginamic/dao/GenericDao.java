package fr.diginamic.dao;

import jakarta.persistence.EntityManager;

import java.util.List;

/**
 * Créer un généric DAO réutilsable dans les classes
 * avec 3 methodes save, findById, findAll
 * @param <T> le type de l'entité gérée
 * @param <ID> le type de l'id de l'entité gérée
 */
public class GenericDao<T, ID> implements Dao<T, ID> {

    // 1. définir les attributs
    private EntityManager em;
    private Class<T> entityClass;

    /**
     * Constructeur pour créer un objet GenericDao
     * il recoit la connexion et la classe
     * @param entityManager la connexion active à la base de donnée
     * @param entityClass la class gérée par le DAO
     */
    // 2. définir constructor
    public GenericDao(EntityManager entityManager, Class<T> entityClass) {
        this.em = entityManager;
        this.entityClass = entityClass;
    }

    // 3. les méthodes

    /**
     * Enregistre une nouvelle entité en base
     * @param entity l'entité à enregistrer
     */
    @Override
    public void save(T entity) {
        try {
            em.getTransaction().begin();
            em.persist(entity);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        }
    }

    /**
     * Recherche d'une entité par son id
     * @param id l'identifiant recherché
     * @return l'entité trouvée
     */
    @Override
    public T findById(ID id) {
        return em.find(this.entityClass, id);
    }

    /**
     * Récupère toutes les entités d'un type donné
     * le nom de classe utilisé dynamiquement pour construire la requete
     * @return la liste de toutes les entités
     */
    @Override
    public List<T> findAll() {
        // A. écrire la requête JPQL, avec le nom de la classe généré dynamiquement
        String jpql = "select t from " + this.entityClass.getSimpleName() + " t ";
        // B. créer la requête et récupérer la liste complète des résultats
        return em.createQuery(jpql, entityClass).getResultList();
    }

    /**
     * Getter
     * Permet aux classes filles d'accéder à l'EntityManager
     * @return EntityManager géré par le DAO
     */
    public EntityManager getEm() {
        return em;
    }

    /**
     * Getter
     * Permet aux classes filles d'accéder à la classe
     * @return la classe gérée par le DAO
     */
    public Class<T> getEntityClass() {
        return entityClass;
    }
}
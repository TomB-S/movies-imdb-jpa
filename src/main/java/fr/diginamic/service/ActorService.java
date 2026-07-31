package fr.diginamic.service;

import fr.diginamic.dao.ActorDao;
import fr.diginamic.entities.Actor;

import java.util.List;

/**
 * Service pour l'entité Actor.
 * Pas besoin de vérifier les doublons : l'id de l'acteur vient déjà du fichier source.
 */
public class ActorService {
    private ActorDao actorDao;

    /**
     * Constructeur pour créer un objet ActorService
     * @param actorDao le DAO utilisé pour accéder aux données
     */
    public ActorService(ActorDao actorDao) {
        this.actorDao = actorDao;
    }

    /**
     * Enregistre un acteur en base
     * @param actor l'acteur à enregistrer
     */
    public void create(Actor actor) {
        actorDao.save(actor);
    }

    /**
     * Recherche un acteur par son id
     * @param id l'identifiant de l'acteur
     * @return l'acteur trouvé, ou null
     */
    public Actor findById(String id) {
        return actorDao.findById(id);
    }

    /**
     * Recherche les acteurs ayant joué dans deux films donnés.
     * @param firstMovieName le titre du premier film (recherche partielle)
     * @param secondMovieName le titre du second film (recherche partielle)
     * @return la liste des acteurs communs aux deux films
     */
    public List<Actor> findCommonActors(String firstMovieName, String secondMovieName) {
        return actorDao.findCommonActors(firstMovieName, secondMovieName);
    }
}
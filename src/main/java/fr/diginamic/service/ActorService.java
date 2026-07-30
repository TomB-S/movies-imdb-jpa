package fr.diginamic.service;

import fr.diginamic.dao.ActorDao;
import fr.diginamic.entities.Actor;

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
}
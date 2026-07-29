package fr.diginamic.service;

import fr.diginamic.dao.ActorDao;
import fr.diginamic.entities.Actor;

public class ActorService {
    private ActorDao actorDao;

    public ActorService(ActorDao actorDao) {
        this.actorDao = actorDao;
    }

    public void create(Actor actor) {
        actorDao.save(actor);
    }

}

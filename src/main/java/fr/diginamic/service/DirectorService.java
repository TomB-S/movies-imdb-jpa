package fr.diginamic.service;

import fr.diginamic.dao.DirectorDao;
import fr.diginamic.entities.Director;

public class DirectorService {
    private DirectorDao directorDao;

    public DirectorService(DirectorDao directorDao) {
        this.directorDao = directorDao;
    }

    public void create(Director director) {
        directorDao.save(director);
    }
}

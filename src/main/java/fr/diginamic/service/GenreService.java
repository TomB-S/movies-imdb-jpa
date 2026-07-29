package fr.diginamic.service;

import fr.diginamic.dao.GenreDao;
import fr.diginamic.entities.Genre;

public class GenreService {
    private GenreDao genreDao;

    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public Genre getOrCreateGenre(String name) {
        try {
            return genreDao.findByName(name);
        } catch (Exception e) {
            Genre g = new Genre();
            genreDao.save(g);
            return g;
        }
    }
}

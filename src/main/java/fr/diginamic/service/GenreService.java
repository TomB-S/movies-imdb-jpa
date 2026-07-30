package fr.diginamic.service;

import fr.diginamic.dao.GenreDao;
import fr.diginamic.entities.Genre;

/**
 * Service pour l'entité Genre.
 * Utilise GenreDao et évite les doublons sur le nom.
 */
public class GenreService {
    private GenreDao genreDao;

    /**
     * Constructeur pour créer un objet GenreService
     * @param genreDao le DAO utilisé pour accéder aux données
     */
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    /**
     * Recherche un genre existant, sinon le crée
     * @param name le nom du genre
     * @return le genre trouvé ou créé
     */
    public Genre getOrCreateGenre(String name) {
        try {
            return genreDao.findByName(name);
        } catch (Exception e) {
            // ATTENTION : new Genre(name) et non new Genre() vide,
            // sinon le genre est cree sans son nom
            Genre g = new Genre(name);
            genreDao.save(g);
            return g;
        }
    }
}
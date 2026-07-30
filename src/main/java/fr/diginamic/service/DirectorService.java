package fr.diginamic.service;

import fr.diginamic.dao.DirectorDao;
import fr.diginamic.entities.Director;

/**
 * Service pour l'entité Director.
 * Pas besoin de vérifier les doublons : l'id du réalisateur vient déjà du fichier source.
 */
public class DirectorService {
    private DirectorDao directorDao;

    /**
     * Constructeur pour créer un objet DirectorService
     * @param directorDao le DAO utilisé pour accéder aux données
     */
    public DirectorService(DirectorDao directorDao) {
        this.directorDao = directorDao;
    }

    /**
     * Enregistre un réalisateur en base
     * @param director le réalisateur à enregistrer
     */
    public void create(Director director) {
        directorDao.save(director);
    }

    /**
     * Recherche un réalisateur par son id
     * @param id l'identifiant du réalisateur
     * @return le réalisateur trouvé, ou null
     */
    public Director findById(String id) {
        return directorDao.findById(id);
    }
}
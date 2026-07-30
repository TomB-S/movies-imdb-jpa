package fr.diginamic.service;

import fr.diginamic.dao.BirthPlaceDao;
import fr.diginamic.entities.BirthPlace;
import fr.diginamic.entities.Country;

/**
 * Service pour l'entité BirthPlace.
 * Utilise BirthPlaceDao et évite les doublons sur la combinaison ville/état/pays.
 */
public class BirthPlaceService {
    private BirthPlaceDao birthPlaceDao;

    /**
     * Constructeur pour créer un objet BirthPlaceService
     * @param birthPlaceDao le DAO utilisé pour accéder aux données
     */
    public BirthPlaceService(BirthPlaceDao birthPlaceDao) {
        this.birthPlaceDao = birthPlaceDao;
    }

    /**
     * Recherche un lieu de naissance existant, sinon le crée
     * @param city la ville
     * @param state l'état/la région
     * @param country le pays
     * @return le lieu de naissance trouvé ou créé
     */
    public BirthPlace getOrCreateBirthPlace(String city, String state, Country country) {
        try {
            return birthPlaceDao.findByCityAndStateAndCountry(city, state, country);
        } catch (Exception ex) {
            BirthPlace b = new BirthPlace(city, state, country);
            birthPlaceDao.save(b);
            return b;
        }
    }
}
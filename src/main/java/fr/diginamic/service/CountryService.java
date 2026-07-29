package fr.diginamic.service;

import fr.diginamic.dao.CountryDao;
import fr.diginamic.entities.Country;

// Service : orchestre la logique metier autour de Country, en utilisant le DAO
public class CountryService {
    // Le service utilise le DAO, jamais l'EntityManager directement
    private CountryDao countryDao;

    public CountryService(CountryDao countryDao) {
        this.countryDao = countryDao;
    }

    // Regle metier : ne jamais dupliquer un pays (unicite imposee par le sujet)
    public Country getOrCreateCountry(String name, String url) {
        try {
            // 1. On cherche d'abord si ce pays existe deja en base
            return countryDao.findByName(name);
        } catch (Exception e) {
            // 2. getSingleResult() leve une exception si rien n'est trouve
            //    => on catch cette exception, ca veut dire "n'existe pas encore"
            //    => on cree et sauvegarde un nouveau Country
            Country c = new Country(name, url);
            countryDao.save(c);
            return c;
        }
    }
}
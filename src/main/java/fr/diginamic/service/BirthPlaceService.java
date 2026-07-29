package fr.diginamic.service;

import fr.diginamic.dao.BirthPlaceDao;
import fr.diginamic.entities.BirthPlace;
import fr.diginamic.entities.Country;

public class BirthPlaceService {
    private BirthPlaceDao birthPlaceDao;

    public BirthPlaceService(BirthPlaceDao birthPlaceDao) {
        this.birthPlaceDao = birthPlaceDao;
    }

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

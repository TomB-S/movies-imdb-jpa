package fr.diginamic.service;

import fr.diginamic.dao.LanguageDao;
import fr.diginamic.entities.Language;

public class LanguageService {
    private LanguageDao languageDao;

    public LanguageService(LanguageDao languageDao) {
        this.languageDao = languageDao;
    }

    public Language getOrCreateLanguage(String name) {
        try {
            return languageDao.findByName(name);
        } catch (Exception ex) {
            Language l = new Language();
            languageDao.save(l);
            return l;
        }
    }
}

package fr.diginamic.parser;
import fr.diginamic.service.*;

public class CsvParser {

    private CountryService countryService;
    private GenreService genreService;
    private LanguageService languageService;
    private DirectorService directorService;
    private MovieService movieService;
    private ActorService actorService;
    private BirthPlaceService birthPlaceService;
    private RoleService roleService;

    public CsvParser(CountryService countryService, GenreService genreService, LanguageService languageService, DirectorService directorService, MovieService movieService, ActorService actorService, BirthPlaceService birthPlaceService, RoleService roleService) {
        this.countryService = countryService;
        this.genreService = genreService;
        this.languageService = languageService;
        this.directorService = directorService;
        this.movieService = movieService;
        this.actorService = actorService;
        this.birthPlaceService = birthPlaceService;
        this.roleService = roleService;
    }
}
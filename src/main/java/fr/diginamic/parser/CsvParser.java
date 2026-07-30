package fr.diginamic.parser;

import fr.diginamic.entities.*;
import fr.diginamic.service.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Lit les fichiers CSV du projet et utilise les services pour enregistrer
 * les données correspondantes en base.
 */
public class CsvParser {

    private CountryService countryService;
    private GenreService genreService;
    private LanguageService languageService;
    private DirectorService directorService;
    private MovieService movieService;
    private ActorService actorService;
    private BirthPlaceService birthPlaceService;
    private RoleService roleService;

    /**
     * Constructeur pour créer un objet CsvParser
     * @param countryService le service Country
     * @param genreService le service Genre
     * @param languageService le service Language
     * @param directorService le service Director
     * @param movieService le service Movie
     * @param actorService le service Actor
     * @param birthPlaceService le service BirthPlace
     * @param roleService le service Role
     */
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

    /**
     * Lit films.csv et enregistre chaque film en base.
     * Crée et recupère la langue, le pays et les genres.
     * @throws Exception si le fichier est introuvable ou illisible
     */
    public void initFilms() throws Exception {
        // 1. Localiser le fichier
        Path home = Paths.get(CsvParser.class.getClassLoader().getResource("csv/films.csv").toURI());
        // 2. Lire le fichier d'un coup
        List<String> lignes = Files.readAllLines(home);
        // 3.Découper les lignes
        for(int i = 1; i < lignes.size(); i++){
            String[] row = lignes.get(i).split(";", -1);
            String id = row[0];
            String name = row[1];
            Double rating = Double.parseDouble(row[3]);
            String filmingPlace = row[5];
            String genre = row[6];
            String language = row[7];
            String summary = row[8];
            String country = row[9];

            String rowYear = row[2].substring(0,4);
            Integer year = Integer.parseInt(rowYear);

            Language languageEntity = languageService.getOrCreateLanguage(language);
            Country countryEntity = countryService.getOrCreateCountry(country);

            String[] rowGenre = row[6].split(",");
            List<Genre> genres = new ArrayList<>();
            for(String genreEntity : rowGenre){
                genres.add(genreService.getOrCreateGenre(genreEntity));
            }

            List<Director> directors = new ArrayList<>();

            Movie movie = new Movie(id, name, year, rating, filmingPlace, summary, languageEntity, countryEntity, genres, directors);

            movieService.create(movie);
        }
    }

    public void initActors() throws Exception {
        Path home = Paths.get(CsvParser.class.getClassLoader().getResource("csv/acteurs.csv").toURI());
        List<String> lignes = Files.readAllLines(home);
        for(int i = 1; i < lignes.size(); i++){
            String[] row = lignes.get(i).split(";");
            String id = row[0];
            String identite = row[1];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH);
            LocalDate date = LocalDate.parse(row[2], formatter);
            double size = Double.parseDouble(row[4]);
            String url = row[5];

            // Découper les lignes de birthplace
            String [] birthPlace = row[3].split(",");
            String cityName = birthPlace[0];
            String stateName = birthPlace[1];
            String countryName = birthPlace[2];
            Country countryEntity = countryService.getOrCreateCountry(countryName);

            BirthPlace birthPlaceEntity = birthPlaceService.getOrCreateBirthPlace(cityName, stateName, countryEntity);

            Actor actor = new Actor(id, identite, date, url, birthPlaceEntity,size);

            actorService.create(actor);
        }
    }

    /**
     * Lit realisateurs.csv et enregistre chaque réalisateur en base.
     * Crée/récupère le lieu de naissance et le pays au passage.
     * @throws Exception si le fichier est introuvable ou illisible
     */
    public void initDirectors() throws Exception {
        Path home = Paths.get(CsvParser.class.getClassLoader().getResource("csv/realisateurs.csv").toURI());
        List<String> lignes = Files.readAllLines(home);
        for (int i = 1; i < lignes.size(); i++) {
            String[] row = lignes.get(i).split(";");
            String id = row[0];
            String identite = row[1];
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH);
            LocalDate date = LocalDate.parse(row[2], formatter);
            String url = row[4];

            String[] birthPlace = row[3].split(",");
            String cityName = birthPlace[0];
            String stateName = birthPlace[1];
            String countryName = birthPlace[2];
            Country countryEntity = countryService.getOrCreateCountry(countryName);

            BirthPlace birthPlaceEntity = birthPlaceService.getOrCreateBirthPlace(cityName, stateName, countryEntity);

            Director director = new Director(id, identite, date, url, birthPlaceEntity);

            directorService.create(director);
        }
    }

    /**
     * Lit film_realisateurs.csv et relie chaque film à son (ses) réalisateur(s).
     * Le film et le réalisateur doivent déjà exister en base (initFilms/initDirectors avant).
     * @throws Exception si le fichier est introuvable ou illisible
     */
    public void initFilmDirectors() throws Exception {
        Path home = Paths.get(CsvParser.class.getClassLoader().getResource("csv/film_realisateurs.csv").toURI());
        List<String> lignes = Files.readAllLines(home);
        for (int i = 1; i < lignes.size(); i++) {
            String[] row = lignes.get(i).split(";");
            String movieId = row[0];
            String directorId = row[1];

            // 1. Recuperer le film et le realisateur deja existants
            Movie movie = movieService.findById(movieId);
            Director director = directorService.findById(directorId);

            // 2. Ajouter le realisateur a la liste du film, puis sauvegarder la mise a jour
            if (movie != null && director != null) {
                movie.getDirectors().add(director);
                movieService.create(movie);
            }
        }
    }

    /**
     * Lit roles.csv et castingPrincipal.csv, et enregistre chaque rôle en base.
     * Le film et l'acteur doivent déjà exister en base.
     * @throws Exception si un fichier est introuvable ou illisible
     */
    public void initRoles() throws Exception {
        // 1. Charger castingPrincipal.csv dans un Set, pour savoir vite si un couple film/acteur en fait partie
        Path castingHome = Paths.get(CsvParser.class.getClassLoader().getResource("csv/castingPrincipal.csv").toURI());
        List<String> castingLignes = Files.readAllLines(castingHome);
        Set<String> mainActors = new HashSet<>();
        for (int i = 1; i < castingLignes.size(); i++) {
            String[] row = castingLignes.get(i).split(";");
            mainActors.add(row[0] + "|" + row[1]); // clé "movieId|actorId"
        }

        // 2. Parser roles.csv
        Path home = Paths.get(CsvParser.class.getClassLoader().getResource("csv/roles.csv").toURI());
        List<String> lignes = Files.readAllLines(home);
        for (int i = 1; i < lignes.size(); i++) {
            String[] row = lignes.get(i).split(";");
            String movieId = row[0];
            String actorId = row[1];
            String character = row[2];

            Movie movie = movieService.findById(movieId);
            Actor actor = actorService.findById(actorId);

            boolean isMainActor = mainActors.contains(movieId + "|" + actorId);

            if (movie != null && actor != null) {
                Role role = new Role(character, isMainActor, movie, actor);
                roleService.create(role);
            }
        }
    }
}
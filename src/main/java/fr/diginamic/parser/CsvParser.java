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
        // 3. Découper les lignes
        for (int i = 1; i < lignes.size(); i++) {
            try {
                String[] row = lignes.get(i).split(";", -1); // "-1" garde les colonnes vides en fin de ligne
                String id = row[0];
                String name = row[1];
                String filmingPlace = row[5];
                String genre = row[6];
                String language = row[7];
                String summary = row[8];
                String country = row[9];

                // le rating peut etre vide dans le CSV, on met 0 par defaut
                String textRating = row[3].trim();
                double rating = textRating.isEmpty() ? 0 : Double.parseDouble(textRating);

                // l'annee peut etre une plage donc on garde que les 4 premiers caracteres (=1ère année)
                String rowYear = row[2].substring(0, 4);
                Integer year = Integer.parseInt(rowYear);

                // language/country sont juste du texte dans le CSV
                // => on recupere ou cree l'objet correspondant en base, via les services
                Language languageEntity = languageService.getOrCreateLanguage(language);
                Country countryEntity = countryService.getOrCreateCountry(country);

                // un film peut avoir plusieurs genres separes par une virgule
                // => on decoupe puis on recupere/cree chaque Genre
                String[] rowGenre = row[6].split(",");
                List<Genre> genres = new ArrayList<>();
                for (String genreEntity : rowGenre) {
                    genres.add(genreService.getOrCreateGenre(genreEntity));
                }

                // les realisateurs seront ajoutes plus tard par initFilmDirectors()
                List<Director> directors = new ArrayList<>();

                Movie movie = new Movie(id, name, year, rating, filmingPlace, summary, languageEntity, countryEntity, genres, directors);

                // certains films apparaissent en double dans le CSV source
                // => on ne cree le film que s'il n'existe pas deja en base
                if (movieService.findById(movie.getId()) == null) {
                    movieService.create(movie);
                }
            } catch (Exception e) {
                System.out.println("Ligne " + i + " ignorée : " + e.getMessage());
            }
        }
    }

    /**
     * Lit acteurs.csv et enregistre chaque acteur en base.
     * Crée/récupère le lieu de naissance et le pays au passage.
     * @throws Exception si le fichier est introuvable ou illisible
     */
    public void initActors() throws Exception {
        // 1. Localiser le fichier
        Path home = Paths.get(CsvParser.class.getClassLoader().getResource("csv/acteurs.csv").toURI());
        // 2. Lire le fichier d'un coup
        List<String> lignes = Files.readAllLines(home);
        // 3. Découper les lignes
        for (int i = 1; i < lignes.size(); i++) {
            try {
                String[] row = lignes.get(i).split(";");
                String id = row[0];
                String identite = row[1];

                // dates ecrites en anglais ("March 15 1954") => formatter
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH);
                LocalDate date = LocalDate.parse(row[2].trim(), formatter);
                double size = Double.parseDouble(row[4].replace(" m", "").trim());
                String url = row[5];

                // le lieu de naissance est "ville, state, pays" => on decoupe en 3
                String[] birthPlace = row[3].split(",");
                String cityName = birthPlace.length > 0 ? birthPlace[0].trim() : "Inconnu";
                String stateName = birthPlace.length > 1 ? birthPlace[1].trim() : "Inconnu";
                String countryName = birthPlace.length > 2 ? birthPlace[2].trim() : "Inconnu";
                Country countryEntity = countryService.getOrCreateCountry(countryName);

                BirthPlace birthPlaceEntity = birthPlaceService.getOrCreateBirthPlace(cityName, stateName, countryEntity);

                Actor actor = new Actor(id, identite, date, url, birthPlaceEntity, size);

                actorService.create(actor);
            } catch (Exception e) {
                System.out.println("Ligne " + i + " ignorée : " + e.getMessage());
            }
        }
    }

    /**
     * Lit realisateurs.csv et enregistre chaque réalisateur en base.
     * Crée/récupère le lieu de naissance et le pays au passage.
     * @throws Exception si le fichier est introuvable ou illisible
     */
    public void initDirectors() throws Exception {
        System.out.println("=== DEBUT initDirectors ===");
        // 1. Localiser le fichier
        Path home = Paths.get(CsvParser.class.getClassLoader().getResource("csv/realisateurs.csv").toURI());
        // 2. Lire le fichier d'un coup
        List<String> lignes = Files.readAllLines(home);
        // 3. Découper les lignes
        for (int i = 1; i < lignes.size(); i++) {
            try {
                String[] row = lignes.get(i).split(";");
                String id = row[0];
                String identite = row[1];
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH);
                LocalDate date = LocalDate.parse(row[2].trim(), formatter);
                String url = row[4];

                String[] birthPlace = row[3].split(","); // decoupe "ville, etat, pays"
                String cityName = birthPlace.length > 0 ? birthPlace[0].trim() : "Inconnu"; // ville si presente
                String stateName = birthPlace.length > 1 ? birthPlace[1].trim() : "Inconnu"; // etat si present
                String countryName = birthPlace.length > 2 ? birthPlace[2].trim() : "Inconnu"; // pays si present
                Country countryEntity = countryService.getOrCreateCountry(countryName); // recupere/cree le pays

                BirthPlace birthPlaceEntity = birthPlaceService.getOrCreateBirthPlace(cityName, stateName, countryEntity);

                Director director = new Director(id, identite, date, url, birthPlaceEntity);
                System.out.println("Creation director id=" + id);
                directorService.create(director);
            } catch (Exception e) {
                System.out.println("DIRECTOR - Ligne " + i + " ignorée : " + e.getMessage());
            }
        }
        System.out.println("=== FIN initDirectors ===");
    }

    /**
     * Lit film_realisateurs.csv et relie chaque film à son (ses) réalisateur(s).
     * Le film et le réalisateur doivent déjà exister en base (initFilms/initDirectors avant).
     * @throws Exception si le fichier est introuvable ou illisible
     */
    public void initFilmDirectors() throws Exception {
        // 1. Localiser le fichier
        Path home = Paths.get(CsvParser.class.getClassLoader().getResource("csv/film_realisateurs.csv").toURI());
        // 2. Lire le fichier d'un coup
        List<String> lignes = Files.readAllLines(home);
        // 3. Découper les lignes
        for (int i = 1; i < lignes.size(); i++) {
            try {
                String[] row = lignes.get(i).split(";");
                String movieId = row[0];
                String directorId = row[1];

                // ce fichier ne contient que des ids => on va chercher les vrais objets deja en base
                Movie movie = movieService.findById(movieId);
                Director director = directorService.findById(directorId);

                // on ne relie que si les deux existent, puis on sauvegarde le film mis a jour
                if (movie != null && director != null) {
                    movie.getDirectors().add(director);
                    movieService.create(movie);
                }
            } catch (Exception e) {
                System.out.println("Ligne " + i + " ignorée : " + e.getMessage());
            }
        }
    }

    /**
     * Lit roles.csv et castingPrincipal.csv, et enregistre chaque rôle en base.
     * Le film et l'acteur doivent déjà exister en base.
     * @throws Exception si un fichier est introuvable ou illisible
     */
    public void initRoles() throws Exception {
        // 1. Charger castingPrincipal.csv dans un Set, pour savoir  si un couple film/acteur en fait partie
        Path castingHome = Paths.get(CsvParser.class.getClassLoader().getResource("csv/castingPrincipal.csv").toURI());
        List<String> castingLignes = Files.readAllLines(castingHome);
        // creer boite vide avec Set (les doublons ne sont pas autorises)
        Set<String> mainActors = new HashSet<>();
        for (int i = 1; i < castingLignes.size(); i++) {
            String[] row = castingLignes.get(i).split(";", -1); // empeche suppression colonne vide
            mainActors.add(row[0] + "|" + row[1]); // etiquette "movieId|actorId" pour chaque ligne
        }

        // 2. Parser roles.csv
        Path home = Paths.get(CsvParser.class.getClassLoader().getResource("csv/roles.csv").toURI());
        List<String> lignes = Files.readAllLines(home);
        for (int i = 1; i < lignes.size(); i++) {
            try {
                String[] row = lignes.get(i).split(";");
                String movieId = row[0];
                String actorId = row[1];
                String character = row[2];

                // chercher en base si les objets existent deja
                Movie movie = movieService.findById(movieId);
                Actor actor = actorService.findById(actorId);

                // verifie si le couple (film, acteur) existe deja dans mainActors (castingPrincipal.csv)
                boolean isMainActor = mainActors.contains(movieId + "|" + actorId);

                // on ne cree le role que si le film ET l'acteur ont ete trouves en base
                if (movie != null && actor != null) {
                    Role role = new Role(character, isMainActor, movie, actor);
                    roleService.create(role);
                }
            } catch (Exception e) {
                System.out.println("Ligne " + i + " ignorée : " + e.getMessage());
            }
        }
    }
}
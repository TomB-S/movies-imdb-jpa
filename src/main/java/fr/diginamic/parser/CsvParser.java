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
 * Les fichiers sources contiennent des lignes mal formées (colonnes manquantes,
 * dates incomplètes, valeurs vides) : chaque ligne est donc traitée dans un
 * try/catch individuel pour qu'une erreur n'interrompe pas tout l'import.
 */
public class CsvParser {

    /** Format des dates dans les CSV sources, écrites en anglais (ex : "March 15 1954"). */
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("MMMM d yyyy", Locale.ENGLISH);

    /** Longueur maximale acceptée pour un nom de pays (voir @Column sur l'entité Country). */
    private static final int MAX_COUNTRY_LENGTH = 200;

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

    // ==================== Méthodes utilitaires ====================

    /**
     * Récupère la colonne demandée, ou une chaîne vide si elle n'existe pas.
     * Évite les IndexOutOfBounds sur les lignes CSV incomplètes.
     * @param row la ligne découpée
     * @param index l'indice de la colonne voulue
     * @return la valeur nettoyée, ou "" si la colonne est absente
     */
    private String getColumn(String[] row, int index) {
        return index < row.length ? row[index].trim() : "";
    }

    /**
     * Convertit une date du CSV en LocalDate.
     * Gère les dates vides et celles réduites à la seule année (ex : "1960").
     * @param text la date brute issue du CSV
     * @return la date convertie, ou null si elle n'est pas exploitable
     */
    private LocalDate parseDate(String text) {
        // certaines dates contiennent des espaces en trop : on les normalise
        text = text.trim().replaceAll("\\s+", " ");

        if (text.isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(text, DATE_FORMATTER);
        } catch (Exception e) {
            // certaines lignes ne contiennent que l'année : on garde le 1er janvier
            try {
                return LocalDate.of(Integer.parseInt(text), 1, 1);
            } catch (Exception e2) {
                return null;
            }
        }
    }

    /**
     * Convertit une valeur numérique du CSV en double.
     * @param text la valeur brute issue du CSV
     * @return la valeur convertie, ou 0 si elle est vide ou illisible
     */
    private double parseDouble(String text) {
        if (text.isEmpty()) {
            return 0;
        }
        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * Nettoie un nom de pays issu du CSV.
     * Certaines lignes sont mal découpées (point-virgule présent dans un résumé)
     * et produisent des valeurs trop longues pour la colonne en base :
     * on les considère alors comme inconnues plutôt que de perdre la ligne.
     * @param text le nom de pays brut issu du CSV
     * @return le nom exploitable, ou "Inconnu"
     */
    private String cleanCountryName(String text) {
        text = text.trim();
        return (text.isEmpty() || text.length() > MAX_COUNTRY_LENGTH) ? "Inconnu" : text;
    }

    /**
     * Construit un lieu de naissance à partir du texte "ville, état, pays".
     * Les parties absentes sont remplacées par "Inconnu".
     * @param text le lieu de naissance brut issu du CSV
     * @return le lieu de naissance récupéré ou créé en base
     */
    private BirthPlace parseBirthPlace(String text) {
        String[] parts = text.split(",");
        String cityName = parts.length > 0 && !parts[0].trim().isEmpty() ? parts[0].trim() : "Inconnu";
        String stateName = parts.length > 1 && !parts[1].trim().isEmpty() ? parts[1].trim() : "Inconnu";
        String countryName = parts.length > 2 ? cleanCountryName(parts[2]) : "Inconnu";

        Country countryEntity = countryService.getOrCreateCountry(countryName);
        return birthPlaceService.getOrCreateBirthPlace(cityName, stateName, countryEntity);
    }

    /**
     * Localise un fichier CSV dans les ressources du projet et lit toutes ses lignes.
     * @param fileName le nom du fichier, ex : "films.csv"
     * @return la liste des lignes du fichier, en-tête comprise
     * @throws Exception si le fichier est introuvable ou illisible
     */
    private List<String> readCsv(String fileName) throws Exception {
        Path home = Paths.get(CsvParser.class.getClassLoader().getResource("csv/" + fileName).toURI());
        return Files.readAllLines(home);
    }

    // ==================== Import des données ====================

    /**
     * Lit films.csv et enregistre chaque film en base.
     * Crée et récupère la langue, le pays et les genres.
     * Colonnes : ID IMDB, NOM, ANNEE, RATING, URL, LIEU TOURNAGE, GENRES, LANGUE, RESUME, PAYS
     * @throws Exception si le fichier est introuvable ou illisible
     */
    public void initFilms() throws Exception {
        List<String> lignes = readCsv("films.csv");

        // on démarre à 1 pour sauter la ligne d'en-tête
        for (int i = 1; i < lignes.size(); i++) {
            try {
                String[] row = lignes.get(i).split(";", -1); // "-1" garde les colonnes vides en fin de ligne

                String id = getColumn(row, 0);
                String name = getColumn(row, 1);
                String filmingPlace = getColumn(row, 5);
                String language = getColumn(row, 7);
                String summary = getColumn(row, 8);

                // certaines lignes sont mal découpées : le pays peut dépasser la taille en base
                String country = cleanCountryName(getColumn(row, 9));

                // sans id ni nom la ligne est inexploitable
                if (id.isEmpty() || name.isEmpty()) {
                    continue;
                }

                // le rating peut être vide dans le CSV, on met 0 par défaut
                double rating = parseDouble(getColumn(row, 3));

                // l'année peut être une plage ("1998-2002") : on garde les 4 premiers caractères
                String rowYear = getColumn(row, 2);
                Integer year = rowYear.length() >= 4 ? Integer.parseInt(rowYear.substring(0, 4)) : null;

                // language/country sont juste du texte dans le CSV
                // => on récupère ou crée l'objet correspondant en base, via les services
                Language languageEntity = languageService.getOrCreateLanguage(language);
                Country countryEntity = countryService.getOrCreateCountry(country);

                // un film peut avoir plusieurs genres séparés par une virgule
                // => on découpe puis on récupère/crée chaque Genre
                List<Genre> genres = new ArrayList<>();
                for (String genreName : getColumn(row, 6).split(",")) {
                    if (!genreName.trim().isEmpty()) {
                        genres.add(genreService.getOrCreateGenre(genreName.trim()));
                    }
                }

                // les réalisateurs seront ajoutés plus tard par initFilmDirectors()
                List<Director> directors = new ArrayList<>();

                Movie movie = new Movie(id, name, year, rating, filmingPlace, summary, languageEntity, countryEntity, genres, directors);

                // certains films apparaissent en double dans le CSV source
                // => on ne crée le film que s'il n'existe pas déjà en base
                if (movieService.findById(id) == null) {
                    movieService.create(movie);
                }
            } catch (Exception e) {
                System.out.println("FILM - Ligne " + i + " ignorée : " + e.getMessage());
            }
        }
    }

    /**
     * Lit acteurs.csv et enregistre chaque acteur en base.
     * Crée/récupère le lieu de naissance et le pays au passage.
     * Colonnes : ID, IDENTITE, DATE NAISSANCE, LIEU NAISSANCE, TAILLE, URL
     * @throws Exception si le fichier est introuvable ou illisible
     */
    public void initActors() throws Exception {
        List<String> lignes = readCsv("acteurs.csv");

        for (int i = 1; i < lignes.size(); i++) {
            try {
                String[] row = lignes.get(i).split(";", -1);

                String id = getColumn(row, 0);
                String identite = getColumn(row, 1);

                // sans id ni identité la ligne est inexploitable
                if (id.isEmpty() || identite.isEmpty()) {
                    continue;
                }

                // la date peut être vide ou réduite à l'année => null accepté en base
                LocalDate date = parseDate(getColumn(row, 2));

                // la taille est écrite avec son unité ("1.75 m") et peut être vide
                double size = parseDouble(getColumn(row, 4).replace(" m", ""));

                String url = getColumn(row, 5);
                BirthPlace birthPlaceEntity = parseBirthPlace(getColumn(row, 3));

                Actor actor = new Actor(id, identite, date, url, birthPlaceEntity, size);

                // le même acteur peut apparaître plusieurs fois dans le CSV source
                // => on ne le crée que s'il n'existe pas déjà en base
                if (actorService.findById(id) == null) {
                    actorService.create(actor);
                }
            } catch (Exception e) {
                System.out.println("ACTEUR - Ligne " + i + " ignorée : " + e.getMessage());
            }
        }
    }

    /**
     * Lit realisateurs.csv et enregistre chaque réalisateur en base.
     * Crée/récupère le lieu de naissance et le pays au passage.
     * Colonnes : ID, IDENTITE, DATE NAISSANCE, LIEU NAISSANCE, URL
     * @throws Exception si le fichier est introuvable ou illisible
     */
    public void initDirectors() throws Exception {
        List<String> lignes = readCsv("realisateurs.csv");

        for (int i = 1; i < lignes.size(); i++) {
            try {
                String[] row = lignes.get(i).split(";", -1);

                String id = getColumn(row, 0);
                String identite = getColumn(row, 1);

                if (id.isEmpty() || identite.isEmpty()) {
                    continue;
                }

                LocalDate date = parseDate(getColumn(row, 2));
                String url = getColumn(row, 4);
                BirthPlace birthPlaceEntity = parseBirthPlace(getColumn(row, 3));

                Director director = new Director(id, identite, date, url, birthPlaceEntity);

                // le même réalisateur peut apparaître plusieurs fois dans le CSV source
                if (directorService.findById(id) == null) {
                    directorService.create(director);
                }
            } catch (Exception e) {
                System.out.println("REALISATEUR - Ligne " + i + " ignorée : " + e.getMessage());
            }
        }
    }

    /**
     * Lit film_realisateurs.csv et relie chaque film à son (ses) réalisateur(s).
     * Le film et le réalisateur doivent déjà exister en base (initFilms/initDirectors avant).
     * @throws Exception si le fichier est introuvable ou illisible
     */
    public void initFilmDirectors() throws Exception {
        List<String> lignes = readCsv("film_realisateurs.csv");

        for (int i = 1; i < lignes.size(); i++) {
            try {
                String[] row = lignes.get(i).split(";", -1);

                String movieId = getColumn(row, 0);
                String directorId = getColumn(row, 1);

                if (movieId.isEmpty() || directorId.isEmpty()) {
                    continue;
                }

                // ce fichier ne contient que des ids => on va chercher les vrais objets déjà en base
                Movie movie = movieService.findById(movieId);
                Director director = directorService.findById(directorId);

                // on ne relie que si les deux existent, et si le lien n'est pas déjà présent
                if (movie != null && director != null && !movie.getDirectors().contains(director)) {
                    movie.getDirectors().add(director);
                    movieService.create(movie);
                }
            } catch (Exception e) {
                System.out.println("FILM/REALISATEUR - Ligne " + i + " ignorée : " + e.getMessage());
            }
        }
    }

    /**
     * Lit roles.csv et castingPrincipal.csv, et enregistre chaque rôle en base.
     * Le film et l'acteur doivent déjà exister en base.
     * @throws Exception si un fichier est introuvable ou illisible
     */
    public void initRoles() throws Exception {
        // 1. Charger castingPrincipal.csv dans un Set, pour savoir si un couple film/acteur en fait partie
        List<String> castingLignes = readCsv("castingPrincipal.csv");

        // un Set n'autorise pas les doublons et permet une recherche instantanée
        Set<String> mainActors = new HashSet<>();
        for (int i = 1; i < castingLignes.size(); i++) {
            String[] row = castingLignes.get(i).split(";", -1);
            // étiquette "movieId|actorId" pour identifier chaque couple
            mainActors.add(getColumn(row, 0) + "|" + getColumn(row, 1));
        }

        // 2. Parser roles.csv
        List<String> lignes = readCsv("roles.csv");

        for (int i = 1; i < lignes.size(); i++) {
            try {
                String[] row = lignes.get(i).split(";", -1);

                String movieId = getColumn(row, 0);
                String actorId = getColumn(row, 1);
                String character = getColumn(row, 2); // peut être vide sur certaines lignes

                if (movieId.isEmpty() || actorId.isEmpty()) {
                    continue;
                }

                // chercher en base si les objets existent déjà
                Movie movie = movieService.findById(movieId);
                Actor actor = actorService.findById(actorId);

                // vérifie si le couple (film, acteur) fait partie du casting principal
                boolean isMainActor = mainActors.contains(movieId + "|" + actorId);

                // on ne crée le rôle que si le film ET l'acteur ont été trouvés en base
                if (movie != null && actor != null) {
                    Role role = new Role(character, isMainActor, movie, actor);
                    roleService.create(role);
                }
            } catch (Exception e) {
                System.out.println("ROLE - Ligne " + i + " ignorée : " + e.getMessage());
            }
        }
    }
}
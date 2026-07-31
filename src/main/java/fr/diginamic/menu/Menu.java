package fr.diginamic.menu;

import java.util.List;
import java.util.Scanner;

import fr.diginamic.entities.Actor;
import fr.diginamic.entities.Movie;
import fr.diginamic.entities.Role;
import fr.diginamic.service.ActorService;
import fr.diginamic.service.MovieService;
import fr.diginamic.service.RoleService;

/**
 * Menu console permettant de lancer les différentes recherches
 * dans la base de données IMDB.
 */
public class Menu {

    private Scanner scanner = new Scanner(System.in);
    private MovieService movieService;
    private ActorService actorService;
    private RoleService roleService;

    /**
     * Constructeur pour créer un objet Menu
     * @param movieService le service des films
     * @param actorService le service des acteurs
     * @param roleService le service des rôles
     */
    public Menu(MovieService movieService, ActorService actorService, RoleService roleService) {
        this.movieService = movieService;
        this.actorService = actorService;
        this.roleService = roleService;
    }

    /**
     * Boucle principale : affiche le menu et traite le choix
     * de l'utilisateur jusqu'à ce qu'il quitte l'application.
     */
    public void run() {
        boolean running = true;

        while (running) {
            display();
            String choice = readLine("Votre choix : ");

            switch (choice) {
                case "1" -> actorFilmography();
                case "2" -> movieCasting();
                case "3" -> moviesByYearRange();
                case "4" -> commonMovies();
                case "5" -> commonActors();
                case "6" -> moviesByYearRangeAndActor();
                case "7" -> running = false;
                default -> System.out.println("Choix invalide, veuillez recommencer.");
            }
        }

        System.out.println("Fin de l'application.");
        scanner.close();
    }

    /** Affiche la liste des options disponibles. */
    private void display() {
        System.out.println();
        System.out.println("===== RECHERCHES IMDB =====");
        System.out.println("1 - Filmographie d'un acteur");
        System.out.println("2 - Casting d'un film");
        System.out.println("3 - Films sortis entre 2 années");
        System.out.println("4 - Films communs à 2 acteurs");
        System.out.println("5 - Acteurs communs à 2 films");
        System.out.println("6 - Films entre 2 années avec un acteur donné");
        System.out.println("7 - Fin de l'application");
    }

    // ---------- Les 6 recherches ----------

    /** Recherche 1 : films dans lesquels un acteur a joué. */
    private void actorFilmography() {
        String actorName = readLine("Nom de l'acteur : ");
        displayMovies(movieService.findByActor(actorName));
    }

    /** Recherche 2 : acteurs et personnages d'un film. */
    private void movieCasting() {
        String movieName = readLine("Titre du film : ");
        List<Role> roles = roleService.findByMovieName(movieName);

        if (roles.isEmpty()) {
            System.out.println("Aucun résultat.");
            return;
        }
        for (Role role : roles) {
            System.out.println("- " + role.getActor().getIdentity()
                    + " dans le rôle de " + role.getCharacterName()
                    + " (" + role.getMovie().getName() + ")");
        }
        System.out.println(roles.size() + " rôle(s) trouvé(s).");
    }

    /** Recherche 3 : films sortis dans un intervalle d'années. */
    private void moviesByYearRange() {
        int startYear = readInt("Année de début : ");
        int endYear = readInt("Année de fin : ");
        displayMovies(movieService.findByYearRange(startYear, endYear));
    }

    /** Recherche 4 : films dans lesquels 2 acteurs ont joué ensemble. */
    private void commonMovies() {
        String firstActorName = readLine("Premier acteur : ");
        String secondActorName = readLine("Second acteur : ");
        displayMovies(movieService.findCommonMovies(firstActorName, secondActorName));
    }

    /** Recherche 5 : acteurs ayant joué dans 2 films donnés. */
    private void commonActors() {
        String firstMovieName = readLine("Premier film : ");
        String secondMovieName = readLine("Second film : ");
        List<Actor> actors = actorService.findCommonActors(firstMovieName, secondMovieName);

        if (actors.isEmpty()) {
            System.out.println("Aucun résultat.");
            return;
        }
        for (Actor actor : actors) {
            System.out.println("- " + actor.getIdentity());
        }
        System.out.println(actors.size() + " acteur(s) trouvé(s).");
    }

    /** Recherche 6 : films d'un intervalle avec un acteur donné au casting. */
    private void moviesByYearRangeAndActor() {
        int startYear = readInt("Année de début : ");
        int endYear = readInt("Année de fin : ");
        String actorName = readLine("Nom de l'acteur : ");
        displayMovies(movieService.findByYearRangeAndActor(startYear, endYear, actorName));
    }

    // ---------- Méthodes utilitaires ----------

    /**
     * Affichage commun à toutes les recherches renvoyant des films.
     * @param movies la liste des films à afficher
     */
    private void displayMovies(List<Movie> movies) {
        if (movies.isEmpty()) {
            System.out.println("Aucun résultat.");
            return;
        }
        for (Movie movie : movies) {
            System.out.println("- " + movie.getName() + " (" + movie.getYear() + ")");
        }
        System.out.println(movies.size() + " film(s) trouvé(s).");
    }

    /**
     * Lit une saisie texte au clavier
     * @param message le message affiché à l'utilisateur
     * @return la saisie, sans espaces superflus
     */
    private String readLine(String message) {
        System.out.print(message);
        return scanner.nextLine().trim();
    }

    /**
     * Lit un entier au clavier, en redemandant tant que la saisie est invalide
     * @param message le message affiché à l'utilisateur
     * @return l'entier saisi
     */
    private int readInt(String message) {
        while (true) {
            try {
                return Integer.parseInt(readLine(message));
            } catch (NumberFormatException e) {
                System.out.println("Veuillez saisir un nombre entier.");
            }
        }
    }
}
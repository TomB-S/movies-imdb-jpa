package fr.diginamic.entities;

import jakarta.persistence.*;

import java.util.List;

/**
 * Entité Movie qui représente les films.
 * Un film est parlé seulement en une seule langue (relation manytoone).
 * Un film est tourné dans un seul pays (relation manytoone).
 * Un film peut être partagé entre plusieurs genres (relation manytomany).
 * Un film peut être partagé entre plusieurs réalisateurs (relation manytomany).
 * Un film peut avoir plusieurs rôles, via l'entité Role (relation onetomany).
 */
@Entity
public class Movie {
    @Id
    private String id;

    private String name;
    private Integer year;
    private double rating;
    private String filmingPlace;

    @Column(length = 2000)
    private String summary;

    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    @ManyToMany
    @JoinTable(
            name = "MOVIE_GENRE",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> genres;

    @ManyToMany
    @JoinTable(
            name = "MOVIE_DIRECTOR",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "director_id")
    )
    private List<Director> directors;


    @OneToMany(mappedBy = "movie")
    private List<Role> roles;

    /**
     * Constructeur vide requis pour JPA/Hibernate.
     */
    public Movie() {
    }

    /**
     * Constructeur pour créer un objet Movie.
     * @param id l'identifiant du film (issu du fichier source, non généré)
     * @param name le nom du film
     * @param year l'année de sortie du film
     * @param rating la note du film
     * @param filmingPlace le lieu de tournage du film
     * @param summary le résumé du film
     * @param language la langue du film
     * @param country le pays du film
     * @param genres la liste des genres du film
     * @param directors la liste des réalisateurs du film
     */
    public Movie(String id, String name, Integer year, double rating, String filmingPlace, String summary, Language language, Country country, List<Genre> genres, List<Director> directors) {
        this.id = id;
        this.name = name;
        this.year = year;
        this.rating = rating;
        this.filmingPlace = filmingPlace;
        this.summary = summary;
        this.language = language;
        this.country = country;
        this.genres = genres;
        this.directors = directors;
    }

    /**
     * Getter
     * @return l'id du film
     */
    public String getId() {
        return id;
    }

    /**
     * Getter
     * @return le nom du film
     */
    public String getName() {
        return name;
    }

    /**
     * Getter
     * @return l'année de sortie du film
     */
    public Integer getYear() {
        return year;
    }

    /**
     * Getter
     * @return la note du film
     */
    public double getRating() {
        return rating;
    }

    /**
     * Getter
     * @return le lieu de tournage du film
     */
    public String getFilmingPlace() {
        return filmingPlace;
    }

    /**
     * Getter
     * @return le résumé du film
     */
    public String getSummary() {
        return summary;
    }

    /**
     * Getter
     * @return la langue du film
     */
    public Language getLanguage() {
        return language;
    }

    /**
     * Getter
     * @return le pays du film
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Getter
     * @return la liste des genres du film
     */
    public List<Genre> getGenres() {
        return genres;
    }

    /**
     * Getter
     * @return la liste des réalisateurs du film
     */
    public List<Director> getDirectors() {
        return directors;
    }

    /**
     * Setter
     * @param country le nouveau pays du film
     */
    public void setCountry(Country country) {
        this.country = country;
    }

    /**
     * Setter
     * @param name le nouveau nom du film
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter
     * @param year la nouvelle année de sortie du film
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    /**
     * Setter
     * @param rating la nouvelle note du film
     */
    public void setRating(double rating) {
        this.rating = rating;
    }

    /**
     * Setter
     * @param filmingPlace le nouveau lieu de tournage du film
     */
    public void setFilmingPlace(String filmingPlace) {
        this.filmingPlace = filmingPlace;
    }

    /**
     * Setter
     * @param summary le nouveau résumé du film
     */
    public void setSummary(String summary) {
        this.summary = summary;
    }

    /**
     * Setter
     * @param language la nouvelle langue du film
     */
    public void setLanguage(Language language) {
        this.language = language;
    }

    /**
     * Setter
     * @param genres la nouvelle liste des genres du film
     */
    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    /**
     * Setter
     * @param directors la nouvelle liste des réalisateurs du film
     */
    public void setDirectors(List<Director> directors) {
        this.directors = directors;
    }
}
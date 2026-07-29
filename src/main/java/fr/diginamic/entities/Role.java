package fr.diginamic.entities;

import jakarta.persistence.*;

/**
 * Entité role représentant un rôle d'acteur.
 * Un rôle est joué par un seul acteur.
 * Un rôle appartient à un seul film.
 */
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String characterName;
    private boolean mainActor;

    @ManyToOne
    @JoinColumn(name = "movie_id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "actor_id")
    private Actor actor;

    /**
     * Constructeur vide requis pour JPA/Hibernate.
     */
    public Role() {
    }

    /**
     * Constructeur pour créer un objet Role sans id (id auto-généré par la base).
     * @param characterName le nom du personnage joué
     * @param mainActor indique si l'acteur fait partie du casting principal
     * @param movie le film concerné
     * @param actor l'acteur concerné
     */
    public Role(String characterName, boolean mainActor, Movie movie, Actor actor) {
        this.characterName = characterName;
        this.mainActor = mainActor;
        this.movie = movie;
        this.actor = actor;
    }

    /**
     * Setter
     * @param characterName le nouveau nom du personnage joué
     */
    public void setCharacterName(String characterName) {
        this.characterName = characterName;
    }

    /**
     * Setter
     * @param mainActor le nouveau statut de casting principal
     */
    public void setMainActor(boolean mainActor) {
        this.mainActor = mainActor;
    }

    /**
     * Setter
     * @param movie le nouveau film concerné
     */
    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    /**
     * Setter
     * @param actor le nouvel acteur concerné
     */
    public void setActor(Actor actor) {
        this.actor = actor;
    }

    /**
     * Getter
     * @return l'id du rôle
     */
    public Long getId() {
        return id;
    }

    /**
     * Getter
     * @return le nom du personnage joué
     */
    public String getCharacterName() {
        return characterName;
    }

    /**
     * Getter
     * @return true si l'acteur fait partie du casting principal
     */
    public boolean isMainActor() {
        return mainActor;
    }

    /**
     * Getter
     * @return le film concerné
     */
    public Movie getMovie() {
        return movie;
    }

    /**
     * Getter
     * @return l'acteur concerné
     */
    public Actor getActor() {
        return actor;
    }
}
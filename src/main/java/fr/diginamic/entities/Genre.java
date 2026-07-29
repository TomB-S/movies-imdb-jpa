package fr.diginamic.entities;

import jakarta.persistence.*;

/**
 * Entité genre représentant les genres cinématographiques.
 * Un genre concerne un ou plusieurs films (relation manytomany, portée par Movie).
 */
@Entity
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    /**
     * Constructeur vide requis pour JPA/Hibernate.
     */
    public Genre() {
    }

    /**
     * Constructeur pour créer un objet Genre sans id (id auto-généré par la base).
     * @param name le nom du genre
     */
    public Genre(String name) {
        this.name = name;
    }

    /**
     * Getter
     * @return l'id du genre
     */
    public Long getId() {
        return id;
    }

    /**
     * Getter
     * @return le nom du genre
     */
    public String getName() {
        return name;
    }

    /**
     * Setter
     * @param name le nouveau nom du genre
     */
    public void setName(String name) {
        this.name = name;
    }
}
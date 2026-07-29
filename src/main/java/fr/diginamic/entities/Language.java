package fr.diginamic.entities;

import jakarta.persistence.*;

/**
 * Entité language représentant les langues des films.
 * Une langue concerne un ou plusieurs films (relation onetomany, portée par Movie).
 */
@Entity
public class Language {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    /**
     * Constructeur vide requis pour JPA/Hibernate.
     */
    public Language() {
    }

    /**
     * Constructeur pour créer un objet Language sans id (id auto-généré par la base).
     * @param name le nom de la langue
     */
    public Language(String name) {
        this.name = name;
    }

    /**
     * Getter
     * @return l'id de la langue
     */
    public Long getId() {
        return id;
    }

    /**
     * Getter
     * @return le nom de la langue
     */
    public String getName() {
        return name;
    }

    /**
     * Setter
     * @param name le nouveau nom de la langue
     */
    public void setName(String name) {
        this.name = name;
    }
}
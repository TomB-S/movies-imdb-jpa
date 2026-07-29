package fr.diginamic.entities;

import jakarta.persistence.*;

/**
 * Entité country représentant les pays
 * Un pays possède un ou plusieurs films (relation onetomany)
 * Un pays possède un ou plusieurs lieux de naissance (relation onetomany)
 */
@Entity
public class Country {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String url;

    /**
     * Constructor vide requis pour JPA/Hibernate
     */
    public Country() {}

    /**
     * Constructor pour créer un objet Country
     * @param name le nom du pays
     * @param url l'url du pays
     */
    public Country(String name, String url) {
        this.name = name;
        this.url = url;
    }

    /**
     * Getter
     * @return id
     */
    public long getId() {
        return id;
    }

    /**
     * Getter
     * @return le nom du pays
     */
    public String getName() {
        return name;
    }

    /**
     * Getter
     * @return l'url du pays
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Setter
     * @param url
     */
    public void setUrl(String url) {
        this.url = url;
    }
}

package fr.diginamic.entities;

import jakarta.persistence.*;

import java.time.LocalDate;

/**
 * Entité person représentant une personne.
 * Classe mère dont héritent les entités Actor et Director.
 * Une personne ne peut être née que dans un seul lieu de naissance (relation manytoone).
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Person {
    @Id
    private String id;

    private String identity;
    private LocalDate dateOfBirth;
    private String url;

    @ManyToOne
    @JoinColumn(name = "birth_place_id")
    private BirthPlace birthPlace;

    /**
     * Constructeur vide requis pour JPA/Hibernate.
     */
    public Person() {
    }

    /**
     * Constructeur pour créer un objet Person.
     * @param id l'identifiant de la personne (issu du fichier csv source)
     * @param identity l'identité de la personne
     * @param dateOfBirth la date de naissance de la personne
     * @param url l'url de la personne
     * @param birthPlace le lieu de naissance de la personne
     */
    public Person(String id, String identity, LocalDate dateOfBirth, String url, BirthPlace birthPlace) {
        this.id = id;
        this.identity = identity;
        this.dateOfBirth = dateOfBirth;
        this.url = url;
        this.birthPlace = birthPlace;
    }

    /**
     * Getter
     * @return l'id de la personne
     */
    public String getId() {
        return id;
    }

    /**
     * Getter
     * @return l'identité de la personne
     */
    public String getIdentity() {
        return identity;
    }

    /**
     * Setter
     * @param identity la nouvelle identité de la personne
     */
    public void setIdentity(String identity) {
        this.identity = identity;
    }

    /**
     * Getter
     * @return la date de naissance de la personne
     */
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Setter
     * @param dateOfBirth la nouvelle date de naissance de la personne
     */
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Getter
     * @return l'url de la personne
     */
    public String getUrl() {
        return url;
    }

    /**
     * Setter
     * @param url la nouvelle url de la personne
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Getter
     * @return le lieu de naissance de la personne
     */
    public BirthPlace getBirthPlace() {
        return birthPlace;
    }

    /**
     * Setter
     * @param birthPlace le nouveau lieu de naissance de la personne
     */
    public void setBirthPlace(BirthPlace birthPlace) {
        this.birthPlace = birthPlace;
    }
}
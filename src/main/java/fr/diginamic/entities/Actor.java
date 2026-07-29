package fr.diginamic.entities;

import jakarta.persistence.Entity;

import java.time.LocalDate;

/**
 * Entité actor représentant un acteur.
 * Classe fille qui hérite de la classe mère Person.
 * Un acteur joue dans un ou plusieurs films via l'entité Role (relation onetomany, portée par Role).
 */
@Entity
public class Actor extends Person {
    private double size;

    /**
     * Constructeur vide requis pour JPA/Hibernate.
     */
    public Actor() {
    }

    /**
     * Constructeur pour créer un objet Actor.
     * @param id l'identifiant de l'acteur (issu du fichier csv)
     * @param identity le nom/identité de l'acteur
     * @param dateOfBirth la date de naissance de l'acteur
     * @param url l'url de l'acteur
     * @param birthPlace le lieu de naissance de l'acteur
     * @param size la taille de l'acteur
     */
    public Actor(String id, String identity, LocalDate dateOfBirth, String url, BirthPlace birthPlace, double size) {
        super(id, identity, dateOfBirth, url, birthPlace);
        this.size = size;
    }

    /**
     * Getter
     * @return la taille de l'acteur
     */
    public double getSize() {
        return size;
    }

    /**
     * Setter
     * @param size la nouvelle taille de l'acteur
     */
    public void setSize(double size) {
        this.size = size;
    }
}
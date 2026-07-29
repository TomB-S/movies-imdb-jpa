package fr.diginamic.entities;

import jakarta.persistence.Entity;

import java.time.LocalDate;

/**
 * Entité director représentant un réalisateur.
 * Classe fille qui hérite de la classe mère Person, sans attribut propre.
 * Un réalisateur réalise un ou plusieurs films (relation manytomany, portée par Movie).
 */
@Entity
public class Director extends Person {

    /**
     * Constructeur vide requis pour JPA/Hibernate.
     */
    public Director() {
    }

    /**
     * Constructeur pour créer un objet Director.
     * @param id l'identifiant du réalisateur (issu du fichier csv)
     * @param identity l'identité du réalisateur
     * @param dateOfBirth la date de naissance du réalisateur
     * @param url l'url du réalisateur
     * @param birthPlace le lieu de naissance du réalisateur
     */
    public Director(String id, String identity, LocalDate dateOfBirth, String url, BirthPlace birthPlace) {
        super(id, identity, dateOfBirth, url, birthPlace);
    }
}
package fr.diginamic.entities;

import jakarta.persistence.*;

/**
 * Entité birthplace représentant un lieu de naissance (city, state, country).
 * Un lieu de naissance appartient à un seul pays (relation manytoone).
 * Un lieu de naissance peut être partagé par plusieurs personnes (relation onetomany, portée par Person).
 * La combinaison des attributs doit être unique en base.
 */
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "city", "state", "country_id"
        })
})
public class BirthPlace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String city;
    private String state;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country country;

    /**
     * Constructeur vide requis pour JPA/Hibernate.
     */
    public BirthPlace() {
    }

    /**
     * Constructeur pour créer un objet BirthPlace sans id (id auto-généré par la base).
     * @param city la ville du lieu de naissance
     * @param state le state du lieu de naissance
     * @param country le pays du lieu de naissance
     */
    public BirthPlace(String city, String state, Country country) {
        this.city = city;
        this.state = state;
        this.country = country;
    }

    /**
     * Getter
     * @return l'id du lieu de naissance
     */
    public Long getId() {
        return id;
    }

    /**
     * Getter
     * @return la ville du lieu de naissance
     */
    public String getCity() {
        return city;
    }

    /**
     * Setter
     * @param city la nouvelle ville du lieu de naissance
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Getter
     * @return le state du lieu de naissance
     */
    public String getState() {
        return state;
    }

    /**
     * Setter
     * @param state le nouvel state du lieu de naissance
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * Getter
     * @return le pays du lieu de naissance
     */
    public Country getCountry() {
        return country;
    }

    /**
     * Setter
     * @param country le nouveau pays du lieu de naissance
     */
    public void setCountry(Country country) {
        this.country = country;
    }
}
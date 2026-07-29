package fr.diginamic;


import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        // Création de la factory sur la persistence-unit
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("movies-bdd");
        // Création de la transaction
        EntityManager em = entityManagerFactory.createEntityManager();
        // Ouverture de la transaction
        EntityTransaction transaction = em.getTransaction();


    }
}
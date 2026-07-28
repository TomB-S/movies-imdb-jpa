-- ============================================================
-- MPD - Projet Movies (JPA)
-- ============================================================


CREATE TABLE COUNTRY (
                         id      INT AUTO_INCREMENT PRIMARY KEY,
                         nom     VARCHAR(255) NOT NULL UNIQUE,
                         url     VARCHAR(255)
);

CREATE TABLE GENRE (
                       id      INT AUTO_INCREMENT PRIMARY KEY,
                       nom     VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE LANGUAGE (
                          id      INT AUTO_INCREMENT PRIMARY KEY,
                          nom     VARCHAR(255) NOT NULL UNIQUE
);

-- ---------- Entites dependantes (1 FK) ----------

CREATE TABLE BIRTH_PLACE (
                             id              INT AUTO_INCREMENT PRIMARY KEY,
                             ville           VARCHAR(255),
                             etat_dept       VARCHAR(255),
                             pays_id         INT NOT NULL,
                             CONSTRAINT uq_birth_place UNIQUE (ville, etat_dept, pays_id),
                             CONSTRAINT fk_birthplace_country FOREIGN KEY (pays_id) REFERENCES COUNTRY(id)
);

-- ---------- Personne (classe mere) et heritage JOINED ----------

CREATE TABLE PERSONNE (
                          id                  VARCHAR(255) PRIMARY KEY,
                          identite            VARCHAR(255) NOT NULL,
                          date_naissance      DATE,
                          url                 VARCHAR(255),
                          lieu_naissance_id   INT,
                          CONSTRAINT fk_personne_birthplace FOREIGN KEY (lieu_naissance_id) REFERENCES BIRTH_PLACE(id)
);

CREATE TABLE ACTOR (
                       id      VARCHAR(255) PRIMARY KEY,
                       taille  DOUBLE,
                       CONSTRAINT fk_actor_personne FOREIGN KEY (id) REFERENCES PERSONNE(id)
);

CREATE TABLE DIRECTOR (
                          id      VARCHAR(255) PRIMARY KEY,
                          CONSTRAINT fk_director_personne FOREIGN KEY (id) REFERENCES PERSONNE(id)
);

-- ---------- Movie ----------

CREATE TABLE MOVIE (
                       id              VARCHAR(255) PRIMARY KEY,
                       nom             VARCHAR(255) NOT NULL,
                       annee           INT,
                       rating          DECIMAL(3,1),
                       lieu_tournage   VARCHAR(255),
                       resume          TEXT,
                       langue_id       INT NOT NULL,
                       pays_id         INT NOT NULL,
                       CONSTRAINT fk_movie_language FOREIGN KEY (langue_id) REFERENCES LANGUAGE(id),
                       CONSTRAINT fk_movie_country FOREIGN KEY (pays_id) REFERENCES COUNTRY(id)
);

-- ---------- Role (classe d'association Movie <-> Actor) ----------

CREATE TABLE ROLE (
                      id              INT AUTO_INCREMENT PRIMARY KEY,
                      movie_id        VARCHAR(255) NOT NULL,
                      actor_id        VARCHAR(255) NOT NULL,
                      personnage      VARCHAR(255),
                      principal       TINYINT(1) NOT NULL DEFAULT 0,
                      CONSTRAINT fk_role_movie FOREIGN KEY (movie_id) REFERENCES MOVIE(id),
                      CONSTRAINT fk_role_actor FOREIGN KEY (actor_id) REFERENCES ACTOR(id)
);

-- ---------- Tables de jonction (many-to-many) ----------

CREATE TABLE MOVIE_GENRE (
                             movie_id    VARCHAR(255) NOT NULL,
                             genre_id    INT NOT NULL,
                             PRIMARY KEY (movie_id, genre_id),
                             CONSTRAINT fk_moviegenre_movie FOREIGN KEY (movie_id) REFERENCES MOVIE(id),
                             CONSTRAINT fk_moviegenre_genre FOREIGN KEY (genre_id) REFERENCES GENRE(id)
);

CREATE TABLE MOVIE_DIRECTOR (
                                movie_id    VARCHAR(255) NOT NULL,
                                director_id VARCHAR(255) NOT NULL,
                                PRIMARY KEY (movie_id, director_id),
                                CONSTRAINT fk_moviedirector_movie FOREIGN KEY (movie_id) REFERENCES MOVIE(id),
                                CONSTRAINT fk_moviedirector_director FOREIGN KEY (director_id) REFERENCES DIRECTOR(id)
);
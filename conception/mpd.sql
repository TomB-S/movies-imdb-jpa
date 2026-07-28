-- ============================================================
--  Modele Physique de Donnees
-- ============================================================

```sql

CREATE TABLE COUNTRY (
                         id      INT AUTO_INCREMENT PRIMARY KEY,
                         name    VARCHAR(255) NOT NULL UNIQUE,
                         url     VARCHAR(255)
);

CREATE TABLE GENRE (
                       id      INT AUTO_INCREMENT PRIMARY KEY,
                       name    VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE LANGUAGE (
                          id      INT AUTO_INCREMENT PRIMARY KEY,
                          name    VARCHAR(255) NOT NULL UNIQUE
);

-- ---------- Dependent entities (1 FK) ----------

CREATE TABLE BIRTH_PLACE (
                             id          INT AUTO_INCREMENT PRIMARY KEY,
                             city        VARCHAR(255),
                             state       VARCHAR(255),
                             country_id  INT NOT NULL,
                             CONSTRAINT uq_birth_place UNIQUE (city, state, country_id),
                             CONSTRAINT fk_birthplace_country FOREIGN KEY (country_id) REFERENCES COUNTRY(id)
);

-- ---------- Person (parent class) and JOINED inheritance ----------

CREATE TABLE PERSON (
                        id              VARCHAR(255) PRIMARY KEY,
                        identity        VARCHAR(255) NOT NULL,
                        date_of_birth   DATE,
                        url             VARCHAR(255),
                        birth_place_id  INT,
                        CONSTRAINT fk_person_birthplace FOREIGN KEY (birth_place_id) REFERENCES BIRTH_PLACE(id)
);

CREATE TABLE ACTOR (
                       id      VARCHAR(255) PRIMARY KEY,
                       size    DOUBLE,
                       CONSTRAINT fk_actor_person FOREIGN KEY (id) REFERENCES PERSON(id)
);

CREATE TABLE DIRECTOR (
                          id      VARCHAR(255) PRIMARY KEY,
                          CONSTRAINT fk_director_person FOREIGN KEY (id) REFERENCES PERSON(id)
);

-- ---------- Movie ----------

CREATE TABLE MOVIE (
                       id              VARCHAR(255) PRIMARY KEY,
                       name            VARCHAR(255) NOT NULL,
                       year            INT,
                       rating          DECIMAL(3,1),
                       filming_place   VARCHAR(255),
                       summary         VARCHAR(2000),
                       language_id     INT NOT NULL,
                       country_id      INT NOT NULL,
                       CONSTRAINT fk_movie_language FOREIGN KEY (language_id) REFERENCES LANGUAGE(id),
                       CONSTRAINT fk_movie_country FOREIGN KEY (country_id) REFERENCES COUNTRY(id)
);

-- ---------- Role (association class Movie <-> Actor) ----------

CREATE TABLE ROLE (
                      id              INT AUTO_INCREMENT PRIMARY KEY,
                      movie_id        VARCHAR(255) NOT NULL,
                      actor_id        VARCHAR(255) NOT NULL,
                      character_name  VARCHAR(255),
                      main_actor      TINYINT(1) NOT NULL DEFAULT 0,
                      CONSTRAINT fk_role_movie FOREIGN KEY (movie_id) REFERENCES MOVIE(id),
                      CONSTRAINT fk_role_actor FOREIGN KEY (actor_id) REFERENCES ACTOR(id)
);

-- ---------- Junction tables (many-to-many) ----------

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
```
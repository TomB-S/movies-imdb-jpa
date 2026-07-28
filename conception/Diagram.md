# Modèle Physique de Données (MPD)

Représentation visuelle du schéma de base de données du projet Movies : tables, colonnes, clés primaires/étrangères et relations entre les entités.
Fait sur Mermaid. 

```mermaid
erDiagram
  PERSONNE ||--o| BIRTH_PLACE : a
  BIRTH_PLACE }o--|| COUNTRY : situe
  MOVIE }o--|| COUNTRY : produit
  MOVIE }o--|| LANGUAGE : parle
  MOVIE ||--o{ MOVIE_GENRE : a
  GENRE ||--o{ MOVIE_GENRE : concerne
  MOVIE ||--o{ MOVIE_DIRECTOR : a
  DIRECTOR ||--o{ MOVIE_DIRECTOR : realise
  ACTOR ||--|| PERSONNE : est
  DIRECTOR ||--|| PERSONNE : est
  MOVIE ||--o{ ROLE : a
  ACTOR ||--o{ ROLE : joue

  PERSONNE {
    string id PK
    string identite
    date date_naissance
    string url
    int lieu_naissance_id FK
  }
  ACTOR {
    string id PK
    double taille
  }
  DIRECTOR {
    string id PK
  }
  MOVIE {
    string id PK
    string nom
    int annee
    decimal rating
    string lieu_tournage
    text resume
    int langue_id FK
    int pays_id FK
  }
  COUNTRY {
    int id PK
    string nom
    string url
  }
  GENRE {
    int id PK
    string nom
  }
  LANGUAGE {
    int id PK
    string nom
  }
  BIRTH_PLACE {
    int id PK
    string ville
    string etat_dept
    int pays_id FK
  }
  ROLE {
    int id PK
    string movie_id FK
    string actor_id FK
    string personnage
    boolean principal
  }
  MOVIE_GENRE {
    string movie_id FK
    int genre_id FK
  }
  MOVIE_DIRECTOR {
    string movie_id FK
    string director_id FK
  }
```
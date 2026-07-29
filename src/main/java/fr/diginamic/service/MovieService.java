package fr.diginamic.service;

import fr.diginamic.dao.MovieDao;
import fr.diginamic.entities.Movie;

public class MovieService {
    private MovieDao movieDao;

    public MovieService(MovieDao movieDao) {
        this.movieDao = movieDao;
    }

    public void create(Movie movie) {
        movieDao.save(movie);
    }
}

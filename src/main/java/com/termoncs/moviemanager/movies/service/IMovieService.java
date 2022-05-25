package com.termoncs.moviemanager.movies.service;

import com.termoncs.moviemanager.movies.model.Movie;
import com.termoncs.moviemanager.movies.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface IMovieService {

    public long getCount();

    public List<Movie> getMovies();

    public Page<Movie> getMovies(Pageable p);

    public Movie getMovieById(long id);

    public void addMovie(Movie m);

    public void updateMovie(Movie m);

    public void deleteMovieById(long id);

    public void deleteAllMovies();

    public Movie addReview(Review r);

    public void deleteReview(long id);
}

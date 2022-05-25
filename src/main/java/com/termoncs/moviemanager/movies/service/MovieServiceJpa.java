package com.termoncs.moviemanager.movies.service;

import com.termoncs.moviemanager.movies.model.Movie;
import com.termoncs.moviemanager.movies.model.Review;
import com.termoncs.moviemanager.movies.repository.MovieJpaRepository;
import com.termoncs.moviemanager.movies.repository.ReviewJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 *
 * @author aiden
 */
public class MovieServiceJpa implements IMovieService {
   
    @Autowired
    private MovieJpaRepository repository;

    @Autowired
    private ReviewJpaRepository reviewRepository;

    @Override
    public long getCount() {
        return repository.count();
    }

    @Override
    public List<Movie> getMovies() {
        return (List<Movie>) repository.findAll();
    }

    @Override
    public Page<Movie> getMovies(Pageable p) {
        return repository.findAll(p);
    }

    @Override
    public Movie getMovieById(long id) {
       return repository.findById(id).get();
    }

    @Override
    public void addMovie(Movie m) {
        repository.save(m);
    }

    public Movie addReview(Review review) {
        var movie = getMovieById(review.getMovieId());
        review.setMovie(movie);
        reviewRepository.save(review);

        // unwrap the optional, returning value or 0 if null
        var rating = reviewRepository.averageReviewRating(movie.getId()).orElseGet(() -> 0);;
        movie.setRating(rating);
        repository.save(movie);

        return review.getMovie();
    }

    @Override
    public void deleteReview(long id) {
        var review = reviewRepository.findById(id).get();
        var movie = review.getMovie();
        reviewRepository.delete(review);

        // unwrap the optional, returning value or 0 if null
        int rating = reviewRepository.averageReviewRating(review.getMovie().getId()).orElseGet(() -> 0);
        movie.setRating(rating);
        repository.save(movie);
    }

    @Override
    public void updateMovie(Movie m) {
        repository.save(m);
    }

    @Override
    public void deleteMovieById(long id) {
        repository.deleteById(id);
    }

    @Override
    public void deleteAllMovies() {
        repository.deleteAll();
    }


}

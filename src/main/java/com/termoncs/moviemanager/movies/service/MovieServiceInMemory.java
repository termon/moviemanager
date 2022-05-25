/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.termoncs.moviemanager.movies.service;

import com.termoncs.moviemanager.movies.model.Movie;
import com.termoncs.moviemanager.movies.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 *
 * @author aiden
 */
public class MovieServiceInMemory implements IMovieService {
    
    private List<Movie> movies = new ArrayList<Movie>();

    public MovieServiceInMemory() {
    }

    @Override
    public long getCount() {
        return movies.size();
    }

    @Override
    public List<Movie> getMovies() {
        return movies;
    }

    @Override
    public Page<Movie> getMovies(Pageable p) {
        var limit = p.getPageSize();
        var offset = p.getOffset();
        var results = movies.stream()
                .skip(offset)
                .limit(limit)
                .collect(Collectors.toList());
        return new PageImpl<Movie>(results,p,getCount());
    }

    @Override
    public Movie getMovieById(long id) {
        return movies.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .get();
    }

    @Override
    public void addMovie(Movie m) {
        if (m.getId() == 0) {
            var nextId = movies.stream().count() + 1;
            m.setId(nextId);
        }
        movies.add(m);
    }

    @Override
    public void updateMovie(Movie m) {
        var orig = getMovieById(m.getId());
        orig.setName(m.getName())
                .setDirector(m.getDirector())
                .setBudget(m.getBudget())
                .setCast(m.getCast())
                .setGenre(m.getGenre())
                .setDuration(m.getDuration())
                .setDuration(m.getDuration())
                .setPlot(m.getPlot())
                .setYear(m.getYear())
                .setPosterUrl(m.getPosterUrl())
                .setRating(m.getRating())
                .setReviews(m.getReviews());
    }

    @Override
    public void deleteMovieById(long id) {
        movies = movies.stream().dropWhile(m -> m.getId() == id).collect(Collectors.toList());
    }

    @Override
    public void deleteAllMovies() {
        movies = new ArrayList<>();
    }

    @Override
    public Movie addReview(Review r) {
        var id = getReviews().size() + 1L;
        r.setId(id);
        var m = getMovieById(r.getMovieId());
        m.getReviews().add(r);
        return m;
    }

    @Override
    public void deleteReview(long id) {
        var review = getReview(id);
        var movie = getMovieById(review.getMovieId());
        movie.setReviews(movie.getReviews().stream().filter(r -> r.getId() != id).collect(Collectors.toList()));
    }

    private List<Review> getReviews() {
        return movies.stream()
                .flatMap(m -> m.getReviews().stream())
                .collect(Collectors.toList());
    }

    private Review getReview(long id) {
        return getReviews()
                .stream()
                .filter(m -> m.getId() == id)
                .findFirst().get();
    }

}

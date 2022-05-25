package com.termoncs.moviemanager.movies.service;

import com.termoncs.moviemanager.movies.model.Movie;
import com.termoncs.moviemanager.movies.model.Review;
import com.termoncs.moviemanager.movies.repository.MovieMyBatisMapper;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 *
 * @author aiden
 */
public class MovieServiceMyBatis implements IMovieService {
   
    @Autowired
    private MovieMyBatisMapper repository;

    @Override
    public long getCount() {
        return repository.count();
    }

    @Override
    public List<Movie> getMovies() {
        return  repository.getMovies();
    }

    @Override
    public Page<Movie> getMovies(Pageable p) {
        var movies = repository.getPagedMovies(p.getPageSize(),p.getOffset());
        return new PageImpl<Movie>(movies,p,getCount());
    }

    @Override
    public Movie getMovieById(long id) {        
        var m = repository.getMovieById(id);
        if (m != null) {
           var r = repository.getReviewsForMovie(id);
            m.setReviews(r);
        }
        return m;
    }

    @Override
    public void addMovie(Movie m) {
        repository.addMovie(m);
    }

    @Override
    public void updateMovie(Movie m) {
        repository.updateMovie(m);
    }

    @Override
    public void deleteMovieById(long id) {
        repository.deleteReviewsForMovie(id);
        repository.deleteMovieById(id);
    }

    @Override
    public void deleteAllMovies() {
        repository.deleteAllMovies();
    }

    @Override
    public Movie addReview(Review r) {
        repository.addReview(r);
        var avg = repository.averageReview(r.getMovie().getId());
        var m = getMovieById(r.getMovie().getId());
        m.setRating(avg);
        repository.updateMovie(m);
        return m;
    }

    @Override
    public void deleteReview(long id) {
        repository.deleteReview(id);
    }
}

package com.termoncs.moviemanager.movies.service;

import com.termoncs.moviemanager.movies.model.Genre;
import com.termoncs.moviemanager.movies.model.Movie;
import com.termoncs.moviemanager.movies.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

/**
 *
 * @author aiden
 */
public class MovieServiceJdbc implements IMovieService {

    @Autowired
    JdbcTemplate jdbcTemplate;

    public long getCount() {
        return jdbcTemplate.queryForObject("select count(*) from movie", Long.class).longValue();
    }

    public List<Movie> getMovies() {
        return jdbcTemplate.query(
                "select id, name, director, budget, year, genre, duration, rating, poster_url, cast, plot from movie",
                (rs,row) -> new Movie()
                        .setId( rs.getLong("id"))
                        .setName( rs.getString("name"))
                        .setDirector( rs.getString("director"))
                        .setBudget( rs.getDouble("budget"))
                        .setYear( rs.getInt("year"))
                        .setGenre( Genre.values()[rs.getInt("genre")])
                        .setPosterUrl( rs.getString("poster_url"))
                        .setCast( rs.getString("cast"))
                        .setPlot( rs.getString("plot"))
        );
    }

    private List<Review> getReviewsForMovie(Movie movie) {
        return jdbcTemplate.query("select id, name, created, rating, comment from review where movie_id = ?",
                (rs,row) -> new Review()
                        .setId( rs.getLong("id") )
                        .setName( rs.getString("name"))
                        .setOn( rs.getDate("created"))
                        .setComment( rs.getString("comment"))
                        .setMovie(movie),
                movie.getId()
        );
    }

    @Override
    public Page<Movie> getMovies(Pageable p) {
        // for now return an empty page
        var movies = jdbcTemplate.query(
                 "select id, name, director, budget, year, genre, duration, rating, poster_url, cast, plot from movie LIMIT ? OFFSET ? ",
                     (rs,row) ->  new Movie()
                            .setId( rs.getLong("id"))
                            .setName( rs.getString("name"))
                            .setDirector( rs.getString("director"))
                            .setBudget( rs.getDouble("budget"))
                            .setYear( rs.getInt("year"))
                            .setGenre( Genre.values()[rs.getInt("genre")])
                            .setPosterUrl( rs.getString("poster_url"))
                            .setCast( rs.getString("cast"))
                            .setPlot( rs.getString("plot")),
                    p.getPageSize(), p.getOffset()
        );
        return new PageImpl<Movie>(movies,p,getCount());
    }

    @Override
    public Movie getMovieById(long id) {
        var movie = jdbcTemplate.queryForObject(
                "select id, name, director, budget, year, genre, duration, rating, poster_url, cast, plot from movie where id = ?",
                (rs,row) -> new Movie()
                        .setId( rs.getLong("id") )
                        .setName( rs.getString("name") )
                        .setDirector( rs.getString("director") )
                        .setBudget( rs.getDouble("budget"))
                        .setYear( rs.getInt("year") )
                        .setGenre( Genre.values()[rs.getInt("genre")] )
                        .setPosterUrl( rs.getString("poster_url") )
                        .setCast( rs.getString("cast") )
                        .setPlot( rs.getString("plot") ),
                id
        );
        movie.setReviews(getReviewsForMovie(movie));
        return movie;
    }

    @Override
    public void addMovie(Movie m) {
        m.setId(getCount()+1);
        jdbcTemplate.update(
                "insert into movie (id, name, director, budget, year, genre, duration, rating, poster_url, cast, plot) values (?,?,?,?,?,?,?,?,?,?,?)",
                m.getId(),
                m.getName(),
                m.getDirector(),
                m.getBudget(),
                m.getYear(),
                m.getGenre().ordinal(),
                m.getDuration(),
                m.getRating(),
                m.getPosterUrl(),
                m.getCast(),
                m.getPlot()
        );
    }

    @Override
    public void updateMovie(Movie m) {
        jdbcTemplate.update(
                "update movie set name=?, director=?, budget=?, year=?, genre=?, duration=?, rating=?, poster_url=?, cast=?, plot=? where id=?",
                m.getName(),
                m.getDirector(),
                m.getBudget(),
                m.getYear(),
                m.getGenre().ordinal(),
                m.getDuration(),
                m.getRating(),
                m.getPosterUrl(),
                m.getCast(),
                m.getPlot(),
                m.getId()
        );
    }

    @Override
    public void deleteMovieById(long id) {
        jdbcTemplate.update("delete from review where movie_id = ?",id);
        jdbcTemplate.update("delete from movie where id = ?",id);
    }

    @Override
    public void deleteAllMovies() {
        jdbcTemplate.update("delete from review");
        jdbcTemplate.update("delete from movie");
    }

    @Override
    public Movie addReview(Review r) {
        jdbcTemplate.update(
                "insert into review(name, rating, created, comment, movie_id) values (?,?,?,?,?)",
                r.getName(),
                r.getRating(),
                r.getCreated(),
                r.getComment(),
                r.getMovieId()
        );
        var movie = getMovieById(r.getMovieId());
        r.setMovie(movie);
        return movie;
    }

    @Override
    public void deleteReview(long id) {
        jdbcTemplate.update("delete from review where id = ?",id);
    }

}

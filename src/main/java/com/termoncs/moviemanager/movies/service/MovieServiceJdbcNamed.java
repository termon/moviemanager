package com.termoncs.moviemanager.movies.service;

import com.termoncs.moviemanager.movies.model.Genre;
import com.termoncs.moviemanager.movies.model.Movie;
import com.termoncs.moviemanager.movies.model.Review;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.util.List;

/**
 *
 * @author aiden
 */
public class MovieServiceJdbcNamed implements IMovieService {

    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    public long getCount() {
        return jdbcTemplate.queryForObject("select count(*) from movie",new MapSqlParameterSource(), Long.class).longValue();
    }

    private long getReviewCount() {
        return jdbcTemplate.queryForObject("select count(*) from review",new MapSqlParameterSource(), Long.class).longValue();
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

    @Override
    public Page<Movie> getMovies(Pageable p) {
        var movies = jdbcTemplate.query(
                "select id, name, director, budget, year, genre, duration, rating, poster_url, cast, plot from movie LIMIT :limit OFFSET :offset",
                new MapSqlParameterSource()
                        .addValue("limit", p.getPageSize())
                        .addValue("offset", p.getOffset()),
                (rs,row) ->  new Movie()
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
        return new PageImpl<Movie>(movies,p,getCount());
    }


    private List<Review> getReviewsForMovie(Movie movie) {
        return jdbcTemplate.query(
                "select id, name, created, rating, comment from review where movie_id = :id",
                new MapSqlParameterSource("id", movie.getId()),
                (rs,row) -> new Review()
                        .setId( rs.getLong("id") )
                        .setName( rs.getString("name"))
                        .setOn( rs.getDate("created"))
                        .setComment( rs.getString("comment"))
                        .setMovie(movie)
        );
    }

    public Movie getMovieById(long id) {
        var movie = jdbcTemplate.queryForObject(
                "select * from movie where id = :id",
                new MapSqlParameterSource("id", id),
                (rs,row) -> new Movie()
                        .setId( rs.getLong("id") )
                        .setName( rs.getString("name") )
                        .setDirector( rs.getString("director") )
                        .setBudget( rs.getDouble("budget"))
                        .setYear( rs.getInt("year") )
                        .setGenre( Genre.values()[rs.getInt("genre")] )
                        .setPosterUrl( rs.getString("poster_url") )
                        .setCast( rs.getString("cast") )
        );
        movie.setReviews(getReviewsForMovie(movie));
        return movie;
    }

    private void updateMovieRating(Movie movie) {
        var result = jdbcTemplate.queryForObject(
                "select avg(rating) from review where movie_id = :id",
                new MapSqlParameterSource( "id", movie.getId()),
                Integer.class
        );

        int rating = result != null ? result.intValue() : 0;
        movie.setRating(rating);

        var params = new MapSqlParameterSource()
                .addValue("id", movie.getId())
                .addValue("rating", rating);

        jdbcTemplate.update("update movie set rating=:rating where id=:id",params);
    }

    public void addMovie(Movie m) {
        var query = "insert into movie (name, director, budget, year, duration, rating, poster_url, cast, plot) values "
                        + "( :name, :director, :budget, :year, :duration, :rating, :poster_url, :cast, :plot)";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", m.getName())
                .addValue("director", m.getDirector())
                .addValue("budget", m.getBudget())
                .addValue("year", m.getYear())
                .addValue("duration", m.getDuration())
                .addValue("rating", m.getRating())
                .addValue("poster_url", m.getPosterUrl())
                .addValue("cast", m.getCast())
                .addValue("plot", m.getPlot());

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(query, params, keyHolder );
        m.setId( keyHolder.getKey().longValue() );
    }

    public void updateMovie(Movie m) {
        var query = "update movie "
                        + "set name = :name, "
                        + "director = :director, "
                        + "budget = :budget, "
                        + "year = :year, "
                        + "duration = :duration, "
                        + "rating = :rating, "
                        + "poster_url = :poster_url, "
                        + "cast = :cast, "
                        + "plot = :plot "
                        + "where id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", m.getId())
                .addValue("name", m.getName())
                .addValue("director", m.getDirector())
                .addValue("budget", m.getBudget())
                .addValue("year", m.getYear())
                .addValue("duration", m.getDuration())
                .addValue("rating", m.getRating())
                .addValue("poster_url", m.getPosterUrl())
                .addValue("cast", m.getCast())
                .addValue("plot", m.getPlot());
        jdbcTemplate.update(query, params );
    }

    public void deleteMovieById(long id) {
        jdbcTemplate.update("delete from review where movie_id = :id", new MapSqlParameterSource("id", id));
        jdbcTemplate.update("delete from movie where id = :id", new MapSqlParameterSource("id", id));
    }

    @Override
    public void deleteAllMovies() {
        jdbcTemplate.update("delete from review ", new MapSqlParameterSource());
        jdbcTemplate.update("delete from movie ", new MapSqlParameterSource());
    }

    @Override
    public Movie addReview(Review r) {
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", r.getName())
                .addValue("rating", r.getRating())
                .addValue("created", r.getCreated())
                .addValue( "comment", r.getComment())
                .addValue("movie_id", r.getMovieId());

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                "insert into review( name, rating, created, comment, movie_id) values ( :name, :rating, :created, :comment, :movie_id)",
                params,
                keyHolder
        );
        r.setId( keyHolder.getKey().longValue() );

        var movie = getMovieById(r.getMovieId());
        updateMovieRating(movie);
        r.setMovie(movie);
        return movie;
    }

    private Review getReview(long id) {
        var review = jdbcTemplate.queryForObject(
                "select * from review where id = :id",
                new MapSqlParameterSource("id", id),
                (rs,row) -> new Review()
                        .setId( rs.getLong("id") )
                        .setName( rs.getString("name") )
                        .setOn( rs.getDate("created") )
                        .setMovieId( rs.getLong("movie_id"))
                        .setComment( rs.getString("comment") )
        );
        review.setMovie( getMovieById(review.getMovieId()));
        return review;
    }

    @Override
    public void deleteReview(long id) {
        var movie = getReview(id).getMovie();
        jdbcTemplate.update("delete from review where id = :id", new MapSqlParameterSource("id", id));
        updateMovieRating(movie);
    }


}

package com.termoncs.moviemanager.movies.repository;

import com.termoncs.moviemanager.movies.model.Movie;
import com.termoncs.moviemanager.movies.model.Review;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MovieMyBatisMapper {

    @Select("SELECT COUNT(*) FROM movie")
    public long count();

    @Select("SELECT AVG(rating) FROM review WHERE movie_id=#{movie_id}")
    public int averageReview(long movie_id);

    @Select("SELECT id, name, director, budget, year, duration, rating, genre, poster_url as posterUrl, cast, plot FROM movie")
    public List<Movie> getMovies();

    @Select("SELECT id, name, director, budget, year, duration, rating, genre, poster_url as posterUrl, cast, plot FROM movie LIMIT #{pageSize} OFFSET #{offset}")
    public List<Movie> getPagedMovies(int pageSize, long offset);

    @Select("SELECT id, name, comment, created, rating FROM review WHERE movie_id=#{id}")
    public List<Review> getReviewsForMovie(long id);

    @Select("SELECT id, name, director, budget, year, duration, rating, genre, poster_url as posterUrl, cast, plot FROM movie WHERE id = #{id}")
    public Movie getMovieById(long id);

    @Insert("INSERT INTO movie (name, director, budget, year, duration, rating, poster_url, genre, cast, plot) VALUES (#{name}, #{director}, #{budget}, #{year}, #{duration}, ${rating}, #{posterUrl}, #{genre}, #{cast}, #{plot})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public void addMovie(Movie m);

    @Update("UPDATE movie SET name=#{name}, director=#{director}, budget=#{budget}, year=#{year}, duration=#{duration}, rating=#{rating}, poster_url=#{posterUrl}, genre=#{genre}, cast=#{cast}, plot=#{plot} WHERE id=#{id}")
    //@UpdateProvider(type=MovieSqlBuilder.class, method="updateMovie")
    public void updateMovie(Movie m);

    @Delete("DELETE FROM movie WHERE id=#{id}")
    public void deleteMovieById(long id);

    @Delete("DELETE FROM movie")
    public void deleteAllMovies();

    @Insert("INSERT INTO review (name, comment, created, rating, movie_id) VALUES (#{name}, #{comment}, #{created}, #{rating}, #{movieId})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public void addReview(Review r);

    @Delete("DELETE FROM review WHERE id=#{id}")
    public void deleteReview(long id);

    @Delete("SELECT FROM review WHERE id=#{id}")
    public void getReviewById(long id);

    @Delete("DELETE FROM review WHERE movie_id=#{movie_id}")
    public void deleteReviewsForMovie(long movie_id);

}

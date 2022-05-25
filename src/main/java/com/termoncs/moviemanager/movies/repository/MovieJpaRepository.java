/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.termoncs.moviemanager.movies.repository;

import com.termoncs.moviemanager.movies.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author aiden
 */
public interface MovieJpaRepository extends JpaRepository<Movie, Long> {

        @Query("SELECT m FROM Movie m WHERE m.rating = 0")
        Collection<Movie> findAllZeroRatings();

        @Query(value = "SELECT * FROM Movie m WHERE m.rating = 0", nativeQuery = true)
        Collection<Movie> finaAllZeroRatingsNative();

        @Modifying
        @Transactional
        @Query(value = "insert into review(movie_id, name, comment, created, rating) values (:movie_id, :name, :comment, :created, :rating)", nativeQuery = true)
        void addReviewToMovie(@Param("movie_id") long movie_id, @Param("name") String name, @Param("comment") String comment, @Param("created") Date created, @Param("rating") int rating);
}

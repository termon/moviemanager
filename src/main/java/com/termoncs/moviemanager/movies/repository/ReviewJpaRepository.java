/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.termoncs.moviemanager.movies.repository;

import com.termoncs.moviemanager.movies.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 *
 * @author aiden
 */
public interface ReviewJpaRepository extends JpaRepository<Review, Long> {
    // query can return null if resultset is empty, thus wrap in an optional
    @Query("SELECT avg(rating) FROM Review WHERE movie_id=?1")
    public Optional<Integer> averageReviewRating(Long movie_id);
}

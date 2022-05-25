/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.termoncs.moviemanager.auth.repository;

import com.termoncs.moviemanager.auth.model.MovieUser;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author aiden
 */
public interface MovieUserJpaRepository extends CrudRepository<MovieUser, Long> {
    public MovieUser findByUsername(String name);
}

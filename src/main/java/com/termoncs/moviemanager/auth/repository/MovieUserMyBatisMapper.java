/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.termoncs.moviemanager.auth.repository;

import com.termoncs.moviemanager.auth.model.MovieUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
/**
 *
 * @author aiden
 */
@Mapper
public interface MovieUserMyBatisMapper  {

    @Select("SELECT id, username, password, role FROM user WHERE username = #{name} LIMIT 1")
    public MovieUser findByUsername(String name);

    @Insert("INSERT INTO user(username, password, role) VALUES (#{username}, #{password}, #{role})")
    public void save(MovieUser user);
}


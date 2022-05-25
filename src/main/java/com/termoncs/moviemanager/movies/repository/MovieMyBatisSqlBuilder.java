
package com.termoncs.moviemanager.movies.repository;

import org.apache.ibatis.jdbc.SQL;
import org.springframework.stereotype.Component;

@Component
public class MovieMyBatisSqlBuilder {

    public String updateMovie() {
        return new SQL()
                .UPDATE("movie")
                .SET("name=#{name}, director=#{director}, budget=#{budget}, year=#{year}, duration=#{duration}, " +
                        "rating=#{rating}, poster_url=#{posterUrl}, genre=${genre}, cast=#{cast}, plot=#{plot}")
                .WHERE("id = #{id}")
                .toString();
    }
}



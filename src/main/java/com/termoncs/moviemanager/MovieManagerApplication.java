package com.termoncs.moviemanager;

import com.termoncs.moviemanager.movies.service.IMovieService;
import com.termoncs.moviemanager.movies.service.*;

import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.transaction.Transactional;

@SpringBootApplication
public class MovieManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(MovieManagerApplication.class, args);
	}

	/**
	 *  Wire up implementation of IMovieService required by application
	 */
	@Bean
	@Transactional
	IMovieService getMovieService() {
		return new MovieServiceJpa();
	}

	/**
	 * Needed since Thymeleaf 3 - configures extension that adds layout support from nz.net.ultraq
	 * @return LayoutDialect
	 */
	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}

}

package com.termoncs;

import com.termoncs.moviemanager.MovieManagerApplication;
import com.termoncs.moviemanager.movies.model.Genre;
import com.termoncs.moviemanager.movies.model.Movie;
import com.termoncs.moviemanager.movies.repository.MovieJpaRepository;
import com.termoncs.moviemanager.movies.service.IMovieService;
import com.termoncs.moviemanager.movies.service.MovieServiceInMemory;
import com.termoncs.moviemanager.movies.service.MovieServiceJpa;
import nz.net.ultraq.thymeleaf.LayoutDialect;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
//@SpringBootTest(classes = MovieManagerApplication.class)
@ContextConfiguration(classes = { MovieManagerTestConfig.class })
class MovieManagerApplicationTests {

	@Autowired
	IMovieService service;

	@Test
	void getAllMoviesWhenEmptyReturnsZero() {
		var movies = service.getMovies();

		assertThat(movies.size()).isEqualTo(0);
	}

	@Test
	void getAllMoviesWhenOneAddedReturnsOne() {
		// Arrange
		service.addMovie(createMovie(1,"Dummy"));

		// Act
		var movies = service.getMovies();

		// Assert
		assertThat(movies.size()).isEqualTo(1);
	}

	@Test
	void deleteAllMoviesReturnsZero() {
		// Arrange
		service.addMovie(createMovie(1,"Dummy"));

		// Act
		service.deleteAllMovies();
		var movies = service.getMovies();

		// Assert
		assertThat(movies.size()).isEqualTo(0);
	}

	private Movie createMovie(int id, String name) {
		return new Movie()
				.setId(id)
				.setName(name)
				.setDirector("J Bloggs")
				.setBudget(45.0)
				.setYear(2020)
				.setRating(5)
				.setDuration(140)
				.setPosterUrl("https://image.tmdb.org/t/p/w1280/5KCVkau1HEl7ZzfPsKAPM0sMiKc.jpg")
				.setGenre(Genre.ACTION)
				.setPlot(name + " plot")
				.setCast(name + " cast");
	}

}

@Configuration
class MovieManagerTestConfig {
	@Bean
	@Transactional
	/**
	 *  Wire up implementation of IMovieService required by application
	 */
	IMovieService getMovieService() {
		return new MovieServiceInMemory();
	}
}
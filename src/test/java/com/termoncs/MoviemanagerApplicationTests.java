package com.termoncs;

import com.termoncs.moviemanager.MovieManagerApplication;
import com.termoncs.moviemanager.movies.service.IMovieService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = MovieManagerApplication.class)
class MovieManagerApplicationTests {

	@Autowired
	IMovieService service;

	public MovieManagerApplicationTests(IMovieService service) {
		this.service = service;
	}

	@Test
	void getAllMoviesReturnsCorrectSize() {
		var movies = service.getMovies();

		assertThat(movies.size()).isEqualTo(0);
	}

}

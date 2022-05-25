package com.termoncs.moviemanager.movies.controller;

import com.termoncs.moviemanager.core.ResponseMessage;
import com.termoncs.moviemanager.movies.model.Movie;
import com.termoncs.moviemanager.movies.service.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  Using Custom ResponseMessage class to "wrap" response into a json object
 *  that can be consumed by client applications
 * @author aiden
 */

@RestController
@CrossOrigin
//@CrossOrigin(origins = "http://example.com", maxAge = 3600)
public class MovieApiController {
    
    @Autowired
    private IMovieService service;
       
    @RequestMapping("/api/movies")
    public ResponseMessage<List<Movie>> index() {

        return new ResponseMessage<List<Movie>>()
                        .withSuccess(true)
                        .withMessage("List of movies")
                        .withResponse(service.getMovies()
        );
    }
    
    @RequestMapping("/api/movies/{id}")
    public ResponseMessage<Movie> find(@PathVariable final int id) {
        final var movie = service.getMovieById(id);
        return new ResponseMessage<Movie>()
                .withSuccess(movie != null)
                .withMessage("Individual movie")
                .withResponse(movie);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/api/movies")
    public ResponseMessage<Movie> add(@Valid @RequestBody final Movie m) {
        service.addMovie(m);
        return new ResponseMessage<Movie>()
                    .withSuccess(true)
                    .withMessage("Added movie")
                    .withResponse(m);
    }

    /**
     * Will convert validation errors into a custom json object. This can be
     * modified to suit any front end framework requirements
     *
     * @param ex - validation exception
     * @return json object containing validation results
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseMessage<Object> handleValidationExceptions(final MethodArgumentNotValidException ex) {
        final Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            final String fieldName = ((FieldError) error).getField();
            final String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return new ResponseMessage<Object>()
                .withSuccess(false)
                .withMessage("Validation errors")
                .withErrors(errors);
    }
}
// @RestController
// public class MovieApiController {
    
//     @Autowired
//     private IMovieService service;
       
//     @RequestMapping("/api/movies")
//     public ResponseEntity<ResponseMessage<List<Movie>>> index() {

//         return ResponseEntity.ok(
//                 new ResponseMessage<List<Movie>>()
//                         .withSuccess(true)
//                         .withMessage("List of movies")
//                         .withResponse(service.getMovies())
//         );
//     }
    
//     @RequestMapping("/api/movies/{id}")
//     public ResponseEntity<ResponseMessage<Movie>> find(@PathVariable final int id) {
//         final var movie = service.getMovieById(id);
//         return ResponseEntity.ok(new ResponseMessage<Movie>().withSuccess(movie != null).withMessage("Individual movie")
//                 .withResponse(movie));
//     }

//     @RequestMapping(method = RequestMethod.POST, value = "/api/movies")
//     public ResponseEntity<ResponseMessage<Movie>> add(@Valid @RequestBody final Movie m) {
//         service.addMovie(m);
//         return ResponseEntity
//                 .ok(new ResponseMessage<Movie>().withSuccess(true).withMessage("Added movie").withResponse(m));
//     }

//     /**
//      * Will convert validation errors into a custom json object. This can be
//      * modified to suit any front end framework requirements
//      *
//      * @param ex - validation exception
//      * @return json object containing validation results
//      */
//     @ResponseStatus(HttpStatus.BAD_REQUEST)
//     @ExceptionHandler(MethodArgumentNotValidException.class)
//     public ResponseMessage<Object> handleValidationExceptions(final MethodArgumentNotValidException ex) {
//         final Map<String, String> errors = new HashMap<>();
//         ex.getBindingResult().getAllErrors().forEach((error) -> {
//             final String fieldName = ((FieldError) error).getField();
//             final String errorMessage = error.getDefaultMessage();
//             errors.put(fieldName, errorMessage);
//         });
//         return new ResponseMessage<Object>()
//                 .withSuccess(false)
//                 .withMessage("Validation errors")
//                 .withErrors(errors);

//     }
// }

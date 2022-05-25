/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.termoncs.moviemanager.movies.controller;


import com.termoncs.moviemanager.auth.service.IAuthenticationFacade;
import com.termoncs.moviemanager.movies.model.Movie;
import com.termoncs.moviemanager.movies.model.Review;
import com.termoncs.moviemanager.movies.service.IMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.time.Instant;
import java.util.Date;

/**
 *
 * @author aiden
 */
@Controller
public class MovieController {

    @Autowired
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    private IMovieService service;

    @GetMapping("/movies")
    public String index(Model model,
                        @RequestParam(value="page",defaultValue="0") int page,
                        @RequestParam(value="size",defaultValue="5") int size) {

        var pageResult = service.getMovies(PageRequest.of(page,size));
        model.addAttribute("pages", pageResult);
        return "movies/index";
    }

    @RequestMapping("/movies/{id}")
    public String get(@PathVariable int id, Model model) {
        var movie = service.getMovieById(id);
        model.addAttribute("movie", movie);
        return "movies/view";
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @GetMapping("/movies/create")
    public String createForm(Model model) {
        model.addAttribute("movie", new Movie());
        return "movies/create";
    }

    // @RequestMapping(method= RequestMethod.POST, value="/movies")
    @RolesAllowed({"ROLE_ADMIN"})
    @PostMapping("/movies")
    public String create(@Valid @ModelAttribute("movie") Movie movie, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("movie", movie);
            return "movies/create";
        }
        service.addMovie(movie);
        return "redirect:/movies/" + movie.getId();
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @GetMapping("/movies/edit/{id}")
    public String editForm(@PathVariable int id, Model model) {
        var movie = service.getMovieById(id);
        model.addAttribute("movie", movie);
        return "movies/edit";
    }

    // @RequestMapping(method= RequestMethod.POST, value="/movies")
    @RolesAllowed({"ROLE_ADMIN"})
    @PostMapping("/movies/edit/{id}")
    public String update(@PathVariable int id,
                         @Valid @ModelAttribute("movie") Movie movie,
                         BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("movie", movie);
            return "movies/edit";
        }
        service.updateMovie(movie);
        return "redirect:/movies/" + movie.getId();
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @GetMapping("/movies/delete/{id}")
    public String deleteForm(@PathVariable int id, Model model) {
        var movie = service.getMovieById(id);
        model.addAttribute("movie", movie);
        return "movies/delete";
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @PostMapping("/movies/delete/{id}")
    public String delete(@PathVariable int id) {
        service.deleteMovieById(id);
        return "redirect:/movies";
    }

    @RolesAllowed({"ROLE_ADMIN"})
    @PostMapping("/movies/reviews/delete")
    public String deleteReview(@ModelAttribute("movie_id") int movie_id, @ModelAttribute("review_id") int review_id, RedirectAttributes attr) {
        attr.addFlashAttribute("info","Review deleted successfully");
        service.deleteReview(review_id);
        return "redirect:/movies/" + movie_id;
    }

    /**
     * Rather rather than creating AuthenticationFacade and injecting where required, we could just use the fact that
     * controller actions can use the Http request to access to the Security principal by adding
     * HttpServletRequest req as method parameter, then access user via req.getUserPrincipal()
     */
    @RolesAllowed({"ROLE_ADMIN", "ROLE_GUEST"})
    @GetMapping("/movies/createreview/{id}")
    public String createReviewForm(@PathVariable int id, Model model) {
        var user = authenticationFacade.getAuthentication();
        var movie = service.getMovieById(id);
        var review = new Review()
                .setName(user.getName())
                .setComment("leave a comment..")
                .setMovie(movie)
                .setOn(Date.from(Instant.now()));


        model.addAttribute("review", review);
        return "movies/createreview";
    }

    // @RequestMapping(method= RequestMethod.POST, value="/movies")
    @RolesAllowed({"ROLE_ADMIN", "ROLE_GUEST"})
    @PostMapping("/movies/createreview")
    public String createReview(@Valid @ModelAttribute("review") Review review, BindingResult bindingResult, Model model, RedirectAttributes attr) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("review", review);
            return "movies/createreview";
        }
        service.addReview(review);
        attr.addFlashAttribute("info","Review added successfully");
        return "redirect:/movies/" + review.getMovieId();
    }
}

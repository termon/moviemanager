package com.termoncs.moviemanager.movies.seeder;

import com.termoncs.moviemanager.MovieManagerApplication;
import com.termoncs.moviemanager.auth.model.MovieUser;
import com.termoncs.moviemanager.auth.service.AuthUserDetailsService;
import com.termoncs.moviemanager.movies.model.Genre;
import com.termoncs.moviemanager.movies.model.Movie;
import com.termoncs.moviemanager.movies.model.Review;
import com.termoncs.moviemanager.movies.service.IMovieService;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;


@Component
public class ServiceSeeder {

    @Autowired
    private IMovieService service; // for movie seeding

    @Autowired
    AuthUserDetailsService userService; // for user  seeding

    org.slf4j.Logger logger = LoggerFactory.getLogger(MovieManagerApplication.class);

    /**
     * The ContextRefreshedEvent is fired when the application has been totally bootstrapped and all bean objects have
     * been instantiated. Then, we will use the Models and configured repositories to persist default data into the
     * database. One crucial advantage of running the seeders when the ContextRefreshEvent is fired is that we get
     * access to all auto-wired beans in the application — including repositories, services etc.
     *
     * @param event
     */
    @EventListener
    public void seed(ContextRefreshedEvent event) {

        logger.info("*********** +++ Seeding Database +++ **********");

        service.deleteAllMovies();
        userService.deleteAllUsers();

        var m1 = new Movie()
                //.setId(1)
                .setName("The Shawshank Redemption")
                .setDirector("J Bloggs")
                .setBudget(45.0)
                .setYear(2007)
                .setRating(5)
                .setDuration(140)
                .setPosterUrl("https://image.tmdb.org/t/p/w1280/5KCVkau1HEl7ZzfPsKAPM0sMiKc.jpg")
                .setGenre(Genre.ACTION)
                .setPlot("plot")
                .setCast("Cast Shawshank");

        var m2 = new Movie()
                //.setId(2)
                .setName("The Terminator")
                .setDirector("James Cameron")
                .setBudget(6.4)
                .setDuration(108)
                .setYear(1984)
                .setPosterUrl("https://image.tmdb.org/t/p/w1280/qvktm0BHcnmDpul4Hz01GIazWPr.jpg")
                .setGenre(Genre.SCI_FI)
                .setPlot("In the post-apocalyptic future, reigning tyrannical supercomputers teleport a cyborg assassin known as the \"Terminator\" back to 1984 to kill Sarah Connor, whose unborn son is destined to lead insurgents against 21st century mechanical hegemony. Meanwhile, the human-resistance movement dispatches a lone warrior to safeguard Sarah. Can he stop the virtually indestructible killing machine?")
                .setCast("Arnold Schwarzenegger, Michael Bien, Linda Hamilton");

        var m3 = new Movie()
                //.setId(3)
                .setName("Jaws")
                .setDirector("Steven Speilberg")
                .setBudget(7.0)
                .setDuration(124)
                .setYear(1975)
                .setPosterUrl("https://image.tmdb.org/t/p/w1280/s2xcqSFfT6F7ZXHxowjxfG0yisT.jpg")
                .setGenre(Genre.HORROR)
                .setPlot("When an insatiable great white shark terrorizes the townspeople of Amity Island, the police chief, an oceanographer and a grizzled shark hunter seek to destroy the blood-thirsty beast.")
                .setCast("Cast Jaws");


        var m4 = new Movie()
               // .setId(4)
                .setName("The Cable Guy")
                .setDirector("Ben Stiller")
                .setBudget(47.0)
                .setDuration(96)
                .setYear(1996)
                .setPosterUrl("https://image.tmdb.org/t/p/w1280/5cZySBvy41eHTD5LyQn48aP444k.jpg")
                .setGenre(Genre.COMEDY)
                .setPlot("plot")
                .setCast("Cast Cable");


        var m5 = new Movie()
               // .setId(5)
                .setName("Alien")
                .setDirector("Ridley Scott")
                .setBudget(190.0)
                .setDuration(90)
                .setYear(1979)
                .setPosterUrl("https://image.tmdb.org/t/p/w1280/vfrQk5IPloGg1v9Rzbh2Eg3VGyM.jpg")
                .setGenre(Genre.SCI_FI)
                .setPlot("plot")
                .setCast("Cast Alien");


        var m6 = new Movie()
                //.setId(6)
                .setName("Boo")
                .setGenre(Genre.CHILDREN)
                .setDirector("Another")
                .setBudget(100)
                .setYear(2019)
                .setDuration(120)
                .setCast("Cast")
                .setPlot("plot")
                .setPosterUrl("https://image.tmdb.org/t/p/w1280/5Lnt9yuLlPwmYW3LLzWmQQWrUmK.jpg")
                .setGenre(Genre.ROMANCE)
                .setPlot("plot")
                .setCast("Cast Boo");

        var m7 = new Movie()
                //.setId(7)
                .setName("Terminator Dark Fate")
                .setDirector("Tim Miller")
                .setBudget(185.0)
                .setDuration(128)
                .setYear(2019)
                .setPosterUrl("https://image.tmdb.org/t/p/w1280/vqzNJRH4YyquRiWxCCOH0aXggHI.jpg")
                .setGenre(Genre.SCI_FI)
                .setPlot("More than two decades have passed since Sarah Connor prevented Judgment Day, changed the future, and re-wrote the fate of the human race.")
                .setCast("Cast Terminator Dark Fate");

        var m8 = new Movie()
               // .setId(8)
                .setName("The Joker")
                .setDirector("Tim Miller")
                .setBudget(55.0)
                .setDuration(122)
                .setYear(2019)
                .setPosterUrl("https://image.tmdb.org/t/p/w1280/udDclJoHjfjb8Ekgsd4FDteOkCU.jpg")
                .setGenre(Genre.SCI_FI)
                .setPlot("During the 1980s, a failed stand-up comedian is driven insane and turns to a life of crime and chaos in Gotham City while becoming an infamous psychopathic crime figure.")
                .setCast("Joaquin Phoenix");

        service.addMovie(m1);
        service.addReview(new Review().setMovie(m1).setName("user1").setComment("excellent").setRating(5));
        service.addReview(new Review().setMovie(m1).setName("user2").setComment("best Ever").setRating(5));

        service.addMovie(m2);
        service.addReview(new Review().setMovie(m2).setName("user3").setComment("ground breaking").setRating(5));

        service.addMovie(m3);
        service.addMovie(m4);
        service.addMovie(m5);
        service.addMovie(m6);
        service.addReview(new Review().setMovie(m6).setName("user3").setComment("for children only").setRating(2));
        service.addMovie(m7);
        service.addMovie(m8);



        // add default users
        userService.addUser(new MovieUser().setUsername("admin").setPassword("admin").setRole("ADMIN"));
        userService.addUser(new MovieUser().setUsername("guest").setPassword("guest").setRole("GUEST"));

    }
}

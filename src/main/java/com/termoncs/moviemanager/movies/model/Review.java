package com.termoncs.moviemanager.movies.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.Instant;
import java.util.Date;

@Entity()
@Table(name="review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String comment;
    private Date created;

    @Min(1) @Max(5)
    private int rating;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    // configure join column to be non insertable/updateable as this is handled by the movieId column
    @JoinColumn(name = "movie_id", insertable = false, updatable = false)
    @JsonIgnore
    private Movie movie;

    // adding this column to support non JPA services
    @Column(name ="movie_id", nullable = false)
    @JsonIgnore
    private Long movieId;

    public Review() {
        setOn(Date.from(Instant.now()));
    }


    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getComment() {
        return comment;
    }
    public Date getCreated() {
        return created;
    }
    public int getRating() {
        return rating;
    }
    public Movie getMovie() {
        return movie;
    }
    public long getMovieId() { return this.movieId; }

    public Review setId(Long id) { this.id = id; return this; }
    public Review setName(String name) { this.name = name; return this; }
    public Review setComment(String comment) { this.comment = comment; return this; }
    public Review setOn(Date on) { this.created = on; return this; }
    public Review setRating(int rating) { this.rating = rating; return this; }
    public Review setMovie(Movie movie) { this.movie = movie; this.movieId = movie.getId(); return this; }
    public Review setMovieId(long movieId) { this.movieId = movieId; return this; }
}

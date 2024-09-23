package com.project.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.time.ZonedDateTime;
import java.util.Objects;
import com.project.enums.MovieGenre;
import com.project.enums.MpaaRating;
import com.project.util.ZonedDateTimeAdapter;

public class Movie implements Comparable<Movie> {
    private static int idCounter = 1;

    private Integer id;
    private String name;
    private Coordinates coordinates;
    private ZonedDateTime creationDate;
    private Integer oscarsCount;
    private MovieGenre genre;
    private MpaaRating mpaaRating;
    private Person screenwriter;

    public Movie(String name, Coordinates coordinates, Integer oscarsCount, MovieGenre genre, MpaaRating mpaaRating, Person screenwriter) {
        this.id = idCounter++;
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = ZonedDateTime.now();
        this.oscarsCount = oscarsCount;
        this.genre = genre;
        this.mpaaRating = mpaaRating;
        this.screenwriter = screenwriter;
    }

    public Integer getId() {
        return id;
    }

    public Integer getOscarsCount() {
        return oscarsCount;
    }

    public static Movie fromJson(String json) {
       Gson gson = new GsonBuilder()
        .registerTypeAdapter(ZonedDateTime.class, new ZonedDateTimeAdapter())
        .create();
        return gson.fromJson(json, Movie.class);
    }

    public String toJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(this);
    }

    @Override
    public int compareTo(Movie other) {
     return Integer.compare(this.id, other.id);
}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movie)) return false;
        Movie movie = (Movie) o;
        return Objects.equals(id, movie.id) && Objects.equals(name, movie.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", oscarsCount=" + oscarsCount +
                ", genre=" + genre +
                ", mpaaRating=" + mpaaRating +
                ", screenwriter=" + screenwriter +
                '}';
    }
}

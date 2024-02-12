package com.seb_main_034.SERVER.recommendation.algorithm;

import java.util.*;
import java.util.stream.Collectors;

import com.seb_main_034.SERVER.movie.entity.Movie;
import com.seb_main_034.SERVER.movie.repository.MovieRepository;
import com.seb_main_034.SERVER.users.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class MovieRecommendationAlgorithm {

    @Autowired
    private MovieRepository movieRepository;

    public List<Movie> recommendMoviesBasedOnUserGenre(Users user) {
        List<Movie> recommendedMovies = new ArrayList<>();
        Set<String> highRatedGenres = new HashSet<>();
        for (Movie movie : user.getRecommendedMovies()) {
            if (movie.getAverageRating() >= 4.0) {
                highRatedGenres.add(movie.getGenre());
            }
        }
        for (String genre : highRatedGenres) {
            recommendedMovies.addAll(movieRepository.findByGenre(genre));
        }
        return recommendedMovies;
    }

    public List<Movie> recommendTopRatedMovies() {
        List<Movie> highRatedMovies = movieRepository.findByAverageRatingGreaterThanEqual(4.0);
        return highRatedMovies.stream()
                .sorted(Comparator.comparingDouble(Movie::getAverageRating).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
}
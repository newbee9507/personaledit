package com.seb_main_034.SERVER.recommendation.service;

import com.seb_main_034.SERVER.movie.dto.MovieResponseDto;  // 이 부분을 추가
import com.seb_main_034.SERVER.movie.entity.Movie;
import com.seb_main_034.SERVER.movie.mapper.MovieMapper;
import com.seb_main_034.SERVER.recommendation.algorithm.MovieRecommendationAlgorithm;
import com.seb_main_034.SERVER.users.entity.Users;
import com.seb_main_034.SERVER.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {

    private final MovieRecommendationAlgorithm movieRecommendationAlgorithm;
    private final UserRepository userRepository;
    private final MovieMapper movieMapper;

    public List<MovieResponseDto> recommendMoviesBasedOnUserGenre(Long userId) {
        Users user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        List<Movie> recommendedMovies = movieRecommendationAlgorithm.recommendMoviesBasedOnUserGenre(user);
        return movieMapper.toResponseDtos(recommendedMovies);
    }

    public List<MovieResponseDto> recommendTopRatedMovies() {
        List<Movie> recommendedMovies = movieRecommendationAlgorithm.recommendTopRatedMovies();
        return movieMapper.toResponseDtos(recommendedMovies);
    }
}

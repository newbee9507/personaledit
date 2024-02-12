package com.seb_main_034.SERVER.moviesearch.service;

import com.seb_main_034.SERVER.moviesearch.dto.MovieSearchDTO;
import com.seb_main_034.SERVER.moviesearch.repository.MovieSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MovieSearchService {
    @Autowired
    private MovieSearchRepository movieSearchRepository;

    public List<String> searchMovies(MovieSearchDTO movieSearchDTO) {
        String keyword = movieSearchDTO.getKeyword(); // 그린
        String field = movieSearchDTO.getField();   // null

        // keyword가 빈 문자열이거나 null인 경우 빈 리스트 반환
        if (keyword == null || keyword.trim().isEmpty()) {
            return new ArrayList<>();
        } // 동작x

        if ("title".equals(field)) {
            return movieSearchRepository.searchMoviesByTitle(keyword); // 동작x
        } else if ("genre".equals(field)) {
            return movieSearchRepository.searchMoviesByGenre(keyword); // 동작x
        } else {
            // Fallback: Search both fields if 'field' is not specified or invalid.
            return movieSearchRepository.searchMoviesByField(keyword);
        }
    }
}


package com.seb_main_034.SERVER.moviesearch.controller;

import com.seb_main_034.SERVER.moviesearch.dto.MovieSearchDTO;
import com.seb_main_034.SERVER.moviesearch.service.MovieSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movie-search")
public class MovieSearchController {

    @Autowired
    private MovieSearchService movieSearchService;

    @PostMapping
    public List<String> searchMovies(@RequestBody MovieSearchDTO movieSearchDTO) {
        return movieSearchService.searchMovies(movieSearchDTO);
    }


}

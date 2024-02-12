package com.seb_main_034.SERVER.streaming.controller;

import com.seb_main_034.SERVER.movie.entity.Movie;
import com.seb_main_034.SERVER.movie.service.MovieService;
import com.seb_main_034.SERVER.streaming.service.StreamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/streaming")
public class StreamingController {

    @Autowired
    private StreamingService streamingService;

    @Autowired
    private MovieService movieService;  // MovieService 추가

    @GetMapping("/{movieId}")
    public ResponseEntity<Resource> streamVideo(@PathVariable Long movieId) {
        // movieId를 통해 DB에서 Movie 엔터티를 가져옴
        Optional<Movie> optionalMovie = movieService.getMovieById(movieId);

        if (!optionalMovie.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Movie movie = optionalMovie.get();

        String videoPath = movie.getStreamingURL();  // Movie 엔터티에서 streamingURL을 가져옴

        return streamingService.getVideoStream(videoPath);
    }
}


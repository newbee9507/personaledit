package com.seb_main_034.SERVER.rating.controller;

import com.seb_main_034.SERVER.movie.entity.Movie;
import com.seb_main_034.SERVER.movie.service.MovieService;
import com.seb_main_034.SERVER.rating.dto.RatingDTO;
import com.seb_main_034.SERVER.rating.entity.Rating;
import com.seb_main_034.SERVER.rating.service.RatingService;
import com.seb_main_034.SERVER.users.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.DoubleStream;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {
    @Autowired
    private RatingService ratingService;

    @PostMapping
    public void saveRating(@RequestBody RatingDTO ratingDTO,
                           @AuthenticationPrincipal Users user) {
        Long userId = user.getUserId();
        ratingService.saveRating(ratingDTO, userId);
    }
}


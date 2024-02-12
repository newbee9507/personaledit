package com.seb_main_034.SERVER.rating.service;

import com.seb_main_034.SERVER.comment.entity.Comment;
import com.seb_main_034.SERVER.comment.service.CommentService;
import com.seb_main_034.SERVER.movie.entity.Movie;
import com.seb_main_034.SERVER.movie.service.MovieService;
import com.seb_main_034.SERVER.rating.dto.RatingDTO;
import com.seb_main_034.SERVER.rating.entity.Rating;
import com.seb_main_034.SERVER.rating.mapper.RatingMapper;
import com.seb_main_034.SERVER.rating.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RatingService {
    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private MovieService movieService;  // MovieService를 주입

    public Rating saveRating(RatingDTO ratingDTO, Long userId) {
        Rating rating = new Rating();
        rating.setUserId(userId);
        rating.setMovieId(ratingDTO.getMovieId());
        rating.setRating(ratingDTO.getRating());

        Rating savedRating = ratingRepository.save(rating);

        // 평점을 저장한 후에 해당 영화의 평균 평점을 업데이트
        Movie movie = movieService.findMovie(ratingDTO.getMovieId());
        movie.updateAverageRating();
        movieService.save(movie);  // 업데이트된 평균 평점을 저장

        return savedRating;
    }
    public List<Rating> sendRating(Long movieId) {
        return ratingRepository.findByMovieId(movieId);
    }
}

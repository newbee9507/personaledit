package com.seb_main_034.SERVER.movie.service;

import com.amazonaws.services.mq.model.UnauthorizedException;
import com.seb_main_034.SERVER.movie.entity.Movie;
import com.seb_main_034.SERVER.movie.repository.MovieRepository;
import com.seb_main_034.SERVER.rating.entity.Rating;
import com.seb_main_034.SERVER.rating.repository.RatingRepository;
import com.seb_main_034.SERVER.users.entity.Users;
import com.seb_main_034.SERVER.users.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MovieService {
    private final MovieRepository movieRepository;
    private final UserService userService;
    private final RatingRepository ratingRepository;  // RatingRepository를 추가합니다.


    //영화 등록
    public Movie createMovie(Movie movie, Long userId) {
        movie.setUser(userService.findById(userId));
        return movieRepository.save(movie);
    }

    //영화 게시글 수정
    public Movie updateMovie(Movie movie, Long userId) {
        Movie findMovie = findMovie(movie.getMovieId());
        Long findMovieUserId = findMovie.getUser().getUserId();
        if (findMovieUserId.equals(userId)) {
            findMovie.setTitle(movie.getTitle());
            findMovie.setGenre(movie.getGenre());
            findMovie.setStreamingURL(movie.getStreamingURL());
            findMovie.setDescription(movie.getDescription());
            return movieRepository.save(findMovie);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,"이 영화를 수정할 권한이 없습니다.");
        }
    }
    //단일 영화 조회
    public Movie viewMovie(Long movieId) {
        return findMovie(movieId);
    }

    // 전체 글 조회
    public Page<Movie> findMovies(int page, int size) {
        movieRepository.count();
        return movieRepository.findAll(PageRequest.of(page, size,
                Sort.by(Sort.Direction.DESC, "MovieId").descending()));
    }

    // 평점 상위 10개 조회
    public Page<Movie> findTop10Movies() {
        return movieRepository.findAll(PageRequest.of(0, 10, Sort.by("rating").descending()));
    }

    // 영화 정보 조회
    public Movie findMovie(Long movieId) {
        Optional<Movie> optionalMovie = movieRepository.findById(movieId);
        return optionalMovie.orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "영화를 찾을 수 없습니다."));
    }


    //게시글 작성자에 따른 전체 글 조회(관리자에 의한 필요?)
    public Page<Movie> findUserMovies(int page, Long userId) {
        Pageable pageable = PageRequest.of(page, 10, Sort.by("movieId").descending());
        Page<Movie> findMovies = movieRepository.findByUserId(userId, pageable);

        return findMovies;
    }

    //쿼리문 검색을 위한 본문 조회
    private Movie findMovieByQuery(long movieId) {
        Optional<Movie> optionalMovie = movieRepository.findById(movieId);
        return optionalMovie.orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "영화를 찾을 수 없습니다."));
    }

    public void deleteMovie(long movieId, Long userId) {
        Movie findMovie = findMovie(movieId);
        Long findMovieUserId = findMovie.getUser().getUserId();
        if (findMovieUserId.equals(userId)){
            movieRepository.delete(findMovie);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "이 영화 정보를 삭제 할 권한이 없습니다.");
        }
    }

    // 10개의 영화 출력, 검색기능 구현
    public Page<Movie> findKeyWordMovies(String keyword, int page) {
        Pageable pageable = PageRequest.of(page-1, 10, Sort.by("movieId").descending());

        return movieRepository.findByKeyWordMovie(keyword, pageable);
    }

    public Optional<Movie> getMovieById(Long movieId) {
        return movieRepository.findById(movieId);
    }

    public void updateAverageRating(Long movieId) {
        // 평균 평점을 계산하는 로직
        List<Rating> ratings = ratingRepository.findByMovieId(movieId);  // 평점 리스트를 가져옵니다.
        double average = ratings.stream().mapToDouble(Rating::getRating).average().orElse(0.0);  // 평균을 계산합니다.

        Movie movie = findMovie(movieId);  // 영화 정보를 가져옵니다.
        movie.setAverageRating(average);  // 평균 평점을 movie 엔터티에 설정합니다.
        movieRepository.save(movie);  // movie 엔터티를 저장합니다.
    }
    public Movie save(Movie movie) {
        return movieRepository.save(movie);
    }


}

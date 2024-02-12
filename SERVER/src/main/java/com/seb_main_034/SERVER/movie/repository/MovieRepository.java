package com.seb_main_034.SERVER.movie.repository;

import com.seb_main_034.SERVER.movie.entity.Movie;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    //검색어를 통해 영화의 제목 및 설명(본문 내용) 부분을 키워드를 통해 검색하는 쿼리문으로 구성
    // 와일드카드 % 를 사용해 title, description 에 포함된 문자열중 일치하는 특정 부분을 찾음
    @Query("SELECT m FROM Movie m WHERE m.title LIKE %:keyWord% OR m.genre LIKE %:keyWord% OR m.description LIKE %:keyWord%")
    Page<Movie> findByKeyWordMovie(@Param("keyWord") String keyWord, Pageable pageable);

    @Query("SELECT m FROM Movie m WHERE m.user.userId = :userId")
    Page<Movie> findByUserId(@Param("userId") long userId, Pageable pageable);

    List<Movie> findByGenre(String genre);
    List<Movie> findByAverageRatingGreaterThanEqual(Double rating);
}
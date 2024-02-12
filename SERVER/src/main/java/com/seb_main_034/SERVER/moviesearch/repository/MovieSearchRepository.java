package com.seb_main_034.SERVER.moviesearch.repository;

import com.seb_main_034.SERVER.moviesearch.entity.MovieSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieSearchRepository extends JpaRepository<MovieSearch, Long> {
    @Query("SELECT m.title FROM Movie m WHERE m.title LIKE %:keyword% OR (m.genre LIKE %:keyword% AND m.genre IS NOT NULL AND m.genre != '')")
    List<String> searchMoviesByField(@Param("keyword") String keyword);

    @Query("SELECT m.title FROM Movie m WHERE m.title LIKE %:keyword%")
    List<String> searchMoviesByTitle(@Param("keyword") String keyword);

    @Query("SELECT m.title FROM Movie m WHERE (m.genre LIKE %:keyword% AND m.genre IS NOT NULL AND m.genre != '')")
    List<String> searchMoviesByGenre(@Param("keyword") String keyword);
}


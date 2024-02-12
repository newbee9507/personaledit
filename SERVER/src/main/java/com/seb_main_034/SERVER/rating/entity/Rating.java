package com.seb_main_034.SERVER.rating.entity;

import com.seb_main_034.SERVER.movie.entity.Movie;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long movieId;
    private double rating;  // Integer에서 double로 변경

    @ManyToOne
    @JoinColumn(name = "movieId", insertable = false, updatable = false)  // 옵션 추가
    private Movie movie;  // 새로운 필드 추가
}

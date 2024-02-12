package com.seb_main_034.SERVER.rating.dto;

import lombok.Data;

@Data
public class RatingDTO {
    private Long userId;
    private Long movieId;
    private double rating;  // int에서 double로 변경
}

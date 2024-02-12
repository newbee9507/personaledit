package com.seb_main_034.SERVER.movie.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MoviePostDto {

    private String title;

    private String genre;

    private String streamingURL;

    private String previewPicture;

    private String description;
}

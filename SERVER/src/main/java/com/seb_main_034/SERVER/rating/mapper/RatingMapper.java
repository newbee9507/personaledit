package com.seb_main_034.SERVER.rating.mapper;

import com.seb_main_034.SERVER.rating.dto.RatingDTO;
import com.seb_main_034.SERVER.rating.entity.Rating;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RatingMapper {

    List<RatingDTO> ratingListToRatingListDto(List<Rating> ratingList);
}

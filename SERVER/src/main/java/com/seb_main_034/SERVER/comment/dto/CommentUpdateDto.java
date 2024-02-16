package com.seb_main_034.SERVER.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommentUpdateDto {

    private Long commentId;

    @NotBlank
    private String text;
}

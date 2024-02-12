package com.seb_main_034.SERVER.comment.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CommentUpdateDto {

    private Long commentId;

    @NotBlank
    private String text;
}

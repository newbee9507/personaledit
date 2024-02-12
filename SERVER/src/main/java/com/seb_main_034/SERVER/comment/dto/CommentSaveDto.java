package com.seb_main_034.SERVER.comment.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class CommentSaveDto {

    @NotBlank
    private String text;

    public CommentSaveDto(String text) {
        this.text = text;
    }

    public CommentSaveDto() {
    }
}

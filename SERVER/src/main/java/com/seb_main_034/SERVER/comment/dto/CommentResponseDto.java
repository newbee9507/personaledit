package com.seb_main_034.SERVER.comment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CommentResponseDto {

    private Long commentId;

    private String text;

    private String nickName;

    private LocalDateTime createAt;

    private LocalDateTime modifyAt;
}

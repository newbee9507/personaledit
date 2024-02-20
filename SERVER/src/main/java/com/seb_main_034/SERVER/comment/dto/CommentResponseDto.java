package com.seb_main_034.SERVER.comment.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Getter
@Setter
public class CommentResponseDto {

    private Long commentId;

    private String text;

    private String nickName;

    private OffsetDateTime createAt;

    private OffsetDateTime modifyAt;
}

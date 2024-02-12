package com.seb_main_034.SERVER.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CommentListResponseDto<T>{
    
    private T allCommentList;
}

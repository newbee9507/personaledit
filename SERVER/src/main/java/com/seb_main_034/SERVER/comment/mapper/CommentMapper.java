package com.seb_main_034.SERVER.comment.mapper;

import com.seb_main_034.SERVER.comment.dto.CommentResponseDto;
import com.seb_main_034.SERVER.comment.dto.CommentSaveDto;
import com.seb_main_034.SERVER.comment.dto.CommentUpdateDto;
import com.seb_main_034.SERVER.comment.entity.Comment;
import com.seb_main_034.SERVER.users.entity.Users;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "user", target = "user")
    @Mapping(source = "user.nickName", target = "nickName")
    Comment saveDtoToComment(CommentSaveDto saveDto, Users user);

    Comment updateDtoToComment(CommentUpdateDto updateDto, Users user);

    CommentResponseDto commentToResponseDto(Comment comment);

    List<CommentResponseDto> commentListToResponseListDto(List<Comment> commentList);

//    Comment saveDtoToComment(CommentSaveDto saveDto);
//
//    Comment updateDtoToComment(CommentUpdateDto updateDto);
//
//    CommentResponseDto commentToResponseDto(Comment comment);

}

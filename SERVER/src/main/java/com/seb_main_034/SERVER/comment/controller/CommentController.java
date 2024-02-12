package com.seb_main_034.SERVER.comment.controller;

import com.seb_main_034.SERVER.comment.dto.CommentResponseDto;
import com.seb_main_034.SERVER.comment.dto.CommentSaveDto;
import com.seb_main_034.SERVER.comment.dto.CommentUpdateDto;
import com.seb_main_034.SERVER.comment.entity.Comment;
import com.seb_main_034.SERVER.comment.mapper.CommentMapper;
import com.seb_main_034.SERVER.comment.service.CommentService;
import com.seb_main_034.SERVER.exception.ExceptionCode;
import com.seb_main_034.SERVER.exception.GlobalException;
import com.seb_main_034.SERVER.movie.entity.Movie;
import com.seb_main_034.SERVER.movie.service.MovieService;
import com.seb_main_034.SERVER.users.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comment/{movieId}") // 댓글은 영화에 포함되기 때문에 이렇게 작성.
public class CommentController {

    private final CommentService service;
    private final CommentMapper mapper;
    private final MovieService movieService;

//    @GetMapping
//    public Comment getComment() { // 필요한 기능인지는 의문. 하나의 질문만을 찾을 이유가 있을까..?
//        service.getComment()
//    }

//    @GetMapping("/all") // 해당 Id를 가진 영화에 한정해서, 등록된 모든 댓글을 가져옴. 그런데 이거보다 영화컨트롤러에서 함께 반환하는게 맞지 않을까?
//    public ResponseEntity getComment(@PathVariable Long movieId) {
//        CommentListResponseDto<List<CommentResponseDto>> listCommentListResponseDto =
//                new CommentListResponseDto<>(mapper.commentListToResponseListDto(service.getAllComment(movieId)));
//
//        return new ResponseEntity(listCommentListResponseDto, HttpStatus.OK);
//    }

    @PostMapping("/add")
    public ResponseEntity createComment(@PathVariable Long movieId, // 이 댓글이 어떤 영화에 쓰여진 댓글인지를 알아야하지 않을까?
                                        @Valid @RequestBody CommentSaveDto saveDto,
                                        @AuthenticationPrincipal Users user // 어떤 유저가 이 댓글을 달았는지 설정하기 위해. 자동으로 현재 로그인한 회원의 정보를 가져온다고 함.
                                        ) {
        checkExistComment(movieId, user);
        Comment savedComment = service.saveComment(mapper.saveDtoToComment(saveDto, user), movieId);
        CommentResponseDto responseDto = mapper.commentToResponseDto(savedComment);

        responseDto.setNickName(savedComment.getUser().getNickName()); // 댓글창에는 유저의 닉네임을 표시함.

        return new ResponseEntity<>(responseDto, HttpStatus.CREATED);
    }

    @PatchMapping("/update/{commentId}")
    public ResponseEntity updateComment(@PathVariable Long movieId,
                                        @PathVariable Long commentId,
                                        @Valid @RequestBody CommentUpdateDto updateDto,
                                        @AuthenticationPrincipal Users user
                                        ) {
        Comment comment = mapper.updateDtoToComment(updateDto, user);
        Comment updatedComment = service.update(comment, user, commentId);

        CommentResponseDto responseDto = mapper.commentToResponseDto(updatedComment);

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity deleteComment(@PathVariable Long movieId,
                                        @AuthenticationPrincipal Users users,
                                        @PathVariable Long commentId) {

        service.delete(commentId, users);
        return new ResponseEntity<>("삭제완료", HttpStatus.OK);
    }

    private void checkExistComment(Long movieId, Users user) {
        Movie movie = movieService.findMovie(movieId);
        List<Comment> commentList = movie.getCommentList();

        List<Long> collect = commentList.stream()
                                        .map(comment -> comment.getUser().getUserId())
                                        .collect(Collectors.toList());

        if (collect.contains(user.getUserId())) {
            throw new GlobalException(ExceptionCode.COMMENT_EXIST);
        }

    }

}

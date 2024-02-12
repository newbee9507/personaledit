package com.seb_main_034.SERVER.comment.service;

import com.seb_main_034.SERVER.comment.entity.Comment;
import com.seb_main_034.SERVER.comment.repository.CommentRepository;
import com.seb_main_034.SERVER.exception.ExceptionCode;
import com.seb_main_034.SERVER.exception.GlobalException;
import com.seb_main_034.SERVER.movie.entity.Movie;
import com.seb_main_034.SERVER.movie.service.MovieService;
import com.seb_main_034.SERVER.users.entity.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository repository;
    private final MovieService movieService;

    @Transactional(readOnly = true)
    public List<Comment> getAllComment(Long movieId) {
        Movie movie = movieService.findMovie(movieId); // 수정된 부분
        return movie.getCommentList();  // 해당 영화의 모든 댓글들을 가져옴
    }

    public Comment saveComment(Comment comment, Long movieId) {
        Movie findMovie = movieService.findMovie(movieId); // 수정된 부분
        comment.setMovie(findMovie);
        Movie movie = comment.getMovie();
        Users user = comment.getUser();

        user.getCommentList().add(comment);
        movie.getCommentList().add(comment);
        log.info("영화ID = {} , 작성자ID = {}", movie.getMovieId(), user.getUserId());
        return repository.save(comment);
    }

    public Comment update(Comment comment, Users user, Long commentId) {
        Comment wantUpdateComment = findById(commentId);
        log.info("수정요청한 댓글ID = {}", wantUpdateComment.getCommentId());

        if (verifyUser(wantUpdateComment, user)) {
            wantUpdateComment.setText(comment.getText());
            return repository.save(wantUpdateComment);
        }
        throw new GlobalException(ExceptionCode.FORBIDDEN);
    }

    public void delete(Long commentId, Users user) {
        Comment findComment = findById(commentId);
        if (isAdmin(user) || verifyUser(findComment, user)) {
            repository.delete(findComment);
            return;
        }
        throw new GlobalException(ExceptionCode.FORBIDDEN);

    }

    @Transactional(readOnly = true)
    public Comment findById(Long commentId) {
        return repository.findById(commentId).orElseThrow(
                () -> new GlobalException(ExceptionCode.COMMENT_NOT_FOUND));
    }

    /**
     * 본인이 작성한 댓글만을 수정 및 삭제할 수 있음. 관리자는 검증없이 가능.
     * 현재 페이지의 영화ID와, 원래 댓글의 영화ID가 달라도 수정할 수 없음..? ( 페이지에 안 보이게 하는게 기본 )
     */
    private boolean verifyUser(Comment comment, Users user) {
        log.info("본인의 댓글인지 검증");

        Long writerId = comment.getUser().getUserId();
        Long requestId = user.getUserId();

        return writerId.equals(requestId);
    }

    private boolean isAdmin(Users user) {
        return user.getRoles().contains("ADMIN");
    }
}

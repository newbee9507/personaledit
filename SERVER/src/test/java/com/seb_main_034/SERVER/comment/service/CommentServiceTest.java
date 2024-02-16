package com.seb_main_034.SERVER.comment.service;

import com.seb_main_034.SERVER.comment.entity.Comment;
import com.seb_main_034.SERVER.comment.repository.CommentRepository;
import com.seb_main_034.SERVER.exception.GlobalException;
import com.seb_main_034.SERVER.movie.entity.Movie;
import com.seb_main_034.SERVER.movie.repository.MovieRepository;
import com.seb_main_034.SERVER.movie.service.MovieService;
import com.seb_main_034.SERVER.rating.entity.Rating;
import com.seb_main_034.SERVER.users.entity.Users;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository repository;

    @Mock
    private MovieService movieService;

    @InjectMocks
    private CommentService commentService;

    private static final List<Object> list = new ArrayList<>();

    static final Users admin = new Users(1L, "admin@gmail.com", "adminPw123!", "admin",
            "noPicture", List.of("ADMIN","USER"), new ArrayList<>(), new ArrayList<>());

    static final Users user = new Users(10L, "user@gmail.com", "userPw123!", "user",
            "noPicture", List.of("USER"), new ArrayList<>(), new ArrayList<>());

    @BeforeAll
    static void setting() {
        Movie movie = new Movie();
        movie.setMovieId(1L);
        movie.setCommentList(new ArrayList<>());
        list.add(movie);

        list.add(new Comment(1L, "댓글", "닉네임",
                LocalDateTime.now(), LocalDateTime.now(), admin, movie, new Rating()));

        list.add(new Comment(10L, "업데이트댓글", "닉네임",
                LocalDateTime.now(), LocalDateTime.now(), user, movie, new Rating()));
    }

    @Test
    @DisplayName("모든 댓글목록 가져오기")
    void getAllComment() {
        //given
        Movie movie = (Movie) list.get(0);
        movie.setCommentList(List.of(new Comment(), new Comment()));

        given(movieService.findMovie(Mockito.anyLong())).willReturn(movie);

        //when
        List<Comment> commentList = commentService.getAllComment(1L);

        //then
        assertThat(commentList.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("댓글저장")
    void saveComment() {
        //given
        Movie movie = (Movie) list.get(0);
        Comment comment = (Comment) list.get(1);
        Users user = comment.getUser();

        given(movieService.findMovie(Mockito.anyLong())).willReturn(movie);
        given(repository.save(Mockito.any(Comment.class))).willReturn(comment);

        //when
        Comment result = commentService.saveComment(comment, 1L);

        //then
        assertThat(result).isEqualTo(comment);
        assertThat(user.getCommentList().size()).isNotEqualTo(0);
        assertThat(movie.getCommentList().size()).isNotEqualTo(0);
    }

    @Test
    @DisplayName("댓글수정")
    void update() {
        //given
        Comment beforeUpdateComment = (Comment) list.get(1);
        Comment afterUpdateComment = (Comment) list.get(2);

        given(repository.findById(Mockito.anyLong())).willReturn(Optional.of(beforeUpdateComment));
        given(repository.save(Mockito.any(Comment.class))).willReturn(afterUpdateComment);

        //when
        Comment result = commentService.update(afterUpdateComment, admin, beforeUpdateComment.getCommentId());

        //then
        assertThat(result).isEqualTo(afterUpdateComment);
    }

    @Test
    void delete() {
        //given
        Comment adminComment = (Comment) list.get(1);
        Comment userComment = (Comment) list.get(2);

        given(repository.findById(1L)).willReturn(Optional.of(adminComment));
        given(repository.findById(10L)).willReturn(Optional.of(userComment));
        doNothing().when(repository).delete(Mockito.any(Comment.class));

        //when
        commentService.delete(1L, admin);
        commentService.delete(10L, user);

        //then
        verify(repository, times(1)).delete(adminComment);
        verify(repository, times(1)).delete(userComment);
        assertThatThrownBy(() -> commentService.delete(1L, user))
                .isInstanceOf(GlobalException.class).hasMessage("접근 권한이 없습니다");
    }
}
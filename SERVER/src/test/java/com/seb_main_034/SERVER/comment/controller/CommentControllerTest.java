package com.seb_main_034.SERVER.comment.controller;

import com.google.gson.Gson;
import com.seb_main_034.SERVER.comment.dto.CommentUpdateDto;
import com.seb_main_034.SERVER.forTestUtils.ForMockTestCustomUser;
import com.seb_main_034.SERVER.comment.dto.CommentSaveDto;
import com.seb_main_034.SERVER.comment.entity.Comment;
import com.seb_main_034.SERVER.comment.mapper.CommentMapper;
import com.seb_main_034.SERVER.comment.service.CommentService;
import com.seb_main_034.SERVER.movie.entity.Movie;
import com.seb_main_034.SERVER.movie.service.MovieService;
import com.seb_main_034.SERVER.rating.entity.Rating;
import com.seb_main_034.SERVER.users.entity.Users;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private CommentService service;

    @MockBean
    private MovieService movieService;

    @SpyBean
    private CommentMapper mapper;

    private static ArrayList<Comment> tmp = new ArrayList<>();

    @BeforeAll
    static void setting() {
        tmp.add(new Comment(1L, "adminText", "admin", LocalDateTime.now(),
                LocalDateTime.now(), createUser("admin@gmail.com"), new Movie(), new Rating()));
        tmp.add(new Comment(2L, "userText1", "user1", LocalDateTime.now(),
                LocalDateTime.now(), createUser("user@gmail.com"), new Movie(), new Rating()));
        tmp.add(new Comment(2L, "updateText", "user1", tmp.get(1).getCreateAt(),
                LocalDateTime.now(), tmp.get(1).getUser(), tmp.get(1).getMovie(), tmp.get(1).getRating()));
    }

    @Test
    @ForMockTestCustomUser()
    @DisplayName("댓글등록")
    void createComment() throws Exception {
        //given
        Comment adminComment = tmp.get(0);

        given(movieService.findMovie(Mockito.anyLong())).willReturn(new Movie());

        given(mapper.saveDtoToComment(Mockito.any(CommentSaveDto.class), Mockito.any(Users.class)))
                .willReturn(new Comment());

        given(service.saveComment(Mockito.any(Comment.class), Mockito.anyLong()))
                .willReturn(tmp.get(0));

        //when
        ResultActions saveComment = mockMvc.perform(
                post("/api/comment/1/add")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(new CommentSaveDto("adminText")))
        );

        //then
        saveComment.andExpect(status().isCreated())
                .andExpect(jsonPath("$.commentId").value(adminComment.getCommentId()))
                .andExpect(jsonPath("$.text").value(adminComment.getText()));
    }

    @Test
    @ForMockTestCustomUser(email = "user@gmail.com")
    @DisplayName("댓글수정")
    void updateComment() throws Exception {
        //given
        Comment beforeUpdateComment = tmp.get(1);
        Comment afterUpdateComment = tmp.get(2);
        CommentUpdateDto updateDto = new CommentUpdateDto(2L, afterUpdateComment.getText());

        given(mapper.updateDtoToComment( any(CommentUpdateDto.class), any(Users.class) )).willReturn(beforeUpdateComment);
        given(service.findById(anyLong())).willReturn(beforeUpdateComment);
        given(service.update(any(Comment.class), any(Users.class), anyLong())).willReturn(afterUpdateComment);

        //when
        ResultActions updateActions = mockMvc.perform(
                patch("/api/comment/1/update/2")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(updateDto))
        );

        //then
        updateActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.commentId").value(beforeUpdateComment.getCommentId()))
                .andExpect(jsonPath("$.text").value(updateDto.getText()));

    }

    @Test
    @ForMockTestCustomUser()
    @DisplayName("댓글삭제")
    void deleteComment() throws Exception {
        //given
        Users admin = createUser("admin@gmail.com");
        admin.setRoles(List.of("ADMIN", "USER"));
        Comment adminComment = tmp.get(0);

        given(service.findById(1L)).willReturn(adminComment);
        doNothing().when(service).delete(Mockito.anyLong(), Mockito.any(Users.class));

        //when
        ResultActions deleteActions = mockMvc.perform(
                delete("/api/comment/1/delete/1"));

        //then
        deleteActions.andExpect(status().isOk())
                .andExpect(content().string("삭제완료"));
    }

    private static Users createUser(String email) {
        if(email.equals("admin@gmail.com")) {
            Users admin = new Users(email, "adminPw123!", "admin", "adminPicture");
            admin.setUserId(1L);
            return admin;
        }

        Users user = new Users("user@gmail.com", "userPw123!", "user", "userPicture");
        user.setUserId(2L);
        return user;
    }
}
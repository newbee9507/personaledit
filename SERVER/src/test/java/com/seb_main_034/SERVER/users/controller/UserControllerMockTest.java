package com.seb_main_034.SERVER.users.controller;

import com.google.gson.Gson;
import com.seb_main_034.SERVER.auth.utils.UsersAuthorityUtils;
import com.seb_main_034.SERVER.forTestUtils.ForMockTestCustomUser;
import com.seb_main_034.SERVER.forTestUtils.ForTestUserDetailsService;
import com.seb_main_034.SERVER.users.dto.PasswordDto;
import com.seb_main_034.SERVER.users.dto.UserPatchDto;
import com.seb_main_034.SERVER.users.dto.UserSaveDto;
import com.seb_main_034.SERVER.users.entity.Users;
import com.seb_main_034.SERVER.users.mapper.UserMapper;
import com.seb_main_034.SERVER.users.mapper.UserMapperImpl;
import com.seb_main_034.SERVER.users.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@WebMvcTest(controllers = {UserController.class})
@Import({UserMapperImpl.class, UsersAuthorityUtils.class, ForTestUserDetailsService.class})
class UserControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    @MockBean
    private UserService userService;

    @Autowired
    private UserMapper mapper;

    private static ArrayList<UserSaveDto> dtos = new ArrayList<>();

    /**
     * 순서대로 관리자  -  회원  -  비밀번호 에러  -  닉네임 에러 테스트를 위한 dto
     */
    @BeforeAll
    static void setDtos() {
        dtos.add(new UserSaveDto("admin@gmail.com", "adminPw123", "admin", "adminPicture"));
        dtos.add(new UserSaveDto("test@gmail.com", "123456789a", "회원", ""));
        dtos.add(new UserSaveDto("test@gmail.com", "pwError", "일반유저", ""));
        dtos.add(new UserSaveDto("test2@gmail.com", "123456789a", "1", ""));
    }

    @Test
    @ForMockTestCustomUser
    @DisplayName("유저정보 조회")
    void info() throws Exception {
        //given
        UserSaveDto adminDto = dtos.get(0);
        Users admin = createUser(adminDto);
        given(userService.findById(Mockito.anyLong())).willReturn(admin);

        //when
        ResultActions getAdminInfoAction = mockMvc.perform(
                get("/api/users/info/1").with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        getAdminInfoAction.andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(admin.getUserId()))
                .andExpect(jsonPath("$.email").value(admin.getEmail()))
                .andExpect(jsonPath("$.nickName").value(admin.getNickName()))
                .andExpect(jsonPath("$.proFilePicture").value(admin.getProFilePicture()));
    }

    @Test
    @ForMockTestCustomUser
    @DisplayName("모든 유저정보 조회")
    void allUsers() throws Exception {
        //given
        UserSaveDto adminDto = dtos.get(0);
        UserSaveDto userDto = dtos.get(1);

        Users admin = createUser(adminDto);
        Users user = createUser(userDto);
        user.setUserId(2L);

        List<Users> usersList = List.of(admin, user);

        given(userService.findAllUsers()).willReturn(usersList);

        //when
        ResultActions getAllInfoAction = mockMvc.perform(
                get("/api/users/info/all")
                        .accept(MediaType.APPLICATION_JSON)
        );

        //then
        getAllInfoAction.andExpect(status().isOk())
                .andExpect(jsonPath("$.allUserList[0].userId").value(admin.getUserId()))
                .andExpect(jsonPath("$.allUserList[0].email").value(admin.getEmail()))
                .andExpect(jsonPath("$.allUserList[0].nickName").value(admin.getNickName()))
                .andExpect(jsonPath("$.allUserList[0].proFilePicture").value(admin.getProFilePicture()))

                .andExpect(jsonPath("$.allUserList[1].userId").value(user.getUserId()))
                .andExpect(jsonPath("$.allUserList[1].email").value(user.getEmail()))
                .andExpect(jsonPath("$.allUserList[1].nickName").value(user.getNickName()))
                .andExpect(jsonPath("$.allUserList[1].proFilePicture").value(user.getProFilePicture()));
    }

    @Test
    @WithMockUser
    @DisplayName("회원가입 - 성공,에러 모두 테스트")
    void signUp() throws Exception {
        //given
        UserSaveDto adminDto = dtos.get(0);
        UserSaveDto passwordErrorDto = dtos.get(2);
        UserSaveDto nickNameErrorDto = dtos.get(3);

        Users admin = createUser(adminDto);

        Users passwordError = createUser(adminDto);
        passwordError.setUserId(2L);

        Users nickNameError = createUser(adminDto);
        nickNameError.setUserId(3L);

        given(userService.save(Mockito.any(Users.class))).willReturn(admin);

        //when
        ResultActions adminSaveAction = mockMvc.perform(
                post("/api/users/register").with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(adminDto))
        );

        ResultActions passwordErrorAction = mockMvc.perform(
                post("/api/users/register").with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(passwordErrorDto))
        );

        ResultActions nickNameErrorAction = mockMvc.perform(
                post("/api/users/register").with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(nickNameErrorDto))
        );

        //then
        adminSaveAction.andExpect(status().isCreated())
                .andExpect(jsonPath("$.userId").value(admin.getUserId()))
                .andExpect(jsonPath("$.email").value(admin.getEmail()))
                .andExpect(jsonPath("$.nickName").value(admin.getNickName()))
                .andExpect(jsonPath("$.proFilePicture").value(admin.getProFilePicture()));

        passwordErrorAction.andExpect(status().isBadRequest());
        nickNameErrorAction.andExpect(status().isBadRequest());
    }

    @Test
    @ForMockTestCustomUser()
    @DisplayName("유저정보 수정")
    void update() throws Exception {
        //given
        UserPatchDto patchDto = new UserPatchDto("업데이트","업데이트");
        Users updatedAdmin = createUser(new UserSaveDto
                ("admin@gamil.com", "123456789a", "업데이트", "업데이트"));

        given(userService.update(Mockito.anyLong(), Mockito.any(UserPatchDto.class))).willReturn(updatedAdmin);
        String updateJson = gson.toJson(patchDto);

        //when
        ResultActions updateAction = mockMvc.perform(
                                    patch("/api/users/update/1").with(csrf())
                                    .accept(MediaType.APPLICATION_JSON)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(updateJson)
        );

        //then
        updateAction.andExpect(status().isOk())
                .andExpect(jsonPath("$.nickName").value(patchDto.getNickName()))
                .andExpect(jsonPath("$.proFilePicture").value(patchDto.getProFilePicture()));

    }

    @Test
    @ForMockTestCustomUser
    @DisplayName("비밀번호 변경 - 성공,에러 모두 테스트")
    void updatePassword() throws Exception {
        //given
        PasswordDto successDto = new PasswordDto("asdfasdf123!");
        PasswordDto failDto = new PasswordDto("fail");

        String successJson = gson.toJson(successDto);
        String failJson = gson.toJson(failDto);

        doNothing().when(userService).changePw(Mockito.anyLong(),Mockito.any(PasswordDto.class));

        //when
        ResultActions successActions = mockMvc.perform(
                patch("/api/users/password/1").with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(successJson));

        ResultActions failActions = mockMvc.perform(
                patch("/api/users/password/1").with(csrf())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(failJson)
        );

        //then
        successActions.andExpect(status().isOk())
                .andExpect((content().string("변경 완료")));

        failActions.andExpect(status().isBadRequest());

    }

    @Test
    @ForMockTestCustomUser
    @DisplayName("회원탈퇴")
    void deleteUser() throws Exception {
        //given
        Users user = createUser(dtos.get(1));
        user.setRoles(List.of("USER"));

        given(userService.findById(Mockito.anyLong())).willReturn(user);
        doNothing().when(userService).delete(any(Users.class));

        //when
        ResultActions deleteActions = mockMvc.perform(
                delete("/api/users/delete/1").with(csrf())
        );

        //then
        deleteActions.andExpect(status().isOk())
                .andExpect(content().string("삭제 완료"));
        verify(userService, times(1)).delete(user);
    }

    private Users createUser(UserSaveDto saveDto) {

        Users user = mapper.UserSaveDTOtoUser(saveDto);
        user.setUserId(1L);

        return user;
    }

}
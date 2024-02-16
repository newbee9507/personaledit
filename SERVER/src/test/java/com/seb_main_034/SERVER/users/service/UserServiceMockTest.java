package com.seb_main_034.SERVER.users.service;

import com.seb_main_034.SERVER.auth.utils.UsersAuthorityUtils;
import com.seb_main_034.SERVER.configuration.PasswordConfig;
import com.seb_main_034.SERVER.exception.ExceptionCode;
import com.seb_main_034.SERVER.exception.GlobalException;
import com.seb_main_034.SERVER.users.dto.PasswordDto;
import com.seb_main_034.SERVER.users.dto.UserPatchDto;
import com.seb_main_034.SERVER.users.entity.Users;
import com.seb_main_034.SERVER.users.mapper.PatchMapperImp;
import com.seb_main_034.SERVER.users.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@Import({PasswordConfig.class, PatchMapperImp.class})
class UserServiceMockTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder encoder;

    @Spy
    private PatchMapperImp patchMapper;

    @Spy
    private UsersAuthorityUtils authorityUtils = new UsersAuthorityUtils("admin@gmail.com");

    @InjectMocks
    private UserService userService;

    private final static ArrayList<Users> userList = new ArrayList<>();

    @BeforeAll
    static void setting() {
        userList.add(new Users("admin@gmail.com", "asdfasdf123!", "운영자", "사진없음"));
        userList.add(new Users("user@gmail.com", "qwerqwer123!", "회원", "사진있음"));
        userList.add(new Users("user2@gmail.com", "qwerqwer123!", "업데이트", "테스트"));
    }

    @Test
    @DisplayName("회원가입")
    void save() {
        //given
        Users data = userList.get(0);

        Users admin = settingUser(1L, new Users(), data);

        given(userRepository.save(Mockito.any(Users.class))).willReturn(admin);
        given(encoder.encode(Mockito.anyString())).willReturn("encodedPw123!");

        //when
        Users result = userService.save(admin);

        //then
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo(data.getEmail());
        assertThat(result.getPassword()).isEqualTo("encodedPw123!");
        assertThat(result.getNickName()).isEqualTo(data.getNickName());
        assertThat(result.getProFilePicture()).isEqualTo(data.getProFilePicture());
        assertThat(result.getRoles().size()).isEqualTo(2);

    }

    @Test
    @DisplayName("회원정보 수정")
    void updateTest() {
        //given
        UserPatchDto patchDto = new UserPatchDto("업데이트", "테스트");

        Users beforeUpdateUser = settingUser(1L,new Users(), userList.get(1));
        Users afterUpdateUser = settingUser(1L,new Users(), userList.get(2));

        given(userRepository.findById(Mockito.anyLong())).willReturn(Optional.of(beforeUpdateUser));
        given(userRepository.save(Mockito.any(Users.class))).willReturn(afterUpdateUser);

        //when
        Users result = userService.update(1L, patchDto);

        //then
        assertThat(result.getUserId()).isEqualTo(beforeUpdateUser.getUserId());
        assertThat(result.getNickName()).isEqualTo(patchDto.getNickName());
        assertThat(result.getProFilePicture()).isEqualTo(patchDto.getProFilePicture());
        assertThat(result.getRoles()).isEqualTo(beforeUpdateUser.getRoles());
    }

    @Test
    @DisplayName("비밀번호 변경")
    void changePw() {
        //given
        PasswordDto pwDto = new PasswordDto("changePw1234!");
        Users forChangeData = userList.get(1);
        Users beforeUpdatePw = settingUser(1L, new Users(), forChangeData);

        Users afterUpdatePw = settingUser(1L, new Users(), forChangeData);
        afterUpdatePw.setPassword(pwDto.getPassword());

        given(userRepository.findById(Mockito.anyLong())).willReturn(Optional.of(beforeUpdatePw));
        given(encoder.encode(Mockito.anyString())).willReturn(pwDto.getPassword());
        given(userRepository.save(Mockito.any(Users.class))).willReturn(afterUpdatePw);

        //when
        userService.changePw(1L, pwDto);

        //then
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(beforeUpdatePw);
    }

    @Test
    @DisplayName("회원탈퇴")
    void delete() {
        //given
        Users user1 = userList.get(1);
        Users user2 = userList.get(2);

        doNothing().when(userRepository).delete(Mockito.any(Users.class));

        //when
        userService.delete(user1);
        userService.delete(user2);
        userService.delete(user2);

        //then
        verify(userRepository, times(1)).delete(user1);
        verify(userRepository, times(2)).delete(user2);
    }

    @Test
    @DisplayName("중복검사")
    void checkEmail() {
        //given
        String email = "emailError@gmail.com";
        String nickName = "nicknameError";
        given(userRepository.findByEmail(email))
                .willThrow(new GlobalException(ExceptionCode.USER_EXISTS));
        given(userRepository.findBynickName(nickName))
                .willThrow(new GlobalException(ExceptionCode.NICKNAME_EXISTS));

        //when, then
        assertThatThrownBy(() -> userService.checkEmail(email))
                .isInstanceOf(GlobalException.class)
                .hasMessage("이미 존재하는 유저입니다");

        assertThatThrownBy(() -> userService.checkNickname(nickName))
                .isInstanceOf(GlobalException.class)
                .hasMessage("이미 존재하는 닉네임입니다");
    }

    @Test
    @DisplayName("유저목록에서 관리자삭제")
    void deleteAdmin() {
        //given
        Users adminData = userList.get(0);
        Users userData = userList.get(1);

        Users admin = settingUser(1L, new Users(), adminData);
        Users user = settingUser(2L, new Users(), userData);

        List<Users> allUsersList = List.of(admin, user);

        //when
        List<Users> resultList = userService.deleteAdmin(allUsersList);

        //then
        assertThat(resultList.size()).isEqualTo(1);
        assertThat(resultList.contains(admin)).isFalse();
    }

    @Test
    void findById() {
        //given
        Users data = userList.get(0);
        Users admin = settingUser(1L, new Users(), data);

        given(userRepository.findById(1L)).willReturn(Optional.of(admin));

        //when
        Users result = userService.findById(1L);

        //then
        assertThat(result).isEqualTo(admin);
        assertThatThrownBy(() -> userService.findById(2L))
                .isInstanceOf(GlobalException.class)
                .hasMessage("회원을 찾을 수 없습니다");
    }

    @Test
    @DisplayName("이메일로 찾기")
    void findByEmail() {
        //given
        Users data = userList.get(0);
        Users admin = settingUser(1L, new Users(), data);

        given(userRepository.findByEmail("admin@gmail.com")).willReturn(Optional.of(admin));

        //when
        Users result = userService.findByEmail("admin@gmail.com");

        //then
        assertThat(result).isEqualTo(admin);
        assertThatThrownBy(() -> userService.findByEmail("exception@gmail.com"))
                .isInstanceOf(GlobalException.class)
                .hasMessage("회원을 찾을 수 없습니다");
    }

    private Users settingUser(Long userId, Users initial, Users data) {
        initial.setUserId(userId);
        initial.setEmail(data.getEmail());
        initial.setPassword(data.getPassword());
        initial.setNickName(data.getNickName());
        initial.setProFilePicture(data.getProFilePicture());
        initial.setRoles(authorityUtils.createRoles(initial.getEmail()));

        return initial;
    }
}
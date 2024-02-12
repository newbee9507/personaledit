package com.seb_main_034.SERVER.users.service;

import com.seb_main_034.SERVER.auth.utils.UsersAuthorityUtils;
import com.seb_main_034.SERVER.exception.ExceptionCode;
import com.seb_main_034.SERVER.exception.GlobalException;
import com.seb_main_034.SERVER.users.dto.PasswordDto;
import com.seb_main_034.SERVER.users.dto.UserPatchDto;
import com.seb_main_034.SERVER.users.entity.Users;
import com.seb_main_034.SERVER.users.mapper.PatchMapper;
import com.seb_main_034.SERVER.users.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@Slf4j
//@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceMockTest {

    @MockBean
    private UserRepository userRepository;

    @SpyBean
    private UserService userService;

    @Autowired
    private PatchMapper patchMapper;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private UsersAuthorityUtils authorityUtils;

    private final static ArrayList<Users> userList = new ArrayList<>();

    @BeforeAll
    static void readyForTest() {
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

        //when
        Users result = userRepository.save(admin);

        //then
        assertThat(result.getUserId()).isEqualTo(1L);
        assertThat(result.getNickName()).isEqualTo(data.getNickName());
        assertThat(result.getProFilePicture()).isEqualTo(data.getProFilePicture());
        assertThat(result.getRoles().size()).isEqualTo(2);

        assertThat(result.getPassword()).isNotEqualTo(data.getPassword());
    }

    @Test
    @DisplayName("회원정보 수정")
    void updateTest() {
        //given
        UserPatchDto patchDto = new UserPatchDto("업데이트", "테스트");

        Users beforeUpdateUser = settingUser(1L,new Users(), userList.get(1));
        Users afterUpdateUser = settingUser(1L,new Users(), userList.get(2));

        given(userRepository.save(Mockito.any(Users.class))).willReturn(afterUpdateUser);

        //when
        Users result = userRepository.save(patchMapper.UserPatchDTOtoUser(beforeUpdateUser, patchDto));

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

        given(userRepository.save(Mockito.any(Users.class))).willReturn(afterUpdatePw);

        //when
        Users result = userRepository.save(beforeUpdatePw);

        //then
        assertThat(result.getPassword()).isEqualTo(pwDto.getPassword());
        assertThat(result.getPassword()).isNotEqualTo(beforeUpdatePw.getPassword());
    }

    @Test
    @DisplayName("회원탈퇴")
    void delete() {
        //given
        Users user1 = userList.get(1);
        Users user2 = userList.get(2);

        doNothing().when(userRepository).delete(Mockito.any(Users.class));

        //when
        userRepository.delete(user1);
        userRepository.delete(user2);
        userRepository.delete(user2);

        //then
        verify(userRepository, times(1)).delete(user1);
        verify(userRepository, times(2)).delete(user2);
    }

    @Test
    @DisplayName("중복검사")
    void checkEmail() {
        //given
        given(userRepository.findByEmail("emailError@gmail.com"))
                .willThrow(new GlobalException(ExceptionCode.USER_EXISTS));
        given(userRepository.findBynickName("nicknameError@gmail.com"))
                .willThrow(new GlobalException(ExceptionCode.NICKNAME_EXISTS));

        //when, then
        assertThatThrownBy(() -> userRepository.findByEmail("emailError@gmail.com"))
                .isInstanceOf(GlobalException.class)
                .hasMessage("이미 존재하는 유저입니다");

        assertThatThrownBy(() -> userRepository.findBynickName("nicknameError@gmail.com"))
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
        List<Users> succesList = List.of(user);

        given(userService.deleteAdmin(allUsersList)).willReturn(succesList);

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
        given(userRepository.findById(2L)).willThrow(new GlobalException(ExceptionCode.USER_NOT_FOUND));

        //when
        Optional<Users> result = userRepository.findById(1L);

        //then
        assertThat(result.orElseThrow()).isEqualTo(admin);
        assertThatThrownBy(() -> userRepository.findById(2L))
                .isInstanceOf(GlobalException.class)
                .hasMessage("회원을 찾을 수 없습니다");
    }

    @Test
    void findByEmail() {
        //given
        Users data = userList.get(0);
        Users admin = settingUser(1L, new Users(), data);

        given(userRepository.findByEmail("admin@gmail.com")).willReturn(Optional.of(admin));
        given(userRepository.findByEmail("exception@gmail.com"))
                .willThrow(new GlobalException(ExceptionCode.USER_NOT_FOUND));

        //when
        Optional<Users> result = userRepository.findByEmail("admin@gmail.com");

        //then
        assertThat(result.orElseThrow()).isEqualTo(admin);
        assertThatThrownBy(() -> userRepository.findByEmail("exception@gmail.com"))
                .isInstanceOf(GlobalException.class)
                .hasMessage("회원을 찾을 수 없습니다");
    }

    private Users settingUser(Long userId, Users initial, Users data) {
        initial.setUserId(userId);
        initial.setEmail(data.getEmail());
        initial.setPassword(encoder.encode(data.getPassword()));
        initial.setNickName(data.getNickName());
        initial.setProFilePicture(data.getProFilePicture());
        initial.setRoles(authorityUtils.createRoles(initial.getEmail()));

        return initial;
    }
}
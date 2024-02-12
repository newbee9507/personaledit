package com.seb_main_034.SERVER.users.controller;

import com.seb_main_034.SERVER.exception.GlobalException;
import com.seb_main_034.SERVER.users.dto.UserSaveDto;
import com.seb_main_034.SERVER.users.entity.Users;
import com.seb_main_034.SERVER.users.mapper.UserMapper;
import com.seb_main_034.SERVER.users.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@Transactional
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
class UserControllerTest {

    @Autowired
    private UserService service;

    @Autowired
    private UserMapper mapper;

    @Autowired
    private PasswordEncoder encoder;

    @BeforeEach
    void saveUsers() {
        Users admin = new Users("admin@gmail.com", "123456789!",
                                                "관리자", "사진없음");
        Users user = new Users("test@naver.com", "12345678!",
                                                "회원", "사진없음");
        service.save(admin);
        service.save(user);
    }

    @AfterEach
    void clearDb() {
        service.deleteAll();
    }

    @Test
    @Order(1)
    void info() {
        //given = BeforeEach

        //when
        Users findUser = service.findById(1L);

        //then
        assertThat(findUser.getNickName()).isEqualTo("관리자");
        assertThatThrownBy(() -> service.findById(3L)).isInstanceOf(GlobalException.class);
    }

    @Test
    void signUp() {
        //given
        UserSaveDto successDto = new UserSaveDto("test2@naver.com", "123456789!",
                "회원2", "사진없음");
        UserSaveDto emailErrorDto = new UserSaveDto("test2@naver.com", "123456789!",
                "회원3", "사진없음");
        UserSaveDto nickNameErrorDto = new UserSaveDto("test3@naver.com", "123456789!",
                "회원2", "사진없음");

        Users savedUser = service.save(mapper.UserSaveDTOtoUser(successDto));
        //when
        Users findByEmail = service.findByEmail("test2@naver.com");

        //then
        assertThat(savedUser).isEqualTo(findByEmail);
        assertThatThrownBy(() -> service.save(mapper.UserSaveDTOtoUser(nickNameErrorDto))).isInstanceOf(GlobalException.class);
        assertThatThrownBy(() -> service.save(mapper.UserSaveDTOtoUser(emailErrorDto))).isInstanceOf(GlobalException.class);
    }

    @Test
    void allUsers() {
        //given = BeforeEach

        //when
        List<Users> allUsers = service.findAllUsers();
        Users admin = service.findByEmail("admin@gmail.com");

        //then
        assertThat(allUsers.contains(admin)).isFalse();
        assertThat(allUsers.size()).isEqualTo(1);
    }

    @Test
    void update() {
        //given = BeforeEach

        //when
        Users beforeUpdate = service.findByEmail("admin@gmail.com");
        beforeUpdate.setNickName("수정한관리자");

        //then
        Users afterUpdate = service.findByEmail("admin@gmail.com");
        assertThat(beforeUpdate).isEqualTo(afterUpdate);

    }

    @Test
    void updatePassword() {
        //given = BeforeEach

        //when
        String changePw = "asdfasdf123!";
        Users beforeUpdate = service.findByEmail("admin@gmail.com");
        beforeUpdate.setPassword(changePw);

        //then
        Users afterUpdate = service.findByEmail("admin@gmail.com");
        assertThat(changePw).isEqualTo(afterUpdate.getPassword());
    }

    @Test
    void delete() {
        //given
        UserSaveDto saveDto = new UserSaveDto("delete@naver.com", "123456789!",
                "삭제회원2", "사진없음");
        Users savedUser = service.save(mapper.UserSaveDTOtoUser(saveDto));

        //when
        List<Users> beforeDeleteList = service.findAllUsers();
        assertThat(beforeDeleteList.size()).isEqualTo(2);

        //then
        service.delete(savedUser);
        List<Users> afterDeleteList = service.findAllUsers();
        assertThat(afterDeleteList.size()).isEqualTo(1);
    }

}
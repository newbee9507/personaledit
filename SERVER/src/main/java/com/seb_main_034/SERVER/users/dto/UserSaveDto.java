package com.seb_main_034.SERVER.users.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class UserSaveDto {

    @Email
    @NotBlank(message = "필수 기입 항목입니다")
    private String email;

    @NotBlank(message = "필수 기입 항목입니다")
    @Length(min = 8, message = "비밀번호는 8자 이상이어야 합니다")
    private String password;

    @NotBlank
    @Length(min = 2, message = "닉네임은 2자 이상이어야 합니다")
    private String nickName;

    private String proFilePicture;
}

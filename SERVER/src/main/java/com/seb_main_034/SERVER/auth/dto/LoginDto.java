package com.seb_main_034.SERVER.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}

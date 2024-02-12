package com.seb_main_034.SERVER.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class LoginSuccessResponseDto {

    private Long userId;

    private String email;

    private String roles;
}

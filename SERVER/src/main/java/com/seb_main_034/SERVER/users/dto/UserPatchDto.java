package com.seb_main_034.SERVER.users.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPatchDto {

    @Length(min = 2)
    private String nickName;

    private String proFilePicture;
}

package com.seb_main_034.SERVER.users.mapper;

import com.seb_main_034.SERVER.users.dto.UserResponseDto;
import com.seb_main_034.SERVER.users.dto.UserSaveDto;
import com.seb_main_034.SERVER.users.entity.Users;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    Users UserSaveDTOtoUser(UserSaveDto userSaveDto);

    UserResponseDto UsertoUserResponseDto(Users user);


}

package com.seb_main_034.SERVER.users.mapper;

import com.seb_main_034.SERVER.users.dto.UserPatchDto;
import com.seb_main_034.SERVER.users.entity.Users;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


public interface PatchMapper {


    Users UserPatchDTOtoUser(Users user, UserPatchDto userPatchDto);
}

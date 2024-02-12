package com.seb_main_034.SERVER.users.mapper;

import com.seb_main_034.SERVER.users.dto.UserPatchDto;
import com.seb_main_034.SERVER.users.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class PatchMapperImp implements PatchMapper{


    // 유저정보 수정시, 닉네임과 프로필사진 중 하나만 수정할 수 있게 하는 매퍼.
    @Override
    public Users UserPatchDTOtoUser(Users user, UserPatchDto userPatchDto) {

        if (userPatchDto == null) {
            return user;
        }

        if (userPatchDto.getNickName() != null) {
            user.setNickName( userPatchDto.getNickName() );
        }
        if (userPatchDto.getProFilePicture() != null) {
            user.setProFilePicture( userPatchDto.getProFilePicture() );
        }


        return user;
    }
}

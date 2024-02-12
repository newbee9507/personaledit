package com.seb_main_034.SERVER.users.dto;

import com.seb_main_034.SERVER.users.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@AllArgsConstructor
public class AllUserListDto {

    List<Users> allUserList;

}

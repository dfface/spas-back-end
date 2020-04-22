package com.spas.backend.mapper;

import com.spas.backend.dto.UserDto;
import com.spas.backend.dto.UserRoleUpdateDto;
import com.spas.backend.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
public interface UserMapper extends BaseMapper<User> {
  UserDto selectUserByEmail(String email);
  UserDto selectUserByEmailAndOfficeId(String email, String officeId);
  UserDto selectUserById(String id);
  void updateUserByUserRoleUpdateDto(UserRoleUpdateDto userRoleUpdateDto);
}

package com.spas.backend.mapper;

import com.spas.backend.dto.UserDto;
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
  public UserDto selectUserByEmail(String email);
  public UserDto selectUserByEmailAndOfficeId(String email, String officeId);
  public UserDto selectUserById(String id);
}

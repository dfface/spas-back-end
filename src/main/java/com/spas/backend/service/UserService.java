package com.spas.backend.service;

import com.spas.backend.common.ApiResponse;
import com.spas.backend.dto.UserDto;
import com.spas.backend.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spas.backend.vo.UserVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
public interface UserService extends IService<User> {

  public ApiResponse insertUser(UserDto userDto);

  public UserDto selectUser(String email);

  public UserDto selectUserById(String id);

  public UserVo selectUserByIdToVo(String id);
}

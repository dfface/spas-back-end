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

  /**
   * 全库搜索，存在问题.
   * @param email 邮箱
   * @return 用户
   */
  public UserDto selectUser(String email);

  /**
   * 检察院下，所有邮箱.
   * @param email 邮箱
   * @param officeId 检察院 id
   * @return
   */
  public UserDto selectUser(String email, String officeId);

  public UserDto selectUserById(String id);

  public UserVo selectUserByIdToVo(String id);
}

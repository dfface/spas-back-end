package com.spas.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.dto.UserDto;
import com.spas.backend.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spas.backend.vo.UserOutlineVo;
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

  /**
   * 插入用户(生成密码及盐值)，用于新注册状态，由于HomeController存在，这里并不需要(历史问题).
   * @param userDto 用户相关详细信息.
   * @return 成功
   */
  ApiResponse insertUser(UserDto userDto);

  /**
   * 全库搜索，存在问题.
   * @param email 邮箱
   * @return 用户
   */
  UserDto selectUser(String email);

  /**
   * 检察院下，所有邮箱.
   * @param email 邮箱
   * @param officeId 检察院 id
   * @return 用户详细信息
   */
  UserDto selectUser(String email, String officeId);

  /**
   * 通过id查找用户.
   * @param id 用户id
   * @return UserDto 用户详细信息
   */
  UserDto selectUserById(String id);

  /**
   * 通过id查找用户.
   * @param id 用户id
   * @return UserVo 用户信息（无盐值、无状态）
   */
  UserVo selectUserByIdToVo(String id);

  /**
   * 修改用户信息
   * @param userOutlineVo userOutlineVo 用户基本信息
   */
  void updateUserByUserOutlineVo(UserOutlineVo userOutlineVo);

  /**
   * 通过officeId 分页查询所有用户.
   * @param page 哪个页面
   * @param officeId 检察院id
   * @return 页面信息
   */
  IPage<UserOutlineVo> selectUserOutlineVoByOfficeIdToPage(Page<?> page, String officeId);
}

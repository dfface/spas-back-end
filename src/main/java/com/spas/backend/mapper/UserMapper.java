package com.spas.backend.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spas.backend.dto.UserDto;
import com.spas.backend.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spas.backend.vo.UserOutlineVo;

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
  void updateUserByUserOutlineVo(UserOutlineVo userOutlineVo);
  IPage<UserOutlineVo> selectUserOutlineVoByOfficeIdToPage(Page<?> page, String officeId);
}

package com.spas.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spas.backend.dto.UserRoleDto;
import com.spas.backend.entity.UserRole;

import java.util.List;

public interface UserRoleMapper extends BaseMapper<UserRole> {
  public List<UserRoleDto> selectUserRolesByUseId(String userId);
}

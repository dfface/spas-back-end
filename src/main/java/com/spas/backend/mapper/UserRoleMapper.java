package com.spas.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spas.backend.dto.UserRoleDto;
import com.spas.backend.entity.UserRole;
import com.spas.backend.vo.UserRoleVo;

import java.util.List;

public interface UserRoleMapper extends BaseMapper<UserRole> {
  List<UserRoleDto> selectUserRolesByUseId(String userId);
  IPage<UserRoleVo> selectUserRolesByOfficeIdToUserRoleVo(Page<?> page, String officeId);
  List<UserRoleVo> selectUserRolesByOfficeIdToUserRoleVoOnce(String officeId);
}

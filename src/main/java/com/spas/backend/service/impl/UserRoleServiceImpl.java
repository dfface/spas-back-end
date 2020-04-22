package com.spas.backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.entity.UserRole;
import com.spas.backend.mapper.UserRoleMapper;
import com.spas.backend.service.UserRoleService;
import com.spas.backend.vo.UserRoleVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-24
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

  @Resource
  private UserRoleMapper userRoleMapper;

  @Override
  public ApiResponse selectRoles(String useId) {
    return new ApiResponse(userRoleMapper.selectUserRolesByUseId(useId));
  }

  @Override
  public IPage<UserRoleVo> selectRolesByOfficeId(Page<?> page, String officeId) {
    return userRoleMapper.selectUserRolesByOfficeIdToUserRoleVo(page,officeId);
  }
}

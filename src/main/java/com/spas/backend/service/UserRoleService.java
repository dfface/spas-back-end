package com.spas.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.entity.UserRole;
import com.spas.backend.vo.UserRoleVo;

/**
 * <p>
 *  服务类.
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
public interface UserRoleService extends IService<UserRole> {

  ApiResponse selectRoles(String useId);

  /**
   * 通过检察院id查找所有用户及其角色，分页
   * @param officeId 检察院id
   * @return 分页
   */
  IPage<UserRoleVo> selectRolesByOfficeId(Page<?> page, String officeId);
}

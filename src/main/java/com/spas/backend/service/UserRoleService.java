package com.spas.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.entity.UserRole;

/**
 * <p>
 *  服务类.
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
public interface UserRoleService extends IService<UserRole> {

  public ApiResponse selectRoles(String useId);
}

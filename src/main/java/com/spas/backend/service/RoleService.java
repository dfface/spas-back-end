package com.spas.backend.service;

import com.spas.backend.entity.Role;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
public interface RoleService extends IService<Role> {

  /**
   * 通过检察院id查询所有的角色.
   * @param officeId 检察院id
   * @return 角色列表
   */
  List<Role> selectAll(String officeId);
}

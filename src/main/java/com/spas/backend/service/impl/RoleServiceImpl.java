package com.spas.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.spas.backend.entity.Role;
import com.spas.backend.mapper.RoleMapper;
import com.spas.backend.service.RoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

  @Resource
  private RoleMapper roleMapper;

  @Override
  public List<Role> selectAll(String officeId) {
    QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("office_id",officeId);
    return roleMapper.selectList(queryWrapper);
  }
}

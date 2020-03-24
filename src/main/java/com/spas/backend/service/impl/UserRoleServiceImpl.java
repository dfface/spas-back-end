package com.spas.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spas.backend.entity.UserRole;
import com.spas.backend.mapper.UserRoleMapper;
import com.spas.backend.service.UserRoleService;
import org.springframework.stereotype.Service;

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
}

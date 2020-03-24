package com.spas.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spas.backend.entity.UserCase;
import com.spas.backend.mapper.UserCaseMapper;
import com.spas.backend.service.UserCaseService;
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
public class UserCaseServiceImpl extends ServiceImpl<UserCaseMapper, UserCase> implements UserCaseService {
}

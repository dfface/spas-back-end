package com.spas.backend.service.impl;

import com.spas.backend.entity.Case;
import com.spas.backend.mapper.CaseMapper;
import com.spas.backend.service.CaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
@Service
public class CaseServiceImpl extends ServiceImpl<CaseMapper, Case> implements CaseService {

}
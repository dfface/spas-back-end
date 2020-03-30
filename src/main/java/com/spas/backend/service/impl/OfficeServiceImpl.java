package com.spas.backend.service.impl;

import com.spas.backend.entity.Office;
import com.spas.backend.mapper.OfficeMapper;
import com.spas.backend.service.OfficeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
@Service
public class OfficeServiceImpl extends ServiceImpl<OfficeMapper, Office> implements OfficeService {

  @Resource
  private OfficeMapper officeMapper;

  @Override
  public Office select(String id) {
    return officeMapper.selectById(id);
  }
}

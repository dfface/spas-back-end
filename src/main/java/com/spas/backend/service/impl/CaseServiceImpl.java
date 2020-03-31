package com.spas.backend.service.impl;

import com.spas.backend.entity.Cases;
import com.spas.backend.entity.User;
import com.spas.backend.mapper.CaseMapper;
import com.spas.backend.mapper.UserMapper;
import com.spas.backend.service.CaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spas.backend.vo.CaseVo;
import org.modelmapper.ModelMapper;
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
public class CaseServiceImpl extends ServiceImpl<CaseMapper, Cases> implements CaseService {

  @Resource
  private CaseMapper caseMapper;

  @Resource
  private ModelMapper modelMapper;

  @Resource
  private UserMapper userMapper;

  @Override
  public CaseVo selectDetail(String id) {
    Cases cases = caseMapper.selectById(id);
    CaseVo caseVo = new CaseVo();
    modelMapper.map(cases,caseVo);
    User user = userMapper.selectById(cases.getCreatorId());
    caseVo.setCreatorName(user.getName());
    return caseVo;
  }

  @Override
  public int insertCase(Cases cases) {
    return caseMapper.insert(cases);
  }
}

package com.spas.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spas.backend.entity.Cases;
import com.spas.backend.entity.User;
import com.spas.backend.mapper.CaseMapper;
import com.spas.backend.mapper.UserMapper;
import com.spas.backend.service.CaseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spas.backend.vo.CaseOutlineVo;
import com.spas.backend.vo.CaseVo;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
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
@Slf4j
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
  public void insertCase(Cases cases) {
    caseMapper.insert(cases);
  }

  @Override
  public List<CaseOutlineVo> selectOutline(String creatorId, Integer ...state) {
    QueryWrapper<Cases> queryWrapper = new QueryWrapper<Cases>().eq("creator_id",creatorId);
    for(int i = 0; i < state.length; i++){
      if(i == 0){
        queryWrapper.eq("state",state[i]);
      }
      else{
        queryWrapper.or().eq("state",state[i]);
      }
    }
    List<Cases> casesList = caseMapper.selectList(queryWrapper.orderByDesc("create_time","update_time"));
    log.debug(casesList.toString());
    List<CaseOutlineVo> caseOutlineVos = new ArrayList<>();
    for(Cases cases : casesList){
      CaseOutlineVo caseOutlineVo = new CaseOutlineVo();
      modelMapper.map(cases,caseOutlineVo);
      caseOutlineVos.add(caseOutlineVo);
    }
    return caseOutlineVos;
  }

  @Override
  public IPage<CaseOutlineVo> selectOutlineAllByPage(String creatorId, Page<CaseOutlineVo> page) {
    return caseMapper.selectOutlineAllByPage(page,creatorId);
  }

  @Override
  public List<CaseOutlineVo> selectOutlineAuditing(String officeId) {
    return caseMapper.selectOutlineAuditing(officeId);
  }

  @Override
  public Boolean updateState(String id, Integer state, String opinion) {
    return caseMapper.updateState(id,state,opinion);
  }
}

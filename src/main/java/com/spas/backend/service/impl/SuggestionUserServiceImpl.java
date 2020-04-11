package com.spas.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.spas.backend.entity.SuggestionUser;
import com.spas.backend.mapper.SuggestionUserMapper;
import com.spas.backend.service.SuggestionUserService;

import javax.annotation.Resource;
import java.util.List;

public class SuggestionUserServiceImpl extends ServiceImpl<SuggestionUserMapper, SuggestionUser> implements SuggestionUserService {

  @Resource
  private SuggestionUserMapper suggestionUserMapper;

  @Override
  public List<SuggestionUser> selectSuggestionUser(String userId, String officeId) {
    QueryWrapper<SuggestionUser> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("use_id",userId).eq("office_id",officeId);
    return suggestionUserMapper.selectList(queryWrapper);
  }
}

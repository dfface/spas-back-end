package com.spas.backend.service.impl;

import com.spas.backend.entity.Suggestion;
import com.spas.backend.mapper.SuggestionMapper;
import com.spas.backend.service.SuggestionService;
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
public class SuggestionServiceImpl extends ServiceImpl<SuggestionMapper, Suggestion> implements SuggestionService {

  @Resource
  private SuggestionMapper suggestionMapper;

  @Override
  public int newSuggestion(Suggestion suggestion) {
    return suggestionMapper.insert(suggestion);
  }

  @Override
  public Suggestion select(String id) {
    return suggestionMapper.selectById(id);
  }
}

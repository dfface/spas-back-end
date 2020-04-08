package com.spas.backend.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spas.backend.entity.Suggestion;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.spas.backend.vo.SuggestionVo;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
public interface SuggestionMapper extends BaseMapper<Suggestion> {

  IPage<SuggestionVo> selectSuggestionVoAllByPage(Page<?> page, String creatorId);
}

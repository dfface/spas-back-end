package com.spas.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spas.backend.entity.Suggestion;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spas.backend.vo.SuggestionVo;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
public interface SuggestionService extends IService<Suggestion> {

  /**
   * 新建一个检察建议
   * @param suggestion 检察建议实体
   */
  public void newSuggestion(Suggestion suggestion);

  /**
   * 通过id查找检察建议.
   * @param id 检察建议id
   * @return 检察建议
   */
  public Suggestion select(String id);

  /**
   * 通过创建者 id 分页查找检察建议.
   * @param creatorId 创建者id
   * @param page 页面
   * @return 分页
   */
  public IPage<SuggestionVo> selectSuggestionVoAllByPage(String creatorId, Page<?> page);
}

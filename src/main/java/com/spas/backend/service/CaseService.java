package com.spas.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spas.backend.entity.Cases;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spas.backend.vo.CaseOutlineVo;
import com.spas.backend.vo.CaseVo;
import org.apache.ibatis.annotations.Case;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
public interface CaseService extends IService<Cases> {

  public CaseVo selectDetail(String id);

  /**
   * 插入一条 case 记录.
   * @param cases 案件信息
   */
  public void insertCase(Cases cases);

  /**
   * 根据状态和创建者id查询符合条件的案件大纲.
   * @param creatorId 创建者id
   * @param state 状态(不定参数)
   * @return 列表
   */
  public List<CaseOutlineVo> selectOutline(String creatorId, Integer ...state);


  /**
   * 根据创建者id查询所有记录，分页模式
   * @param creatorId 创建者id
   * @param page 分页
   * @return 列表
   */
  public IPage<CaseOutlineVo> selectOutlineAllByPage(String creatorId, Page<CaseOutlineVo> page);

}

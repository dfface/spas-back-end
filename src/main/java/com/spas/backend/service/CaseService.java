package com.spas.backend.service;

import com.spas.backend.entity.Cases;
import com.baomidou.mybatisplus.extension.service.IService;
import com.spas.backend.vo.CaseVo;

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
   * @return 该案件的 id
   */
  public int insertCase(Cases cases);
}

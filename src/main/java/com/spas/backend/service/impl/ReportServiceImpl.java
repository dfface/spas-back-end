package com.spas.backend.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spas.backend.entity.Report;
import com.spas.backend.mapper.ReportMapper;
import com.spas.backend.service.ReportService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class ReportServiceImpl extends ServiceImpl<ReportMapper, Report> implements ReportService {

  @Resource
  private ReportMapper reportMapper;

  @Override
  public List<Report> selectReportBySuggestionCreatedByProcurator(String procuratorId, Integer state) {
    return reportMapper.selectReportBySuggestionCreatedByProcurator(procuratorId,state);
  }

  @Override
  public IPage<Report> selectReportBySuggestionEvaluatedByProcurator(Page<Report> page, String procuratorId) {
    return reportMapper.selectReportBySuggestionEvaluatedByProcurator(page,procuratorId);
  }
}

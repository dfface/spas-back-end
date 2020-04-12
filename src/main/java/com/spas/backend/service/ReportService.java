package com.spas.backend.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spas.backend.entity.Report;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
public interface ReportService extends IService<Report> {

  List<Report> selectReportBySuggestionCreatedByProcurator(String procuratorId, Integer state);

  IPage<Report> selectReportBySuggestionEvaluatedByProcurator(Page<Report> page, String procuratorId);

}

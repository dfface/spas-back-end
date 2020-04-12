package com.spas.backend.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spas.backend.entity.Report;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
public interface ReportMapper extends BaseMapper<Report> {

  List<Report> selectReportBySuggestionCreatedByProcurator(String procuratorId, Integer state);

  IPage<Report> selectReportBySuggestionEvaluatedByProcurator(Page<?> page, String procuratorId);
}

package com.spas.backend.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.spas.backend.common.ApiCode;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.dto.ReportDto;
import com.spas.backend.dto.ReportJudgeDto;
import com.spas.backend.entity.Report;
import com.spas.backend.entity.Suggestion;
import com.spas.backend.service.ReportService;
import com.spas.backend.service.SuggestionService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Yuhan Liu
 * @since 2020-03-23
 */
@RestController
@RequestMapping("/report")
public class ReportController {

  @Resource
  private ReportService reportService;

  @Resource
  private SuggestionService suggestionService;

  @Resource
  private ModelMapper modelMapper;

  /**
   * 新建整改报告.
   * @param reportDto 整改报告信息.
   * @return OK
   */
  @PostMapping("/new")
  @ApiOperation("新建整改报告")
  public ApiResponse newReport(@RequestBody ReportDto reportDto){
    // 参数校验
    Report report = new Report();
    modelMapper.map(reportDto,report);
    reportService.save(report);
    return new ApiResponse(ApiCode.OK, report.getId());
  }

  /**
   * 整改效果评价.
   * @param reportJudgeDto 整改报告评价内容和得分
   * @return OK
   */
  @ApiOperation("评价整改报告")
  @PostMapping("/evaluate")
  public ApiResponse evaluate(@RequestBody ReportJudgeDto reportJudgeDto){
    // 保存评价信息
    UpdateWrapper<Report> updateWrapper = new UpdateWrapper<>();
    updateWrapper.eq("id",reportJudgeDto.getId())
        .set("judge",reportJudgeDto.getJudge())
        .set("score",reportJudgeDto.getScore())
        .set("state",2);  // 表示已经评价过
    reportService.update(updateWrapper);
    // 更改检察建议状态
    Report report = reportService.getById(reportJudgeDto.getId());
    UpdateWrapper<Suggestion> suggestionUpdateWrapper = new UpdateWrapper<>();
    suggestionUpdateWrapper.eq("id",report.getSuggestionId())
        .set("state",2);  // 表示已经回复过
    suggestionService.update(suggestionUpdateWrapper);
    return new ApiResponse(ApiCode.OK);
  }

  /**
   * 修改整改报告.
   * @param reportDto 报告信息
   * @param id 报告id
   * @return OK
   */
  @PostMapping("/revise/{id}")
  @ApiOperation("修改整改报告")
  public ApiResponse revise(@RequestBody ReportDto reportDto, @PathVariable String id){
    Report report = new Report();
    modelMapper.map(reportDto,report);
    report.setId(id);
    reportService.updateById(report);
    return new ApiResponse(ApiCode.OK);
  }

}


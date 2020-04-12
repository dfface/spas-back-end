package com.spas.backend.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spas.backend.common.ApiCode;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.dto.ReportDto;
import com.spas.backend.dto.ReportJudgeDto;
import com.spas.backend.entity.Report;
import com.spas.backend.entity.Suggestion;
import com.spas.backend.service.OfficeService;
import com.spas.backend.service.ReportService;
import com.spas.backend.service.SuggestionService;
import com.spas.backend.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
  private UserService userService;

  @Resource
  private OfficeService officeService;

  @Resource
  private SuggestionService suggestionService;

  @Resource
  private ModelMapper modelMapper;

  @Value("${user.page.size}")
  private long pageSize;

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
    report.setState(1);  // 表示报告等待评价
    reportService.save(report);
    // 修改对应检察建议的状态为2 表示检察建议已经被回复，需要评价报告
    UpdateWrapper<Suggestion> suggestionUpdateWrapper = new UpdateWrapper<>();
    suggestionUpdateWrapper.set("state",2)
        .eq("id",reportDto.getSuggestionId());
    suggestionService.update(suggestionUpdateWrapper);
    return new ApiResponse(ApiCode.OK, report.getId());
  }

  /**
   * 查看整改报告细节.
   * @param id 报告id
   * @return Report
   */
  @GetMapping("/detail/{id}")
  @ApiOperation("查看整改报告细节")
  public ApiResponse detail(@PathVariable String id){
    return new ApiResponse(ApiCode.OK,reportService.getById(id));
  }

  /**
   * 检察官查看需要评价的整改报告有哪些.
   * @param userId 检察官id
   * @return List Report
   */
  @GetMapping("/evaluating/{userId}")
  @ApiOperation("检察官查看需要评价的整改报告有哪些")
  public ApiResponse evaluating(@PathVariable String userId){
    // 搜索自己发出的检察建议， 状态为2，才是等待评价的
    // join Report suggestion_id
    return new ApiResponse(ApiCode.OK,reportService.selectReportBySuggestionCreatedByProcurator(userId,2));
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
    Report report = reportService.getById(reportJudgeDto.getId());
    UpdateWrapper<Suggestion> suggestionUpdateWrapper = new UpdateWrapper<>();
    // 更改检察建议状态
    if(reportJudgeDto.getNextState() == 3) {  // 等待新一轮报告
      suggestionUpdateWrapper.eq("id",report.getSuggestionId())
          .set("state",3);  // 表示继续等待回复
    }
    else if(reportJudgeDto.getNextState() == 2){
      suggestionUpdateWrapper.eq("id",report.getSuggestionId())
          .set("state",5);  // 表示已经回复过，准备起诉
    }
    else if(reportJudgeDto.getNextState() == 1){
      suggestionUpdateWrapper.eq("id",report.getSuggestionId())
          .set("state",4);  // 表示整改完成
    }
    suggestionService.update(suggestionUpdateWrapper);
    return new ApiResponse(ApiCode.OK);
  }

  /**
   * 修改整改报告(已经评价的报告不允许修改).
   * @param reportDto 报告信息
   * @param id 报告id
   * @return OK
   */
  @PostMapping("/revise/{id}")
  @ApiOperation("修改整改报告")
  public ApiResponse revise(@RequestBody ReportDto reportDto, @PathVariable String id){
    // 已经评价过的报告不许再更改
    Report oldReport = reportService.getById(id);
    if(oldReport.getState() == 2){
      return new ApiResponse(ApiCode.REPORT_HAS_BEEN_JUDGED);
    }
    Report report = new Report();
    modelMapper.map(reportDto,report);
    report.setId(id);
    reportService.updateById(report);
    return new ApiResponse(ApiCode.OK,id);
  }

  /**
   * 行政机关人员查看自己提交的一些整改报告
   * @param userId 用户id
   * @param current 当前页面
   * @return Report List Page
   */
  @GetMapping("/history/{userId}/{current}")
  public ApiResponse history(@PathVariable String userId,@PathVariable Integer current){
    if(userService.selectUserById(userId) == null){
      return new ApiResponse(ApiCode.UNKNOWN_ACCOUNT);
    }
    QueryWrapper<Report> reportQueryWrapper = new QueryWrapper<>();
    reportQueryWrapper.eq("creator_id",userId);
    IPage<Report> reportIPage = reportService.page(new Page<>(current,pageSize),reportQueryWrapper);
    return getApiResponse(reportIPage);
  }

  /**
   * 检察官查看自己已经评价过的报告历史
   * @param userId 用户id
   * @param current 当前页面
   * @return OK，List Report
   */
  @GetMapping("/evaluateHistory/{userId}/{current}")
  @ApiOperation("检察官查看自己已经评价过的报告历史")
  public ApiResponse evaluateHistory(@PathVariable String userId,@PathVariable Integer current){
    // 先查找检察官的suggestion 状态没有影响 ，再查看报告 state 要为 2
    IPage<Report> reportIPage = reportService.selectReportBySuggestionEvaluatedByProcurator(new Page<>(current,pageSize),userId);
    return getApiResponse(reportIPage);
  }

  private ApiResponse getApiResponse(IPage<Report> reportIPage) {
    Map<String,Object> map = new HashMap<>();
    map.put("count",reportIPage.getPages());
    map.put("current",reportIPage.getCurrent());
    map.put("content",reportIPage.getRecords());
    return new ApiResponse(ApiCode.OK,map);
  }

}


package com.spas.backend.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.spas.backend.common.ApiCode;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.entity.SuggestionUser;
import com.spas.backend.entity.Cases;
import com.spas.backend.service.*;
import com.spas.backend.vo.CaseOutlineVo;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
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
@RequestMapping("/case")
@Slf4j
public class CaseController {

  @Resource
  private CaseService caseService;

  @Value("${user.page.size}")
  private String pageSize;

  /**
   * 查询案件详细信息.
   * @param id 案件id
   * @return 该案件的 CaseVo 信息
   */
  @GetMapping("/detail/{id}")
  @ApiOperation("查询案件详细信息")
  public ApiResponse detail(@PathVariable String id){
    // 参数校验
    return new ApiResponse(ApiCode.OK,caseService.selectDetail(id));
  }

  /**
   * 新建案件.
   * @param cases 案件实体
   * @return 新案件的id
   */
  @PostMapping("/new")
  @ApiOperation("新建案件")
  public ApiResponse newCase(@RequestBody Cases cases) {
    // 参数校验(虽然前台已经足够充足)
    caseService.insertCase(cases);
    log.debug("Case New ID: " + cases.getId());
    ApiResponse apiResponse = new ApiResponse(ApiCode.OK);
    apiResponse.setData(cases.getId());
    return apiResponse;
  }

  /**
   * 正在处理的案件，没审核、审核通过、审核不通过的.
   * @param id 用户id
   * @return 所有满足要求的案件大纲
   */
  @GetMapping("/handling/{id}")
  @ApiOperation("正在处理的案件，没审核、审核通过、审核不通过的")
  public ApiResponse handling(@PathVariable String id){
    // 参数校验
    return new ApiResponse(ApiCode.OK, caseService.selectOutline(id,1,2,3));
  }

  /**
   * 正在处理的案件，已经通过审核，新建检察建议时会查询.
   * @param id 用户id
   * @return 所有满足要求的案件大纲
   */
  @GetMapping("/handlingAudited/{id}")
  @ApiOperation("正在处理的案件，已经通过审核，新建检察建议时会查询")
  public ApiResponse handlingAudited(@PathVariable String id){
    return new ApiResponse(ApiCode.OK, caseService.selectOutline(id,3));
  }

  /**
   * 用户创建的案件的历史，分页查询.
   * @param id 用户id
   * @param current 当前页
   * @return 满足分页格式约定的信息
   */
  @GetMapping("/history/{id}/{current}")
  @ApiOperation("用户创建的案件的历史，分页查询")
  public ApiResponse history(@PathVariable String id, @PathVariable long current){
    // 参数校验
    IPage<CaseOutlineVo> caseOutlineVoIpage = caseService.selectOutlineAllByPage(id,new Page<CaseOutlineVo>(current, Integer.valueOf(pageSize).longValue()));
    Map<String,Object> map = new HashMap<>();
    map.put("count",caseOutlineVoIpage.getPages());
    map.put("current",caseOutlineVoIpage.getCurrent());
    map.put("content",caseOutlineVoIpage.getRecords());
    return new ApiResponse(ApiCode.OK, JSON.toJSON(map));
  }

  /**
   * 查询等待审核的案件（以检察院为单位）.
   * @param id 检察院id
   * @return 待审核的案件大纲列表
   */
  @GetMapping("/auditing/{id}")
  @ApiOperation("查询等待审核的案件（以检察院为单位）")
  public ApiResponse auditing(@PathVariable String id){
    // 参数校验
    return new ApiResponse(ApiCode.OK,caseService.selectOutlineAuditing(id));
  }

  /**
   * 案件审核，更新案件的状态、建议.
   * @param id 案件id
   * @param state 案件状态
   * @param opinion 案件审核建议
   * @return 成功与否
   */
  @PostMapping("/auditing/{id}/{state}")
  @ApiOperation("案件审核，更新案件的状态、建议")
  public ApiResponse auditing(@PathVariable String id, @PathVariable Integer state, @RequestBody String opinion){
    // 参数校验
    return new ApiResponse(ApiCode.OK,caseService.updateState(id,state,opinion));
  }

  /**
   * 修改案件信息.
   * @param data 案件信息
   * @return 成功与否
   */
  @PostMapping("/revise")
  @ApiOperation("修改案件信息")
  public ApiResponse revise(@RequestBody Cases data){
    // 参数校验
    return new ApiResponse(ApiCode.OK,caseService.updateById(data));
  }
}


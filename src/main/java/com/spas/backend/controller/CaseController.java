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

  @GetMapping("/detail/{id}")
  public ApiResponse detail(@PathVariable String id){
    // 参数校验
    return new ApiResponse(ApiCode.OK,caseService.selectDetail(id));
  }

  @PostMapping("/new")
  public ApiResponse newCase(@RequestBody Cases cases) {
    // 参数校验(虽然前台已经足够充足)
    caseService.insertCase(cases);
    log.debug("Case New ID: " + cases.getId());
    ApiResponse apiResponse = new ApiResponse(ApiCode.OK);
    apiResponse.setData(cases.getId());
    return apiResponse;
  }

  @GetMapping("/handling/{id}")
  public ApiResponse handling(@PathVariable String id){
    // 参数校验
    return new ApiResponse(ApiCode.OK, caseService.selectOutline(id,1,2,3));
  }

  @GetMapping("/history/{id}/{current}")
  public ApiResponse history(@PathVariable String id, @PathVariable long current){
    // 参数校验
    IPage<CaseOutlineVo> caseOutlineVoIpage = caseService.selectOutlineAllByPage(id,new Page<CaseOutlineVo>(current, Integer.valueOf(pageSize).longValue()));
    Map<String,Object> map = new HashMap<>();
    map.put("count",caseOutlineVoIpage.getPages());
    map.put("current",caseOutlineVoIpage.getCurrent());
    map.put("content",caseOutlineVoIpage.getRecords());
    return new ApiResponse(ApiCode.OK, JSON.toJSON(map));
  }

  @GetMapping("/auditing/{id}")
  public ApiResponse auditing(@PathVariable String id){
    // 参数校验
    return new ApiResponse(ApiCode.OK,caseService.selectOutlineAuditing(id));
  }

  @PostMapping("/auditing/{id}/{state}")
  public ApiResponse auditing(@PathVariable String id, @PathVariable Integer state, @RequestBody String opinion){
    // 参数校验
    return new ApiResponse(ApiCode.OK,caseService.updateState(id,state,opinion));
  }

  /**
   * 修改案件信息.
   * @param data 案件信息
   * @return
   */
  @PostMapping("/revise")
  public ApiResponse revise(@RequestBody Cases data){
    // 参数校验
      return new ApiResponse(ApiCode.OK,caseService.updateById(data));
  }
}


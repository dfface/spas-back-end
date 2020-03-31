package com.spas.backend.controller;


import com.spas.backend.common.ApiCode;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.entity.Cases;
import com.spas.backend.service.CaseService;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/case")
@Slf4j
public class CaseController {

  @Resource
  private CaseService caseService;

  @GetMapping("/detail/{id}")
  public ApiResponse detail(@PathVariable String id){
    // 参数校验
    return new ApiResponse(ApiCode.OK,caseService.selectDetail(id));
  }

  @PostMapping("/new")
  public ApiResponse newCase(@RequestBody Cases cases) {
    // 参数校验
    caseService.insertCase(cases);
    log.debug("Case New ID: " + cases.getId());
    ApiResponse apiResponse = new ApiResponse(ApiCode.OK);
    apiResponse.setData(cases.getId());
    return apiResponse;
  }
}


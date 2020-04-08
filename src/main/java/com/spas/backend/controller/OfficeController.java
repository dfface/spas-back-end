package com.spas.backend.controller;


import com.spas.backend.common.ApiResponse;
import com.spas.backend.service.OfficeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/office")
public class OfficeController {

  @Resource
  private OfficeService officeService;

  @GetMapping("/all")
  public ApiResponse selectAll(){
    return new ApiResponse(officeService.list());
  }
}


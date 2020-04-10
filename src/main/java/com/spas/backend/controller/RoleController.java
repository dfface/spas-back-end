package com.spas.backend.controller;


import com.spas.backend.common.ApiCode;
import com.spas.backend.common.ApiResponse;
import com.spas.backend.service.RoleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/role")
public class RoleController {

  @Resource
  private RoleService roleService;

  /**
   * 通过检察院 id 获取所有角色.
   * @param officeId 检察院id
   * @return 所有角色
   */
  @GetMapping("/all/{officeId}")
  public ApiResponse selectAll(@PathVariable String officeId){
    return new ApiResponse(ApiCode.OK,roleService.selectAll(officeId));
  }
}

